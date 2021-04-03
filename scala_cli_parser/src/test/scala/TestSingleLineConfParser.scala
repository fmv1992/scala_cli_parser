package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestSingleLineConfParser extends AnyFunSuite {

  val valid01 = "name: cliarg"
  val valid02 = "\ttype: int"
  val inValid01 = "no colon line"

  test("`SingleLineConfParser` valid.") {
    assert(
      SingleLineConfParser.isValid(valid01)
    )
    assert(
      SingleLineConfParser.isValid(valid02)
    )
  }

  test("`SingleLineConfParser` invalid.") {
    assert(
      !SolidLineConfParser.isValid(inValid01)
    )
    assert(
      !SolidLineConfParser.isValid("")
    )
  }

}
