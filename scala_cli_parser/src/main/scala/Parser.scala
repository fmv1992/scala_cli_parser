package fmv1992.scala_cli_parser

trait Parser[-A, +B] {

  def parse(input: A): B

}

trait ParserWithEither[A, +B] extends Parser[A, Either[Throwable, B]] {

  def parse(
      input: A
  ): Either[Throwable, B] = {
    if (isValid(input)) {
      Right(transform(input))
    } else {
      Left(ParseException(input.toString))
    }
  }

  def transform(input: A): B

  def isValid(input: A): Boolean

  def getValidSubSequence(input: A): Option[A]
}

/** CURRENT: The issue is that `ParserImpl` gets the `transform` function but
  * it is more suitable to have a `getValidSubSequence` so that parser
  * combiners could combine them.
  */
case class ParserImpl[A, +B](private val _transform: A => B)
    extends ParserWithEither[A, B] {

  def transform(input: A): B = _transform(input)

  def isValid(input: A): Boolean = scala.util.Try(transform(input)).isSuccess

  def getValidSubSequence(input: A): Option[A] = {
    if (isValid(input)) {
      Some(input)
    } else {
      None
    }
  }
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
