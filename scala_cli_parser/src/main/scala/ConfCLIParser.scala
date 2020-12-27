package fmv1992.scala_cli_parser

import fmv1992.fmv1992_scala_utilities.util.Utilities

object ConfCLIParser {

  def parseStringOpt(s: String): Either[Seq[Throwable], Map[String, String]] = {
    val definedLineParser: Parser[String, Map[String, String]] = List(
      ParserPrimitives.emptyLine,
      ParserPrimitives.commentLine,
      ParserPrimitives.nameContentLine,
      ParserPrimitives.generalContentLine
    ).reduce((x, y) => ParserCombinator.or(x, y))
    // definedLineParser.parse(s)
    s.lines.foldLeft(
      Right(emptyMapSS): Either[Seq[Throwable], Map[String, String]]
    )((either, line) => {
      val oPMap = definedLineParser.parse(line)
      val keyIntersection = either
        .getOrElse(emptyMapSS)
        .keys
        .toSet
        .intersect(oPMap.getOrElse(emptyMapSS).keys.toSet)
      require(keyIntersection.isEmpty, keyIntersection)
      oPMap.fold(
        (seq: Seq[Throwable]) => either.left.map(_ ++ seq),
        (mss: Map[String, String]) => either.map(_ ++ mss)
      )
      oPMap.flatMap(x => either.map(y => y ++ x))
    })
  }

  def parseString(s: String): Map[String, String] =
    getOrElseEitherShim(parseStringOpt(s), throw new Exception())

  def groupContiguousText(s: String): List[List[String]] = {
    val lines = s.lines.toList
    val i = Utilities.getContiguousElementsIndexes(lines.map(_.isEmpty))
    val blocks: List[List[String]] =
      i.flatMap(x => List(lines.slice(x._1, x._2)))
    val cleanedBlocks = blocks
      .map(ls => ls.filterNot(x => x.isEmpty || x.trim.startsWith("#")))
      .filterNot(_.isEmpty)
    cleanedBlocks
  }

  def nestConfigMaps(
      lm: List[Map[String, String]]
  ): Map[String, Map[String, String]] = {
    require(lm.length == lm.map(x => x.keys.toSet).reduce(_ ++ _).size, lm)
    val flattenedM = lm.reduce(_ ++ _)
    val key = flattenedM("name")
    val others: Map[String, String] = flattenedM - "name"
    Map((key -> others))
  }

  def parseConf(s: String): ParsedConfigStructure = {
    val blocks: List[List[String]] = groupContiguousText(s)
    val mapsFromBlocks: List[List[Map[String, String]]] =
      blocks.map(x => x.map(parseString))
    val listNestedMap: List[Map[String, Map[String, String]]] =
      mapsFromBlocks.map(nestConfigMaps)
    val nestedMap: Map[String, Map[String, String]] =
      listNestedMap.reduceLeft(_ ++ _)
    nestedMap
  }

}
