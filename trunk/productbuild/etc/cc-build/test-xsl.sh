#!/bin/sh

java org.apache.xalan.xslt.Process -IN $1 -XSL xsl/andromda.xsl -OUT test.html
