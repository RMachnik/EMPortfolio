#!/bin/bash

set -e
echo "build parameter: $1 $2"
echo $JAVA_12
mvn --version
mvn releaser:release
