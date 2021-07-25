// project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestSolidLineConfParser
package fmv1992.scala_cli_parser.test

import scala.util.Success

import fmv1992.scala_cli_parser._
import org.scalatest.funsuite.AnyFunSuite

class TestSolidLineConfParser extends AnyFunSuite {

  val valid01 = "name: cliarg"
  val invalid01 = "no colon line"

  test("`SolidLineConfParser.partialParse`.") {
    assert(
      (
        List(),
        Success(ParsedResult(valid01.toSeq, Map("name" -> "cliarg")))
      ) === SolidLineConfParser.partialParse(valid01)
    )
    assert(
      SolidLineConfParser.partialParse(invalid01)._2.isFailure
    )
  }

}
