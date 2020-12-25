package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite
import java.nio.file.Paths

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
