// package fmv1992.scala_cli_parser
//
// object SpaceConfParser
//     extends ParserWithEither[
//       Seq[Char],
//       ParsedResult[Seq[Char], Map[String, String]]
//     ] {
//
//   override def parse(
//       input: Seq[Char]
//   ): Either[Throwable, ParsedResult[Seq[Char], Map[String, String]]] = {
//     if (isValid(input)) {
//       Right(transform(input))
//     } else {
//       Left(ParseException())
//     }
//   }
//
//   def transform(
//       input: Seq[Char]
//   ): ParsedResult[Seq[Char], Map[String, String]] =
//     ParsedResult(input, emptyMapSS)
//
//   def isValid(input: Seq[Char]) = {
//     input.forall(_.isWhitespace)
//   }
//
//   def getValidSubSequence(input: Seq[Char]): Option[Seq[Char]] = {
//     val subSeq = input.takeWhile(_.isWhitespace)
//     if (subSeq.isEmpty) {
//       None
//     } else {
//       Some(subSeq)
//     }
//   }
//
// }
