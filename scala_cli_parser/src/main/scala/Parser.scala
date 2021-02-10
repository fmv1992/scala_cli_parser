package fmv1992.scala_cli_parser

trait Parser[A, +B] {

  def parse(input: A): B

}

trait ParserWithEither[A, +B] extends Parser[A, Either[Throwable, B]] {

  def parse(
      input: A
  ): Either[Throwable, B] = {
    if (isValid(input)) {
      Right(transform(input))
    } else {
      Left(ParseException())
    }
  }

  def transform(input: A): B

  def isValid(input: A): Boolean
}

case class ParserImpl[A, +B](private val _transform: A => B)
    extends ParserWithEither[A, B] {

  def transform(input: A): B = _transform(input)

  def isValid(input: A): Boolean = scala.util.Try(transform(input)).isSuccess
  // def isValid(input: A): Boolean = parse(input).isRight
}

case class ParsedResult[A, +B](data: A, result: B) {}

// object ParsedResult {
//
//   def fromParser[A, B](parser: Parser[A, B]): A => ParsedResult[A, B] = {
//     (data: A) => ParsedResult(data, parser.parse(data))
//   }
//
// }
