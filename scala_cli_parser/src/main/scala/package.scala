/** Notice the "semi" import here. */
package fmv1992

/** Provide utilities to parse, check and manipulate CLI arguments. */
package object scala_cli_parser {
  type Parser = String ⇒ Option[Map[String, String]]
}
