<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml"/>

    <xsl:template match="/">
        <document>
            <properties>
                <title>%module% UML Profile</title>
            </properties>
            <body>
                <xsl:apply-templates/>
            </body>
        </document>
    </xsl:template>

    <xsl:template match="mappings">
        <section name="%module% UML Profile">
            <xsl:value-of select="documentation"/>
            <xsl:apply-templates/>
        </section>
    </xsl:template>

    <xsl:template match="mapping">
        <xsl:element name="a">
            <xsl:attribute name="name"><xsl:value-of select="to"/></xsl:attribute>
            <h3><xsl:value-of select="to"/></h3>
        </xsl:element>
        <div class="profileElement">
            <p>
                <xsl:value-of select="documentation"/>
            </p>
        </div>
    </xsl:template>

</xsl:stylesheet>
