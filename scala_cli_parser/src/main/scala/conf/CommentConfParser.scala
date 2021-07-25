package fmv1992.scala_cli_parser.conf

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import fmv1992.scala_cli_parser._

object CommentConfParser extends PP with PWT {

  override def parse(
      input: Seq[Char]
  ): Try[ParsedResult[Seq[Char], Map[String, String]]] =
    Try(super[ParserPartial].parse(input)) match {
      case Success(_) => Success(ParsedResult(input, emptyMapSS))
      case Failure(ParseException(m)) =>
        Failure(ParseException(s"${this.getClass.getName}': '${m}'."))
      case Failure(t) => Failure(t)
    }

  def partialParse(
      input: Seq[Char]
  ): (Seq[Char], Try[ParsedResult[Seq[Char], Map[String, String]]]) = {
    def go(
        i: Seq[Char],
        acc: Seq[Char]
    ): (Seq[Char], Try[ParsedResult[Seq[Char], Map[String, String]]]) = {
      if (i.isEmpty) {
        if (acc.isEmpty) {
          (Seq.empty, Failure(ParseException(input.mkString)))
        } else {
          (i, Success(ParsedResult(acc, emptyMapSS)))
        }
      } else if (i.head == '#') {
        val spaceIndex = i.indexOf('\n')
        if (spaceIndex == -1) {
          (Seq.empty, Success(ParsedResult(input, emptyMapSS)))
        } else {
          go(i.drop(spaceIndex + 1), acc ++ i.take(spaceIndex + 1))
        }
      } else {
        if (acc.isEmpty) {
          (input, Failure(ParseException(input.mkString)))
        } else {
          (i, Success(ParsedResult(acc, emptyMapSS)))
        }
      }
    }
    go(input, Seq.empty)
  }

}
