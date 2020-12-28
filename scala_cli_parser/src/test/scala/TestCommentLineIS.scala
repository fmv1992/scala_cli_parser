package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestCombinedIS extends AnyFunSuite {

  val mostComplexCase = "|" + """
  |
  |# cOMMENt 01.
  |	# cOMMENt 02.
  |
  |
  |# cOMMENt 03.
  |
  |# cOMMENt 04.
  |
  """.stripMargin + "|"

  test("Parse complex string with different intermediate states.") {
    assert(
      true
      // throw new Exception(mostComplexCase)
    )
  }

}
