package org.andromda.cartridges.ejb.metafacades;

import java.util.Collection;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.AttributeFacade;


/**
 *
 *
 * Metaclass facade implementation.
 *
 */
public class EJBPrimaryKeyFacadeLogicImpl
       extends EJBPrimaryKeyFacadeLogic
       implements org.andromda.cartridges.ejb.metafacades.EJBPrimaryKeyFacade
{
    // ---------------- constructor -------------------------------

    public EJBPrimaryKeyFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    public boolean handleIsComplex() {
        return getSimplePkField() == null;
    }

    /**
     * If this <code>object</code> does not have a complex primary key, get
     * the (unqiue) attribute that is used as the primary key.
     *
     * @return the attribute used as primary key, or <code>null</code> if
     *         there is none or the class has a complex primary key.
     */
    private AttributeFacade getSimplePkField() {
        AttributeFacade primaryKey = null;
        Collection primaryKeys =
            ((EJBEntityFacade) this.getOwner()).getIdentifiers();
        if (primaryKeys.size() == 1) {
            AttributeFacade pkField =
                (AttributeFacade) primaryKeys.iterator().next();
            if (pkField.hasStereotype(EJBProfile.STEREOTYPE_IDENTIFIER)) {
                primaryKey = pkField;
            }
        }
        return primaryKey;
    }

    // ------------- relations ------------------

}
