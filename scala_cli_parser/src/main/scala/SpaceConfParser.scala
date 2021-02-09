package fmv1992.scala_cli_parser

object SpaceConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], String]
    ] {

  def parse(
      input: Seq[Char]
  ): Either[Throwable, ParsedResult[Seq[Char], String]] = {
    if (isValid(input)) {
      Right(ParsedResult(input, input.mkString))
    } else {
      Left(ParseException())
    }
  }

  def isValid(input: Seq[Char]) = {
    input.forall(_.isWhitespace)
  }

}
