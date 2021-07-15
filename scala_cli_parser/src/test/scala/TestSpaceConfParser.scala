// project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestSpaceConfParser

package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite
import scala.util.Try
import scala.util.Success

class TestSpaceConfParser extends AnyFunSuite {

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
  }

}
