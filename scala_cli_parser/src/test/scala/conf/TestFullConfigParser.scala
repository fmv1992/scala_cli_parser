package fmv1992.scala_cli_parser.conf.test

import fmv1992.scala_cli_parser._
import fmv1992.scala_cli_parser.cli._
import fmv1992.scala_cli_parser.conf._
import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span

class TestFullConfigParser extends AnyFunSuite with TimeLimits {

  test("`fullConfigParser` applied to `test_parser_simple_01.txt`.")(
    failAfter(Span(500, Millis))({
      val fullConfig =
        loadTestResource("test_parser_simple_01.txt")
      assert(
        ParserCLI(
          Set(
            ArgumentConf(
              "debug",
              "Turn on debugging.",
              "int",
              0
            )
          )
        )
          === ParserConfigFile.parse(fullConfig)
      )
    })
  )

  test("`fullConfigParser` applied to `test_multiline_01.txt`.")(
    failAfter(Span(500, Millis))({
      val fullConfig =
        loadTestResource("test_multiline_01.txt")
      assert(
        ParserCLI(
          Set(
            ArgumentConf(
              "multiline",
              """
This is a multi line help string.

It may also contain examples and etc...
This just contains a perchance aligned '|' on this line. It is a single line.
""".trim,
              "int",
              1
            )
          )
        )
          === ParserConfigFile.parse(fullConfig)
      )
    })
  )

  test("`fullConfigParser` applied to `test_cli_example_01.txt`.")(
    failAfter(Span(500, Millis))({
      val fullConfig =
        loadTestResource("test_cli_example_01.txt")
      assert(
        ParserCLI(
          Set(
            ArgumentConf(
              "debug",
              "Turn on debug flag.",
              "int",
              1
            ),
            ArgumentConf("verbose", "Help text.", "int", 0),
            ArgumentConf("help", "Help text.", "int", 0),
            ArgumentConf("version", "Help text.", "int", 0)
          )
        ) === ParserConfigFile.parse(fullConfig)
      )
    })
  )

}
