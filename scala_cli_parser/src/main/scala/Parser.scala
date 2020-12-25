package fmv1992.scala_cli_parser

trait Parser[A, +B] {
// ???:         ↑
//              Why is this necessary?

  def parse(input: A): B

}
