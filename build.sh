#!/bin/bash

set -e
echo "build parameter: $1 $2"
JAVA_HOME=$JAVA_12/bin
mvn --version
mvn releaser:release
