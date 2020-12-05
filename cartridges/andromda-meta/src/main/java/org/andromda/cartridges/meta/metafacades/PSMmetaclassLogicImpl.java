package org.andromda.cartridges.meta.metafacades;
import java.util.Collection;
import java.util.HashSet;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.meta.metafacades.PSMmetaclass.
 *
 * @see org.andromda.cartridges.meta.metafacades.PSMmetaclass
 * @author Bob Fields
 */
public class PSMmetaclassLogicImpl extends PSMmetaclassLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObjectIn
     * @param context
     */
    public PSMmetaclassLogicImpl(Object metaObjectIn, String context)
    {
        super(metaObjectIn, context);
    }
    /**
     * @see org.andromda.cartridges.meta.metafacades.PSMmetaclass#isOperationsPresent()
     */
    @Override
    protected boolean handleIsOperationsPresent()
    {
        return !this.getOperations().isEmpty();
    }
    /**
     * @see org.andromda.cartridges.meta.metafacades.PSMmetaclassLogic#handleIsImplMustBeAbstract()
     */
    @Override
    protected boolean handleIsImplMustBeAbstract()
    {
        boolean result = false;
        // if the class itself is abstract, make the impl abstract, too.
        if (this.isAbstract())
        {
            result = true;
        }
        else
        {
            // if the class contains abstract operations, the impl must be
            // abstract, too, because the abstract operations will not be
            // generated as methods.
            for (OperationFacade operation : this.getOperations())
            {
                if (operation.isAbstract())
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
	@Override
    public Collection<String> getAdditionalAnnotations() {
        HashSet<String> annotations = new HashSet<String>();
        for (Object o : this.findTaggedValues(UMLProfile.TAGGEDVALUE_ADDITIONAL_ANNOTATION))
        {
            annotations.add(o.toString());
        }
        return annotations;
    }
}