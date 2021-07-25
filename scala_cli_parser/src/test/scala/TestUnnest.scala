//  test("`fullConfigParser` applied to `test_parser_simple_01.txt`.")(
//    failAfter(Span(500, Millis))({
//      val fullConfig =
//        scala.io.Source
//          .fromResource("test_parser_simple_01.txt")
//          .getLines()
//          .mkString("\n")
//      assert(
//        (
//          "".toSeq,
//          Success(
//            ParsedResult(
//              fullConfig.toSeq,
//              Map(
//                "debug" -> Map(
//                  "n" -> "0",
//                  "type" -> "int",
//                  "help" -> "Turn on debugging."
//                )
//              )
//            )
//          )
//        )
//          ===
//            ParserUtils.fullConfigParser.partialParse(fullConfig)
//      )
//    })
//  )
