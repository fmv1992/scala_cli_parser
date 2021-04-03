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

  test("`MultiLineConfParser` valid.")(failAfter(Span(100, Millis))({
    assert(
      MultiLineConfParser.isValid(valid01)
    )
  }))

}
