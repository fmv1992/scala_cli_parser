package fmv1992.scala_cli_parser

case class CommentLineIS(intermediateState: Seq[Char])
    extends ParsedIntermediateState[Char, Map[String, String]] {

  def isValid: Boolean = ???

  def copy(
      intermediateState: Seq[Char]
  ): ParsedIntermediateState[Char, Map[String, String]] =
    CommentLineIS(intermediateState)

  def getFirstSignificantCharInLastLine: Option[Char] = {
    val newlinePos = intermediateState.lastIndexOf('\n')
    intermediateState.drop(newlinePos + 1).dropWhile(_.isWhitespace).headOption
  }

  def isPossibleInput(input: Char): Boolean = {
    getFirstSignificantCharInLastLine match {
      case Some(x)                           => x == '#'
      case None if intermediateState.isEmpty => input == '#'
      case None                              => input.isWhitespace || input == '#'
    }
  }

  def getMeaningfulInput()
      : (ParsedIntermediateState[Char, Map[String, String]], Seq[Char]) = {
    val commentLastPost = intermediateState.lastIndexOf('#')
    val newlineAfterLastCommentLastPos =
      intermediateState
        .drop(commentLastPost + 1)
        .lastIndexOf('\n') + (commentLastPost + 1) + 1
    val (valid, invalid) =
      intermediateState.splitAt(newlineAfterLastCommentLastPos)
    (CommentLineIS(valid), invalid)
  }

}

// https://stackoverflow.com/questions/65544763/how-to-force-case-class-constructors-to-have-a-pre-defined-signature-in-scala
//
// https://stackoverflow.com/questions/15441589/scala-copy-case-class-with-generic-type
trait Entity[E <: Entity[E]] {
  // self-typing to E to force withId to return this type
  self: E =>
  def id: Option[Long]
  def withId(id: Long): E
}

case class Foo(id: Option[Long]) extends Entity[Foo] {
  def withId(id: Long) = this.copy(id = Some(id))
}
