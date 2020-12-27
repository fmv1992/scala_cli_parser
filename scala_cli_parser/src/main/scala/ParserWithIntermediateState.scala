package fmv1992.scala_cli_parser

/** Parse a sequence of A and turn them into C.
  *
  *  Use B[A] to represent an internal state while accumulating.
  */
trait ParsedIntermediateState[A, B] {

  def cons(i: Iterable[A]): ParsedIntermediateState[A, B]

  def accumulated: Iterable[A]

  def isPossibleInput(input: A): Boolean

  def hasMeaningfulInputAccumulated(): Boolean

  def accumulate(input: A): Either[Throwable, ParsedIntermediateState[A, B]] = {
    if (isPossibleInput(input) && hasMeaningfulInputAccumulated()) {
      Right(this.cons(accumulated ++ Iterable(input)))
    } else {
      Left(new Exception())
    }
  }

  @scala.annotation.tailrec
  final def consume(
      i: Iterable[A],
      acc: ParsedIntermediateState[A, B] = cons(Iterable.empty)
  ): (ParsedIntermediateState[A, B], Iterable[A]) = {
    if (i.isEmpty) {
      (acc, i)
    } else {
      if (acc.isPossibleInput(i.head)) {
        val newAcc = acc.cons(acc.accumulated ++ Iterable(i.head))
        newAcc.consume(i.tail, newAcc)
      } else {
        (acc, i)
      }
    }
  }

}

case class CommentLine(accumulated: Seq[Char] = Seq.empty)
    extends ParsedIntermediateState[Char, Map[String, String]] {

  def cons(
      i: Iterable[Char]
  ): ParsedIntermediateState[Char, Map[String, String]] = {
    if (isPossibleInput(i.head)) {
      CommentLine(i.toSeq)
    } else {
      throw new Exception()
    }
  }

  def isPossibleInput(input: Char): Boolean = {
    if (input == '#' && accumulated.isEmpty) {
      true
    } else {
      false
    }
  }

  def hasMeaningfulInputAccumulated(): Boolean = {
    accumulated.head == '#'
  }

}

// case class StandardParserWithIntermediateState(
