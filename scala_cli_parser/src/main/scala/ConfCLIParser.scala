package fmv1992.scala_cli_parser

import java.nio.file.Path

import fmv1992.fmv1992_scala_utilities.util.Utilities

case class StandardConfParser(p: Path)
    extends Parser[Path, ParsedConfigStructure] {

  def parse(input: Path): ParsedConfigStructure = {
    ???
  }

}

object ConfCLIParser {

  def parseStringOpt(s: String): Option[Map[String, String]] = {
    val mEmpty = Map.empty: Map[String, String]
    val definedLineParser = ParserCombinator.requireSucessful(
      ParserCombinator.or(
        ParserCombinator.or(
          ParserCombinator
            .or(ParserPrimitives.emptyLine, ParserPrimitives.commentLine),
          ParserPrimitives.nameContentLine
        ),
        ParserPrimitives.generalContentLine
      )
    )
    s.lines.foldLeft(Option(mEmpty))((om, s) => {
      val oPMap = definedLineParser(s)
      val keyIntersection = om
        .getOrElse(mEmpty)
        .keys
        .toSet
        .intersect(oPMap.getOrElse(mEmpty).keys.toSet)
      require(keyIntersection.isEmpty, keyIntersection)
      om.flatMap(x => oPMap.map(y => y ++ x))
    })
  }

  def parseString(s: String): Map[String, String] =
    parseStringOpt(s).getOrElse(throw new Exception())

  def groupContiguousText(s: String): Seq[Seq[String]] = {
    val lines = s.lines.toSeq
    val i = Utilities.getContiguousElementsIndexes(lines.map(_.isEmpty))
    val blocks: Seq[Seq[String]] =
      i.flatMap(x => Seq(lines.slice(x._1, x._2)))
    val cleanedBlocks = blocks
      .map(ls => ls.filterNot(x => x.isEmpty || x.trim.startsWith("#")))
      .filterNot(_.isEmpty)
    cleanedBlocks
  }

  def nestConfigMaps(
      lm: Seq[Map[String, String]]
  ): Map[String, Map[String, String]] = {
    require(lm.length == lm.map(x => x.keys.toSet).reduce(_ ++ _).size, lm)
    val flattenedM = lm.reduce(_ ++ _)
    val key = flattenedM("name")
    val others: Map[String, String] = flattenedM - "name"
    Map((key -> others))
  }

  def parseConf(s: String): ParsedConfigStructure = {
    val blocks: Seq[Seq[String]] = groupContiguousText(s)
    val mapsFromBlocks: Seq[Seq[Map[String, String]]] =
      blocks.map(x => x.map(parseString))
    val listNestedMap: Seq[Map[String, Map[String, String]]] =
      mapsFromBlocks.map(nestConfigMaps)
    val nestedMap: Map[String, Map[String, String]] =
      listNestedMap.reduceLeft(_ ++ _)
    nestedMap
  }

}
