<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:UML = 'org.omg/UML/1.3'
                version="1.0"
                exclude-result-prefixes="#default">

<xsl:template match="/">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="UML:Classifier">
  <xsl:apply-templates />
</xsl:template>

<xsl:template match="UML:Classifier/UML:Namespace.ownedElement">
  <xsl:apply-templates />
</xsl:template>


<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>
