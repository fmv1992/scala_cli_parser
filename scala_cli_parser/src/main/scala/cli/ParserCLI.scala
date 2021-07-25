package fmv1992.scala_cli_parser.cli

import fmv1992.scala_cli_parser.ParseException
import fmv1992.scala_cli_parser.Parser

trait Argument {

  def name: String

  def description: String

  override def toString: String = {
    s"Argument '${this.getClass.getSimpleName}': '${name}'.\n${description}"
  }

}

trait ArgumentCLI extends Argument {

  def argumentType: String

  def values: Seq[String]

}

object ArgumentCLI {

  private case class ArgumentCLIImpl(
      name: String,
      description: String,
      argumentType: String,
      values: Seq[String]
  ) extends ArgumentCLI

  def apply(
      name: String,
      description: String,
      argumentType: String,
      values: Seq[String]
  ): ArgumentCLI = ArgumentCLIImpl(name, description, argumentType, values)

}

trait ArgumentConf extends Argument {

  def argumentType: String

  def n: Int

  override def equals(x: Any): Boolean = {
    x match {
      case a: ArgumentConf =>
        (this.argumentType == a.argumentType) && (this.n == a.n)
      case _ => false
    }
  }

}

object ArgumentConf {

  private case class ArgumentConfImpl(
      name: String,
      description: String,
      argumentType: String,
      n: Int
  ) extends ArgumentConf

  def apply(
      name: String,
      description: String,
      argumentType: String,
      n: Int
  ): ArgumentConf = {
    ArgumentConfImpl(name, description, argumentType, n)
  }

}

trait ParserCLI extends Parser[Seq[String], Set[ArgumentCLI]] {

  def arguments: Set[ArgumentConf]

  def parse(input: Seq[String]): Set[ArgumentCLI]

  override def equals(x: Any): Boolean = {
    x match {
      case p: ParserCLI => this.arguments == p.arguments
      case _            => false
    }
  }

}

object ParserCLI {

  private case class ParserCLIImpl(arguments: Set[ArgumentConf])
      extends ParserCLI {

    def parse(input: Seq[String]): Set[ArgumentCLI] = {
      def go(
          remaining: Seq[String],
          acc: Either[Seq[String], Set[ArgumentCLI]]
      ): Either[Seq[String], Set[ArgumentCLI]] = {
        if (remaining.isEmpty) {
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
              acc match {
                case Left(x) => go(remainingNew, Left(x))
                case Right(x) =>
                  go(
                    remainingNew,
                    Right(
                      (x + ArgumentCLI(
                        argumentCLIName,
                        argConf.description,
                        argConf.argumentType,
                        values_
                      ))
                    )
                  )
              }
            } else {
              ???
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
    val args = input.map(t => {
      val k = t._1
      val vv = t._2
      new ArgumentConf() {
        val name = k
        val description = vv("help")
        val argumentType = vv("type")
        val n = vv("n").toInt
      }
    })
    ParserCLIImpl(args.toSet)

  }

  def apply(input: Set[ArgumentConf]): ParserCLI = ParserCLIImpl(input)

}
