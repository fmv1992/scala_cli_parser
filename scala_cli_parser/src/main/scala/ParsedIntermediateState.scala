package fmv1992.scala_cli_parser

/** Parse a sequence of A and turn them into C.
  */
trait ParsedIntermediateState[A, B] {

  def update(i: Iterable[A]): ParsedIntermediateState[A, B]

  def accumulated: Iterable[A]

  def isPossibleInput(input: A): Boolean

  def getMeaningfulInput(): (ParsedIntermediateState[A, B], Iterable[A])

  @scala.annotation.tailrec
  final def consume(
      i: Iterable[A],
      acc: ParsedIntermediateState[A, B] = update(Iterable.empty)
  ): (ParsedIntermediateState[A, B], Iterable[A]) = {
    if (i.isEmpty) {
      val (validAcc, trailingInput) = acc.getMeaningfulInput()
      (validAcc, trailingInput)
    } else {
      if (acc.isPossibleInput(i.head)) {
        val newAcc = acc.update(acc.accumulated ++ Iterable(i.head))
        newAcc.consume(i.tail, newAcc)
      } else {
        val (validAcc, trailingInput) = acc.getMeaningfulInput()
        (validAcc, trailingInput ++ i)
      }
    }
  }

}
