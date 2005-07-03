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
            <p>
                This profile contains all elements that can be applied on the model used as the
                MDA transformation process. These elements are specific to the %module% component.
            </p>
            <xsl:for-each select="elementGroup">
                <xsl:sort select="@name"/>
                <div class="profileElementGroup">
                    <b><xsl:value-of select="@name"/></b><br/>
                    <xsl:if test="@name='Stereotypes'">
                        <div class="profileElementGroupDocumentation">
                            Stereotypes are the names you sometimes see appear in UML diagrams, they typically look
                            like <![CDATA[<<MyStereotype>>]]> and can be applied on any type of UML model element.
                        </div>
                    </xsl:if>
                    <xsl:if test="@name='Tagged Values'">
                        <div class="profileElementGroupDocumentation">
                            Sometimes it happens you want to add information to the model but there is no clean way
                            of doing it. In those cases you may want to resort to tagged values, but these cases should
                            be considered with caution. A tagged value is something extra, something optional,
                            and the application should run fine without them.
                        </div>
                    </xsl:if>
                    <xsl:if test="@name='Data Types'">
                        <div class="profileElementGroupDocumentation">
                            @TODO: write the documentation for 'Data Types' profile element group !!!
                        </div>
                    </xsl:if>
                    <xsl:if test="normalize-space(documentation)">
                        <div class="profileElementGroupDocumentation">
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
                                    <xsl:attribute name="href">#<xsl:value-of select="normalize-space(value)"/></xsl:attribute>
                                    <xsl:attribute name="title">
                                        <xsl:if test="appliedOnElement">Applies on: <xsl:value-of select="normalize-space(appliedOnElement)"/></xsl:if>
                                    </xsl:attribute>
                                    <xsl:value-of select="normalize-space(value)"/>
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
            <xsl:attribute name="name"><xsl:value-of select="normalize-space(value)"/></xsl:attribute>
            <h3><xsl:value-of select="value"/></h3>
        </xsl:element>
        <div class="profileElement">
            <table>
                <xsl:if test="appliedOnElement">
                    <tr>
                        <td>
                            Applies on: <xsl:copy-of select="appliedOnElement"/>
                        </td>
                    </tr>
                </xsl:if>
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
                <xsl:if test="allowedValues">
                    <tr>
                        <td>
                            Allowed values for this element:
                            <ul>
                                <xsl:for-each select="allowedValues/value">
                                    <li>
                                        <xsl:choose>
                                            <xsl:when test="@default='true'">
                                                <code class="defaultValue"><xsl:copy-of select="."/></code> (default)
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <code><xsl:copy-of select="."/></code>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </li>
                                </xsl:for-each>
                            </ul>
                        </td>
                    </tr>
                </xsl:if>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="documentation//*">
        <xsl:apply-templates/>
    </xsl:template>

</xsl:stylesheet>
