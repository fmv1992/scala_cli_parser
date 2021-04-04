package fmv1992.scala_cli_parser

object SolidLineConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  def isValid(input: Seq[Char]): Boolean = {
    lazy val isNotEmpty = !input.isEmpty
    lazy val headIsSolidAndContainsColon =
      (!input.head.isWhitespace && input.tail.exists(_ == ':'))
    lazy val newLinePos = input.indexOf('\n')
    lazy val newLinesOnlyAtEnd =
      (newLinePos == -1) || (newLinePos == input.length - 1)
    isNotEmpty && headIsSolidAndContainsColon && newLinesOnlyAtEnd
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
          .trim
      )
    )
  }

}
