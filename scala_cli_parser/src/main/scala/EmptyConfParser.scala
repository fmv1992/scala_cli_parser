package fmv1992.scala_cli_parser

object EmptyConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], String]
    ] {

  def isValid(input: Seq[Char]): Boolean = {
    input.isEmpty
  }

  def transform(
      input: Seq[Char]
  ): fmv1992.scala_cli_parser.ParsedResult[Seq[Char], String] = {
    ParsedResult(input, input.mkString)
  }

  def getValidSubSequence(input: Seq[Char]) = {
    if (isValid(input)) {
      Some(input)
    } else {
      None
    }
  }

}
