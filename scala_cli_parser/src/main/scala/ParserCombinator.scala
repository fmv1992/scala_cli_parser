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
