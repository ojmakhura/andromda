<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:import href="file:/D:/develop/libraries/docbook/docbook-xsl-1.69.1/eclipse/eclipse.xsl" />

    <xsl:param name="html.stylesheet">../book.css</xsl:param>
    <xsl:param name="chunk.first.sections" select="1" />
    <xsl:param name="chunk.section.depth" select="3" />
    <xsl:param name="base.dir" select="'contents/'"></xsl:param>
    <xsl:param name="use.id.as.filename" select="0" />
    <xsl:param name="suppress.navigation" select="1" />
    <xsl:param name="chapter.autolabel" select="0" />
    <xsl:param name="generate.section.toc.level" select="0"></xsl:param>

    <xsl:param name="table.borders.with.css" select="0"></xsl:param>
    <xsl:param name="table.cell.border.thickness" select="'1'"></xsl:param>
    <xsl:param name="html.cellspacing" select="'0'"></xsl:param>
    <xsl:param name="html.cellpadding" select="'10'"></xsl:param>

    <xsl:param name="html.cleanup" select="1"></xsl:param>

    <xsl:param name="generate.toc">
        appendix  toc,title
        article/appendix  nop
        article   toc,title
        book      nop
        chapter   nop
        part      nop
        preface   toc,title
        qandadiv  toc
        qandaset  toc
        reference toc,title
        sect1     toc
        sect2     toc
        sect3     toc
        sect4     toc
        sect5     toc
        section   toc
        set       toc,title
    </xsl:param>

</xsl:stylesheet>