package fmv1992.scala_cli_parser

object TestSum extends CLIConfigTestableMain {

  val version = "0.0.0"

  val programName = "TestSum"

  val CLIConfigPath = Example.cli02Path

  def testableMain(args: Seq[Argument]): List[String] = {

    val res = args.foldLeft(0)((l, x) ⇒ {
      x match {
        case y: Argument if y.longName == "sum" ⇒ x.value.map(_.toInt).sum + l
        case _ ⇒ println(x) ; throw new Exception()
      }
    })

    List(res.toString)
  }
}
