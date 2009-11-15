package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.cartridges.hibernate.HibernateUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.JavaTypeConverter;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute
 * @author Bob Fields
 */
public class HibernateEntityAttributeLogicImpl
    extends HibernateEntityAttributeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    // ---------------- constructor -------------------------------
    public HibernateEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    @Override
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        if (this.getOwner() instanceof HibernateEntity)
        {
            HibernateEntity entity = (HibernateEntity)this.getOwner();
            if (entity.isHibernateInheritanceClass() && entity.getGeneralization() != null)
            {
                required = false;
            }
        }
        return required;
    }

    /**
     * Override to provide java specific handling of the default value.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    @Override
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String fullyQualifiedName = StringUtils.trimToEmpty(type.getFullyQualifiedName());
            if (("java.lang.String".equals(fullyQualifiedName) || "String".equals(fullyQualifiedName)) && fullyQualifiedName.indexOf('"')<0)
            {
                defaultValue = "\"" + defaultValue.replaceAll("\"", "") + "\"";
            }
            else if (fullyQualifiedName.startsWith("java.lang"))
            {
                defaultValue = fullyQualifiedName + ".valueOf(" + defaultValue + ")";
            }
            else if (type.isEnumeration())
            {
                final String mask = (String)this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK);
                defaultValue = type.getFullyQualifiedName() + '.' + NameMasker.mask(defaultValue, mask);
            }
        }
        return defaultValue;
    }

    /**
     * @return returnValue = true if this.getType() instanceof HibernateEmbeddedValue
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isContainsEmbeddedObject()
     */
    @Override
    protected boolean handleIsContainsEmbeddedObject()
    {
        boolean returnValue = false;
        if (this.getType() instanceof HibernateEmbeddedValue)
        {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * @param prefix 
     * @param name 
     * @return returnValue prefix + this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) + name
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#concatColumnName(String, String)
     */
    @Override
    protected String handleConcatColumnName(
        String prefix,
        String name)
    {
        String returnValue = name;
        if (StringUtils.isNotBlank(prefix))
        {
            returnValue = prefix + this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR) + name;

            // handle maxSqlNameLength
            Short maxSqlNameLength =
                Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
            returnValue = EntityMetafacadeUtils.ensureMaximumNameLength(returnValue, maxSqlNameLength);
        }
        return returnValue;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isLazy()
     */
    @Override
    protected boolean handleIsLazy()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : false;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#getFormula()
     */
    @Override
    protected String handleGetFormula()
    {
        return (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_FORMULA);
    }

    /**
     * @return HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isInsertEnabled()
     */
    @Override
    protected boolean handleIsInsertEnabled()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_INSERT);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    /**
     * @return HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttribute#isUpdateEnabled()
     */
    @Override
    protected boolean handleIsUpdateEnabled()
    {
        final String value = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_PROPERTY_UPDATE);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    private boolean isXmlPersistenceActive()
    {
       return HibernateUtils.isXmlPersistenceActive((String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION),
                                                    (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE));
    }

    
    private boolean persistIDAsAttribute() 
    {
        boolean persistAsAttribute = true;
        String prop = (String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_XML_PERSISTENCE_ID_AS_ATTRIBUTE);
        if (prop != null && prop.equalsIgnoreCase("false"))
            persistAsAttribute = false;
        
        return persistAsAttribute;
    }
    
    
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttributeLogic#handleGetXmlTagName()
     */
    @Override
    protected String handleGetXmlTagName() 
    {
        String tagName = null;
        
        if (isXmlPersistenceActive())
        {
            tagName = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_XML_TAG_NAME);

            if (StringUtils.isBlank(tagName))
            {
                if (this.isIdentifier() && this.persistIDAsAttribute())
                    tagName = "@" + this.getName();
                else
                    tagName = this.getName();
            }

        }
        return (StringUtils.isBlank(tagName)) ? null : tagName;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAttributeLogic#handleGetFullyQualifiedHibernateType()
     */
    @Override
    protected String handleGetFullyQualifiedHibernateType() 
    {
        String fullyQualifiedName;

        if (this.getType().isEnumeration())
        {
            fullyQualifiedName = "org.andromda.persistence.hibernate.usertypes.HibernateEnumType";
        }
        else
        {
            final String hibernateTypeMappingsUri = (String)this.getConfiguredProperty("hibernateTypeMappingsUri");
            final TypeMappings mappings = TypeMappings.getInstance(hibernateTypeMappingsUri);

            if (mappings == null)
            {
                fullyQualifiedName = this.getType().getFullyQualifiedName();
            }
            else
            {
                final String fullyQualifiedModelName = this.getType().getFullyQualifiedName(true);
                if (mappings.getMappings().containsFrom(fullyQualifiedModelName))
                {
                    fullyQualifiedName = mappings.getTo(fullyQualifiedModelName);
                }
                else
                {
                    fullyQualifiedName = this.getType().getFullyQualifiedName();
                }
            }
            fullyQualifiedName = new JavaTypeConverter().getJavaLangTypeName(fullyQualifiedName);
        }

        return fullyQualifiedName; 
	}
}