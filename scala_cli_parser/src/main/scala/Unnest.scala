// package fmv1992.scala_cli_parser
//
// import scala.util.Try
// import scala.util.Success
//
// /** Combine Map[String, String] into Map[String, Map[String, String]] where the
//   * new key is given by `name`, which stands for the CLI name.
//   */
// object CombinerFullConfigParser
//     extends Function2[Try[ParsedResult[Seq[Char], Map[String, String]]], Try[
//       ParsedResult[Seq[Char], Map[String, String]]
//     ], Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]]] {
//
//   def checkContainsNameOrIsEmpty(
//       m: Map[String, String]
//   ): Map[String, String] = {
//     assert(m.get("name").isDefined || m.isEmpty, m)
//     m
//   }
//
//   def checkNamesAreDifferentOrIsEmpty(
//       m11: Map[String, String],
//       m22: Map[String, String]
//   ): (Map[String, String], Map[String, String]) = {
//     if (!m11.isEmpty)(assert(m11.get("name").isDefined, m11))
//     if (!m22.isEmpty)(assert(m22.get("name").isDefined, m22))
//     if ((!m11.isEmpty) && (!m22.isEmpty))(assert(
//       m11.get("name") != m22.get("name"),
//       s"'${m11}', '${m22}'."
//     ))
//     (m11, m22)
//   }
//
//   def check(m: Map[String, String]): Map[String, String] = { ??? }
//
//   def apply(
//       v1: Try[ParsedResult[Seq[Char], Map[String, String]]],
//       v2: Try[ParsedResult[Seq[Char], Map[String, String]]]
//   ): Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]] = {
//     println("t" * 79)
//     println(v1.toString)
//     println(v2.toString)
//     println("t" * 79)
//
//     val (v1Valid, v2Valid) = checkNamesAreDifferentOrIsEmpty(
//       checkContainsNameOrIsEmpty(v1.get.result),
//       checkContainsNameOrIsEmpty(v2.get.result)
//     )
//
//     val intersection = v1Valid.keySet.intersect(v2Valid.keySet)
//     if (intersection.isEmpty) {
//       val merged = v1.get.result ++ v2.get.result
//       Success(
//         ParsedResult(
//           v1.get.data ++ v2.get.data,
//           Map(merged("name") -> merged.removed("name"))
//         )
//       )
//     } else {
//       throw new RuntimeException(
//         s"Intersection of dictionary keys is not empty: s'${intersection.toList.sorted}'. "
//       )
//     }
//   }
//
// }
