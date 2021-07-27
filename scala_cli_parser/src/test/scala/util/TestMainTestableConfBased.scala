package fmv1992.scala_cli_parser.util.test

import java.nio.file.Paths

import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.scala_cli_parser.cli.ParserCLI
import fmv1992.scala_cli_parser.conf.ParserConfigFile
import org.scalatest.funsuite.AnyFunSuite

class TestMainTestableConfBased extends AnyFunSuite {

  val defaultArgs: List[String] = "--version --help".split(" ").toList
  val helpArgs: List[String] = "--help".split(" ").toList

  val parserCLI: ParserCLI = ParserConfigFile.parse(
    Paths.get(
      "src/test/resources/test_cli_example_05_sum.txt"
    )
  )

  test("Most basic test: test the idea.") {
    assert(
      Set(
        ArgumentCLI("version", "int", List()),
        ArgumentCLI("help", "int", List())
      ) === parserCLI.parse(defaultArgs)
    )
  }

  test("Test functionality with TestMainExample01.") {
    assert(
      TestSum.testableMain(
        parserCLI.parse("--sum 2 7".split(" ").toList)
      )
        === List("9")
    )
  }

}
