#/bin/sh

mvn clean -o

mvn install -f maven/maven-config

mvn install -f maven/maven-parent

mvn install -f maven/model-archiver

mvn install -Dmaven.test.skip=true

mvn install -f andromda-andromdapp/ -Dmaven.test.skip=true
