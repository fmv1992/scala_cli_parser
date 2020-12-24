package fmv1992.scala_cli_parser

import java.io.File

object Example {

  // Provide examples for CLIParser. --- {

  val cli01Path: String = "./src/test/resources/test_cli_example_01.txt"
  val cli01File: File = new File(cli01Path)
  require(cli01File.exists, cli01File.getCanonicalPath)
  val cli01Parser: StandardCLIParser = StandardCLIParser(cli01File)

  // --- }

  // Provide examples for StandardCLIParser. --- {

  val cli02Path: String = "./src/test/resources/test_cli_example_02_gnu.txt"
  val cli02File: File = new File(cli02Path)
  require(cli02File.exists, cli02File.getCanonicalPath)
  val cli02Parser: StandardCLIParser = StandardCLIParser(cli02File)

  // Invalid files.
  val cli03Path: String =
    "./src/test/resources/test_cli_example_03_no_help_gnu.txt"
  val cli03File: File = new File(cli03Path)
  require(cli03File.exists, cli03File.getCanonicalPath)

  val cli04Path: String =
    "./src/test/resources/test_cli_example_04_no_version_gnu.txt"
  val cli04File: File = new File(cli04Path)
  require(cli04File.exists, cli04File.getCanonicalPath)

  val cli06Path: String =
    "./src/test/resources/test_cli_example_06_default_argument.txt"
  val cli06File: File = new File(cli06Path)
  require(cli06File.exists, cli06File.getCanonicalPath)

  // --- }

  // Provide examples for TestableMain. --- {

  // TestSum
  val cli05TestSumPath: String =
    "./src/test/resources/test_cli_example_05_sum.txt"
  val cli05TestSumFile: File = new File(cli05TestSumPath)
  require(cli05TestSumFile.exists, cli05TestSumFile.getCanonicalPath)

  val cli05TestSumParser: StandardCLIParser = StandardCLIParser(
    cli05TestSumFile
  )

  // --- }

}
