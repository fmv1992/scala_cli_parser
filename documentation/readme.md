# `scala_cli_parser`

*   `dev`:

    *   ![Build status](https://travis-ci.org/fmv1992/scala_cli_parser.svg?branch=dev)

    *   [![codecov](https://codecov.io/gh/fmv1992/scala_cli_parser/branch/dev/graph/badge.svg)](https://codecov.io/gh/fmv1992/scala_cli_parser)

*   Available for Scala `2.11` (both JVM and [Scala Native](http://www.scala-native.org/en/latest/user/sbt.html)), `2.12` and `2.13`.

* * *

A library for parsing command line arguments.

It's main feature is that CLI parsing is defined on a config file. For example consider a very simple sum program:

~~~~ {#mycode .scala .numberLines pipe="bash" startFrom="1"}
tail -n +3 scala_cli_parser/src/test/scala/TestSum.scala
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

It can be configured with the following config file:

~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
cat ./scala_cli_parser/src/test/resources/test_cli_example_05_sum.txt
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

And its usages are as follows:

1.  ~~~~ {#mycode .default .numberLines startFrom="1"}
    TestSum --version
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
    make --quiet -B SCALA_CLI_ARGUMENTS='TestSum.main(Array("--version"))' tmp/test_sum.scala
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1.  ~~~~ {#mycode .default .numberLines startFrom="1"}
    TestSum --help
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
    make --quiet -B SCALA_CLI_ARGUMENTS='TestSum.main(Array("--help"))' tmp/test_sum.scala
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1.  ~~~~ {#mycode .default .numberLines startFrom="1"}
    TestSum --sum 1992 1
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
    make --quiet -B SCALA_CLI_ARGUMENTS='TestSum.main(Array("--sum", "1992", "1"))' tmp/test_sum.scala
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

## TODO

*   When Scala 2.11 support is dropped: between `0022b3e0a0198d4c970531db3a74c25e0b055f98` and `37424215d82a77ca618333521bce4827394bee66` some shims had to be introduced to make 2.11 compatible with future versions. Revert this when dropping support for Scala 2.11.

*   `dev_unstable`:

    *   Improve parsing process. Parsers can actually fail and provide a useful error message.

### Backlog

*   Add [scalacheck](https://www.scalacheck.org/) to testing.
