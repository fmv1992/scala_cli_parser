package fmv1992.scala_cli_parser


class TestParseExceptionNative extends TestParseException {

  test("ParseException name matches (exact): 01.") {
    assert(
      ParseException.getExceptionMessage(
        input01.toSeq,
        CommentConfParser
      ) === "'CommentConfParser': 'ErrorPositionExisting(0,0,1)': 'abc'."
    )

    assert(
      ParseException.getExceptionMessage(
        input02.toSeq,
        SpaceConfParser
      ) === "'SpaceConfParser': 'ErrorPositionExisting(0,10,11)': 'x     '."
    )
  }

}
