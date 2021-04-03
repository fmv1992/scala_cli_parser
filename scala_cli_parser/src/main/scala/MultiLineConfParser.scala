package fmv1992.scala_cli_parser

object MultiLineConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  override def parse(
      input: Seq[Char]
  ): Either[Throwable, ParsedResult[Seq[Char], Map[String, String]]] = {
    ???
  }

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Map[String, String]] =
    ???

  def isValid(input: Seq[Char]) = {
    ???
  }

}
