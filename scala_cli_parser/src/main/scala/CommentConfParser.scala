package fmv1992.scala_cli_parser

object CommentConfParser
    extends ParserWithEither2[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  override def parse(
      input: Seq[Char]
  ): Either[Throwable, ParsedResult[Seq[Char], Map[String, String]]] = {
    if (isValid(input)) {
      Right(transform(input))
    } else {
      // ???: Another chance to use implicits maybe? (mark01)
      Left(ParseException.fromInput(input, CommentConfParser))
    }
  }

  /** Valid lines are non-empty lines starting with the comment character. They
    *  are parsed up to and including the newline.
    */
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
    if (input.isEmpty) false else go(input)
  }

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Map[String, String]] =
    ParsedResult(input, emptyMapSS)

  def getValidSubSequence(input: Seq[Char]): Option[Int] = {
    val idx = input.indexOf('\n')
    if (idx == -1) {
      if (isValid(input)) {
        Some(input.length)
      } else {
        None
      }
    } else {
      if (isValid(input.slice(0, idx))) {
        Some(idx)
      } else {
        None
      }
    }
  }

}
