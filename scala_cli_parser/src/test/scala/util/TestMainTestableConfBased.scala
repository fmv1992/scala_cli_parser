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

  ignore("Test `parse`.") {
    assert(
      Set(
        ArgumentCLI("version", List()),
        ArgumentCLI("help", List())
      ) === parserCLI.parse(defaultArgs)
    )
  }

  ignore("Test functionality with `TestSum`.") {
    assert(
      TestSum.testableMain(
        parserCLI.parse("--sum 2 7".split(" ").toList)
      )
        === List("9")
    )
  }

  test("Test default functionality with `TestSum`.") {
    assert(
      TestSum.testableMain(Seq.empty)
        === List("0")
    )
  }

  ignore("Test `printHelp`.") {
    assert("""
TestSum --debug --help --sum --version
    --debug: Turn on debugging.
    --help: Help text.
    --sum: Sum arguments.

           Use multiline.
    --version: Show the program version.
""".trim === TestSum.testableMain(List("--help")).mkString("\n"))
  }

  ignore("Test `printVersion`.") {
    assert(
      Seq("TestSum 0.0.0") === TestSum
        .testableMain(List("--version"))
    )
  }

}
