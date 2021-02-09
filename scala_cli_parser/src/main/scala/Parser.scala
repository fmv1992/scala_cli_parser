package fmv1992.scala_cli_parser

import scala.language.implicitConversions

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

  implicit def parserWithEitherToParsedResult[A, B](
      p: ParserWithEither[A, ParsedResult[A, B]]
      // e: Either[Throwable, B]
  ): Either[Throwable, ParsedResult[A, B]] = {
    ???
  }

}

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): F[A] => F[B] =
    fa => map(fa)(f)
}
