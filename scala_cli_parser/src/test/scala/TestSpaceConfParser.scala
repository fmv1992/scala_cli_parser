package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestSpaceConfParser extends AnyFunSuite {

  test("`SpaceConfParser` invalid.") {
    assert(
      !SpaceConfParser("abcde").isValid
    )
    assert(
      !SpaceConfParser(" # .").isValid
    )
  }

  test("`SpaceConfParser` valid.") {
    assert(
      SpaceConfParser("\n\n\n\n\n\n\n\n\n\n").isValid
    )
    assert(
      SpaceConfParser("\t \n ").isValid
    )
  }

  test("`SpaceConfParser.parse`.") {
    val ccp = SpaceConfParser(" \n ")
    assert(
      ccp.parse.getOrElse(
        throw new Exception()
      ) === ParsedResult(" \n ".toSeq, " \n ")
    )
  }

}
