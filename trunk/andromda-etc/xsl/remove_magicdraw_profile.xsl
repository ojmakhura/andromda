<?xml version="1.0"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:XMI="http://schema.omg.org/spec/XMI/2.1"
    xmlns:uml="http://schema.omg.org/spec/UML/2.1"
    xmlns:MagicDraw_Profile="http://www.magicdraw.com/schemas/MagicDraw_Profile.xmi"
    >

<!-- <xsl:import href="copy.xsl"/> -->
<xsl:import href="copy.xsl"/>

<xsl:output method="xml" indent="yes"/>
<xsl:strip-space elements="*"/>
<xsl:param name="genNames" select="false"/>


<!-- The following is an empty template that effectively filters out
     all of the nodes in the MagicDraw_Profile namespace. -->
<xsl:template match="MagicDraw_Profile:*" priority="2"/>
<xsl:template match="//XMI.extension:*" priority="2"/>

</xsl:stylesheet>

