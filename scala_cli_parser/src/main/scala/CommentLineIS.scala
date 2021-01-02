package fmv1992.scala_cli_parser

case class CommentLineIS private (accumulated: Seq[Char])
    extends ParsedIntermediateState[Char, Map[String, String]] {

  // // def apply[A, B, C[A, B] <: ParsedIntermediateState[A, B]](
  // //     i: Seq[Char]
  // // ): C = CommentLineIS(i.toSeq)
  // def apply[C >: ParsedIntermediateState[Char, Map[String, String]]](
  //     i: Seq[Char]
  // ): ParsedIntermediateState[Char, Map[String, String]] = CommentLineIS(i.toSeq)

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

object CommentLineIS {

  // [error] /home/monteirobd/dev/pud/_other/not_yet_commited_projects/scala_cli_parser/scala_cli_parser/src/main/scala/CommentLineIS.scala:34:6: ambiguous reference to overloaded definition,
  // [error] both method apply in object CommentLineIS of type (accumulated: Seq[Char]): fmv1992.scala_cli_parser.CommentLineIS
  // [error] and  method apply in object CommentLineIS of type [C >: fmv1992.scala_cli_parser.ParsedIntermediateState[Char,Map[String,String]]](i: Seq[Char]): C
  // [error] match argument types (Seq[Char]) and expected result type fmv1992.scala_cli_parser.ParsedIntermediateState[Char,Map[String,String]]
  // [error]     (CommentLineIS(valid), invalid)
  // [error]      ^

  // def apply[A, B, C[A, B] <: ParsedIntermediateState[A, B]](
  //     i: Seq[Char]
  // ): C = CommentLineIS(i.toSeq)
  def apply[C >: ParsedIntermediateState[Char, Map[String, String]]](
      i: Seq[Char]
  ): C = CommentLineIS(i.toSeq)

}
