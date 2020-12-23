package fmv1992.scala_cli_parser

object ParserCombinator {

  def or(a: OldParserType, b: OldParserType): OldParserType = {
    x =>
      a(x).orElse(b(x))
  }

  def chain(a: OldParserType, b: OldParserType): OldParserType = ???

  def many(a: OldParserType): OldParserType = ???

  def requireSucessful(a: OldParserType): OldParserType = {
    x =>
      {
        val res = a(x)
        require(res.isDefined)
        res
      }
  }

}
