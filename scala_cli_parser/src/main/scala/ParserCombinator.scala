package fmv1992.scala_cli_parser

object ParserCombinator {

  def or[p1, p2](p1: Parser[p1, p2], p2: Parser[p1, p2]): Parser[p1, p2] = {
    ParserConcrete(
      (x: p1) => {
        try {
          p1.parse(x)
        } catch {
          case _: Exception => p2.parse(x)
        }
      }
    )
  }

  // def chain(a: Parser, b: Parser): Parser = ???

  // def many(a: Parser): Parser = ???

  def requireSucessful[A, B](p1: Parser[A, B]): Parser[A, B] = {
    ParserConcrete((x: A) => p1.parse(x))
  }

}
