package org.andromda.cartridges.meta.metafacades;

import java.util.Collection;
import java.util.HashSet;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeOperation.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeOperation
 * @author Bob Fields
 */
public class MetafacadeOperationLogicImpl
    extends MetafacadeOperationLogic
{
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------
    /**
     * @param metaObjectIn
     * @param context
     */
    public MetafacadeOperationLogicImpl(
        Object metaObjectIn,
        String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeOperation#getImplementationName()
     */
    @Override
    protected String handleGetImplementationName()
    {
        return StringUtils.trimToEmpty(
            String.valueOf(
                this.getConfiguredProperty(
                    MetaGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll(
            "\\{0\\}",
            StringUtils.capitalize(this.getName()));
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
}
