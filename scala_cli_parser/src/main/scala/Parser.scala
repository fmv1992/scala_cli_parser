package fmv1992.scala_cli_parser

trait Parser[A, +B] {

  def parse(input: A): B

}

trait ParserWithEither[A, +B] extends Parser[A, Either[Throwable, B]] {

  def parse(input: A): Either[Throwable, B]

}

case class ParsedResult[A, +B](data: A, result: B) {}

object ParsedResult {

  def fromParser[A, B](parser: Parser[A, B]): A => ParsedResult[A, B] = {
    (data: A) => ParsedResult(data, parser.parse(data))
  }

}
