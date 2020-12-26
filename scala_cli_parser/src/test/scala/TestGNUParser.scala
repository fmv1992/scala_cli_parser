package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestGNUParser extends AnyFunSuite {

  test("Test API.") {
    // Test valid instantiation.
    GNUParser(Example.cli02File)

    // Test invalid instantiation. Missing 'help'.
    assertThrows[scala.IllegalArgumentException](
      GNUParser(Example.cli03File)
    )

    // Test invalid instantiation. Missing 'version'.
    assertThrows[scala.IllegalArgumentException](
      GNUParser(Example.cli04File)
    )

    // Test invalid instantiation. Missing 'version' and 'help'.
    assertThrows[scala.IllegalArgumentException](
      GNUParser(Example.cli06File)
    )

  }

  test("Test parsing with GNUParser.") {

    val parser = GNUParser(Example.cli02File)
    assert(parser.parse(List()).isRight)
    assert(
      getOrElseEitherShim(parser.parse(List()), throw new Exception()).isEmpty
    )

    assertThrows[scala.IllegalArgumentException](
      parser.parse(List("--bogus", "bog"))
    )

    val parsed02 = parser.parse(List("--version"))
    println(parsed02)

    // Two args are required and only one was given.
    assertThrows[scala.IllegalArgumentException](
      parser.parse(List("--sum", "0"))
    )

    parser.parse(List("--sum", "0", "15"))

    parser.parse(List("--debug", "--help"))

  }

}
