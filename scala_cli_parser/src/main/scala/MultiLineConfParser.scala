package fmv1992.scala_cli_parser

object MultiLineConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  ParserUtils.and(
    ParserUtils.and(
      SpaceConfParser,
      SingleLineConfParser,
      (
          x: ParsedResult[Seq[Char], Map[String, String]],
          y: ParsedResult[Seq[Char], Map[String, String]]
      ) => ParsedResult(x.data ++ y.data, x.result ++ y.result)
    ),
    ParserUtils.many(
      SolidLineWithPipe,
      (
          x: ParsedResult[Seq[Char], String],
          y: ParsedResult[Seq[Char], String]
      ) => ParsedResult(x.data ++ y.data, x.result + '\n' + y.result)
    ),
    (
        x: ParsedResult[Seq[Char], Map[String, String]],
        // y: ParsedResult[Seq[Char], Map[String, String]]
        y: ParsedResult[Seq[Char], String]
    ) => ParsedResult(x.data ++ y.data, 1)
  )

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Map[String, String]] = {
    throw new Exception()
  }

  def isValid(input: Seq[Char]) = {
    val splitOnLines = input
      .foldLeft(
        (LazyList.empty: LazyList[Seq[Char]], Seq.empty: Seq[Char])
      )((acc, char) => {
        val accll = acc._1
        val accseq = acc._2
        if (char == '\n') {
          (accll.appended(accseq.appended('\n')), Seq.empty)
        } else {
          (accll, accseq.appended(char))
        }
      }) match {
      case (l, Seq()) => l
      case (l, s)     => l.appended(s)
    }
    val firstLine: Seq[Char] = splitOnLines.head
    val otherLines: Seq[Seq[Char]] = splitOnLines.tail
    require(
      (firstLine.toList +: otherLines.toList).flatten == input.toList,
      (firstLine.toList, otherLines.toList, input.toList)
    )
    println("-" * 79)
    Console.err.println(firstLine.toList)
    println("-" * 79)
    Console.err.println(otherLines.toList)
    println("-" * 79)
    println(SingleLineConfParser.isValid(firstLine))
    println("-" * 79)
    println(otherLines.forall(SolidLineWithPipe.isValid(_)))
    println("-" * 79)
    println("-" * 79)
    SingleLineConfParser.isValid(firstLine) && otherLines.forall(
      SolidLineWithPipe.isValid(_)
    )
  }

}

object SolidLineWithPipe
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], String]
    ] {

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], String] = {
    ParsedResult(input, input.tail.mkString)
  }

  def isValid(input: Seq[Char]) = {
    val isPipeFirst = input.headOption.map(_ == '|')
    val newLinePos = input.find(_ == '\n')
    newLinePos match {
      case Some(pos) =>
        (pos == input.length - 1) && isPipeFirst.getOrElse(false)
      case None => isPipeFirst.getOrElse(false)
    }
  }
}
