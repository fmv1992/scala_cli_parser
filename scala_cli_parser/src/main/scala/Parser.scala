package fmv1992.scala_cli_parser

import scala.language.implicitConversions

trait Parser[A, +B] {

  def parse(input: A): B

}

trait ParserWithEither[A, +B] extends Parser[A, Either[Throwable, B]] {

  def parse(input: A): Either[Throwable, B]

  def isValid(input: A): Boolean

}

case class ParsedResult[A, +B](data: A, result: B) {}

object ParsedResult {

  def fromParser[A, B](parser: Parser[A, B]): A => ParsedResult[A, B] = {
    (data: A) => ParsedResult(data, parser.parse(data))
  }

  // It is not possible to "lift" something to `ParsedResult` as it has two
  // parameters. How is it going to find the input parameter?
  implicit def parserWithEitherToParsedResult[A, B](
      // p: ParserWithEither[A, ParsedResult[A, B]]
      // e: Either[Throwable, B]
      b: B
  ): Either[Throwable, ParsedResult[A, B]] = {
    ???
  }

}
