<?xml version="1.0"?>

<xsl:stylesheet

    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"

    xmlns:lxslt="http://xml.apache.org/xslt">



    <xsl:output method="html"/>



    <xsl:template match="/">

        <html>

        <head>

          <style type="text/css">

.white { color:#FFFFFF }



.index { background-color:#FFFFFF }

.index-passed { color:#004400 }

.index-failed { color:#FF0000; font-weight:bold }

.index-header { font-weight:bold }



.link { font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; text-decoration:none; }



.tab-table { margin: 0em 0em 0.5em 0em; }

.tabs { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; font-weight:bold; padding: 0em 2em; background-color:#EEEEEE; }

.tabs-link { color:#000000; text-decoration:none; }

.tabs-link:visited { color:#000000; text-decoration:none; }

.tabs-selected { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; font-weight:bold; padding: 0em 2em; }

.tabs-selected { border: inset; }



.header-title { font-family:arial,helvetica,sans-serif; font-size:12pt; color:#000000; font-weight:bold; }

.header-label { font-weight:bold; }

.header-data { font-family:arial,helvetica,sans-serif; font-size:10pt; color:#000000; }



.modifications-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }

.modifications-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }

.modifications-oddrow { background-color:#CCCCCC }

.modifications-evenrow { background-color:#FFFFCC }



.changelists-oddrow { background-color:#CCCCCC }

.changelists-evenrow { background-color:#FFFFCC }

.changelists-file-spacer { background-color:#FFFFFF }

.changelists-file-evenrow { background-color:#EEEEEE }

.changelists-file-oddrow { background-color:#FFFFEE }

.changelists-file-header { background-color:#666666; font-family:arial,helvetica,sans-serif; font-size:8pt; color:#FFFFFF; }



.compile-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }

.compile-error-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#FF0000; }

.compile-warn-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#CC9900; }

.compile-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }



.distributables-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }

.distributables-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }

.distributables-oddrow { background-color:#CCCCCC }



.unittests-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }

.unittests-oddrow { background-color:#CCCCCC }

.unittests-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }

.unittests-error { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#901090; }

.unittests-failure { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#FF0000; }



.checkstyle-oddrow { background-color:#CCCCCC }

.checkstyle-data { font-family:arial,helvetica,sans-serif; font-size:8pt; color:#000000; }

.checkstyle-sectionheader { background-color:#000066; font-family:arial,helvetica,sans-serif; font-size:10pt; color:#FFFFFF; }

          </style>

        </head>



        <table align="center" cellpadding="2" cellspacing="0" border="0" width="98%">

            <tr><td class="header-data">
                <span class="header-label">Full Build Results:&#160;</span>
                <a href="http://andromda.it.su.se/cruisecontrol">Build results</a>
            </td></tr>

            <xsl:if test="cruisecontrol/build/@time">
                <tr><td class="header-title">BUILD TIME</td></tr>
                <tr><td class="header-data">
                    <xsl:value-of select="cruisecontrol/build/@time"/>
                </td></tr>
            </xsl:if>

            <xsl:if test="cruisecontrol/build/@error">
                <tr><td class="header-title">BUILD FAILED</td></tr>
                <tr><td class="header-data">
                     <xsl:value-of select="cruisecontrol/build/@error"/>
                </td></tr>
                <xsl:apply-templates select="/cruisecontrol/build/message"/>
            </xsl:if>

            <xsl:if test="not (cruisecontrol/build/@error)">
                <tr><td class="header-title">BUILD COMPLETE&#160;-&#160;
                    <xsl:value-of select="cruisecontrol/info/property[@name='label']/@value"/>
                </td></tr>
            </xsl:if>

            <tr><td class="header-data">
                <span class="header-label">Date of build:&#160;</span>
                <xsl:value-of select="cruisecontrol/info/property[@name='builddate']/@value"/>
            </td></tr>

        </table>

        <xsl:variable name="testsuite" select="cruisecontrol/testsuite"/>

        <xsl:variable name="testsuite.syserr" select="cruisecontrol/testsuite/system-err"/>

        <xsl:variable name="junit.failures" select="$testsuite[@failures!='0']"/>


        <HR/><H2>Modifications</H2>

        <table align="center" cellpadding="2" cellspacing="0" border="0" width="98%">

        <xsl:variable name="modification.list" select="cruisecontrol/modifications/modification"/>

        <xsl:apply-templates select="$modification.list">

            <xsl:sort select="date" order="descending" data-type="text" />

        </xsl:apply-templates>

        </table>
        </html>

    </xsl:template>



    <xsl:template name="break">

      <xsl:param name="text" select="."/>
      <xsl:choose>
         <xsl:when test="contains($text, '&#xa;')">
            <xsl:value-of select="substring-before($text, '&#xa;')" disable-output-escaping="yes" /><br/>
            <xsl:call-template name="break">
               <xsl:with-param name="text" select="substring-after($text,'&#xa;')"/>
            </xsl:call-template>
         </xsl:when>
         <xsl:otherwise>
           <xsl:value-of select="$text" disable-output-escaping="yes" />
         </xsl:otherwise>
      </xsl:choose>

    </xsl:template>

    <xsl:template match="testcase/failure">
        <tr><td>
          <span class="compile-error-data">
        <xsl:value-of select="@message"/>
          </span>
        </td></tr>
        <tr><td>
        <example><xsl:call-template name="break" /></example>
        </td></tr>

    </xsl:template>

    <xsl:template match="system-err">

		<xsl:if test="string-length() > 0">
          <tr><td>
          <span class="compile-error-data">
          <xsl:value-of select="@message"/>
          </span>
          </td></tr>

          <tr><td>
          <example><xsl:call-template name="break" /></example>
          </td></tr>

    	</xsl:if>

    </xsl:template>

    <xsl:template match="mavengoal[@name='jar:deploy']">
        <xsl:apply-templates select="message[@priority='info']"/>
    </xsl:template>

    <xsl:template match="mavengoal[@name='deploy-docs']">
        <xsl:apply-templates select="message[@priority='info']"/>
    </xsl:template>

    <xsl:template match="mavengoal[@name='deploy-repository']">
        <xsl:apply-templates select="message[@priority='info']"/>
    </xsl:template>

    <xsl:template match="mavengoal[@name='deploy-build']">
        <xsl:apply-templates select="message[@priority='info']"/>
    </xsl:template>

    <xsl:template match="message[@priority='error']">
      <xsl:param name="text" select="."/>

         <tr><td>

			<!--  [cvs] C -->
			<xsl:if test="contains($text, '[cvs] C')">
				<BOLD>*** CVS Conflict ***</BOLD><BR></BR>
			</xsl:if>

          <span class="compile-error-data">
            <xsl:value-of select="text()"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
          </span>

         </td></tr>

    </xsl:template>

    <xsl:template match="message[@priority='warn']">
    	  <span class="compile-data">
            <xsl:value-of select="text()"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
          </span>
    </xsl:template>

    <xsl:template match="message[@priority='info']">

       <xsl:variable name="infoTest" select="string()" />
       <xsl:choose>

        <xsl:when test="starts-with($infoTest,'[ERROR]')">
          <tr><td class="header-title">ERROR Messages</td></tr>
          <tr><td class="header-data">
          <span class="compile-data">
		  <!-- Identify the stopper - the first node that starts with [INFO] Building  -->
		  <!--xsl:variable name="stop-id" select="generate-id(preceding-sibling::*)"/-->

		  <xsl:value-of select="preceding-sibling::*[11][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[10][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[9][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[8][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[7][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[6][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[5][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[4][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[3][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[2][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:value-of select="preceding-sibling::*[1][text()]"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
          <xsl:value-of select="text()"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  <xsl:for-each select="following-sibling::*">
            <xsl:value-of select="text()"/><xsl:text disable-output-escaping="yes"><![CDATA[<br/>]]></xsl:text>
		  </xsl:for-each>
          </span>
          </td></tr>
        </xsl:when>

        <xsl:otherwise>
        </xsl:otherwise>

       </xsl:choose>

    </xsl:template>
 
    <xsl:template match="modification">

        <tr>
            <xsl:if test="position() mod 2=0">
                <xsl:attribute name="class">modifications-oddrow</xsl:attribute>
            </xsl:if>

            <xsl:if test="position() mod 2!=0">
                <xsl:attribute name="class">modifications-evenrow</xsl:attribute>
            </xsl:if>

            <td class="modifications-data">
                <xsl:value-of select="@type"/>
            </td>

            <td class="modifications-data">
                <xsl:value-of select="user"/>
            </td>

            <td class="modifications-data">
                <xsl:if test="project">
                    <xsl:value-of select="project"/>
                    <xsl:value-of select="'/'"/>
                    <!--xsl:value-of select="system-property('file.separator')"/-->
                </xsl:if>
                <xsl:value-of select="filename"/>
            </td>

            <td class="modifications-data">
                <xsl:value-of select="comment"/>
            </td>

        </tr>

    </xsl:template>

</xsl:stylesheet>
