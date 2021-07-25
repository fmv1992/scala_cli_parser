package fmv1992.scala_cli_parser.cli

import fmv1992.scala_cli_parser.Parser
import fmv1992.scala_cli_parser.ParseException

trait Argument {

  def name: String

  def description: String

  override def toString: String = {
    s"'${this.getClass.getSimpleName}': '${name}'.\n${description}"
  }

}

trait ArgumentCLI extends Argument {

  def argumentType: String

  def values: Seq[String]

}

trait ArgumentConf extends Argument {

  def argumentType: String

  def n: Int

}

trait ParserCLI extends Parser[Seq[String], Set[ArgumentCLI]] {

  def arguments: Set[ArgumentConf]

  def parse(input: Seq[String]): Set[ArgumentCLI]

}

object ParserCLI {

  def apply(input: Map[String, Map[String, String]]): ParserCLI = {
    def go(
        remaining: Seq[String],
        acc: Either[Seq[String], Set[ArgumentCLI]]
    ): Either[Seq[String], Set[ArgumentCLI]] = {
      if (remaining.isEmpty) {
        acc
      } else {
        val h = remaining.head
        if (h.startsWith("--")) {
          val argumentCLIName = h.drop(2)
          if (input.contains(argumentCLIName)) {
            val n: Int = input(argumentCLIName)("n").toInt
            val (values_, remainingNew) =
              (remaining.tail.take(n), remaining.tail.drop(n))
            acc match {
              case Left(x) => go(remainingNew, Left(x))
              case Right(x) =>
                go(
                  remainingNew,
                  Right((x + new ArgumentCLI() {
                    val name: String = argumentCLIName
                    val description: String = input(argumentCLIName)("help")
                    val argumentType = input(argumentCLIName)("type")
                    val values = values_
                  }))
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
    val args = input.map(t => {
      Console.err.println("-" * 79)
      Console.err.println(t.toString)
      Console.err.println("-" * 79)
      val k = t._1
      val vv = t._2
      new ArgumentConf() {
        val name = k
        val description = vv("help")
        val argumentType = vv("type")
        val n = vv("n").toInt
      }
    })
    new ParserCLI() {

      val arguments = args.toSet

      def parse(input: Seq[String]): Set[ArgumentCLI] =
        go(input, Right(Set.empty)) match {
          case Right(x) => x
          case Left(ls) => throw new ParseException(ls.mkString("\n"))
        }

    }
  }

}

case class ParserCLIImpl(arguments: Set[ArgumentConf]) extends ParserCLI {

  def parse(input: Seq[String]): Set[ArgumentCLI] = ???

}
