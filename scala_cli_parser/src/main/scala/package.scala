/** Notice the "semi" import here. */
package fmv1992

/** Provide utilities to parse, check and manipulate CLI arguments. */
package object scala_cli_parser {
  type OldParserType = String => Option[Map[String, String]]
  type ParsedConfigStructure = Map[String, Map[String, String]]
  val emptyMapSS: Map[String, String] = Map.empty
}
