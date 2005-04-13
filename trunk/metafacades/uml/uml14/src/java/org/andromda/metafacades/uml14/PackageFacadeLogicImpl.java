package org.andromda.metafacades.uml14;

import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.MetafacadeProperties;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.UmlClass;

import java.util.Collection;

/**
 * Metaclass facade implementation.
 */
public class PackageFacadeLogicImpl
        extends PackageFacadeLogic
{
    // ---------------- constructor -------------------------------

    public PackageFacadeLogicImpl(org.omg.uml.modelmanagement.UmlPackage metaObject, String context)
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
            public boolean evaluate(Object object)
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
            public boolean evaluate(Object object)
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
        return ((UmlPackage)MetafacadeFactory.getInstance().getModel().getModel()).getCore().getModelElement()
                .refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findModelElement(java.lang.String)
     */
    public ModelElementFacade handleFindModelElement(final String fullyQualifiedName)
    {
        return (ModelElementFacade)this.shieldedElement(UML14MetafacadeUtils.findByFullyQualifiedName(
                fullyQualifiedName, String.valueOf(this.getConfiguredProperty(
                        MetafacadeProperties.METAFACADE_NAMESPACE_SCOPE_OPERATOR))));
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacadeLogic#getOwnedElements()
     */
    protected Collection handleGetOwnedElements()
    {
        return metaObject.getOwnedElement();
    }
}