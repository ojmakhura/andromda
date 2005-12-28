package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
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

    // ---------------- constructor -------------------------------

    public EJB3EntityAttributeFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------
	
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
	public String getGetterName() 
	{
        return "get" + StringUtils.capitalize(super.getName());
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
            if (this.getFetchType().equalsIgnoreCase(EJB3Profile.FETCHTYPE_EAGER))
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
            if (this.getFetchType().equalsIgnoreCase(EJB3Profile.FETCHTYPE_LAZY)) {
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
            genType = String.valueOf(this.getConfiguredProperty(EJB3Globals.ENTITY_DEFAULT_GENERATOR_TYPE));
            if (StringUtils.isBlank(genType))
            {
                genType = EJB3Profile.GENERATORTYPE_AUTO;
            }
        }
        return genType;
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsSequenceGeneratorType()
     */
    protected boolean handleIsSequenceGeneratorType()
    {
        boolean isSequence = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) 
        {
            if (this.getGeneratorType().equalsIgnoreCase(EJB3Profile.GENERATORTYPE_SEQUENCE))
            {
                isSequence = true;
            }
        }
        return isSequence;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EntityAttributeFacadeLogic#handleIsTableGeneratorType()
     */
    protected boolean handleIsTableGeneratorType()
    {
        boolean isTable = false;
        if (StringUtils.isNotBlank(this.getGeneratorType())) 
        {
            if (this.getGeneratorType().equalsIgnoreCase(EJB3Profile.GENERATORTYPE_TABLE))
            {
                isTable = true;
            }
        }
        return isTable;
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
            initialValue = NumberUtils.stringToInt(initialValueStr);
        }
        else
        {
            initialValueStr = 
                String.valueOf(this.getConfiguredProperty(EJB3Globals.ENTITY_DEFAULT_GENERATOR_INITIAL_VALUE));
            if (StringUtils.isNotBlank(initialValueStr))
            {
                initialValue = NumberUtils.stringToInt(initialValueStr);
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
            allocationSize = NumberUtils.stringToInt(allocationSizeStr);
        }
        else
        {
            allocationSizeStr = 
                String.valueOf(this.getConfiguredProperty(EJB3Globals.ENTITY_DEFAULT_GENERATOR_ALLOCATION_SIZE));
            if (StringUtils.isNotBlank(allocationSizeStr))
            {
                allocationSize = NumberUtils.stringToInt(allocationSizeStr);
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
     */
    protected String handleGetColumnDefinition()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_COLUMN_DEFINITION);
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
            nullable = (this.isIdentifier() || this.isUnique() ? false : true);
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