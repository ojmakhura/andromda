package org.andromda.cartridges.meta.metafacades;
import java.util.Collection;
import java.util.HashSet;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    
    @Override
    public boolean isGeneric() {
        return CollectionUtils.isNotEmpty(this.findTaggedValues(UMLProfile.TAGGEDVALUE_GENERIC_PARAMETERS));
    }

    @Override
    public Collection<String> getGenericParameters() {
        
        HashSet<String> params = new HashSet<String>();
        for (Object o : this.findTaggedValues(UMLProfile.TAGGEDVALUE_GENERIC_PARAMETERS))
        {
            params.add(o.toString());
        }
        return params;
    }

    @Override
    public Collection<String> getGenericTypes() {
        
        HashSet<String> types = new HashSet<String>();
        for (Object o : this.findTaggedValues(UMLProfile.TAGGEDVALUE_GENERIC_TYPES))
        {
            types.add(o.toString());
        }
        return types;
    }

    @Override
    public boolean isGenericDeclaration() {
        return CollectionUtils.isNotEmpty(this.findTaggedValues(UMLProfile.TAGGEDVALUE_GENERIC_TYPES));
    }
    
    @Override
    public String getGenericParameterString() {
        String val = String.join(", ", this.getGenericParameters());

        if (StringUtils.isNotBlank(val))
        {
            val = "<" + val + ">";
        } else {
            val = "";
        }

        return val;
    }
    @Override
    public String getGenericTypeString() {
        
        String val = String.join(", ", this.getGenericTypes());

        if (StringUtils.isNotBlank(val))
        {
            val = "<" + val + ">";
        } else {
            val = "";
        }

        return val;
    }
}