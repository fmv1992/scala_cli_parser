object Main {
  def main(): Unit = {

    import java.io.ByteArrayOutputStream
    import java.nio.file.Files
    import java.nio.file.Paths

    require(Files.exists(Paths.get(System.getenv("SCALA_CONSOLE_TMP_FILE"))))

    val outCapture = new ByteArrayOutputStream

    Console.withOut(outCapture) {
      Console.withErr(outCapture) {
${CONTENTS}
      }
    }

    Files.write(Paths.get(System.getenv("SCALA_CONSOLE_TMP_FILE")), outCapture.toByteArray)
  }
}

Main.main()
