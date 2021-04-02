package fmv1992.scala_cli_parser

trait Parser[-A, +B] {

  def parse(input: A): B

}

trait ParserWithEither[-A, +B] extends Parser[A, Either[Throwable, B]] {

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
}

object ParserImpl {

  // def apply[A, B](
  // transformEither: A => Either[Throwable, B]
  // ): ParserImpl[A, B] = {
  // ParserImpl((x: A) => transformEither(x))
  // }

  // def apply[A, B](transform: A => B) = {
  // ParserImpl(transformEither)
  // }

}

case class ParsedResult[A, +B](data: A, result: B) {}
