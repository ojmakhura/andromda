package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.UmlClass;

/**
 * Metaclass facade implementation.
 */
public class PackageFacadeLogicImpl
    extends PackageFacadeLogic
    implements org.andromda.metafacades.uml.PackageFacade
{
    // ---------------- constructor -------------------------------

    public PackageFacadeLogicImpl(
        org.omg.uml.modelmanagement.UmlPackage metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageDecorator#handleGetClasses()
     */
    public java.util.Collection handleGetClasses()
    {
        return new FilteredCollection(metaObject.getOwnedElement())
        {
            protected boolean accept(Object object)
            {
                return object instanceof UmlClass;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.PackageDecorator#handleGetSubPackages()
     */
    protected Collection handleGetSubPackages()
    {
        return new FilteredCollection(metaObject.getOwnedElement())
        {
            protected boolean accept(Object object)
            {
                return object instanceof UmlPackage;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.PackageDecorator#handleGetModelElements()
     */
    protected Collection handleGetModelElements()
    {
        return ((UmlPackage)MetafacadeFactory.getInstance().getModel()
            .getModel()).getCore().getModelElement().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findModelElement(java.lang.String)
     */
    public ModelElementFacade handleFindModelElement(final String fullyQualifiedName)
    {
        return (ModelElementFacade)CollectionUtils.find(
            this.getModelElements(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((ModelElementFacade)object).getFullyQualifiedName()
                        .equals(StringUtils.trimToEmpty(fullyQualifiedName));
                }
            });
    }

    // ------------------------------------------------------------

}