package fmv1992.scala_cli_parser

/** Parse a sequence of A and turn them into C.
  */
trait ParsedIntermediateState[A, B] {

  def update(i: Iterable[A]): ParsedIntermediateState[A, B]

  def accumulated: Iterable[A]

  def isPossibleInput(input: A): Boolean

  def hasMeaningfulInputAccumulated(): Boolean

  def accumulate(input: A): Either[Throwable, ParsedIntermediateState[A, B]] = {
    if (isPossibleInput(input) && hasMeaningfulInputAccumulated()) {
      Right(this.update(accumulated ++ Iterable(input)))
    } else {
      Left(new Exception())
    }
  }

  @scala.annotation.tailrec
  final def consume(
      i: Iterable[A],
      acc: ParsedIntermediateState[A, B] = update(Iterable.empty)
  ): (ParsedIntermediateState[A, B], Iterable[A]) = {
    if (i.isEmpty) {
      (acc, i)
    } else {
      if (acc.isPossibleInput(i.head)) {
        val newAcc = acc.update(acc.accumulated ++ Iterable(i.head))
        newAcc.consume(i.tail, newAcc)
      } else {
        (acc, i)
      }
    }
  }

}

case class CommentLine(accumulated: Seq[Char] = Seq.empty)
    extends ParsedIntermediateState[Char, Map[String, String]] {

  def update(
      i: Iterable[Char]
  ): ParsedIntermediateState[Char, Map[String, String]] = {
    if (i.isEmpty) {
      this
    } else if (isPossibleInput(i.head)) {
      CommentLine(i.toSeq)
    } else {
      throw new Exception()
    }
  }

  def getFirstSignificantCharInLine: Option[Char] = {
    val newlinePos = accumulated.lastIndexOf('\n')
    accumulated.drop(newlinePos + 1).dropWhile(_.isWhitespace).headOption
  }

  def isPossibleInput(input: Char): Boolean = {
    getFirstSignificantCharInLine match {
      case Some(x)                     => x == '#'
      case None if accumulated.isEmpty => input == '#'
      case None                        => input.isWhitespace || input == '#'
    }
    // accumulated match {
    //   case Seq() => input == '#' || input.isWhitespace
    //   case head +: tail =>
    //     if (head.isWhitespace) {
    //       CommentLine(tail).isPossibleInput(input)
    //     } else {
    //       false
    //     }
    //   case _ => throw new Exception()
    // }
  }

  def hasMeaningfulInputAccumulated(): Boolean = {
    accumulated.head == '#'
  }

}

// case class StandardParserWithIntermediateState(
