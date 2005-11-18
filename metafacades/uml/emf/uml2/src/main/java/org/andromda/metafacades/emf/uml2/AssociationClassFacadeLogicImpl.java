package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AssociationClassFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationClassFacade
 */
public class AssociationClassFacadeLogicImpl
    extends AssociationClassFacadeLogic
{
    public AssociationClassFacadeLogicImpl(
        org.eclipse.uml2.AssociationClass metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationClassFacade#getConnectionAssociationEnds()
     */
    protected java.util.Collection handleGetConnectionAssociationEnds()
    {
        return metaObject.getMemberEnds();
    }
}