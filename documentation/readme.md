# scala_cli_parser

A library for parsing command line arguments.

It's main feature is that it is based on a config file. For example consider a very simple sum program:

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
    TestSum --sum 1 10
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
    make --quiet -B SCALA_CLI_ARGUMENTS='TestSum.main(Array("--sum", "1", "10"))' tmp/test_sum.scala
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
