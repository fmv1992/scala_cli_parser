package fmv1992.scala_cli_parser.cli

import fmv1992.scala_cli_parser.ParseException
import fmv1992.scala_cli_parser.Parser

trait Argument {

  def name: String

}

trait ArgumentCLI extends Argument {

  def values: Seq[String]

}

object ArgumentCLI {

  private case class ArgumentCLIImpl(
      name: String,
      values: Seq[String]
  ) extends ArgumentCLI

  def apply(
      name: String,
      values: Seq[String]
  ): ArgumentCLI = ArgumentCLIImpl(name, values)

}

trait ArgumentConf extends Argument {

  def description: String

  def n: Int

  def default: Option[Seq[String]]

}

object ArgumentConf {

  private case class ArgumentConfImpl(
      name: String,
      description: String,
      n: Int,
      default: Option[Seq[String]] = None
  ) extends ArgumentConf

  def apply(
      name: String,
      description: String,
      n: Int,
      default: Option[Seq[String]] = None
  ): ArgumentConf = {
    ArgumentConfImpl(name, description, n, default)
  }

}

trait ParserCLI extends Parser[Seq[String], Set[ArgumentCLI]] {

  def arguments: Set[ArgumentConf]

  def parse(input: Seq[String]): Set[ArgumentCLI]

  override def equals(that: Any): Boolean = {
    that match {
      case p: ParserCLI => this.arguments == p.arguments
      case _            => false
    }
  }

}

object ParserCLI {

  private case class ParserCLIImpl(val arguments: Set[ArgumentConf])
      extends ParserCLI {

    def parse(input: Seq[String]): Set[ArgumentCLI] = {
      def go(
          remaining: Seq[String],
          acc: Either[Seq[String], Set[ArgumentCLI]]
      ): Either[Seq[String], Set[ArgumentCLI]] = {
        if (remaining.isEmpty) {
          // CURRENT: We should add the defaults here if they are not
          // specified.
          acc
        } else {
          val h = remaining.head
          if (h.startsWith("--")) {
            val argumentCLIName: String = h.drop(2)
            if (arguments.map(_.name).contains(argumentCLIName)) {
              val argConf = arguments
                .find(_.name == argumentCLIName)
                .getOrElse(throw new Exception())
              val n: Int = argConf.n
              val (values_, remainingNew) =
                (remaining.tail.take(n), remaining.tail.drop(n))
              require(
                values_.length == n,
                s"'${values_}' should have '${n}' elements. It has '${values_.length}'."
              )
              acc match {
                case Left(x) => go(remainingNew, Left(x))
                case Right(x) =>
                  go(
                    remainingNew,
                    Right(
                      (x + ArgumentCLI(
                        argumentCLIName,
                        values_
                      ))
                    )
                  )
              }
            } else {
              val msgError =
                s"Argument '${h}' is not part of '${arguments.toSeq.sortBy(_.name).toString}'."
              acc match {
                case Left(x) => {
                  Left(x.appended(msgError))
                }
                case Right(_) => Left(Seq(msgError))
              }
            }
          } else {
            ???
          }
        }
      }
      go(input, Right(Set.empty)) match {
        case Right(x) => x
        case Left(ls) => throw new ParseException(ls.mkString("\n"))
      }

    }
  }

  /** This defines how the CLI gets defined. For instance, by having a `map("help")` it enforces this field being defined.
    */
  def apply(input: Map[String, Map[String, String]]): ParserCLI = {
    // require(input(input.keySet.head).keySet == Set("description", "n"), input)
    val args: Iterable[ArgumentConf] = input
      .map(t => {
        val k = t._1
        val vv = t._2
        val default = if (vv.contains("default")) {
          Some(vv("default").split(",").toSeq)
        } else {
          None
        }
        ArgumentConf(k, vv("description"), vv("n").toInt, default)
      })
    require(args.size == args.toSet.size, s"'${args}' and '${args.toSet}'.")
    ParserCLIImpl(args.toSet)
  }

  def apply(input: Set[ArgumentConf]): ParserCLI = ParserCLIImpl(input)

}
