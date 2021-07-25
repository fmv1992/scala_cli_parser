// project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestMultiLineConfParser
package fmv1992.scala_cli_parser.test

import scala.util.Success

import fmv1992.scala_cli_parser._
import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span

class TestMultiLineConfParser extends AnyFunSuite with TimeLimits {

  val valid01 = """
help: | cliarg
      |
      | other line
""".trim + "\n"

  val invalid01 = """
help: | cliarg
      |
       | other line
""".trim

  test("`MultiLineConfParser.partialParse` valid.")(
    failAfter(Span(200_000, Millis))({
      val parseResult = MultiLineConfParser.partialParse(valid01)
      assert(
        (
          "\n".toSeq,
          Success(
            ParsedResult(
              valid01.dropRight(1).toSeq,
              Map("help" -> "cliarg\n\nother line")
            )
          )
        ) ===
          parseResult
      )
      assert(valid01.dropRight(1).toSeq === parseResult._2.get.data)
    })
  )

  ignore("`MultiLineConfParser.splitOnLines`.") {}

}
