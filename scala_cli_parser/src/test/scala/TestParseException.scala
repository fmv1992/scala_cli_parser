package fmv1992.scala_cli_parser

import org.scalatest.EitherValues._
import org.scalatest.funsuite.AnyFunSuite

class TestParseException extends AnyFunSuite {

  test("`CommentConfParser` invalid throws ParseException.") {

    // ???: Revert adding blinking
    // `scala_cli_parser:b45d9a7:scala_cli_parser/src/main/scala/ParseException.scala:15`
    // to exceptions.

    val input01 = "abc"
    val input02 = "" * 10 + "x" + " " * 10

    val parseException = CommentConfParser.parse(input01).left.value
    assert(
      parseException.isInstanceOf[ParseException]
    )
    assert(
      parseException.getMessage === "ErrorPositionExisting(0,0,1)"
    )
    assert(
      ParseException.getExceptionMessage(
        input01.toSeq,
        CommentConfParser
      ) === "'CommentConfParser$': 'ErrorPositionExisting(0,0,1)': 'abc'."
    )

    assert(
      ParseException.getExceptionMessage(
        input02.toSeq,
        SpaceConfParser
      ) === "'SpaceConfParser$': 'ErrorPositionExisting(0,10,11)': 'x     '."
    )
  }

}
