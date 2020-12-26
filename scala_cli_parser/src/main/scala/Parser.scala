package fmv1992.scala_cli_parser

trait Parser[A, +B] {
// ???:         ↑
//              Why is this necessary? But it is not for A.

  def parse(input: A): Either[Seq[Throwable], B]

}
