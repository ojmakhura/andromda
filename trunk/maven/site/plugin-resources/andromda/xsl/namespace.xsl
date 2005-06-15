<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml"/>

    <xsl:template match="/">
        <document>
            <properties>
                <title>%module% Namespace</title>
            </properties>
            <body>
                <xsl:apply-templates/>
            </body>
        </document>
    </xsl:template>

    <xsl:template match="properties">
        <section name="%module% Namespace Properties">
<!--
            <p>
                <xsl:for-each select="property">
                    <xsl:sort select="@name"/>
                    <xsl:element name="a">
                        <xsl:attribute name="href">#<xsl:value-of select="@name"/></xsl:attribute>
                        <xsl:attribute name="title">
                            <xsl:choose>
                                <xsl:when test="@required = 'true'">Required,</xsl:when>
                                <xsl:otherwise>Optional,</xsl:otherwise>
                            </xsl:choose>
                            <xsl:choose>
                                <xsl:when test="normalize-space(default)"> defaults to '<xsl:value-of select="default"/>'</xsl:when>
                                <xsl:otherwise> no default value available</xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:value-of select="@name"/>
                    </xsl:element>
                    <xsl:text> </xsl:text>
                </xsl:for-each>
            </p>
-->
            <p>
                <xsl:apply-templates/>
            </p>
        </section>
    </xsl:template>

    <xsl:template match="property">
        <xsl:element name="a">
            <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
            <h3><xsl:value-of select="@name"/></h3>
        </xsl:element>
        <table>
            <tr>
                <td>
                    <xsl:choose>
                        <xsl:when test="@required = 'true'">
                            <div style="color:red;font-weight:bold;">Required property</div>
                        </xsl:when>
                        <xsl:otherwise>
                            <div style="color:green;font-weight:bold;">Optional property</div>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
                <td>
                    <xsl:choose>
                        <xsl:when test="normalize-space(default)">
                            Default value: <code style="color:blue;"><xsl:value-of select="default"/></code>
                        </xsl:when>
                        <xsl:otherwise>
                            No default value available
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <i>
                        <xsl:choose>
                            <xsl:when test="normalize-space(documentation)">
                                <xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[<xsl:value-of select="documentation" disable-output-escaping="yes"/>]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                No documentation available
                            </xsl:otherwise>
                        </xsl:choose>
                    </i>
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="components">
        <section name="%module% Namespace Components">
            <ul>
                <xsl:apply-templates/>
            </ul>
        </section>
    </xsl:template>

    <xsl:template match="component">
        <li><xsl:value-of select="@name"/></li>
    </xsl:template>

</xsl:stylesheet>
