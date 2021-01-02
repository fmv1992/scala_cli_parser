package fmv1992.scala_cli_parser

/** Parse a sequence of A and turn them into C.
  */
trait ParsedIntermediateState[A, B] {

  def apply[C >: ParsedIntermediateState[A, B]](
      i: Seq[A]
  ): C

  def accumulated: Seq[A]

  def isPossibleInput(input: A): Boolean

  def getMeaningfulInput(): (ParsedIntermediateState[A, B], Seq[A])

  @scala.annotation.tailrec
  final def consume(
      i: Seq[A]
  ): (ParsedIntermediateState[A, B], Seq[A]) = {
    if (i.isEmpty) {
      val (validAcc, trailingInput) = this.getMeaningfulInput()
      (validAcc, trailingInput)
    } else {
      if (this.isPossibleInput(i.head)) {
        val newAcc: ParsedIntermediateState[A, B] =
          this(this.accumulated ++ Seq(i.head))
        newAcc.consume(i.tail)
      } else {
        val (validAcc, trailingInput) = this.getMeaningfulInput()
        (validAcc, trailingInput ++ i)
      }
    }
  }

}

// package fmv1992.scala_cli_parser
//
// /** Parse a sequence of A and turn them into C.
//   */
// trait ParsedIntermediateState[A, B] {
//
//   def apply(i: Iterable[A]): ParsedIntermediateState[A, B]
//
//   def accumulated: Iterable[A]
//
//   def isPossibleInput(input: A): Boolean
//
//   def getMeaningfulInput(): (ParsedIntermediateState[A, B], Iterable[A])
//
//   @scala.annotation.tailrec
//   final def consume(
//       i: Iterable[A]
//   ): (ParsedIntermediateState[A, B], Iterable[A]) = {
//     if (i.isEmpty) {
//       val (validAcc, trailingInput) = this.getMeaningfulInput()
//       (validAcc, trailingInput)
//     } else {
//       if (this.isPossibleInput(i.head)) {
//         val newAcc = this(this.accumulated ++ Iterable(i.head))
//         this.consume(i.tail)
//       } else {
//         val (validAcc, trailingInput) = this.getMeaningfulInput()
//         (validAcc, trailingInput ++ i)
//       }
//     }
//   }
//
// }
