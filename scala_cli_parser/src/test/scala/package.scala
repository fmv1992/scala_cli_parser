package fmv1992.scala_cli_parser.conf

import java.nio.file.Paths

import fmv1992.fmv1992_scala_utilities.util.Utilities.findAllFiles
import fmv1992.scala_cli_parser.conf._

// ???: This is a workaround since Scala Native does not give access to
// `Resources`.
//
// ???: This should be mixed in with `fmv1992.scala_cli_parser`:
//
// ```
// package object test extends fmv1992.scala_cli_parser {
// ```
//
// See also: <https://docs.scala-lang.org/tour/package-objects.html>.
package object test {

  def loadTestResource(basename: String): String = {
    // throw new Exception(Paths.get(".").toAbsolutePath.toString)
    try {
      scala.io.Source
        .fromResource(basename)
        .getLines()
        .mkString("\n")
    } catch {
      case _: NotImplementedError =>
        scala.io.Source
          .fromFile(
            findAllFiles(
              Paths
                .get("src", "test", "resources", basename)
            ).head.toFile
          )
          .getLines()
          .mkString("\n")
    }
  }
}
