package org.andromda.cartridges.meta.metafacades;

import org.andromda.core.mapping.Mappings;
import org.andromda.core.metafacade.MetafacadeException;


/**
 * <p>
 *  Facade for use in the andromda-meta cartridge. It hides an
 *  association between two classifiers that each represent a
 * </p>
 * <p>
 *  > object.
 * </p>
 *
 * Metaclass facade implementation.
 *
 */
public class MetafacadeAssociationEndFacadeLogicImpl
       extends MetafacadeAssociationEndFacadeLogic
       implements org.andromda.cartridges.meta.metafacades.MetafacadeAssociationEndFacade
{
    // ---------------- constructor -------------------------------
    
    public MetafacadeAssociationEndFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class MetafacadeAssociationEndFacade ...

    public java.lang.String getGetterSetterTypeName()
    {
        final String methodName = 
            "MetafacadeAssociationEndFacadeLogicImpl.getGetterSetterTypeName";
        // if many, then list or collection
        if (isOne2Many() || isMany2Many())
        {
            Mappings lm = getLanguageMappings();
            if (lm == null) {
            	throw new MetafacadeException(methodName
                    + " - languageMappings could not be retrieved!");
            }
            return isOrdered()
                ? lm.getTo("datatype.List")
                : lm.getTo("datatype.Collection");
        }

        // If single element, then return the type.
        // However, return the interface type, not the
        // implementation class type!
        MetafacadeFacade otherMetafacade =
            (MetafacadeFacade) getOtherEnd().getType();

        return otherMetafacade.getFullyQualifiedInterfaceName();
    }

    // ------------- relations ------------------

}
