package fmv1992.scala_cli_parser

trait ParserWithIntermediateState[A, B[A], C] extends Parser[B[A], C] {

  def isPossibleInput(input: A): Boolean

}
