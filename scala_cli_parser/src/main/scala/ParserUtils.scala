package fmv1992.scala_cli_parser

object ParserUtils {

  def or[A, B](
      p1: ParserWithEither[A, B],
      p2: ParserWithEither[A, B]
  ): ParserWithEither[A, B] = {
    (x: A) =>
      p1.parse(x) match {
        case Left(_)  => p2.parse(x)
        case Right(r) => Right(r)
      }
  }

  def tryAll[A, B](
      input: ParserWithEither[Seq[A], B]*
  ): ParserWithEither[Seq[A], Seq[B]] = {
    val parserSet = input.toSet
    def go(
        currentSegment: Seq[A],
        remainingSegment: Seq[A],
        validParsers: Set[ParserWithEither[Seq[A], B]],
        acc: Seq[B] = Seq.empty
    ): Either[Throwable, Seq[B]] = {
      if (remainingSegment.isEmpty) {
        val correctedAcc: Seq[B] = if (currentSegment.isEmpty) {
          acc
        } else {
          acc.appended(
            (validParsers.head
              .parse(currentSegment)
              .getOrElse(throw new Exception()): B)
          )
        }
        if (correctedAcc.isEmpty) {
          Left(null)
        } else {
          Right(correctedAcc)
        }
      } else {
        val newCurrentSegment = currentSegment.appended(remainingSegment.head)
        val newValidParsers =
          validParsers.filter(_.parse(newCurrentSegment).isRight)
        if (newValidParsers.size > 0) {
          go(newCurrentSegment, remainingSegment.tail, newValidParsers, acc)
        } else {
          go(
            Seq.empty,
            remainingSegment,
            parserSet,
            validParsers.head.parse(currentSegment) match {
              case Left(_)  => acc
              case Right(r) => acc.appended(r)
            }
          )
        }
      }
    }
    (x: Seq[A]) => {
      go(Seq.empty, x, parserSet)
    }
  }

}
