#!/bin/bash

set -e
echo "build parameter: $1 $2"
JAVA_HOME="/home/rmachnik/sdkman/candidates/java/12.0.2-open"
echo $JAVA_HOME
mvn --version
mvn releaser:release
