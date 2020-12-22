package fmv1992.scala_cli_parser

object ParserCombinator {

  def or(a: Parser, b: Parser): Parser = {
    x =>
      a(x).orElse(b(x))
  }

  def chain(a: Parser, b: Parser): Parser = ???

  def many(a: Parser): Parser = ???

  def requireSucessful(a: Parser): Parser = {
    x =>
      {
        val res = a(x)
        require(res.isDefined)
        res
      }
  }

}
