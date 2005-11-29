package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.PackageFacade.
 *
 * @see org.andromda.metafacades.uml.PackageFacade
 */
public class PackageFacadeLogicImpl
    extends PackageFacadeLogic
{
    public PackageFacadeLogicImpl(
        org.eclipse.uml2.Package metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#findModelElement(java.lang.String)
     */
    protected org.andromda.metafacades.uml.ModelElementFacade handleFindModelElement(
        java.lang.String fullyQualifiedName)
    {
        Object modelElement = null;
        if (logger.isDebugEnabled())
        {
            logger.debug("Looking for >> " + fullyQualifiedName);
        }
        modelElement = UmlUtilities.findByName(
                metaObject.eResource().getResourceSet(),
                fullyQualifiedName);
        if (logger.isDebugEnabled())
        {
            logger.debug("Found: '" + modelElement + "'");
        }
        return (ModelElementFacade)this.shieldedElement(modelElement);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getClasses()
     */
    protected java.util.Collection handleGetClasses()
    {
        return new FilteredCollection(metaObject.getOwnedElements())
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof org.eclipse.uml2.Class;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getSubPackages()
     */
    protected java.util.Collection handleGetSubPackages()
    {
        return metaObject.getNestedPackages();
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getModelElements()
     */
    protected java.util.Collection handleGetModelElements()
    {
        return metaObject.getModel().getOwnedMembers();
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getOwnedElements()
     */
    protected java.util.Collection handleGetOwnedElements()
    {
        return metaObject.getOwnedMembers();
    }
}