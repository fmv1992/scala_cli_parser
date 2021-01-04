package fmv1992.scala_cli_parser

case class CommentLineIS(intermediateState: Seq[Char])
    extends ParsedIntermediateState[Char, Map[String, String], CommentLineIS] {

  def createNewInstance(input: Seq[Char]): CommentLineIS = CommentLineIS(input)

  def isValid: Boolean = {
    @scala.annotation.tailrec
    def provideLines(
        rest: Seq[Char],
        current: IndexedSeq[Char] = IndexedSeq.empty,
        // acc: Seq[Seq[Char]] = scala.collection.immutable.Queue.empty
        acc: Seq[Seq[Char]] = LazyList.empty
    ): Seq[Seq[Char]] = {
      if (rest.isEmpty) {
        if (current.isEmpty) {
          acc
        } else {
          acc.appended(current)
        }
      } else {
        val head = rest.head
        if (head == '\n') {
          provideLines(rest.tail, IndexedSeq.empty, acc.appended(current))
        } else {
          provideLines(rest.tail, current.appended(head), acc)
        }
      }
    }
    val lines = provideLines(intermediateState)
    if (lines.isEmpty) {
      false
    } else {
      lines.forall(x => isValidLine(x.mkString))
    }
  }

  @deprecated
  def getFirstSignificantCharInLastLine: Option[Char] = {
    val newlinePos = intermediateState.lastIndexOf('\n')
    intermediateState.drop(newlinePos + 1).dropWhile(_.isWhitespace).headOption
  }

  def getFirstSignificantCharInLine(line: String): Option[Char] = {
    line.dropWhile(_.isWhitespace).headOption
  }

  def isValidLine(line: String): Boolean = {
    getFirstSignificantCharInLine(line) match {
      case Some(x)                           => x == '#'
      case None if intermediateState.isEmpty => true
      case None                              => false
    }
  }

  def getMeaningfulInput(): (
      ParsedIntermediateState[Char, Map[String, String], CommentLineIS],
      Seq[Char]
  ) = {
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
