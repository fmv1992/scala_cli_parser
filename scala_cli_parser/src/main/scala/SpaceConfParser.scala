package fmv1992.scala_cli_parser

import scala.util.Try
import scala.util.Success
import scala.util.Failure

object SpaceConfParser
    extends ParserPartial[
      Seq[Char],
      Try[ParsedResult[Seq[Char], Map[String, String]]]
    ]
    with ParserWithTry[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  override def parse(
      input: Seq[Char]
  ): Try[ParsedResult[Seq[Char], Map[String, String]]] =
    super[ParserPartial].parse(input)

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
      // Console.err.println("|" + parsed.mkString + "|")
      Success(ParsedResult(parsed, emptyMapSS))
    }
    (parsedNot, parsedResult)
  }

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Map[String, String]] =
    if (input.forall(_.isWhitespace)) {
      ParsedResult(input, emptyMapSS)
    } else {
      throw new ParseException(input.mkString)
    }

}
