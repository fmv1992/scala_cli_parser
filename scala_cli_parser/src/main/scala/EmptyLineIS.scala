
package fmv1992.scala_cli_parser

case class EmptyLineIS(accumulated: Seq[Char] = Seq.empty)
    extends ParsedIntermediateState[Char, Map[String, String]] {

  def update(
      i: Iterable[Char]
  ): ParsedIntermediateState[Char, Map[String, String]] = {
    if (i.isEmpty) {
      this
    } else if (isPossibleInput(i.head)) {
      EmptyLineIS(i.toSeq)
    } else {
      throw new Exception()
    }
  }

  def isPossibleInput(input: Char): Boolean = input.isWhitespace

  def getMeaningfulInput()
      : (ParsedIntermediateState[Char, Map[String, String]], Iterable[Char]) = {
    (this, Iterable.empty)
  }

}
