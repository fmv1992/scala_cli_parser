package fmv1992.scala_cli_parser

class TestParseExceptionJVM extends TestParseException {

  test("ParseException name matches (exact): 01.") {
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
        // Differs from
        // `scala_cli_parser:15d3f98:scala_cli_parser/.native/src/test/scala/TestParseExceptionNative.scala:18`
        // by a `$` sign.
      ) === "'SpaceConfParser$': 'ErrorPositionExisting(0,10,11)': 'x     '."
    )
  }

}
