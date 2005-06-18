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
            <xsl:for-each select="propertyGroup">
                <xsl:sort select="@name"/>
                <div class="namespacePropertyGroup">
                    <b><xsl:value-of select="@name"/></b><br/>
                    <xsl:if test="normalize-space(documentation)">
                        <div class="namespacePropertyGroupDocumentation">
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
                        <xsl:for-each select="property">
                            <xsl:sort select="@name"/>
                            <li>
                                <xsl:element name="a">
                                    <xsl:attribute name="href">#<xsl:value-of select="@name"/></xsl:attribute>
                                    <xsl:attribute name="title">
                                        <xsl:choose>
                                            <!-- when a default value is present the property is considered not to be required
                                                 to have a value specified by the user, when a default value is missing the property
                                                 is considered to be required when the 'required' attribute is not set to 'false' -->
                                            <xsl:when test="default or @required = 'false'">
                                                <xsl:choose>
                                                    <xsl:when test="default">
                                                        <xsl:choose>
                                                            <xsl:when test="normalize-space(default)">Optional, defaults to '<xsl:value-of select="default"/>'</xsl:when>
                                                            <xsl:otherwise>Optional, defaults to the empty string</xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:when>
                                                    <xsl:otherwise>Optional, no default value available</xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:when>
                                            <xsl:otherwise>Required</xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:attribute>
                                    <xsl:value-of select="@name"/>
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

    <xsl:template match="propertyGroup/documentation">
        <!-- do nothing -->
    </xsl:template>

    <xsl:template match="property">
        <xsl:element name="a">
            <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
            <h3><xsl:value-of select="@name"/></h3>
        </xsl:element>
        <div class="namespaceProperty">
            <table>
                <tr>
                    <td>
                        <xsl:choose>
                            <!-- when a default value is present the property is considered not to be required
                                 to have a value specified by the user, when a default value is missing the property
                                 is considered to be required when the 'required' attribute is not set to 'false' -->
                            <xsl:when test="default or @required = 'false'">
                                <div class="optional">Optional property</div>
                            </xsl:when>
                            <xsl:otherwise>
                                <div class="required">Required property</div>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
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
                    <td colspan="2" class="documentation">
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
