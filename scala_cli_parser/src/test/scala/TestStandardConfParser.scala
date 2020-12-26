package fmv1992.scala_cli_parser

import java.nio.file.Paths

import org.scalatest.funsuite.AnyFunSuite

// ~testOnly *TestStandardConfParser*
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
