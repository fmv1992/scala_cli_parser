package fmv1992.scala_cli_parser

/** Parse a solid line (with no leading spaces). It includes the new line at
  * the end.
  */
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
    lazy val newLinesOnlyAtEnd = (newLinePos == input.length - 1)
    // lazy val newLinesOnlyAtEnd =
    // (newLinePos == -1) || (newLinePos == input.length - 1)
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

  def getValidSubSequence(input: Seq[Char]): Option[Seq[Char]] = {
    val newLinePos = input.indexOf('\n')
    val line = if (newLinePos == -1) input else input.slice(0, newLinePos)
    if (isValid(line)) {
      Some(line)
    } else {
      None
    }
  }

}
