package fmv1992.scala_cli_parser

case class SpaceConfParser(data: Seq[Char]) {

  def parse: Either[Throwable, ParsedResult[Seq[Char], String]] = {
    if (isValid) {
      Right(ParsedResult(data, data.mkString))
    } else {
      Left(ParseException())
    }
  }

  def isValid = {
    data.forall(_.isWhitespace)
  }

}

object SpaceConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], String]
    ] {

  def parse(
      input: Seq[Char]
  ): Either[Throwable, ParsedResult[Seq[Char], String]] = {
    SpaceConfParser(input).parse
  }

}
