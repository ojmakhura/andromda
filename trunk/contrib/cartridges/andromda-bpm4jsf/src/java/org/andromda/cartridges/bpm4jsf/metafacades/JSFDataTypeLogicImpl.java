package org.andromda.cartridges.bpm4jsf.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFDataType.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFDataType
 */
public class JSFDataTypeLogicImpl
    extends JSFDataTypeLogic
{

    public JSFDataTypeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * Overridden because we use strings for dates and times.
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        String fullyQualifiedName = null;
        if (this.isDateType())
        {
            fullyQualifiedName = "java.lang.String";
        }
        else
        {
            fullyQualifiedName = super.getFullyQualifiedName();
        }
        return fullyQualifiedName;
    }
}