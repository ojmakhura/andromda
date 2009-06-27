#!/bin/sh

if [ ! -z $2 ];then
   source set-jdk $2
fi

java org.apache.xalan.xslt.Process -IN $1 -XSL xsl/andromda.xsl -OUT test.html
