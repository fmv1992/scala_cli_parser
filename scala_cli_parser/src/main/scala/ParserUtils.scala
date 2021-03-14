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
      p1: ParserWithEither[Seq[_], A],
      p2: ParserWithEither[Seq[_], A],
      combiner: (A, A) => B
  ): ParserWithEither[Seq[_], B] = {
    // Tries to find the longest valid sequence for `p1`.
    ParserImpl(
      (x: Seq[_]) => {
        val allSubSequences =
          (0 to x.length).reverse.toIterator
            // ???: I'm not sure this `combinations` is the right operator
            // here.
            .flatMap(i => x.combinations(i))
        val maxSize: Int =
          allSubSequences.filter(p1.isValid(_)).to(LazyList).head.length
        val left = x.slice(0, maxSize)
        val right = x.drop(maxSize)
        val res = p1
          .parse(left)
          .flatMap(x => p2.parse(right).map(y => combiner(x, y)))
        res match {
          case Left(x)  => throw x
          case Right(x) => x
        }
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

}
