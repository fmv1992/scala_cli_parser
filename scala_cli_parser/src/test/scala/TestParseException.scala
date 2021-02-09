package fmv1992.scala_cli_parser

import org.scalatest.EitherValues._
import org.scalatest.funsuite.AnyFunSuite

class TestParseException extends AnyFunSuite {

  test("`CommentConfParser` invalid throws ParseException.") {
    val parseException = CommentConfParser.parse("abcde").left.value
    assert(
      parseException.isInstanceOf[ParseException]
    )
    assert(
      parseException.getMessage === "ErrorPositionExisting(0,0)"
    )
  }

}
