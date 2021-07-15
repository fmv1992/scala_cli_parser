// // project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestSolidLineConfParser
// package fmv1992.scala_cli_parser
//
// import org.scalatest.funsuite.AnyFunSuite
//
// class TestSolidLineConfParser extends AnyFunSuite {
//
//   val valid01 = "name: cliarg"
//   val valid02 = "\ttype: int"
//   val inValid01 = "no colon line"
//
//   test("`SolidLineConfParser` valid.") {
//     assert(SolidLineConfParser.isValid(valid01))
//     assert(SolidLineConfParser.isValid(valid01 + '\n'))
//   }
//
//   test("`SolidLineConfParser` invalid.") {
//     assert(
//       !SolidLineConfParser.isValid(inValid01)
//     )
//     assert(
//       !SolidLineConfParser.isValid("")
//     )
//   }
//
// }
