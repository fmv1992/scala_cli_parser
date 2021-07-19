/** Notice the "semi" import here. */
package fmv1992

import scala.util.Failure
import scala.util.Success
import scala.util.Try

/** Provide utilities to parse, check and manipulate CLI arguments. */
package object scala_cli_parser {

  type PWT =
    ParserWithTry[Seq[Char], ParsedResult[Seq[Char], Map[String, String]]]

  type PP =
    ParserPartial[Seq[Char], Try[ParsedResult[Seq[Char], Map[String, String]]]]

  val emptyMapSS: Map[String, String] = Map.empty

  implicit val combinerSimple: (
      Try[ParsedResult[Seq[Char], Map[String, String]]],
      Try[ParsedResult[Seq[Char], Map[String, String]]]
  ) => Try[ParsedResult[Seq[Char], Map[String, String]]] = (a, b) =>
    // a match {
    //   case Success(x) =>
    //     b match {
    //       case Success(y) =>
    //         Success(ParsedResult(x.data ++ y.data, x.result ++ y.result))
    //       case Failure(_) => throw new Exception()
    //     }
    //   case Failure(_) => throw new Exception()
    // }
    a.flatMap(
      a_ =>
        b.map(
          b_ => ParsedResult(a_.data ++ b_.data, a_.result ++ b_.result)
        )
    )

  implicit val combinerParsedResultSimple: (
      ParsedResult[Seq[Char], Map[String, String]],
      ParsedResult[Seq[Char], Map[String, String]]
  ) => ParsedResult[Seq[Char], Map[String, String]] =
    (a, b) =>
      ParsedResult(
        a.data ++ b.data,
        a.result ++ b.result
      )

  def parserPartialToParserWithTry[A <: Seq[_], B](
      p: ParserPartial[A, B]
  ): ParserWithTry[A, B] = {
    ParserWithTryImpl((x: A) => {
      val (rest, parsed) = p.partialParse(x)
      if (rest.isEmpty) {
        parsed
      } else {
        throw new ParseException(
          s"Input: '${x.mkString}' → rest: '${rest.mkString}'."
        )
      }
    })
  }

  def parserWithTryToParserPartial[A <: Seq[_], B](
      p: ParserWithTry[A, B]
  )(zero: B): ParserPartial[A, B] = {
    ParserPartialImpl((x: A) => {
      p.parse(x) match {
        case Success(y) => ???
        case Failure(_) => (x, zero)
      }
    })
  }

}
