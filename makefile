# Set variables.
SHELL := /bin/bash
ROOT_DIR := $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))

export PROJECT_NAME := $(notdir $(ROOT_DIR))

# Find all scala files.
SBT_FILES := $(shell find ./ -iname "build.sbt")
SCALA_FILES := $(shell find $(dir $@) -iname '*.scala')
SBT_FOLDERS := $(dir $(SBT_FILES))

export SCALAC_OPTS := -Ywarn-dead-code -Xlint:unused

# Build files.
FINAL_TARGET := ./scala_cli_parser/target/scala-2.11/scala_cli_parser.jar

# Test files.
BASH_TEST_FILES := $(shell find . -name 'tmp' -prune -o -iname '*test*.sh' -print)

# Set scala compilation flags.
# SCALAC_CFLAGS = -cp $$PWD:$(ROOT_DIR)/code/my_scala_project/

# ???: Google drive link to download ~/.sbt needed to compile this project.
# https://drive.google.com/open?id=1FoY3kQi52PWllwc3ytYU9452qJ4ack1u

all: dev test assembly publishlocal doc coverage

format:
	find . \( -iname '*.scala' -o -iname '*.sbt' \) -print0 | xargs --verbose -0 scalafmt --config .scalafmt.conf

doc:
	cd $(dir $(firstword $(SBT_FILES))) && sbt doc

clean:
	find . -iname 'target' -print0 | xargs -0 rm -rf
	find . -path '*/project/*' -type d -prune -print0 | xargs -0 rm -rf
	find . -iname '*.class' -print0 | xargs -0 rm -rf
	find . -iname '*.hnir' -print0 | xargs -0 rm -rf
	find . -type d -empty -delete

coverage:
	# ???: hack to build the report.
	cd ./scala_cli_parser && sbt clean coverage test && (sbt coverageReport || sbt coverageAggregate || true)
	echo "Report can be found on '$$(find . -iname "index.html")'."

# Test actions. --- {{{
# Killing a running process with:
#
#    SIGKILL                 → 137
#    SIGHUP                  → 129
#    CTRL+C                  → 127
#    `throw new Exception()` → 1
#    `require(1 == 2)`       → 1
#    With `make test`: timeout + `require(1 == 2)`          → 143
#    With `make test`: timeout + `throw new Exception()`    → 143
#    With `make test`: timeout + no errors or exceptions... → 143
#
# Also from the program `timeout`'s documentation:
#
# "If  the  command times out, and --preserve-status is not set, then exit with
# status 124."
#
# The logic above does not work because `scala` inside a shell script does not
# terminate with timeout (meaning it always timeouts).
test: test_sbt test_bash

test_bash: $(FINAL_TARGET) $(BASH_TEST_FILES)

test_sbt: $(SBT_FILES)

compile: $(SBT_FILES) $(SCALA_FILES)
	cd $(dir $@) && sbt compile

$(SBT_FILES): .FORCE
	cd $(dir $@) && sbt test assembly
	touch --no-create -m $@

# --- }}}

# ???: make the assembly process general.
assembly: $(FINAL_TARGET)

publishlocal: .FORCE
	# sbt "clean" "set offline := true" "clean" 'publishLocal'
	# test -e $(HOME)/.ivy2/local/fmv1992.org
	cd ./scala_cli_parser && sbt clean update publishLocal

dev:
	cp -f ./other/git_hooks/git_pre_commit_hook.sh ./.git/hooks/pre-commit || true
	cp -f ./other/git_hooks/git_pre_push.sh ./.git/hooks/pre-push || true
	chmod a+x ./.git/hooks/pre-commit
	chmod a+x ./.git/hooks/pre-push

$(FINAL_TARGET): $(SCALA_FILES) $(SBT_FILES)
	cd ./scala_cli_parser && sbt assembly
	find . -iname "*assembly*.jar" | head -n 1 | xargs -I % mv % $@
	touch --no-create -m $@

test%.sh: .FORCE
	bash -xv $@

tmp/scala_pandoc.jar:
	{ \
	export abspathtaget=$(shell readlink -f $@) ; \
	cd $$(mktemp -d) && \
		git clone --depth 1 --branch dev https://github.com/fmv1992/scala_pandoc && \
		cd scala_pandoc && \
		make && find . -iname "*.jar" -print0 | head -n 1 | xargs -0 mv -t $$(dirname $$abspathtaget) ; \
	touch -m $@ ; \
	}

tmp/test_sum.scala:
	echo -e '/*** scalaVersion := "2.11.12"\n     libraryDependencies += "fmv1992" %% "scala_cli_parser" % "0.+"\n*/' > $@
	echo -e "\nimport fmv1992.scala_cli_parser._\n" >> $@
	tail -n +3 ./scala_cli_parser/src/test/scala/TestSum.scala >> $@
	tail -n +3 ./scala_cli_parser/src/test/scala/Example.scala >> $@
	echo -e '$(SCALA_CLI_ARGUMENTS)' >> $@
	abspath=$(shell readlink -f $@) && cd ./scala_cli_parser && sbtx -script $$abspath

readme.md: $(FINAL_TARGET) ./documentation/readme.md ./tmp/scala_pandoc.jar
	pandoc2 --from markdown --to json ./documentation/readme.md \
		| java -jar ./tmp/scala_pandoc.jar \
				--evaluate \
				--embed \
		| pandoc2 \
			--from json \
			--to gfm \
			> /tmp/$(notdir $@)
	cat \
		<(echo '[comment]: # ( ???: XXX: Do not edit this file directly! Edit `./documentation/readme.md` and `make` this file.)') \
		<(echo ) \
		/tmp/$(notdir $@) > $@
	rm /tmp/$(notdir $@)

# Docker actions. --- {{{

docker_build:
	docker build \
        --file ./dockerfile \
        --tag $(PROJECT_NAME) \
        --build-arg project_name=$(PROJECT_NAME) \
        -- . \
        1>&2

docker_run:
	docker run \
        --interactive \
        --tty \
        --entrypoint '' \
        $(PROJECT_NAME) \
        $(if $(DOCKER_CMD),$(DOCKER_CMD),bash)

docker_test:
	DOCKER_CMD='make test' make docker_run

# --- }}}

.FORCE:

# .EXPORT_ALL_VARIABLES:

.PRECIOUS: $(FINAL_TARGET) tmp/scala_pandoc.jar

.PHONY: all clean test doc test_sbt test_bash

# vim: set noexpandtab foldmethod=marker fileformat=unix filetype=make nowrap foldtext=foldtext():
