package org.andromda.cartridges.meta.metafacades;
import java.util.Iterator;import org.andromda.metafacades.uml.OperationFacade;
/** * MetafacadeLogic implementation for * org.andromda.cartridges.meta.metafacades.PSMmetaclass. *  * @see org.andromda.cartridges.meta.metafacades.PSMmetaclass * @author Bob Fields
 */public class PSMmetaclassLogicImpl extends PSMmetaclassLogic{    /**     * @param metaObjectIn     * @param context     */    public PSMmetaclassLogicImpl(Object metaObjectIn, String context)
    {
        super(metaObjectIn, context);
    }
    /**
     * @see org.andromda.cartridges.meta.metafacades.PSMmetaclass#isOperationsPresent()
     */
    @Override    protected boolean handleIsOperationsPresent()
    {
        return this.getOperations().size() > 0;
    }
    /** 
     * @see org.andromda.cartridges.meta.metafacades.PSMmetaclassLogic#handleIsImplMustBeAbstract()
     */
    @Override    protected boolean handleIsImplMustBeAbstract()
    {
        boolean result = false;
        // if the class itself is abstract, make the impl abstract, too.
        if (this.isAbstract())
        {
            result = true;        }        else        {            // if the class contains abstract operations, the impl must be            // abstract, too, because the abstract operations will not be            // generated as methods.            for (Iterator iter = this.getOperations().iterator(); iter.hasNext();)            {                OperationFacade operation = (OperationFacade) iter.next();                if (operation.isAbstract())                {                    result = true;                    break;                }            }        }        return result;    }}