package fmv1992.scala_cli_parser

case class CommentLineIS(override val accumulated: Seq[Char] = Seq.empty)
    extends Accumulator[Char]
    with ParsedIntermediateState[Char, Map[String, String]] {

  def update(
      i: Iterable[Char]
  ): ParsedIntermediateState[Char, Map[String, String]] = {
    if (i.isEmpty) {
      this
    } else if (isPossibleInput(i.head)) {
      CommentLineIS(i.toSeq)
    } else {
      throw new Exception()
    }
  }

  def getFirstSignificantCharInLastLine: Option[Char] = {
    val newlinePos = accumulated.lastIndexOf('\n')
    accumulated.drop(newlinePos + 1).dropWhile(_.isWhitespace).headOption
  }

  def isPossibleInput(input: Char): Boolean = {
    getFirstSignificantCharInLastLine match {
      case Some(x)                     => x == '#'
      case None if accumulated.isEmpty => input == '#'
      case None                        => input.isWhitespace || input == '#'
    }
  }

  def getMeaningfulInput()
      : (ParsedIntermediateState[Char, Map[String, String]], Iterable[Char]) = {
    val commentLastPost = accumulated.lastIndexOf('#')
    val newlineAfterLastCommentLastPos =
      accumulated
        .drop(commentLastPost + 1)
        .lastIndexOf('\n') + (commentLastPost + 1) + 1
    val (valid, invalid) = accumulated.splitAt(newlineAfterLastCommentLastPos)
    (CommentLineIS(valid), invalid)
  }

}
