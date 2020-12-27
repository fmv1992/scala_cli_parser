package fmv1992.scala_cli_parser

import java.nio.file.Paths

import org.scalatest.funsuite.AnyFunSuite

// ~testOnly *TestStandardConfParser*
// project scala_cli_parserCrossProjectJVM;++2.13.3;~test
class TestStandardConfParser extends AnyFunSuite {

  test("Test API.") {

    val parser =
      StandardConfParser(
        Paths.get("src/test/resources/test_cli_example_01.txt")
      )

    assert(
      parser.parse() === Map.empty
    )

  }

}
