<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml"/>

    <xsl:template match="/">
        <document>
            <properties>
                <title>%module% Profile</title>
            </properties>
            <body>
                <xsl:if test="normalize-space(documentation)">
                    <xsl:copy-of select="documentation"/>
                </xsl:if>
                <xsl:apply-templates/>
            </body>
        </document>
    </xsl:template>

    <xsl:template match="elements">
        <section name="%module% Profile">
            <xsl:for-each select="elementGroup">
                <xsl:sort select="@name"/>
                <div class="elementGroup">
                    <b><xsl:value-of select="@name"/></b><br/>
                    <xsl:if test="normalize-space(documentation)">
                        <div class="elementGroupDocumentation">
                            <xsl:choose>
                                <xsl:when test="normalize-space(documentation)">
                                    <xsl:copy-of select="documentation"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    No documentation available
                                </xsl:otherwise>
                            </xsl:choose>
                        </div>
                    </xsl:if>
                    <ul>
                        <xsl:for-each select="element">
                            <xsl:sort select="value"/>
                            <li>
                                <xsl:element name="a">
                                    <xsl:attribute name="href">#<xsl:value-of select="value"/></xsl:attribute>
                                    <xsl:value-of select="value"/>
                                </xsl:element>
                            </li>
                        </xsl:for-each>
                    </ul>
                </div>
            </xsl:for-each>
            <p>
                <xsl:apply-templates/>
            </p>
        </section>
    </xsl:template>

    <xsl:template match="elementGroup/documentation">
        <!-- do nothing -->
    </xsl:template>

    <xsl:template match="element">
        <xsl:element name="a">
            <xsl:attribute name="name"><xsl:value-of select="value"/></xsl:attribute>
            <h3><xsl:value-of select="value"/></h3>
        </xsl:element>
        <div class="element">
            <table>
                <tr>
                    <td>
                        <xsl:choose>
                            <xsl:when test="default">
                                Default value: <code class="defaultValue"><xsl:value-of select="default"/></code>
                            </xsl:when>
                            <xsl:otherwise>
                                No default value available
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
                <tr>
                    <td class="documentation">
                        <xsl:choose>
                            <xsl:when test="normalize-space(documentation)">
                                <xsl:copy-of select="documentation"/>
                            </xsl:when>
                            <xsl:otherwise>
                                No documentation available
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="documentation//*">
        <xsl:apply-templates/>
    </xsl:template>

</xsl:stylesheet>
