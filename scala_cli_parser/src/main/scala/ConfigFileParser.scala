// package fmv1992.scala_cli_parser
//
// import java.nio.file.Path
//
// /** Configuration file based CLI parser. */
// trait ConfigFileParser extends Parser[Path, ParsedConfigStructure] {
//
//   /** Map of parsed options.
//     *
//     * Example:
//     *
//     * ```
//     * name: debug
//     * n: 0
//     * type: int
//     * help: Help text.
//     * ```
//     *
//     * Gets transformed into this:
//     *
//     * Map(debug -> Map(n -> 0, type -> int, help -> Help text.),
//     * verbose -> Map(n -> 0, type -> int, help -> Help text.))
//     */
//   val format: ParsedConfigStructure
//
// }
