package fmv1992.scala_cli_parser

/** Parse a sequence of A and turn them into C.
  *
  *  Use B[A] to represent an internal state while accumulating.
  */
trait ParsedIntermediateState[A, B] {

  def accumulated: Iterable[A]

  def isPossibleInput(input: A): Boolean

  def hasMeaningfulInputAccumulated(): Boolean

  def accumulate(input: A): Either[Throwable, ParsedIntermediateState[A, B]]

}

abstract class PISWithAccumulated[A, B](val accumulated: Iterable[A])
    extends ParsedIntermediateState[A, B] {

  def accumulate(input: A): Either[Throwable, PISWithAccumulated[A, B]] = {
    if (isPossibleInput(input) && hasMeaningfulInputAccumulated()) {
      Right(PISWithAccumulated(accumulated ++ Iterable(input)))
    } else {
      Left(new Exception())
    }
  }

}

case class CommentLine(
    accumulated: Seq[Char]
) extends PISWithAccumulated(accumulated) {
// extends ParsedIntermediateState[Char, Map[String, String]] {

  def isPossibleInput(input: Char): Boolean = {
    if (input == "#" && accumulated.isEmpty) {
      true
    } else {
      false
    }
  }

  def hasMeaningfulInputAccumulated(): Boolean = {
    accumulated.head == "#"
  }

}

// case class StandardParserWithIntermediateState(
