<?xml version="1.0"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:XMI="http://schema.omg.org/spec/XMI/2.1"
    xmlns:uml="http://schema.omg.org/spec/UML/2.1.2"
    xmlns:cmof="http://schema.omg.org/spec/MOF/2.0/cmof.xml"
    >

<!-- <xsl:import href="copy.xsl"/> -->
<xsl:import href="copy.xsl"/>

<xsl:output method="xml" indent="yes"/>
<xsl:strip-space elements="*"/>


<!-- Filter out extensions -->
<xsl:template match="//XMI.Extension"/>


</xsl:stylesheet>

