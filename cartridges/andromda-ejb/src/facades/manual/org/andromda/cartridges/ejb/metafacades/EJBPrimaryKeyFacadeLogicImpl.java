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

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class PrimaryKeyFacade ...

    /**
     * Check if the primary key for <code>class</code> has been specified
     * with a dependency to an external class.
     * 
     * @param clazz
     *            the class to check
     * @return <code>true</code> if the primary key has ben specified by a
     *         dependency, <code>false</code> if it has been specified by an
     *         attribute marked as &lt;&lt;PrimaryKey&gt;&/gt;
     */
    public boolean isComplex() {
        return getSimplePkField() == null;
    }

    /**
     * If this <code>object</code> does not have a complex primary key, get
     * the (unqiue) attribute that is used as the primary key.
     * 
     * @param object
     *            the class to check
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
