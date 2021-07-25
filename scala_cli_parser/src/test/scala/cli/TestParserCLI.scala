package fmv1992.scala_cli_parser.cli.test

import fmv1992.scala_cli_parser._
import fmv1992.scala_cli_parser.cli._
import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span

class TestParserCLI extends AnyFunSuite with TimeLimits {

  test("Test `ParserCLI.parse` simple.")(
    failAfter(Span(500, Millis))({
      assert(
        Set(ArgumentCLI("help", "string", Seq.empty)) ===
          ParserCLI(
            Set(
              ArgumentConf(
                "help",
                "",
                "string",
                0
              )
            )
          ).parse(List("--help"))
      )
    })
  )

  test("Test `ParserCLI.parse` with undefined argument.")(
    failAfter(Span(500, Millis))({
      assertThrows[ParseException](
        ParserCLI(
          Set(
            ArgumentConf(
              "help",
              "",
              "string",
              0
            ),
            ArgumentConf(
              "version",
              "",
              "string",
              0
            )
          )
        ).parse(List("--version"))
      )
    })
  )

  test("Test `ParserCLI.parse` complex.")(
    failAfter(Span(500, Millis))({
      assert(
        Set(ArgumentCLI("help", "string", Seq.empty)) ===
          ParserCLI(
            Set(
              ArgumentConf(
                "help",
                "",
                "string",
                0
              ),
              ArgumentConf(
                "version",
                "",
                "string",
                0
              )
            )
          ).parse(List("--help"))
      )
    })
  )

}
