package fmv1992.scala_cli_parser

case class ParseException(
    val message: String = ""
) extends java.lang.Exception(message)

trait ErrorPosition

case class ErrorPositionExisting(line: Int, column: Int, position: Int)
    extends ErrorPosition
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
          ErrorPositionExisting(currentLine, currentColumn, currentChar)
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
      case inputAsSeq: Seq[Char] =>
        parser match {
          case pa: ParserWithTry[Seq[Char], B] => {
            val position: ErrorPosition =
              getExceptionPosition(input, (x: Seq[Char]) => ???)
            position match {
              case ErrorPositionUnexisting => input.toString
              case ErrorPositionExisting(_, _, humanPosition) => {
                val computerPosition = humanPosition - 1
                val leftContextSize = List(5, computerPosition).min
                val rightContextSize =
                  List(5, inputAsSeq.length - computerPosition).min
                val leftContext =
                  inputAsSeq
                    .slice(
                      computerPosition - leftContextSize,
                      computerPosition
                    )
                    .mkString
                val rightContext =
                  inputAsSeq
                    .slice(
                      computerPosition + 1,
                      computerPosition + rightContextSize + 1
                    )
                    .mkString
                s"'${parser.getClass.getSimpleName}': '${position}': '${leftContext + inputAsSeq
                  .slice(computerPosition, computerPosition + 1)
                  .mkString + rightContext}'."
              }
            }
          }
          case _ => input.toString
        }
      case _ => input.toString
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
      parser: ParserWithTry[Seq[A], B]
  ): ParseException = {
    // ???: Another chance to use implicits maybe? (mark01)
    ParseException(
      ParseException
        .getExceptionPosition(
          input,
          (x: Seq[A]) => {
            parser.parse(x).isFailure
          }
        )
        .toString
    )
  }

}
