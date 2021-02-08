package fmv1992.scala_cli_parser

object ParserCombinator {

  def or[A, B](p1: Parser[A, B], p2: Parser[A, B]): Parser[A, B] = {
    ParserConcrete(
      (x: A) =>
        orElseEitherShim(
          p1.parse(x),
          p2.parse(x)
        )
    )
  }

  // def chain(a: Parser, b: Parser): Parser = ???

  // def many(a: Parser): Parser = ???

  def requireSucessful[A, B](p1: Parser[A, B]): Parser[A, B] = {
    ParserConcrete((x: A) => p1.parse(x))
  }

}

object ParserCombinatorIncremental {

  def sequentialAny[A <: Seq[_], B](parsers: Parser[A, B]*): Parser[A, B] = {
    def consumeData(
        data: A,
        parserSet: Set[Parser[A, B]],
        acc: Seq[Either[Seq[Throwable], B]] = Seq.empty
    ): Either[Seq[Throwable], B] = {
      if (data.isEmpty) {
        acc
      } else {}
    }
    val parserSet = parsers.toSet
    ParserConcrete((x: A) => {
      val consumed = consumeData(x)
      val consolidated: Either[Seq[Throwable], B] = ???
      consolidated
    })
  }

}
