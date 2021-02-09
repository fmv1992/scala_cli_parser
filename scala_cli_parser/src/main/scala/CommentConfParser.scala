package fmv1992.scala_cli_parser

import ParsedResult.parserWithEitherToParsedResult

object CommentConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], String]
    ] {

  def parse(
      input: Seq[Char]
  ): Either[Throwable, ParsedResult[Seq[Char], String]] = {
    if (isValid(input)) {
      Right(input.mkString)
    } else {
      Left(ParseException())
    }
  }

  def isValid(data: Seq[Char]) = {
    @scala.annotation.tailrec
    def go(da: Seq[Char]): Boolean = {
      if (da.isEmpty) {
        true
      } else if (da.head == '#') {
        val spaceIndex = da.indexOf('\n')
        if (spaceIndex == -1) {
          true
        } else {
          go(da.drop(spaceIndex + 1))
        }
      } else {
        false
      }
    }
    go(data)
  }

}
