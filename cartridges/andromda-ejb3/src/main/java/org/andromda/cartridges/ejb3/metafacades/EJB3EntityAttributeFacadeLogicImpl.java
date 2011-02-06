package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade.
 *
 * @see EJB3EntityAttributeFacade
 */
public class EJB3EntityAttributeFacadeLogicImpl
    extends EJB3EntityAttributeFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * The property that stores the default entity ID generator type
     */
    public static final String ENTITY_DEFAULT_GENERATOR_TYPE = "entityDefaultGeneratorType";

    /**
     * The property that stores the default generator initial value
     */
    public static final String ENTITY_DEFAULT_GENERATOR_INITIAL_VALUE = "entityDefaultGeneratorInitialValue";

    /**
     * The property that stores the default generator allocation size for incrementing ids
     */
    public static final String ENTITY_DEFAULT_GENERATOR_ALLOCATION_SIZE = "entityDefaultGeneratorAllocationSize";

    /**
     * The property that stores the default enumeration string literal column length.
     */
    public static final String DEFAULT_ENUM_LITERAL_COLUMN_LENGTH = "entityDefaultEnumLiteralColumnLength";

    /**
     * The property that stores the default temporal type for date based attributes
     */
    public static final String ENTITY_DEFAULT_TEMPORAL_TYPE = "entityDefaultTemporalType";

    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJB3EntityAttributeFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    @Override
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        if (this.getOwner() instanceof EJB3EntityFacade)
        {
            EJB3EntityFacade entity = (EJB3EntityFacade)this.getOwner();

            /**
             * Exclude ONLY if single table inheritance exists
             */
            if (entity.isRequiresGeneralizationMapping() && entity.isInheritanceSingleTable()
                    && !entity.isEmbeddableSuperclassGeneralizationExists())
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
            if (type.isStringType())
            {
                defaultValue = '\"' + defaultValue + '\"';
            }
            else if (fullyQualifiedName.startsWith("java.lang"))
            {
                defaultValue = fullyQualifiedName + ".valueOf(" + defaultValue + ')';
            }
        }
        return defaultValue;
    }

    /**
     * @see EJB3EntityAttributeFacade#getFetchType()
     */
    @Override
    protected String handleGetFetchType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FETCH_TYPE);
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsEager()
     */
    @Override
    protected boolean handleIsEager()
    {
        boolean isEager = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (EJB3Globals.FETCH_TYPE_EAGER.equalsIgnoreCase(this.getFetchType()))
            {
                isEager = true;
            }
        }
        return isEager;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsLazy()
     */
    @Override
    protected boolean handleIsLazy()
    {
        boolean isLazy = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (EJB3Globals.FETCH_TYPE_LAZY.equalsIgnoreCase(this.getFetchType())) 
            {
                isLazy = true;
            }
        }
        return isLazy;
    }

    /**
     * @see EJB3EntityAttributeFacade#isVersion()
     */
    @Override
    protected boolean handleIsVersion()
    {
        boolean isVersion = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_VERSION))
        {
            isVersion = true;
        }
        return isVersion;
    }

    /**
     * @see EJB3EntityAttributeFacade#isLob()
     */
    @Override
    protected boolean handleIsLob()
    {
        return this.getType().isBlobType() || this.getType().isClobType();
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetLobType()
     */
    @Override
    protected String handleGetLobType()
    {
        return StringUtils.trimToEmpty((String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_LOB_TYPE));
    }

    /**
     * @see EJB3EntityAttributeFacade#getGeneratorType()
     */
    @Override
    protected String handleGetGeneratorType()
    {
        String genType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_TYPE);
        if (StringUtils.isBlank(genType))
        {
            if (this.getType().isStringType() || this.getType().isDateType() || this.getType().isTimeType())
            {
                genType = EJB3Globals.GENERATOR_TYPE_NONE;
            }
            else
            {
                genType = String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_GENERATOR_TYPE));
                if (StringUtils.isBlank(genType))
                {
                    genType = EJB3Globals.GENERATOR_TYPE_AUTO;
                }
            }
        }
        return genType;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeSequence()
     */
    @Override
    protected boolean handleIsGeneratorTypeSequence()
    {
        boolean isSequence = false;
        if (StringUtils.isNotBlank(this.getGeneratorType()))
        {
            if (EJB3Globals.GENERATOR_TYPE_SEQUENCE.equalsIgnoreCase(this.getGeneratorType()))
            {
                isSequence = true;
            }
        }
        return isSequence;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeTable()
     */
    @Override
    protected boolean handleIsGeneratorTypeTable()
    {
        boolean isTable = false;
        if (StringUtils.isNotBlank(this.getGeneratorType()))
        {
            if (EJB3Globals.GENERATOR_TYPE_TABLE.equalsIgnoreCase(this.getGeneratorType()))
            {
                isTable = true;
            }
        }
        return isTable;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeAuto()
     */
    @Override
    protected boolean handleIsGeneratorTypeAuto()
    {
        boolean isAuto = false;
        if (StringUtils.isNotBlank(this.getGeneratorType()))
        {
            if (EJB3Globals.GENERATOR_TYPE_AUTO.equalsIgnoreCase(this.getGeneratorType()))
            {
                isAuto = true;
            }
        }
        return isAuto;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeGeneric()
     */
    @Override
    protected boolean handleIsGeneratorTypeGeneric()
    {
        boolean isGeneric = false;
        if (StringUtils.isNotBlank(this.getGeneratorType()))
        {
            if (EJB3Globals.GENERATOR_TYPE_GENERIC.equalsIgnoreCase(this.getGeneratorType()))
            {
                isGeneric = true;
            }
        }
        return isGeneric;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeNone()
     */
    @Override
    protected boolean handleIsGeneratorTypeNone()
    {
        boolean isNone = false;
        if (StringUtils.isNotBlank(this.getGeneratorType()))
        {
            if (EJB3Globals.GENERATOR_TYPE_NONE.equalsIgnoreCase(this.getGeneratorType()))
            {
                isNone = true;
            }
        }
        return isNone;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeIdentity()
     */
    @Override
    protected boolean handleIsGeneratorTypeIdentity()
    {
        boolean isIdentity = false;
        if (StringUtils.isNotBlank(this.getGeneratorType()))
        {
            if (EJB3Globals.GENERATOR_TYPE_IDENTITY.equalsIgnoreCase(this.getGeneratorType()))
            {
                isIdentity = true;
            }
        }
        return isIdentity;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetGeneratorName()
     */
    @Override
    protected String handleGetGeneratorName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME);
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#getGeneratorGenericStrategy()
     */
    @Override
    protected String handleGetGeneratorGenericStrategy()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_GENERIC_STRATEGY);
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetGeneratorSourceName()
     */
    @Override
    protected String handleGetGeneratorSourceName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME);
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetGeneratorPkColumnValue()
     */
    @Override
    protected String handleGetGeneratorPkColumnValue()
    {
        String pkColumnValue = (String)this.findTaggedValue(
                EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE);

        if (StringUtils.isBlank(pkColumnValue))
        {
            pkColumnValue = this.getOwner().getName() + '_' + this.getColumnName();
        }
        return pkColumnValue;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetGeneratorInitialValue()
     */
    @Override
    protected int handleGetGeneratorInitialValue()
    {
        int initialValue = 1;
        String initialValueStr =
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_INITIAL_VALUE);
        if (StringUtils.isNotBlank(initialValueStr))
        {
            initialValue = NumberUtils.toInt(initialValueStr);
        }
        else
        {
            initialValueStr =
                String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_GENERATOR_INITIAL_VALUE));
            if (StringUtils.isNotBlank(initialValueStr))
            {
                initialValue = NumberUtils.toInt(initialValueStr);
            }
        }

        return initialValue;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetGeneratorAllocationSize()
     */
    @Override
    protected int handleGetGeneratorAllocationSize()
    {
        int allocationSize = 1;
        String allocationSizeStr =
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_ALLOCATION_SIZE);
        if (StringUtils.isNotBlank(allocationSizeStr))
        {
            allocationSize = NumberUtils.toInt(allocationSizeStr);
        }
        else
        {
            allocationSizeStr =
                String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_GENERATOR_ALLOCATION_SIZE));
            if (StringUtils.isNotBlank(allocationSizeStr))
            {
                allocationSize = NumberUtils.toInt(allocationSizeStr);
            }
        }

        return allocationSize;
    }

    /**
     * Override the super method to first look at the tagged value if one exists.
     * If not, then return the default column length.
     *
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnLength()
     */
    @Override
    public String getColumnLength()
    {
        String columnLength = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
        if (StringUtils.isEmpty(columnLength))
        {
            columnLength = super.getColumnLength();
        }
        return columnLength;
    }

    /**
     * Override the super method to first look at the tagged value if one exists.
     * If not, then return the default column name.
     *
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnName()
     */
    @Override
    public String getColumnName()
    {
        String columnName = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN);
        if (StringUtils.isEmpty(columnName))
        {
            columnName = super.getColumnName();
        }
        return columnName;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetColumnDefinition()
     *
     * If the column definition has not manually been set and the attribute
     * type is an enumeration, work out the schema from the length and type
     * of the enumeration literals.  The definition is only set for if the
     * literal types are String.
     */
    @Override
    protected String handleGetColumnDefinition()
    {
        String definition = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION);
        if (StringUtils.isBlank(definition) && this.getType().isEnumeration())
        {
            boolean isOrdinal = false;
            int length = NumberUtils.toInt(
                    String.valueOf(this.getConfiguredProperty(DEFAULT_ENUM_LITERAL_COLUMN_LENGTH)));
            for (AttributeFacade attribute : this.getType().getAttributes())
            {
                if (!attribute.getType().isStringType())
                {
                    isOrdinal = true;
                    break;
                }
                if (attribute.getName().length() > length)
                {
                    length = attribute.getName().length();
                }
            }
            if (!isOrdinal)
            {
                definition = "VARCHAR(" + length + ')';
            }
        }
        return definition;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetColumnPrecision()
     */
    @Override
    protected String handleGetColumnPrecision()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION);
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetColumnScale()
     */
    @Override
    protected String handleGetColumnScale()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE);
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsColumnNullable()
     */
    @Override
    protected boolean handleIsColumnNullable()
    {
        boolean nullable = true;
        String nullableString = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_NULLABLE);

        if (StringUtils.isBlank(nullableString))
        {
            nullable = (this.isIdentifier() || this.isUnique() ? false : !this.isRequired());
        }
        else
        {
            nullable = Boolean.valueOf(nullableString).booleanValue();
        }
        return nullable;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetTemporalType()
     */
    @Override
    protected String handleGetTemporalType()
    {
        String temporalType = null;
        if (this.getType().isDateType())
        {
            temporalType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE);
            if (StringUtils.isBlank(temporalType))
            {
                temporalType = String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_TEMPORAL_TYPE));
            }
        }
        return temporalType;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleGetEnumerationType()
     */
    @Override
    protected String handleGetEnumerationType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_ENUMERATION_TYPE);
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsInsertEnabled()
     */
    @Override
    protected boolean handleIsInsertEnabled()
    {
        final String value = (String)super.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_INSERT);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsUpdateEnabled()
     */
    @Override
    protected boolean handleIsUpdateEnabled()
    {
        final String value = (String)super.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_UPDATE);
        return StringUtils.isNotBlank(value) ? Boolean.valueOf(value).booleanValue() : true;
    }

    /**
     * @see EJB3EntityAttributeFacadeLogic#handleIsContainsEmbeddedObject()
     */
    @Override
    protected boolean handleIsContainsEmbeddedObject()
    {
        boolean returnValue = false;
        if (this.getType() instanceof EJB3EmbeddedValueFacade)
        {
            returnValue = true;
        }
        return returnValue;
    }
}
