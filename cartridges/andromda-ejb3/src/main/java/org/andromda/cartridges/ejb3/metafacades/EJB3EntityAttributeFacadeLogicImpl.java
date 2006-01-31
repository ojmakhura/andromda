package org.andromda.cartridges.ejb3.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade
 */
public class EJB3EntityAttributeFacadeLogicImpl
    extends EJB3EntityAttributeFacadeLogic
{
    
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
    
    // ---------------- constructor -------------------------------

    public EJB3EntityAttributeFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
	public String getGetterName() 
	{
        return "get" + StringUtils.capitalize(super.getName());
	}

    /**
     * Overridden to provide handling of inheritance.
     *
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        if (this.getOwner() instanceof EJB3EntityFacade)
        {
            EJB3EntityFacade entity = (EJB3EntityFacade)this.getOwner();
            if (entity.isInheritanceSingleTable() && entity.getGeneralization() != null)
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
    public String getDefaultValue()
    {
        String defaultValue = super.getDefaultValue();
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String fullyQualifiedName = StringUtils.trimToEmpty(type.getFullyQualifiedName());
            if (type.isStringType())
            {
                defaultValue = "\"" + defaultValue + "\"";
            }
            else if (fullyQualifiedName.startsWith("java.lang"))
            {
                defaultValue = fullyQualifiedName + ".valueOf(" + defaultValue + ")";
            }
        }
        return defaultValue;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade#getFetchType()
     */
	protected String handleGetFetchType() 
	{
		return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FETCH_TYPE);
	}

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsEager()
     */
    protected boolean handleIsEager()
    {
        boolean isEager = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (this.getFetchType().equalsIgnoreCase(EJB3Globals.FETCH_TYPE_EAGER))
            {
                isEager = true;
            }
        }
        return isEager;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsLazy()
     */
    protected boolean handleIsLazy()
    {
        boolean isLazy = false;
        if (StringUtils.isNotBlank(this.getFetchType()))
        {
            if (this.getFetchType().equalsIgnoreCase(EJB3Globals.FETCH_TYPE_LAZY)) {
                isLazy = true;
            }
        }
        return isLazy;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade#isVersion()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade#getLobType()
     */
	protected String handleGetLobType() 
	{
		return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_LOB_TYPE);
	}

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade#getGeneratorType()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsSequenceGeneratorType()
     */
    protected boolean handleIsGeneratorTypeSequence()
    {
        boolean isSequence = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) 
        {
            if (this.getGeneratorType().equalsIgnoreCase(EJB3Globals.GENERATOR_TYPE_SEQUENCE))
            {
                isSequence = true;
            }
        }
        return isSequence;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsTableGeneratorType()
     */
    protected boolean handleIsGeneratorTypeTable()
    {
        boolean isTable = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) 
        {
            if (this.getGeneratorType().equalsIgnoreCase(EJB3Globals.GENERATOR_TYPE_TABLE))
            {
                isTable = true;
            }
        }
        return isTable;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeAuto()
     */
    protected boolean handleIsGeneratorTypeAuto()
    {
        boolean isAuto = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) 
        {
            if (this.getGeneratorType().equalsIgnoreCase(EJB3Globals.GENERATOR_TYPE_AUTO))
            {
                isAuto = true;
            }
        }
        return isAuto;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeNone()
     */
    protected boolean handleIsGeneratorTypeNone()
    {
        boolean isNone = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) 
        {
            if (this.getGeneratorType().equalsIgnoreCase(EJB3Globals.GENERATOR_TYPE_NONE))
            {
                isNone = true;
            }
        }
        return isNone;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsGeneratorTypeIdentity()
     */
    protected boolean handleIsGeneratorTypeIdentity()
    {
        boolean isIdentity = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) 
        {
            if (this.getGeneratorType().equalsIgnoreCase(EJB3Globals.GENERATOR_TYPE_IDENTITY))
            {
                isIdentity = true;
            }
        }
        return isIdentity;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetGeneratorName()
     */
    protected String handleGetGeneratorName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_NAME);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetGeneratorSourceName()
     */
    protected String handleGetGeneratorSourceName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_SOURCE_NAME);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetGeneratorPkColumnValue()
     */
    protected String handleGetGeneratorPkColumnValue()
    {
        String pkColumnValue = (String)this.findTaggedValue(
                EJB3Profile.TAGGEDVALUE_PERSISTENCE_GENERATOR_PKCOLUMN_VALUE);
        
        if (StringUtils.isBlank(pkColumnValue))
        {
            pkColumnValue = this.getOwner().getName() + "_" + this.getColumnName();
        }
        return pkColumnValue;
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetGeneratorInitialValue()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetGeneratorAllocationSize()
     */
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
    public String getColumnLength()
    {
        String columnLength = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
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
    public String getColumnName()
    {
        String columnName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN);
        if (StringUtils.isEmpty(columnName))
        {
            columnName = super.getColumnName();
        }
        return columnName;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetColumnDefinition()
     * 
     * If the column definition has not manually been set and the attribute
     * type is an enumeration, work out the schema from the length and type
     * of the enumeration literals.  The definition is only set for if the
     * literal types are String.
     */
    protected String handleGetColumnDefinition()
    {
        String definition = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION);
        if (StringUtils.isBlank(definition) && this.getType().isEnumeration())
        {
            boolean isOrdinal = false;
            int length = NumberUtils.toInt(
                    String.valueOf(this.getConfiguredProperty(DEFAULT_ENUM_LITERAL_COLUMN_LENGTH)));
            for (final Iterator iter = this.getType().getAttributes().iterator(); iter.hasNext(); )
            {
                AttributeFacade attribute = (AttributeFacade)iter.next();
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
                definition = "VARCHAR(" + length + ") NOT NULL";
            }
        }
        return definition;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetColumnPrecision()
     */
    protected String handleGetColumnPrecision()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_PRECISION);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleGetColumnScale()
     */
    protected String handleGetColumnScale()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_SCALE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsColumnNullable()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsTransient()
     */
    protected boolean handleIsTransient()
    {
        boolean isTransient = false;
        
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_TRANSIENT))
        {
            isTransient = true;
        }
        return isTransient;
    }
}