#!/bin/bash

set -e
echo "build parameter: $1 $2"
export JAVA_HOME="~/.sdkman/candidates/java/14.0.1-open"
echo $JAVA_HOME
mvn --version
mvn releaser:release
