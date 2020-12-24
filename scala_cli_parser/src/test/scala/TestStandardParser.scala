package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestStandardParser extends AnyFunSuite {

  test("Test API.") {

    // Test valid instantiation.
    StandardCLIParser(Example.cli02File)

  }

  test("Test default keys.") {

    val parser = StandardCLIParser(Example.cli06File)
    assert(!parser.parse(List()).isEmpty)

    val parsed = parser.parse(List("--execute", "ten"))
    assert(parsed(0).value(0) == "ten")

  }

}
