package fmv1992.scala_cli_parser

trait Parser[A, B] {

  def parse(input: A): B

}
