package org.andromda.metafacades.emf.uml2;

import java.util.Collection;

import org.andromda.core.metafacade.MetafacadeImplsException;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.util.UML2Util;


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
        if (logger.isDebugEnabled())
        {
            logger.debug("Looking for " + fullyQualifiedName);
        }
        Collection elements = UML2Util.findNamedElements(
                metaObject.eResource().getResourceSet(),
                fullyQualifiedName,
                true);
        Element element;
        if (elements.iterator().hasNext())
        {
            element = (Element)elements.iterator().next();
            if (logger.isDebugEnabled())
            {
                logger.debug("Found " + element);
            }
        }
        else
        {
            throw new MetafacadeImplsException("Did not find" + fullyQualifiedName);
        }
        return (ModelElementFacade)this.shieldedElement(element);
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