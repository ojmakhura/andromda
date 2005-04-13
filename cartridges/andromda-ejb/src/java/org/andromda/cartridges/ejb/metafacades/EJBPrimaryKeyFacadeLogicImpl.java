package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.AttributeFacade;

import java.util.Collection;

/**
 * Metaclass facade implementation.
 */
public class EJBPrimaryKeyFacadeLogicImpl
        extends EJBPrimaryKeyFacadeLogic
{
    // ---------------- constructor -------------------------------

    public EJBPrimaryKeyFacadeLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsComplex()
    {
        return getSimplePkField() == null;
    }

    /**
     * If this <code>object</code> does not have a complex primary key, get the (unqiue) attribute that is used as the
     * primary key.
     *
     * @return the attribute used as primary key, or <code>null</code> if there is none or the class has a complex
     *         primary key.
     */
    private AttributeFacade getSimplePkField()
    {
        AttributeFacade primaryKey = null;
        Collection primaryKeys = ((EJBEntityFacade)this.getOwner()).getIdentifiers();
        if (primaryKeys.size() == 1)
        {
            AttributeFacade pkField = (AttributeFacade)primaryKeys.iterator().next();
            if (pkField.hasStereotype(EJBProfile.STEREOTYPE_IDENTIFIER))
            {
                primaryKey = pkField;
            }
        }
        return primaryKey;
    }

    // ------------- relations ------------------

}