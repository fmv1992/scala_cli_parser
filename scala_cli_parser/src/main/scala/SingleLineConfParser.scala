package fmv1992.scala_cli_parser

object SingleLineConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], String]
    ] {

  def isValid(input: Seq[Char]): Boolean = {
    ???
  }

  def transform(
      input: Seq[Char]
  ): fmv1992.scala_cli_parser.ParsedResult[Seq[Char], String] = {
    ???
  }

}
