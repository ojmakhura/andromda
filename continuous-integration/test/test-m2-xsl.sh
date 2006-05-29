#!/bin/sh

if [ ! -z $2 ];then
   source set-jdk $2
fi

export CLASSPATH=/var/maven/repository/xalan/jars/xalan-2.7.0.jar

java org.apache.xalan.xslt.Process -IN $1 -XSL ../xsl/andromda-m2.xsl -OUT test.html
