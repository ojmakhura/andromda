package org.andromda.cartridges.ejb.metafacades;

/**
 * <p>
 * Represents an EJB association end. </p> Metaclass facade implementation.
 */
public class EJBAssociationEndFacadeLogicImpl
        extends EJBAssociationEndFacadeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJBAssociationEndFacadeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBAssociationEndFacadeLogic#handleGetRelationType()
     */
    public String handleGetRelationType()
    {
        String targetType;
        if (this.isMany2Many() || this.isOne2Many())
        {
            targetType = "java.util.Collection";
        }
        else
        {
            targetType = this.getOtherEnd().getType().getFullyQualifiedName();
        }
        return targetType;
    }

    // ------------- relations ------------------

}