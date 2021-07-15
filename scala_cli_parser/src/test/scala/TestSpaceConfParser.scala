// // project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestSpaceConfParser
//
// package fmv1992.scala_cli_parser
//
// import org.scalatest.funsuite.AnyFunSuite
//
// class TestSpaceConfParser extends AnyFunSuite {
//
//   test("`SpaceConfParser` invalid.") {
//     assert(
//       !SpaceConfParser.isValid("abcde")
//     )
//     assert(
//       !SpaceConfParser.isValid(" # .")
//     )
//   }
//
//   test("`SpaceConfParser` valid.") {
//     assert(
//       SpaceConfParser.isValid("\n\n\n\n\n\n\n\n\n\n")
//     )
//     assert(
//       SpaceConfParser.isValid("\t \n ")
//     )
//   }
//
//   test("`SpaceConfParser.parse`.") {
//     assert(
//       SpaceConfParser
//         .parse(" \n ")
//         .getOrElse(
//           throw new Exception()
//         ) === ParsedResult(" \n ".toSeq, emptyMapSS)
//     )
//   }
//
//   test("`SpaceConfParser.getValidSubSequence`.") {
//     val mixedSpace = " \t x a "
//     assert(
//       SpaceConfParser.getValidSubSequence(mixedSpace)
//         == Some(" \t ".toSeq)
//     )
//   }
//
//   // ???: Test that some input throw exception.
//
// }
