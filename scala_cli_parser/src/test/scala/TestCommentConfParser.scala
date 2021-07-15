// // project scala_cli_parserCrossProjectJVM;testOnly ~fmv1992.scala_cli_parser.TestCommentConfParser
//
// package fmv1992.scala_cli_parser
//
// import org.scalatest.funsuite.AnyFunSuite
//
// class TestCommentConfParser extends AnyFunSuite {
//
//   val multilineComment = "# comment 01.\n# comment 02.\n \t \tNot a comment."
//   val comment1 = "# Comment.\n# Other comment."
//
//   test("`CommentConfParser` invalid.") {
//     assert(
//       !CommentConfParser.isValid("abcde")
//     )
//     assert(
//       !CommentConfParser.isValid("# Comment.\n# Other comment.\n ")
//     )
//     assert(
//       !CommentConfParser.isValid("")
//     )
//   }
//
//   test("`CommentConfParser` valid.") {
//     assert(
//       CommentConfParser.isValid("# Comment.")
//     )
//     assert(
//       CommentConfParser.isValid("# Comment.\n# Other comment.")
//     )
//   }
//
//   test("`CommentConfParser.parse`.") {
//     assert(
//       CommentConfParser
//         .parse(comment1)
//         .getOrElse(
//           throw new Exception()
//         ) === ParsedResult(
//         "# Comment.\n# Other comment.".toSeq,
//         emptyMapSS
//       )
//     )
//   }
//
//   test("`CommentConfParser.getValidSubSequence`.") {
//     assert(
//       CommentConfParser.getValidSubSequence(comment1)
//         == Some("# Comment.".toSeq)
//     )
//   }
// }
