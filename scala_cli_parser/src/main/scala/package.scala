/** Notice the "semi" import here. */
package fmv1992

/** Provide utilities to parse, check and manipulate CLI arguments. */
package object scala_cli_parser {
  type OldParserType = String => Option[Map[String, String]]
  type ParsedConfigStructure = Map[String, Map[String, String]]
  val emptyMapSS: Map[String, String] = Map.empty

  val standardCombiner = (
      x: ParsedResult[Seq[Char], Map[String, String]],
      y: ParsedResult[Seq[Char], Map[String, String]]
  ) => ParsedResult(x.data ++ y.data, x.result ++ y.result)

  val SingleLineConfParser =
    ParserUtils.or(
      ParserUtils.and(
        SpaceConfParser,
        ParserUtils.and(SolidLineConfParser, SpaceConfParser, standardCombiner),
        standardCombiner
      ),
      ParserUtils.and(SolidLineConfParser, SpaceConfParser, standardCombiner)
    )

  val SingleCLIPropertyConfParser =
    ParserUtils.or(
      ParserUtils.and(SingleLineConfParser, SpaceConfParser, standardCombiner),
      ParserUtils.and(MultiLineConfParser, SpaceConfParser, standardCombiner)
    )

  val PropertyBlockParser =
    ParserUtils.many(SingleCLIPropertyConfParser, standardCombiner)

  val ConfParser: ParserWithEither[Seq[Char], ParsedResult[
    Seq[Char],
    Map[String, String]
  ]] = ParserUtils.many(
    Vector(
      CommentConfParser,
      SpaceConfParser,
      PropertyBlockParser
    ).reduce(ParserUtils.or(_, _)),
    standardCombiner
  )

}
