package fmv1992.scala_cli_parser

object SpaceConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], String]
    ] {

  override def parse(
      input: Seq[Char]
  ): Either[Throwable, ParsedResult[Seq[Char], String]] = {
    if (isValid(input)) {
      Right(transform(input))
    } else {
      Left(ParseException())
    }
  }

  def transform(input: Seq[Char]): ParsedResult[Seq[Char], String] =
    ParsedResult(input, input.mkString)

  def isValid(input: Seq[Char]) = {
    input.forall(_.isWhitespace)
  }

}
