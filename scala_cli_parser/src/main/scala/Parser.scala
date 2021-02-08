package fmv1992.scala_cli_parser

trait Parser[A, B] {

  def parse(input: A): B

}

trait ParserWithEither[A, B] extends Parser[A, Either[Throwable, B]] {

  def parse(input: A): Either[Throwable, B]

}
