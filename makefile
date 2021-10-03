# Set variables.
SHELL := /bin/bash
ROOT_DIR := $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))
export PATH := $(PATH):./other/bin

export PROJECT_NAME ?= $(notdir $(ROOT_DIR))

# Find all scala files.
SBT_FILES := $(shell find $(PROJECT_NAME) -iname "build.sbt")
SCALA_FILES := $(shell find $(PROJECT_NAME) -iname '*.scala')
SBT_FOLDERS := $(dir $(SBT_FILES))

export SCALAC_OPTS := -Ywarn-dead-code
export _JAVA_OPTIONS ?= -Xms2048m -Xmx4096m
export SCALA_CLI_ARGUMENTS

# Build files.
FINAL_TARGET := ./scala_cli_parser/target/scala-2.11/scala_cli_parser.jar

# Test files.
BASH_TEST_FILES := $(shell find . -name 'tmp' -prune -o -iname '*test*.sh' -print)

# Set scala compilation flags.
# SCALAC_CFLAGS = -cp $$PWD:$(ROOT_DIR)/code/my_scala_project/

# ???: Google drive link to download ~/.sbt needed to compile this project.
# https://drive.google.com/open?id=1FoY3kQi52PWllwc3ytYU9452qJ4ack1u

# High level actions. --- {{{

all: dev test assembly publishlocal format readme.md doc_build doc_upload coverage

format:
	scalafmt --config ./scala_cli_parser/.scalafmt.conf $(SCALA_FILES) $(SBT_FILES)
	cd $(PROJECT_NAME) && sbt 'scalafixAll'

doc_build:
	cd $(PROJECT_NAME) && sbt 'project scala_cli_parserCrossProjectJVM;++2.13.4;+ doc'

doc_show:
	qutebrowser "file://$(shell find $(PWD) -iname 'index.html' -type f -printf '%d\t%p\n' | sort -r -nk1 | cut -f2- | tail -n 1)"

doc_upload:
	cd $(PROJECT_NAME) && sbt 'project root;ghpagesPushSite'

clean:
	find . -iname 'target' -print0 | xargs -0 rm -rf
	find . -path '*/project/*' -type d -prune -print0 | xargs -0 rm -rf
	find . -iname '*.class' -print0 | xargs -0 rm -rf
	find . -iname '.bsp' -print0 | xargs -0 rm -rf
	find . -iname '.metals' -print0 | xargs -0 rm -rf
	find . -iname '.bloop' -print0 | xargs -0 rm -rf
	find . -iname '*.hnir' -print0 | xargs -0 rm -rf
	find . -type d -empty -delete

coverage:
	# ???: hack to build the report.
	cd ./scala_cli_parser && sbt clean coverage test && (sbt coverageReport || sbt coverageAggregate || true)
	echo "Report can be found on '$$(find . -iname "index.html")'."

# ???: make the assembly process general.
assembly: $(FINAL_TARGET)

publishlocal: .FORCE
	cd ./scala_cli_parser && sbt clean update '+ publishLocal'

dev:
	cp -f ./other/git_hooks/git_pre_commit_hook.sh ./.git/hooks/pre-commit || true
	cp -f ./other/git_hooks/git_pre_push.sh ./.git/hooks/pre-push || true
	chmod a+x ./.git/hooks/pre-commit
	chmod a+x ./.git/hooks/pre-push

sbt:
	cd $(PROJECT_NAME) && sbt

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
test: docker_test test_host

test_host: test_sbt test_bash

test_bash: $(FINAL_TARGET)
	find ./test/bash/ -iname '*.sh' -print0 | xargs -0 -I % -n 1 -- bash -xv %

test_sbt:
	cd $(PROJECT_NAME) && sbt '+ test'

nativelink:
	cd $(PROJECT_NAME) && sbt 'nativeLink'

compile: $(SBT_FILES) $(SCALA_FILES)
	cd $(PROJECT_NAME) && sbt '+ compile'

# --- }}}

# --- }}}

# Specific targets. --- {{{

$(FINAL_TARGET): $(SCALA_FILES) $(SBT_FILES)
	cd ./scala_cli_parser && sbt '+ assembly'
	@# find . -iname "*assembly*.jar" | head -n 1 | xargs -I % mv % $@
	@# touch --no-create -m $@

tmp/scala_pandoc.jar:
	{ \
    export abspathtaget=$(shell readlink -f $@) ; \
    export tempd=$$(mktemp -d) ; \
    cd "$${tempd}" && \
        git clone --depth 1 --branch dev https://github.com/fmv1992/scala_pandoc && \
        cd scala_pandoc && \
        make && find . -iname "*.jar" -print0 | head -n 1 | xargs -0 mv -t $$(dirname $$abspathtaget) ; \
    touch -m $@ ; \
    cd $(ROOT_DIR) ; \
    }

tmp/test_sum.scala:
	rm $@ || true
	command -V scala_script >/dev/stderr 2>&1
	echo -e "\nimport fmv1992.scala_cli_parser._\n" >> $@
	tail -n +3 ./scala_cli_parser/src/test/scala/util/TestSum.scala >> $@
	echo -e '$(SCALA_CLI_ARGUMENTS)' >> $@
	abspath=$(shell readlink -f $@) \
            && cat "$$abspath" | scala_script

readme.md: tmp/test_sum.scala ./documentation/readme.md ./tmp/scala_pandoc.jar
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

# --- }}}

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
        --rm \
        --tty \
        --entrypoint '' \
        $(PROJECT_NAME) \
        $(if $(DOCKER_CMD),$(DOCKER_CMD),bash)

docker_test:
	DOCKER_CMD='make test_host' make docker_run
	DOCKER_CMD='make nativelink' make docker_run

# --- }}}

.FORCE:

# .EXPORT_ALL_VARIABLES:

.PRECIOUS: $(FINAL_TARGET) tmp/scala_pandoc.jar tmp/test_sum.scala

.PHONY: all clean test doc test_sbt test_bash

# vim: set noexpandtab foldmethod=marker fileformat=unix filetype=make nowrap foldtext=foldtext():
