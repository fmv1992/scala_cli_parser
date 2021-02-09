package fmv1992.scala_cli_parser

object CommentConfParser
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
      // ???: Another chance to use implicits maybe? (mark01)
      Left(ParseException.fromInput(input, CommentConfParser))
    }
  }

  def isValid(input: Seq[Char]) = {
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
    go(input)
  }

  def transform(input: Seq[Char]): ParsedResult[Seq[Char], String] =
    ParsedResult(input, input.mkString)

}
