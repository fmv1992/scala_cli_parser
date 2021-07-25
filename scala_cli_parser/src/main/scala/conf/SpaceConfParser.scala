package fmv1992.scala_cli_parser.conf

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import fmv1992.scala_cli_parser._

object SpaceConfParser extends PP with PWT {

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
  ): (
      Seq[Char],
      Try[ParsedResult[Seq[Char], Map[String, String]]]
  ) = {
    // ???: This gives redundant info.
    // The first item of the tuple being empty already indicates an error.
    val parsed = input.takeWhile(_.isWhitespace)
    val parsedNot = input.drop(parsed.length)
    // require(parsed ++ parsedNot == input)
    val parsedResult = if (parsed.isEmpty) {
      Failure(ParseException(input.mkString))
    } else {
      Success(ParsedResult(parsed, emptyMapSS))
    }
    (parsedNot, parsedResult)
  }

}
