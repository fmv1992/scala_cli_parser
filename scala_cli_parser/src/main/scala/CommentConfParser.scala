package fmv1992.scala_cli_parser

case class CommentConfParser(data: Seq[Char])
    extends ParserDataHolder[Seq[Char], String] {

  def parse: Either[Seq[Throwable], String] = {
    if (isValid) {
      Right(data.mkString)
    } else {
      Left(Seq(new Exception()))
    }
  }

  def isValid = {
    @scala.annotation.tailrec
    def go(da: Seq[Char]): Boolean = {
      if (da.isEmpty) {
        true
      } else if (da.head == '#') {
        val spaceIndex = da.indexOf('\n')
        if (spaceIndex == -1) {
          true
        } else {
          go(da.drop(spaceIndex + 1))
        }
      } else {
        false
      }
    }
    go(data)
  }

  //   def provideLines(
  //       rest: Seq[Char],
  //       current: IndexedSeq[Char] = IndexedSeq.empty
  //   ): LazyList[Seq[Char]] = {
  //     if (rest.isEmpty) {
  //       if (current.isEmpty) {
  //         LazyList.empty
  //       } else {
  //         LazyList(current)
  //       }
  //     } else {
  //       val head = rest.head
  //       if (head == '\n') {
  //         current #:: provideLines(rest.tail, IndexedSeq.empty)
  //       } else {
  //         provideLines(rest.tail, current.appended(head))
  //       }
  //     }
  //   }
  //   val lines = provideLines(intermediateState)
  //   if (lines.isEmpty) {
  //     false
  //   } else {
  //     lines.forall(x => isValidLine(x.mkString))
  //   }
  // }

  // @deprecated
  // def getFirstSignificantCharInLastLine: Option[Char] = {
  //   val newlinePos = intermediateState.lastIndexOf('\n')
  //   intermediateState.drop(newlinePos + 1).dropWhile(_.isWhitespace).headOption
  // }

  // def getFirstSignificantCharInLine(line: String): Option[Char] = {
  //   line.dropWhile(_.isWhitespace).headOption
  // }

  // def isValidLine(line: String): Boolean = {
  //   getFirstSignificantCharInLine(line) match {
  //     case Some(x)                           => x == '#'
  //     case None if intermediateState.isEmpty => true
  //     case None                              => false
  //   }
  // }

  // def getMeaningfulInput(): (
  //     ParsedIntermediateState[Char, Map[String, String], CommentLineIS],
  //     Seq[Char]
  // ) = {
  //   val commentLastPost = intermediateState.lastIndexOf('#')
  //   val newlineAfterLastCommentLastPos =
  //     intermediateState
  //       .drop(commentLastPost + 1)
  //       .lastIndexOf('\n') + (commentLastPost + 1) + 1
  //   val (valid, invalid) =
  //     intermediateState.splitAt(newlineAfterLastCommentLastPos)
  //   (CommentLineIS(valid), invalid)
  // }

}

object CommentConfParser extends Parser[Seq[Char], String] {

  def parse(input: Seq[Char]): Either[Seq[Throwable], String] = {
    CommentConfParser(input).parse
  }

}
