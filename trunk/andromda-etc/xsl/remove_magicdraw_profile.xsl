<?xml version="1.0"?>
<?altova_samplexml ..\..\metafacades\uml\common\src\main\tiny\Tiny.uml?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xmi="http://schema.omg.org/spec/XMI/2.1" xmlns:uml="http://schema.omg.org/spec/UML/2.1" xmlns:MagicDraw_Profile="http://www.magicdraw.com/schemas/MagicDraw_Profile.xmi" xmlns:DSL_Customization="http://www.magicdraw.com/schemas/DSL_Customization.xmi" xmlns:StandardProfileL2="http://schema.omg.org/spec/UML/2.3/StandardProfileL2.xmi" xmlns:Traceability_customization="http://www.magicdraw.com/schemas/Traceability_customization.xmi" xmlns:meta="http://www.magicdraw.com/schemas/meta.xmi" xmlns:Validation_Profile="http://www.magicdraw.com/schemas/Validation_Profile.xmi" xmlns:StandardProfileL3="http://schema.omg.org/spec/UML/2.3/StandardProfileL3.xmi">
	<!-- <xsl:import href="copy.xsl"/> -->
	<xsl:import href="copy.xsl"/>
	<xsl:output method="xml" indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:param name="genNames" select="false"/>
	<!-- The following is an empty template that effectively filters out
     all of the nodes in the MagicDraw_Profile namespace.>
<xsl:template match="//MagicDraw_Profile:*" mode="subset"/>
<xsl:template match="//DSL_Customization:*" mode="subset"/>
<xsl:template match="//StandardProfileL2:*" mode="subset"/>
<xsl:template match="//StandardProfileL3:*" mode="subset"/>
<xsl:template match="//Validation_Profile:*" mode="subset"/>
<xsl:template match="//DSL_Customization:*" mode="subset"/ -->
	<!--xsl:template match="//xmi:Extension:*"/>
<xsl:template match="MagicDraw_Profile:*" priority="2"/-->
	<xsl:template match="xmi:XMI/uml:Model"/>
	<xsl:template match="//references[@href='UML_Standard_Profile.MagicDraw_Profile*']"/>
	<!--xsl:template match="xmi:XMI/uml:Model/profileApplication"/-->
	<!-- Output a copy of the original xml, but output nothing for elements that match the template values -->
	<xsl:template match="xmi:XMI/uml:Model/packagedElement[@xmi:id='magicdraw_uml_standard_profile_v_0001']"/>
	<xsl:template match="xmi:XMI/uml:Model/profileApplication/eAnnotations/references[@href='*MagicDraw*']"/>
	<xsl:template match="xmi:XMI/uml:Model/profileApplication/eAnnotations/references[@href='*DSL_Customization*']"/>
	<xsl:template match="xmi:XMI/uml:Model/profileApplication/eAnnotations/references[@href='*StandardProfileL2*']"/>
	<xsl:template match="xmi:XMI/uml:Model/profileApplication/eAnnotations/references[@href='*StandardProfileL3*']"/>
	<xsl:template match="xmi:XMI/uml:Model/profileApplication/eAnnotations/references[@href='*Validation_Profile*']"/>
	<xsl:template match="xmi:XMI/uml:Model/profileApplication/eAnnotations/references[@href='*DSL_Customization*']"/>
</xsl:stylesheet>

