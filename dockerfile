# Inspired by `fmv1992_scala_utilities:b42a60f:dockerfile:1`.

FROM ubuntu:18.04@sha256:b58746c8a89938b8c9f5b77de3b8cf1fe78210c696ab03a1442e235eea65d84f

ARG project_name
ENV PROJECT_NAME $project_name

RUN apt-get update

# Install java.
RUN apt-get -y install openjdk-8-jdk
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Install support programs.
RUN apt-get install --yes git make wget zip
# Install Scala Native dependencies.
RUN apt-get install --yes clang libgc-dev
RUN rm -rf /var/lib/apt/lists/*

# Install sbt.
RUN mkdir -p /home/user/bin
WORKDIR /home/user/bin
RUN wget -O sbt.zip -- https://github.com/sbt/sbt/releases/download/v1.3.13/sbt-1.3.13.zip
RUN unzip sbt.zip
RUN rm sbt.zip
ENV PATH $PATH:/home/user/bin/sbt/bin

# Install commit `v1.8.5.dev.2.11.12` at `fmv1992_scala_utilities`.
WORKDIR /tmp
RUN git clone https://github.com/fmv1992/fmv1992_scala_utilities
RUN cd ./fmv1992_scala_utilities \
    && git reset --hard 3c37a2dc1e8f3cf544159dc5ef79ad1c42cc2c74 \
    && make publishlocal
RUN rm -rf ./fmv1992_scala_utilities

WORKDIR /home/user/
COPY . .
RUN cd ./${PROJECT_NAME} && sbt update
RUN make publishlocal

CMD bash
ENTRYPOINT bash

# vim: set filetype=dockerfile fileformat=unix nowrap:
