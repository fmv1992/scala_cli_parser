// project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestSpaceConfParser

package fmv1992.scala_cli_parser

import scala.util.Success
import scala.util.Failure

import org.scalatest.funsuite.AnyFunSuite

class TestSpaceConfParser extends AnyFunSuite {

  val startsWithNonSpace = "# Comment 01.\n "

  test("`SpaceConfParser.parse`.") {
    assert(
      SpaceConfParser
        .parse(" \n ")
        .get === ParsedResult(" \n ".toSeq, emptyMapSS)
    )
    assertThrows[ParseException](
      SpaceConfParser
        .parse(" a ")
        .get
    )
  }

  test("`SpaceConfParser.partialParse`.") {
    assert(
      SpaceConfParser
        .partialParse(" a\t".toSeq)
        === (
          "a\t".toSeq,
          Success(ParsedResult(" ".toSeq, emptyMapSS))
      )
    )
    // Test failure for a string that starts with no space.
    val parseFailureExpected: Tuple2[Seq[Char], scala.util.Try[
      fmv1992.scala_cli_parser.ParsedResult[Seq[Char], Map[String, String]]
    ]] =
      SpaceConfParser.partialParse(startsWithNonSpace)
    val parseFailureTrue: Tuple2[Seq[Char], scala.util.Try[
      fmv1992.scala_cli_parser.ParsedResult[Seq[Char], Map[String, String]]
    ]] =
      (startsWithNonSpace.toSeq, Failure(ParseException(startsWithNonSpace)))
    assert(
      parseFailureTrue === parseFailureExpected
    )
  }

}
