package fmv1992.scala_cli_parser

case class ParseException(
    val message: String = ""
) extends java.lang.Exception(message)

trait ErrorPosition
case class ErrorPositionExisting(line: Int, column: Int) extends ErrorPosition
case object ErrorPositionUnexisting extends ErrorPosition

object ParseException {

  def getExceptionPosition[A](
      input: Seq[A],
      errorFunction: Seq[A] => Boolean
  ): ErrorPosition = {
    def go(
        currentLine: Int = 0,
        currentColumn: Int = 0,
        currentChar: Int = 1
    ): ErrorPosition = {
      val curSlice = input.slice(0, currentChar)
      if (curSlice.length == input.length) {
        ErrorPositionUnexisting
      } else {
        if (errorFunction(curSlice)) {
          ErrorPositionExisting(currentLine, currentColumn)
        } else {
          if (curSlice.last == '\n') {
            go(currentLine + 1, 0, currentChar + 1)
          } else {
            go(currentLine, currentColumn + 1, currentChar + 1)
          }
        }
      }
    }
    go()
  }

  def getExceptionMessage[A, B](input: A, parser: Parser[A, B]): String = {
    input match {
      case _: Seq[Char] => input.toString
      case _            => input.toString
    }
  }

  def fromInput[A, B](
      input: Seq[A],
      parser: Parser[Seq[A], B]
  ): ParseException = {
    // ???: Another chance to use implicits maybe? (mark01)
    ParseException(
      ParseException
        .getExceptionPosition(
          input,
          (x: Seq[A]) => {
            scala.util.Try(parser.parse(x)).isFailure
          }
        )
        .toString
    )
  }

  def fromInput[A, B](
      input: Seq[A],
      parser: ParserWithEither[Seq[A], B]
  ): ParseException = {
    // ???: Another chance to use implicits maybe? (mark01)
    ParseException(
      ParseException
        .getExceptionPosition(
          input,
          (x: Seq[A]) => {
            parser.parse(x).isLeft
          }
        )
        .toString
    )
  }

}
