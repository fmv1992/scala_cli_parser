// project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestMultiLineConfParser

package fmv1992.scala_cli_parser

import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span
import scala.util.Success

class TestMultiLineConfParser extends AnyFunSuite with TimeLimits {

  val valid01 = """
help: | cliarg
      |
      | other line
 """.trim

  val invalid01 = """
 help: | cliarg
      |
       | other line
 """.trim

  test("`MultiLineConfParser.partialParse` valid.")(
    failAfter(Span(200_000, Millis))({
      assert(
        (
          "".toSeq,
          Success(
            ParsedResult(valid01.toSeq, Map("help" -> "cliarg\n\nother line"))
          )
        ) ===
          MultiLineConfParser.partialParse(valid01)
      )
    })
  )

  ignore("`MultiLineConfParser.transform` valid.")(
    failAfter(Span(200_000, Millis))({
      assert(
        (
          "".toSeq,
          ParsedResult(valid01.toSeq, Map("help" -> "cliarg\n\nother line"))
        ) ===
          MultiLineConfParser.transform(valid01)
      )
    })
  )

  ignore("`MultiLineConfParser.transform` invalid.")(
    failAfter(Span(200_000, Millis))({
      assertThrows[ParseException](
        MultiLineConfParser.transform(invalid01)
      )
    })
  )

  // test("`MultiLineConfParser` invalid.")(failAfter(Span(200000, Millis))({
  // assert(
  // !MultiLineConfParser.isValid(inValid01)
  // )
  // }))

  // ignore("`ConfParser` full example.")(failAfter(Span(500, Millis))({
  //   val fullConfig =
  //     scala.io.Source
  //       .fromResource("test_multiline_01.txt")
  //       .getLines()
  //       .mkString("\n")
  //   // assert(ConfParser.parse(fullConfig) === Map.empty)
  // }))

}
