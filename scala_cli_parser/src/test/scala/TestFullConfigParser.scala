package fmv1992.scala_cli_parser

import scala.util.Success

import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span

class TestFullConfigParser extends AnyFunSuite with TimeLimits {

  test("`fullConfigParser` applied to `test_parser_simple_01.txt`.")(
    failAfter(Span(500, Millis))({
      val fullConfig =
        scala.io.Source
          .fromResource("test_parser_simple_01.txt")
          .getLines()
          .mkString("\n")
      assert(
        (
          "".toSeq,
          Success(
            ParsedResult(
              fullConfig.toSeq,
              Map(
                "name" -> "debug",
                "n" -> "0",
                "type" -> "int",
                "help" -> "Turn on debugging."
              )
            )
          )
        )
          ===
            ParserUtils.fullConfigParser.partialParse(fullConfig)
      )
    })
  )

  ignore("`ConfParser` full example.")(failAfter(Span(500, Millis))({
    val fullConfig =
      scala.io.Source
        .fromResource("test_multiline_01.txt")
        .getLines()
        .mkString("\n")
    assert(
      (
        "".toSeq,
        Success(
          ParsedResult(
            fullConfig.toSeq,
            Map(
              "name" -> "multiline",
              "n" -> "1",
              "type" -> "int",
              "help" -> """
This is a multi line help string.

It may also contain examples and etc...
This just contains a perchance aligned '|' on this line. It is a single line.
""".trim,
              "default" -> "yes"
            )
          )
        )
      )

        ===
          ParserUtils.fullConfigParser.partialParse(fullConfig)
    )
  }))

}
