package fmv1992.scala_cli_parser

import scala.util.Failure
import scala.util.Success
import scala.util.Try

object CommentConfParser
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
          go(i.drop(spaceIndex + 1), acc ++ i.take(spaceIndex))
        }
      } else {
        if (acc.isEmpty) {
          (Seq.empty, Failure(ParseException(input.mkString)))
        } else {
          (i, Success(ParsedResult(acc, emptyMapSS)))
        }
      }
    }
    go(input, Seq.empty)
  }

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Map[String, String]] =
    partialParse(input) match {
      case (_, Success(a)) => a
      case (_, Failure(_)) => throw new ParseException(input.mkString)
    }

}
