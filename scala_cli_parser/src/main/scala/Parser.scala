package fmv1992.scala_cli_parser

import scala.util.Try
import scala.util.Success
import scala.util.Failure

trait Parser[-A, +B] {

  def parse(input: A): B

}

trait ParserPartial[A <: Seq[_], +B] {
  this: Parser[A, B] =>

  // ???: Not sure I get the variances right here.
  def partialParse(input: A): (A, B)

  def parse(input: A): B = {
    val pp = partialParse(input)
    if (pp._1.isEmpty) {
      pp._2
    } else {
      throw new ParseException(input.toString)
    }
  }
}

trait ParserWithTry[A, +B] extends Parser[A, Try[B]] {

  def parse(input: A): Try[B] = Try(transform(input))

  def transform(input: A): B

}

case class ParserImpl[A, +B](private val _transform: A => B)
    extends ParserWithTry[A, B] {

  def transform(input: A): B = _transform(input)

}

case class ParsedResult[A, +B](data: A, result: B)
