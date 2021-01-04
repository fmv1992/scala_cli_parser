package fmv1992.scala_cli_parser

/** Parse a sequence of A and turn them into C.
  */
trait ParsedIntermediateState[A, B, T <: ParsedIntermediateState[A, B, T]] {
  self: T =>

  def intermediateState: Seq[A]

  def isValid: Boolean

  def getMeaningfulInput(): (ParsedIntermediateState[A, B, T], Seq[A])

  def createNewInstance(input: Seq[A]): T

  def update(input: Seq[A]): ParsedIntermediateState[A, B, T] = {
    self.createNewInstance(intermediateState ++ input)
  }

  // def incrementState(i: A): ParsedIntermediateState[A, B]

  // @scala.annotation.tailrec
  // final def consume(
  //     i: Seq[A]
  // ): (ParsedIntermediateState[A, B], Seq[A]) = {
  //   if (i.isEmpty) {
  //     val (validAcc, trailingInput) = this.getMeaningfulInput()
  //     (validAcc, trailingInput)
  //   } else {
  //     if (this.isPossibleInput(i.head)) {
  //       // This error shows up if we don't define an `apply` above.
  //       //
  //       // ```
  //       // [error]           this(this.accumulated ++ Seq(i.head)):
  //       // ```
  //       //
  //       // fmv1992.scala_cli_parser.ParsedIntermediateState[A,B] does not take parameters
  //       val newAcc: ParsedIntermediateState[A, B] =
  //         this(this.accumulated ++ Seq(i.head))
  //       newAcc.consume(i.tail)
  //     } else {
  //       val (validAcc, trailingInput) = this.getMeaningfulInput()
  //       (validAcc, trailingInput ++ i)
  //     }
  //   }
  // }

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
