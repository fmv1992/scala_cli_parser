package fmv1992.scala_cli_parser

import org.scalatest.EitherValues._
import org.scalatest.funsuite.AnyFunSuite

class TestParseException extends AnyFunSuite {

  test("`CommentConfParser` invalid throws ParseException.") {

    val input01 = ParseException.blinkString("a") + "bc"
    val input02 = "" * 10 + ParseException.blinkString("x") + " " * 10

    val parseException = CommentConfParser.parse(input01).left.value
    assert(
      parseException.isInstanceOf[ParseException]
    )
    assert(
      parseException.getMessage === "ErrorPositionExisting(0,0,1)"
    )
    assert(
      ParseException
        .getExceptionMessage(
          input01.toSeq,
          CommentConfParser
        )
        === s"'CommentConfParser$$': 'ErrorPositionExisting(0,0,1)': '${ParseException.blinkString("a")}bc'."
    )

    assert(
      ParseException.getExceptionMessage(
        input02.toSeq,
        SpaceConfParser
      ) === s"'SpaceConfParser$$': 'ErrorPositionExisting(0,10,11)': '${ParseException.blinkString("x")}     '."
    )
  }

}
