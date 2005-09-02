package org.andromda.modules.xmilink.uml14;

import java.util.Enumeration;
import java.util.Properties;

import org.andromda.modules.xmilink.BaseExportStrategy;
import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.Logger;
import org.apache.commons.lang.StringEscapeUtils;

import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.Property;

/**
 * The <code>UMLEntityExportStrategy</code> class is the base class for exporting UML entities like <code>class</code>,
 * <code>interface</code>, <code>association</code> and so on.
 *
 * @author Peter Friese
 * @version 1.0
 * @since 22.09.2004
 */
public abstract class UMLEntityExportStrategy
        extends BaseExportStrategy
{

    /** The stereotype of the entity. */
    private String stereotype = null;

    /** The tagged values of this entity. */
    private Properties taggedValues = new Properties();

    /**
     * The super class reference. <b>NOTE</b>: this is the <i>LINK</i> to the superclass, not the superclass itself!
     */
    private Entity superClassReference = null;

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#reset()
     */
    protected void reset()
    {
        super.reset();
        stereotype = null;
        taggedValues = new Properties();
        superClassReference = null;
    }

    /**
     * Returns the name of the UML entity. Concrete subclasses are required to implement this method. The returned
     * string is used in the output, e.g. if a class returns <code>"Class20"</code>, then the XMI file will contain
     * something like:
     *
     * <pre>
     *
     *      &lt;UML:Class20 ...&gt;
     *        ...
     *      &lt;/UML:Class20&gt;
     *
     * </pre>
     *
     * @param entity The entity to export.
     * @return The name of the UML entity.
     */
    protected abstract String getEntityName(Entity entity);

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityPrologue(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityPrologue(Entity entity)
    {
        ExportContext.getWriter().writeOpeningElementStart(getEntityName(entity));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityPrologueEnd(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityPrologueEnd(Entity entity)
    {
        ExportContext.getWriter().writeOpeningElementEnd(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportChildNodes()
     */
    protected boolean doExportChildNodes()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportDependencies()
     */
    protected boolean doExportDependencies()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportEntity(com.togethersoft.openapi.model.elements.Entity)
     */
    protected boolean doExportEntity(Entity entity)
    {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityEpilogue(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityEpilogue(Entity entity)
    {
        ExportContext.getWriter().writeClosingElement(getEntityName(entity));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportProperty(com.togethersoft.openapi.model.elements.Property)
     */
    protected void exportProperty(Property property)
    {
        if (property != null)
        {
            Logger.info("Will we export property " + property.getName() + "(value: " + property.getValue() + ") ?");
            if (doExportProperty(property.getName(), property.getValue()))
            {
                String propName = translatePropertyName(property);
                String propValue = translatePropertyValue(property);
                Logger.info("Yes, we will export property " + propName + " with value " + propValue + "!");
                ExportContext.getWriter().writeProperty(propName, propValue);
            }
            else
            {
                if (isTaggedValue(property.getName(), property.getValue()))
                {
                    String propName = translatePropertyName(property);
                    String propValue = translatePropertyValue(property);
                    taggedValues.setProperty(propName, propValue);
                }
            }
        }
    }

    /**
     * Check whether a name / value pair is a tagged value or not.
     *
     * @param name The name of the property.
     * @param value The value of the property.
     * @return <code>true</code> if it is a tagged value, <code>false</code> if not.
     */
    private boolean isTaggedValue(String name,
        String value)
    {
        // $doc is the documentation for the entity
        if ("$doc".equalsIgnoreCase(name))
        {
            return true;
        }

        // if the property does NOT start with a dollar sign, it is a tagged
        // value:
        else
        {
            if ((name != null) && (name.length() > 0))
            {
                if (name.charAt(0) != '$')
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Translates some property names.
     *
     * @param property The property whose name is to be translated.
     * @return A translated property name.
     */
    protected String translatePropertyName(Property property)
    {
        String result = property.getName();
        if ((result != null) && (result.length() > 0))
        {
            if ("$doc".equalsIgnoreCase(result))
            {
                result = "documentation";
            }
            else if (result.equalsIgnoreCase("$public"))
            {
                result = "visibility";
            }
            else if (result.equalsIgnoreCase("$protected"))
            {
                result = "visibility";
            }
            else if (result.equalsIgnoreCase("$private"))
            {
                result = "visibility";
            }
            else if (result.equalsIgnoreCase("$static"))
            {
                result = "ownerScope";
            }
            else if (result.equalsIgnoreCase("isOrdered"))
            {
                result = "ordering";
            }
            else if (result.equalsIgnoreCase("mod__abstract"))
            {
                result = "isAbstract";
            }
            else if (result.charAt(0) == '$')
            {
                result = result.substring(1);
            }
        }
        return result;
    }

    /**
     * Translates property values.
     *
     * @param property The property whose value is to be translated.
     * @return A translated property value.
     */
    protected String translatePropertyValue(Property property)
    {
        String result = property.getValue();
        String name = property.getName();
        if ("$public".equalsIgnoreCase(name))
        {
            result = "public";
        }
        else if ("$protected".equalsIgnoreCase(name))
        {
            result = "protected";
        }
        else if ("$private".equalsIgnoreCase(name))
        {
            result = "private";
        }
        else if ("$static".equalsIgnoreCase(name))
        {
            result = "classifier";
        }
        else if ("isOrdered".equalsIgnoreCase(name))
        {
            result = "ordered";
        }
        else if ("isQuery".equalsIgnoreCase(name))
        {
            result = "true";
        }
        else if ("$doc".equalsIgnoreCase(name))
        {
            result = StringEscapeUtils.escapeHtml(result);
        }
        else if ("mod__abstract".equalsIgnoreCase(name))
        {
            result = "true";
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityBody(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityBody(Entity entity)
    {
        super.exportEntityBody(entity);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportProperty(java.lang.String, java.lang.String)
     */
    protected boolean doExportProperty(String name,
        String value)
    {
        if ("stereotype".equalsIgnoreCase(name))
        {
            this.stereotype = value;
            return false;
        }
        else if ("$name".equalsIgnoreCase(name))
        {
            return true;
        }
        else if ("$public".equalsIgnoreCase(name))
        {
            return true;
        }
        else if ("$protected".equalsIgnoreCase(name))
        {
            return true;
        }
        else if ("$private".equalsIgnoreCase(name))
        {
            return true;
        }
        else if ("$static".equalsIgnoreCase(name))
        {
            return true;
        }
        else if ("isOrdered".equalsIgnoreCase(name))
        {
            return true;
        }
        else if ("isQuery".equalsIgnoreCase(name))
        {
            return true;
        }
        else if ("mod__abstract".equalsIgnoreCase(name))
        {
            return true;
        }
        return false;
    }

    /**
     * Writes the stereotype of the UML entity.
     */
    protected void writeStereotype()
    {
        if (this.stereotype != null)
        {
            // <UML:ModelElement.stereotype>
            ExportContext.getWriter().writeOpeningElement("UML:ModelElement.stereotype");

            // <UML:Stereotype xmi.idref = 'sm$c5aa00:fe48388b81:-7fe9'/>
            ExportContext.getWriter().writeOpeningElementStart("UML:Stereotype");
            ExportContext.getWriter().writeProperty("name", this.stereotype);
            ExportContext.getWriter().writeOpeningElementEnd(true);

            // </UML:ModelElement.stereotype>
            ExportContext.getWriter().writeClosingElement("UML:ModelElement.stereotype");
        }
    }

    /**
     * Exports the tagged values of the model element.
     */
    protected void exportTaggedValues()
    {
        if ((taggedValues != null) && (taggedValues.size() > 0))
        {
            // <UML:ModelElement.taggedValue>
            ExportContext.getWriter().writeOpeningElement("UML:ModelElement.taggedValue");

            final Enumeration keys = taggedValues.keys();
            while (keys.hasMoreElements())
            {
                String key = (String)keys.nextElement();
                String value = taggedValues.getProperty(key);
                if (doExportTaggedValue(key, value))
                {
                    key = translateTaggedValueName(key, value);
                    value = translateTaggedValueValue(key, value);
                    writeTaggedValue(key, value);
                }
            }

            // </UML:ModelElement.taggedValue>
            ExportContext.getWriter().writeClosingElement("UML:ModelElement.taggedValue");
        }
    }

    /**
     * Subclasses may override this method to translate the value of a tagged value into something different.
     *
     * @param key The name of the tagged value.
     * @param value The value of the tagged value.
     * @return The translated value.
     */
    protected String translateTaggedValueValue(String key,
        String value)
    {
        return value;
    }

    /**
     * Subclasses may override this method to translate the name of a tagged value into something different.
     *
     * @param key The name of the tagged value.
     * @param value The value of the tagged value.
     * @return The translated name.
     */
    protected String translateTaggedValueName(String key,
        String value)
    {
        return key;
    }

    /**
     * Determines whether to export the given tagged value.
     *
     * @param key The name of the tagged value.
     * @param value The value of the tagged value.
     * @return <code>true</code> if the tagged value should be exported, <code>false</code> otherwise.
     */
    protected boolean doExportTaggedValue(String key,
        String value)
    {
        if (key.indexOf("mod_") > -1)
        {
            return false;
        }
        return true;
    }

    /**
     * Writes a single tagged value.
     *
     * @param key The name of the tagged value.
     * @param value The value of the tagged value.
     */
    private void writeTaggedValue(String key,
        String value)
    {

        // <UML:TaggedValue xmi.id = 'sm$19ecd80:1001cda5985:-7ef3'
        // isSpecification = 'false'>
        ExportContext.getWriter().writeOpeningElement("UML:TaggedValue");

        // <UML:TaggedValue.dataValue>&lt;p&gt;Attributdoku&lt;/p&gt;
        ExportContext.getWriter().writeOpeningElement("UML:TaggedValue.dataValue");
        ExportContext.getWriter().writeText(value);

        // </UML:TaggedValue.dataValue>
        ExportContext.getWriter().writeClosingElement("UML:TaggedValue.dataValue");

        // <UML:TaggedValue.type>
        ExportContext.getWriter().writeOpeningElement("UML:TaggedValue.type");

        // <UML:TagDefinition xmi.idref = 'sm$19ecd80:1001cda5985:-7efb'/>
        ExportContext.getWriter().writeOpeningElementStart("UML:TagDefinition");
        ExportContext.getWriter().writeProperty("name", key);
        ExportContext.getWriter().writeOpeningElementEnd(true);

        // </UML:TaggedValue.type>
        ExportContext.getWriter().writeClosingElement("UML:TaggedValue.type");

        // </UML:TaggedValue>
        ExportContext.getWriter().writeClosingElement("UML:TaggedValue");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportDependencyLink(com.togethersoft.openapi.model.elements.Entity)
     */
    protected boolean doExportDependencyLink(Entity link)
    {
        Logger.info("Link metaclass name: " + getMetaClass(link));
        if (getMetaClass(link).indexOf("Dependency") > -1)
        {
            return true;
        }
        else if (getMetaClass(link).indexOf("Implementation") > -1)
        {
            return true;
        }
        else if (getMetaClass(link).indexOf("Generalization") > -1)
        {
            return true;
        }
        else if (getMetaClass(link).indexOf("AssociatesLink") > -1)
        {
            return true;
        }
        return false;
    }

}
