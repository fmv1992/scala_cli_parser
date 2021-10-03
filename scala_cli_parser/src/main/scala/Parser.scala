package fmv1992.scala_cli_parser

import scala.util.Try

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
      throw new ParseException(
        s"${this.getClass.getName}': '${input.toString}'."
      )
    }
  }
}

private case class ParserPartialImpl[A <: Seq[_], +B](
    private val _partialParse: A => (A, B)
) extends ParserPartial[A, B]
    with Parser[A, B] {

  def partialParse(input: A): (A, B) = _partialParse(input)
}

trait ParserWithTry[A, +B] extends Parser[A, Try[B]] {

  def parse(input: A): Try[B]

}

private case class ParserWithTryImpl[A, +B](private val _transform: A => B)
    extends ParserWithTry[A, B] {

  def parse(input: A): Try[B] = Try(_transform(input))

}

private case class ParsedResult[A, +B](data: A, result: B)
