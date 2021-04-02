package fmv1992.scala_cli_parser

object ParserUtils {

  def or[A, B](
      p1: ParserWithEither[A, B],
      p2: ParserWithEither[A, B]
  ): ParserWithEither[A, B] = {
    ParserImpl(
      (x: A) =>
        p2.parse(x).getOrElse(p1.parse(x).getOrElse(throw new Exception()))
    )
  }

  def and[A, B](
      p1: ParserWithEither[Seq[Char], ParsedResult[Seq[Char], String]],
      p2: ParserWithEither[Seq[Char], ParsedResult[Seq[Char], String]],
      combiner: (A, A) => B
  ): ParserWithEither[Seq[_], B] = {
    // Tries to find the longest valid sequence for `p1`.
    ParserImpl(
      (x: Seq[_]) => {
        ???
      }
    )
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
    ): Seq[B] = {
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
          Seq.empty
        } else {
          correctedAcc
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
    ParserImpl((x: Seq[A]) => {
      go(Seq.empty, x, parserSet)
    })
  }

  def allSubsequencesFromStart[A](s: Seq[A]): Seq[Seq[A]] = {
    (0 to s.length).map(l1 => s.slice(0, l1))
  }

}
