package fmv1992.scala_cli_parser.conf

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import fmv1992.scala_cli_parser._

/** Parse a solid line (with no leading spaces). It includes the new line at
  * the end if it is present.
  */
private object SolidLineConfParser extends PP with PWT {

  override def parse(
      input: Seq[Char]
  ): Try[ParsedResult[Seq[Char], Map[String, String]]] =
    Try(super[ParserPartial].parse(input)) match {
      case Success(_) => Success(ParsedResult(input, emptyMapSS))
      case Failure(ParseException(m)) =>
        Failure(ParseException(s"${this.getClass.getName}': '${m}'."))
      case Failure(t) => Failure(t)
    }

  def partialParse(
      input: Seq[Char]
  ): (
      Seq[Char],
      Try[ParsedResult[Seq[Char], Map[String, String]]]
  ) = {
    val startsWithNonSpace = !input.head.isWhitespace
    if (startsWithNonSpace) {
      val firstLineIdx = input.indexOf('\n')
      val (firstLine: Seq[Char], rest: Seq[Char]) =
        if (firstLineIdx == -1) {
          (input, Seq.empty)
        } else {
          (input.take(firstLineIdx + 1), input.drop(firstLineIdx + 1))
        }
      val keyPos = firstLine.indexOf(':')
      if (keyPos == -1) {
        (input, Failure(ParseException(input.mkString)))
      } else {
        val key = firstLine.take(keyPos)
        val value = firstLine.drop(keyPos + 1)
        (
          rest,
          Success(
            ParsedResult(
              firstLine,
              Map(key.mkString.trim -> value.mkString.trim)
            )
          )
        )
      }
    } else {
      (input, Failure(ParseException(input.mkString)))
    }
  }

}
