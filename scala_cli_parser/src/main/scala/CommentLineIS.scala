package fmv1992.scala_cli_parser

case class CommentLineIS(accumulated: Seq[Char])
    extends ParsedIntermediateState[Char, Map[String, String]] {

  // def apply[A, B, C[A, B] <: ParsedIntermediateState[A, B]](
  //     i: Seq[Char]
  // ): C = CommentLineIS(i.toSeq)
  def apply[C >: ParsedIntermediateState[Char, Map[String, String]]](
      i: Seq[Char]
  ): ParsedIntermediateState[Char, Map[String, String]] = CommentLineIS(i.toSeq)

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
      : (ParsedIntermediateState[Char, Map[String, String]], Seq[Char]) = {
    val commentLastPost = accumulated.lastIndexOf('#')
    val newlineAfterLastCommentLastPos =
      accumulated
        .drop(commentLastPost + 1)
        .lastIndexOf('\n') + (commentLastPost + 1) + 1
    val (valid, invalid) = accumulated.splitAt(newlineAfterLastCommentLastPos)
    (CommentLineIS(valid), invalid)
  }

}
