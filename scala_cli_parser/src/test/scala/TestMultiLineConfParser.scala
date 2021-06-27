// project scala_cli_parserCrossProjectJVM;testOnly fmv1992.scala_cli_parser.TestMultiLineConfParser

package fmv1992.scala_cli_parser

import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span

class TestMultiLineConfParser extends AnyFunSuite with TimeLimits {

  val valid01 = """
help: | cliarg
      |
      | other line
""".trim

  val inValid01 = """
help: | cliarg
      |
      | other line
""".trim

  test("`MultiLineConfParser` valid.")(failAfter(Span(200, Millis))({
    assert(
      MultiLineConfParser.isValid(valid01)
    )
  }))

  test("`MultiLineConfParser` invalid.")(failAfter(Span(200, Millis))({
    assert(
      !MultiLineConfParser.isValid(inValid01)
    )
  }))

  ignore("`ConfParser` full example.")(failAfter(Span(500, Millis))({
    val fullConfig =
      scala.io.Source
        .fromResource("test_multiline_01.txt")
        .getLines()
        .mkString("\n")
    assert(ConfParser.parse(fullConfig) === Map.empty)
  }))

}
