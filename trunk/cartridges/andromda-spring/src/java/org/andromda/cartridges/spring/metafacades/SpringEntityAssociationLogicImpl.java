package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringEntityAssociation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociation
 */
public class SpringEntityAssociationLogicImpl
       extends SpringEntityAssociationLogic
{
    // ---------------- constructor -------------------------------

    public SpringEntityAssociationLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociation#getHibernateCacheType()
     */
    protected java.lang.String handleGetHibernateCacheType() 
    {
        return (String)findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_CACHE);
    }
    
}
