package fmv1992.scala_cli_parser

object SolidLineConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  def isValid(input: Seq[Char]): Boolean = {
    (!input.isEmpty) && (!input.head.isWhitespace && input.tail.exists(
      _ == ':'
    ))
  }

  def transform(
      input: Seq[Char]
  ): fmv1992.scala_cli_parser.ParsedResult[Seq[Char], Map[String, String]] = {
    val (left, right) =
      input.splitAt(input.indexOf(':'))
    ParsedResult(
      input,
      Map(
        left.mkString -> right.tail
          .dropWhile(_.isWhitespace)
          .mkString
          .dropWhile(_.isWhitespace)
      )
    )
  }

}

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
