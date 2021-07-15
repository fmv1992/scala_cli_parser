// package fmv1992.scala_cli_parser
//
// import org.scalatest.funsuite.AnyFunSuite
//
// class TestEmptyConfParser extends AnyFunSuite {
//
//   val valid01 = ""
//   val inValid01 = " "
//   val inValid02 = "abcde"
//
//   test("`EmptyConfParser` valid.") {
//     assert(
//       EmptyConfParser.isValid(valid01)
//     )
//   }
//
//   test("`EmptyConfParser` invalid.") {
//     assert(
//       !CommentConfParser.isValid(inValid01)
//     )
//     assert(
//       !CommentConfParser.isValid(inValid02)
//     )
//   }
//
//   test("`EmptyConfParser.parse`.") {
//     assert(
//       EmptyConfParser
//         .parse(valid01)
//         .getOrElse(
//           throw new Exception()
//         ) === ParsedResult(
//         valid01.toSeq,
//         Map.empty
//       )
//     )
//
//     // ???: Test that some input throw exception.
//     // assert(
//     //   EmptyConfParser
//     //     .parse(inValid01)
//     //     .getOrElse(
//     //       throw new Exception()
//     //     ) === ParsedResult(
//     //     "",
//     //     ""
//     //   )
//     // )
//   }
//
//   // test("`CommentConfParser.getFirstSignificantCharInLastLine`.") {
//
//   //   assert(
//   //     CommentConfParser("#").getFirstSignificantCharInLastLine
//   //       === Some('#')
//   //   )
//   //   assert(
//   //     CommentConfParser(" #").getFirstSignificantCharInLastLine
//   //       === Some('#')
//   //   )
//   //   assert(
//   //     CommentConfParser(" x").getFirstSignificantCharInLastLine
//   //       === Some('x')
//   //   )
//   //   assert(
//   //     CommentConfParser(" cc\n x \n l ").getFirstSignificantCharInLastLine
//   //       === Some('l')
//   //   )
//   // }
//
//   // test("`CommentConfParser.getMeaningfulInput`.") {
//   //   assert(
//   //     CommentConfParser("# My comment.\n ")
//   //       .getMeaningfulInput() === (CommentConfParser(
//   //       "# My comment.\n"
//   //     ), " ".toIterable)
//   //   )
//   //   assert(
//   //     CommentConfParser(multilineComment)
//   //       .getMeaningfulInput() === (CommentConfParser(
//   //       "# comment 01.\n# comment 02.\n"
//   //     ), " \t \tNot a comment.".toIterable)
//   //   )
//   // }
//
//   // test("`CommentConfParser.consume`.") {
//   //   val (c1, remaining1) = CommentConfParser("").consume(multilineComment)
//   //   val (c2, remaining2) =
//   //     (
//   //       CommentConfParser("# comment 01.\n# comment 02.\n".toList),
//   //       " \t \tNot a comment.".toIterable
//   //     )
//   //   assert(c1 === c2)
//   //   assert(remaining1 === remaining2)
//   // }
//
// }
