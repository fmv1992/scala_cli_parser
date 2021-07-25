package fmv1992.scala_cli_parser.test

import scala.util.Success

import fmv1992.scala_cli_parser._
import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span

class TestFullConfigParser extends AnyFunSuite with TimeLimits {

  test("`fullConfigParser` applied to `test_parser_simple_01.txt`.")(
    failAfter(Span(10000000, Millis))({
      val fullConfig =
        loadTestResource("test_parser_simple_01.txt")
      assert(
        (
          "".toSeq,
          Success(
            ParsedResult(
              fullConfig.toSeq,
              Map(
                "debug" -> Map(
                  "n" -> "0",
                  "type" -> "int",
                  "help" -> "Turn on debugging."
                )
              )
            )
          )
        )
          ===
            ParserUtils.fullConfigParser.partialParse(fullConfig)
      )
    })
  )

  test("`fullConfigParser` applied to `test_multiline_01.txt`.")(
    failAfter(Span(10000000, Millis))({
      val fullConfig =
        loadTestResource("test_multiline_01.txt")
      assert(
        (
          "".toSeq,
          Success(
            ParsedResult(
              fullConfig.toSeq,
              Map(
                "multiline" -> Map(
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
        )

          ===
            ParserUtils.fullConfigParser.partialParse(fullConfig)
      )
    })
  )

  // This reveals that the current code is buggy. Multiple "names" collide.
  test("`fullConfigParser` applied to `test_cli_example_01.txt`.")(
    failAfter(Span(10000000, Millis))({
      val fullConfig =
        loadTestResource("test_cli_example_01.txt")
      assert(
        (
          "".toSeq,
          Success(
            ParsedResult(
              fullConfig.toSeq,
              Map(
                "debug" -> Map(
                  "n" -> "0",
                  "type" -> "int",
                  "help" -> "Turn on debug flag."
                ),
                "verbose" -> Map(
                  "n" -> "0",
                  "type" -> "int",
                  "help" -> "Help text."
                ),
                "help" -> Map.empty,
                "version" -> Map.empty
              )
            )
          )
        )

          ===
            ParserUtils.fullConfigParser.partialParse(fullConfig)
      )
    })
  )

}
