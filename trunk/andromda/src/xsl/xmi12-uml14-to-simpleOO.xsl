<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:UML = 'org.omg.xmi.namespace.UML'
                version="1.0"
                exclude-result-prefixes="#default">

<xsl:output indent="yes" encoding="ISO-8859-1"
/>
<!--
            doctype-system="simpleOO.dtd"
-->

<xsl:preserve-space elements="*" />

<xsl:key
    name="tagdefinition"
    match="//UML:TagDefinition"
    use="@xmi.id"/>

<xsl:key
    name="generalization"
    match="//UML:Generalization"
    use="@xmi.id"/>

<!-- Document Root -->
<xsl:template match="/">
    <xsl:apply-templates select="//UML:Model" />
</xsl:template>


<!-- Model -->
<xsl:template match="UML:Model" >
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <xsl:variable name="name"
         select="@name"/>

    <model name="{$name}" id="{$xmi_id}">
        <!-- Datatypes -->
        <xsl:apply-templates select="UML:Namespace.ownedElement/UML:DataType">
        </xsl:apply-templates>

        <!-- Classes -->
        <xsl:apply-templates select="UML:Namespace.ownedElement/UML:Class">
        </xsl:apply-templates>

        <!-- Interfaces -->
        <xsl:apply-templates select="UML:Namespace.ownedElement/UML:Interface">
        </xsl:apply-templates>

        <xsl:apply-templates select="UML:Namespace.ownedElement/UML:Package">
        </xsl:apply-templates>

        <!-- Stereotypes with xmi.id (not xmi.idref!) -->
        <xsl:apply-templates select="//UML:Stereotype[@xmi.id]">
        </xsl:apply-templates>

        <!-- Associations -->
        <xsl:apply-templates select="//UML:Association">
        </xsl:apply-templates>

    </model>
</xsl:template>


<!-- Package -->
<xsl:template match="UML:Namespace.ownedElement/UML:Package" >
    <xsl:param name="parentpackage"/>

    <xsl:variable name="name" select="@name"/>
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <package package="{$parentpackage}" name="{$name}" id="{$xmi_id}">
        <xsl:apply-templates select="UML:Namespace.ownedElement/UML:DataType">
            <xsl:with-param name="package" select="$xmi_id"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="UML:Namespace.ownedElement/UML:Class">
            <xsl:with-param name="package" select="$xmi_id"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="UML:Namespace.ownedElement/UML:Interface">
            <xsl:with-param name="package" select="$xmi_id"/>
        </xsl:apply-templates>
    </package>

    <!-- Packages -->
    <xsl:apply-templates select="UML:Namespace.ownedElement/UML:Package">
         <xsl:with-param name="parentpackage" select="$xmi_id"/>
    </xsl:apply-templates>

</xsl:template>


<!-- Association -->
<xsl:template match="UML:Namespace.ownedElement/UML:Association" >

    <xsl:variable name="name" select="@name"/>
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <association name="{$name}" id="{$xmi_id}">
        <xsl:apply-templates select=".//UML:AssociationEnd">
        </xsl:apply-templates>

        <!-- Tagged values -->
        <xsl:apply-templates select="UML:ModelElement.taggedValue/UML:TaggedValue" />
    </association>
</xsl:template>


<!-- DataType -->
<xsl:template match="UML:DataType">
    <xsl:param name="package"/>
    <xsl:variable name="element_name" select="@name"/>
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <xsl:variable name="visibility" select="@visibility"/>

    <xsl:variable name="supertypes">
        <xsl:call-template name="supertypes"/>
    </xsl:variable>

    <datatype name="{$element_name}" package="{$package}" id="{$xmi_id}" visibility="{$visibility}">
        <!-- JAXB has problems with empty NMTOKENS lists, so make these attributes optional -->
        <xsl:if test="string($supertypes)">
            <xsl:attribute name="supertypes">
                <xsl:value-of select="$supertypes" />
            </xsl:attribute>
        </xsl:if>

        <xsl:call-template name="attributes"/>
        <xsl:call-template name="operations"/>
        <xsl:call-template name="associations">
            <xsl:with-param name="source" select="$xmi_id"/>
        </xsl:call-template>

        <xsl:apply-templates select="UML:ModelElement.clientDependency/UML:Dependency" />
        <xsl:apply-templates select="UML:ModelElement.taggedValue/UML:TaggedValue" />
    </datatype>
</xsl:template>


<!-- Class -->
<xsl:template match="UML:Class">
    <xsl:param name="package"/>
    <xsl:variable name="element_name" select="@name"/>
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <xsl:variable name="visibility" select="@visibility"/>
    <xsl:variable name="isAbstract" select="@isAbstract"/>

    <xsl:variable name="supertypes">
        <xsl:call-template name="supertypes"/>
    </xsl:variable>

    <class name="{$element_name}" package="{$package}" id="{$xmi_id}" visibility="{$visibility}" abstract="{$isAbstract}" >
        <!-- JAXB has problems with empty NMTOKENS lists, so make these attributes optional -->
        <xsl:if test="string($supertypes)">
            <xsl:attribute name="supertypes">
                <xsl:value-of select="$supertypes" />
            </xsl:attribute>
        </xsl:if>

        <xsl:call-template name="attributes"/>
        <xsl:call-template name="operations"/>
        <xsl:call-template name="associations">
            <xsl:with-param name="source" select="$xmi_id"/>
        </xsl:call-template>

        <xsl:apply-templates select="UML:ModelElement.clientDependency/UML:Dependency" />
        <xsl:apply-templates select="UML:ModelElement.taggedValue/UML:TaggedValue" />
    </class>
</xsl:template>


<!-- Interface -->
<xsl:template match="UML:Interface">
    <xsl:param name="package"/>
    <xsl:variable name="element_name" select="@name"/>
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <xsl:variable name="visibility" select="@visibility"/>

    <xsl:variable name="supertypes">
        <xsl:call-template name="supertypes"/>
    </xsl:variable>

    <interface name="{$element_name}" package="{$package}" id="{$xmi_id}" visibility="{$visibility}" >
        <!-- JAXB has problems with empty NMTOKENS lists, so make these attributes optional -->
        <xsl:if test="string($supertypes)">
            <xsl:attribute name="supertypes">
                <xsl:value-of select="$supertypes" />
            </xsl:attribute>
        </xsl:if>

        <xsl:call-template name="attributes"/>
        <xsl:call-template name="operations"/>
        <xsl:call-template name="associations">
            <xsl:with-param name="source" select="$xmi_id"/>
        </xsl:call-template>

        <xsl:apply-templates select="UML:ModelElement.clientDependency/UML:Dependency" />
        <xsl:apply-templates select="UML:ModelElement.taggedValue/Foundation.Extension_Mechanisms.TaggedValue" />
    </interface>

</xsl:template>


<!-- Supertypes (inheritance) -->
<xsl:template name="supertypes">

    <!-- Generalizations identify supertypes -->
    <xsl:variable name="generalizations"
         select="UML:GeneralizableElement.generalization/
                 UML:Generalization"/>

    <xsl:variable name="result">
        <xsl:for-each select="$generalizations">

            <!-- get the parent in the generalization -->
            <xsl:variable name="generalization"
                 select="key('generalization', ./@xmi.idref)" />
            <xsl:variable name="target"
                 select="$generalization/
                        UML:Generalization.parent/
                        */@xmi.idref" />

            <xsl:value-of select="$target" />
            <xsl:text> </xsl:text>
        </xsl:for-each>
    </xsl:variable>

    <xsl:value-of select="$result" />

</xsl:template>


<!-- Dependency -->
<xsl:template match="UML:Dependency">

    <xsl:variable name="xmi_id" select="@xmi.idref" />
    <xsl:variable name="name" select="@name"/>

    <xsl:variable name="dependency_definition"
         select="//UML:Dependency[@xmi.id = $xmi_id]" />

    <!-- get the supplier in the dependency -->
    <xsl:variable name="target"
         select="$dependency_definition/UML:Dependency.supplier/*/@xmi.idref" />

    <dependency target-type="{$target}" name="${name}" id="{$xmi_id}" />

</xsl:template>


<!-- Associations -->
<xsl:template name="associations">
    <xsl:param name="source"/>

    <xsl:variable name="association_ends"
         select="//UML:AssociationEnd
                 [UML:AssociationEnd.participant/*/
                  @xmi.idref=$source]" />

    <xsl:if test="count($association_ends) > 0">

        <xsl:for-each select="$association_ends">
            <xsl:variable name="association"
                 select="ancestor::UML:Association" />

            <xsl:variable name="associd" select="$association/@xmi.id" />
            <xsl:variable name="thisend" select="./@xmi.id" />

            <association-link associd="{$associd}" thisend="{$thisend}" />
        </xsl:for-each>
    </xsl:if>
</xsl:template>


<!-- Association End -->
<xsl:template match="UML:AssociationEnd">

  <xsl:variable name="xmi_id" select="@xmi.id" />


  <!-- Visibility -->
  <xsl:variable name="visibility">
    <xsl:value-of select="@visibility"/>
  </xsl:variable>

  <!-- Type -->
  <xsl:variable name="typeid"
       select="UML:AssociationEnd.participant/*/@xmi.idref" />

  <association-end id="{$xmi_id}" visibility="{$visibility}" type="{$typeid}" >

    <!-- Properties -->
    <properties>
        <!-- Rolename -->
        <rolename>
            <xsl:value-of select="@name"/>
        </rolename>

        <!-- Multiplicity -->
        <xsl:apply-templates select=".//UML:Multiplicity" />

        <!-- Navigable -->
        <navigable>
            <xsl:value-of select="@isNavigable"/>
        </navigable>

        <!-- Aggregation -->
        <aggregation>
            <xsl:value-of select="@aggregation"/>
        </aggregation>

        <!-- Ordering -->
        <ordering>
            <xsl:value-of select="@ordering"/>
        </ordering>
    </properties>

    <!-- Tagged values -->
    <xsl:apply-templates select="UML:ModelElement.taggedValue/UML:TaggedValue" />

  </association-end>
</xsl:template>


<!-- Multiplicity (definition) -->
<xsl:template match="UML:Multiplicity">
    <multiplicity>
    <xsl:variable name="lower"
         select=".//UML:MultiplicityRange/@lower"/>

    <xsl:variable name="upper"
         select=".//UML:MultiplicityRange/@upper"/>

    <lower> <xsl:value-of select="$lower" /> </lower>
    <upper> <xsl:value-of select="$upper" /> </upper>

    </multiplicity>
</xsl:template>

<!-- Attributes -->
<xsl:template name="attributes">
    <xsl:apply-templates select="UML:Classifier.feature/UML:Attribute" />
</xsl:template>


<xsl:template match="UML:Attribute">
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <xsl:variable name="target"
         select='UML:StructuralFeature.type//@xmi.idref'/>

    <xsl:variable name="visibility" select="@visibility"/>
    <xsl:variable name="name" select="@name"/>
    <xsl:variable name="owner" select='@ownerScope'/>

    <attribute visibility="{$visibility}" type="{$target}" owner="{$owner}" name="{$name}" id="{$xmi_id}" >
        <xsl:apply-templates select="UML:ModelElement.taggedValue/UML:TaggedValue" />
    </attribute>

</xsl:template>



<!-- Operations -->
<xsl:template name="operations">
      <xsl:apply-templates select="UML:Classifier.feature/UML:Operation" />
</xsl:template>


<xsl:template match="UML:Operation">
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <xsl:variable name="parameters"
         select="UML:BehavioralFeature.parameter/
         UML:Parameter[@kind!='return']"
    />

    <xsl:variable name="return"
         select="UML:BehavioralFeature.parameter/
         UML:Parameter[@kind='return']"
    />

    <xsl:variable name="returntype"
         select="$return/UML:Parameter.type/*/@xmi.idref"
    />

    <xsl:variable name="visibility">
        <xsl:value-of select="@visibility"/>
    </xsl:variable>

    <xsl:variable name="name">
       <xsl:value-of select="@name"/>
    </xsl:variable>

    <operation visibility="{$visibility}" type="{$returntype}" name="{$name}" id="{$xmi_id}">
        <xsl:variable name="parameter-count" select="count($parameters)" />

        <xsl:if test="not(normalize-space($parameter-count)='0')">
            <xsl:apply-templates select="$parameters" />
        </xsl:if >

        <xsl:apply-templates select="UML:ModelElement.taggedValue/UML:TaggedValue" />
    </operation>
</xsl:template>



<!-- Parameter -->
<xsl:template match="UML:Parameter">
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <xsl:variable name="target"
         select="UML:Parameter.type/*/@xmi.idref" />

    <xsl:variable name="name" select="@name" />

    <parameter type="{$target}" name="{$name}" id="{$xmi_id}" />
</xsl:template>


<!-- Stereotype -->
<xsl:template match="UML:Stereotype">
    <xsl:variable name="name" select="@name"/>
    <xsl:variable name="xmi_id" select="@xmi.id" />

    <!-- search all elements that reference this stereotype -->
    <xsl:variable name="references"
         select="//UML:ModelElement.stereotype/UML:Stereotype[@xmi.idref = $xmi_id]" />

    <!-- iterate and get ancestor's id each time -->
    <xsl:variable name="result">
        <xsl:for-each select="$references">
            <xsl:value-of select="../../@xmi.id" />
            <xsl:text> </xsl:text>
        </xsl:for-each>
    </xsl:variable>

    <xsl:variable name="elements">
        <xsl:value-of select="$result" />
    </xsl:variable>

    <stereotype name="{$name}" id="{$xmi_id}" >
        <!-- JAXB has problems with empty NMTOKENS lists, so make these attributes optional -->
        <xsl:if test="string($elements)">
            <xsl:attribute name="extended-elements">
                <xsl:value-of select="$elements" />
            </xsl:attribute>
        </xsl:if>
    </stereotype>
</xsl:template>


<!-- Tagged value -->
<xsl:template match="UML:ModelElement.taggedValue/UML:TaggedValue">
  <xsl:variable name="xmi_id" select="@xmi.id" />

  <xsl:variable name="tagdefinition"
       select="key('tagdefinition', ./UML:TaggedValue.type/UML:TagDefinition/@xmi.idref)" />

  <tagged-value id="{$xmi_id}">
    <tag><xsl:value-of select="$tagdefinition/@tagType"/></tag>
    <value><xsl:value-of select="@dataValue"/></value>
  </tagged-value>
</xsl:template>


</xsl:stylesheet>
