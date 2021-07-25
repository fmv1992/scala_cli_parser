// package fmv1992.scala_cli_parser.conf
//
// object EmptyConfParser
//     extends ParserWithEither[
//       Seq[Char],
//       ParsedResult[Seq[Char], Map[String, String]]
//     ] {
//
//   def isValid(input: Seq[Char]): Boolean = {
//     input.isEmpty
//   }
//
//   def transform(
//       input: Seq[Char]
//   ): ParsedResult[Seq[Char], Map[String, String]] = {
//     ParsedResult(input, Map.empty)
//   }
//
//   def getValidSubSequence(input: Seq[Char]) = {
//     if (isValid(input)) {
//       Some(input)
//     } else {
//       None
//     }
//   }
//
// }
