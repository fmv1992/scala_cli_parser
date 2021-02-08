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

  def tryAll[E, B](
      input: ParserWithEither[Seq[E], B]*
  ): ParserWithEither[Seq[E], Seq[B]] = {
    val parserSet = input.toSet
    def go(
        currentSegment: Seq[E],
        remainingSegment: Seq[E],
        validParsers: Set[ParserWithEither[Seq[E], B]],
        acc: Seq[B] = Seq.empty,
        parsed: Option[B] = None
    ): Either[Throwable, Seq[B]] = {
      println("-" * 79)
      println(currentSegment)
      println(remainingSegment)
      println(validParsers)
      println(acc)
      println(parsed)
      println("-" * 79)
      if (remainingSegment.isEmpty) {
        if (currentSegment.isEmpty) {
          Right(acc)
        } else {
          Right(acc.appended(parsed.getOrElse(throw new Exception())))
        }
      } else {
        if (validParsers.isEmpty) {
          val last = currentSegment.last
          val remainder = currentSegment.dropRight(1)
          go(
            remainder,
            last +: remainingSegment,
            parserSet,
            acc.appended(parsed.getOrElse(throw new Exception())),
            None
          )
        } else {
          val newSet = validParsers.filter(x => x.parse(currentSegment).isRight)
          val parsed = if (newSet.size == 1) {
            Some(
              newSet.head.parse(currentSegment).getOrElse(throw new Exception())
            )
          } else {
            None
          }
          go(
            currentSegment.appended(remainingSegment.head),
            remainingSegment.tail,
            newSet,
            acc,
            parsed
          )
        }
      }
    }
    (x: Seq[E]) => {
      go(Seq(x.head), x.tail, parserSet)
    }
  }

}
