package fmv1992.scala_cli_parser

case class ParserConcrete[A, B](f: A => B) extends Parser[A, B] {

  def parse(a: A) = f(a)

}
