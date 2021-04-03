package fmv1992.scala_cli_parser

object MultiLineConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  // val MultilineTail = ParserUtils.and(SpaceConfParser, SolidLineWithPipe)

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Map[String, String]] = {
    ???
  }

  def isValid(input: Seq[Char]) = {
    // SingleLineConfParser.isVali
    ???
  }

}

object SolidLineWithPipe
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], Seq[Char]]
    ] {

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Seq[Char]] = {
    ParsedResult(input, input.tail)
  }

  def isValid(input: Seq[Char]) = {
    val isPipeFirst = input.headOption.map(_ == '|')
    val newLinePos = input.find(_ == '\n')
    newLinePos match {
      case Some(pos) =>
        (pos == input.length - 1) && isPipeFirst.getOrElse(false)
      case None => isPipeFirst.getOrElse(false)
    }
  }
}
