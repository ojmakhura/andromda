<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:UML="omg.org/UML/1.4"
                version="1.0"
                exclude-result-prefixes="#default">

<xsl:template match="/">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="UML:MultiplicityRange.upper/text()">
	<xsl:choose>
		<xsl:when test="contains(.,'*')">-1</xsl:when>
		<xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>
