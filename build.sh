#!/bin/bash

set -e
echo "build parameter: $1 $2"
echo $JAVA_HOME
JAVA_HOME=$JAVA_12
echo $JAVA_HOME
mvn --version
mvn releaser:release
