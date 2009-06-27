package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.ObjectUtils;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.UmlClass;


/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class PackageFacadeLogicImpl
    extends PackageFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public PackageFacadeLogicImpl(
        org.omg.uml.modelmanagement.UmlPackage metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getClasses()
     */
    @Override
    public Collection<UmlClass> handleGetClasses()
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
     * @see org.andromda.metafacades.uml.PackageFacade#getSubPackages()
     */
    @Override
    protected Collection<UmlPackage> handleGetSubPackages()
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
     * @see org.andromda.metafacades.uml.PackageFacade#getModelElements()
     */
    @Override
    protected Collection<ModelElement> handleGetModelElements()
    {
        return ((UmlPackage)MetafacadeFactory.getInstance().getModel().getModel()).getCore().getModelElement()
                .refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#findModelElement(String)
     */
    @Override
    public ModelElementFacade handleFindModelElement(final String fullyQualifiedName)
    {
        return (ModelElementFacade)this.shieldedElement(
            UML14MetafacadeUtils.findByFullyQualifiedName(
                fullyQualifiedName,
                ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
                true));
    }

    /**
     * @see org.andromda.metafacades.uml.PackageFacade#getOwnedElements()
     */
    @Override
    protected Collection handleGetOwnedElements()
    {
        return metaObject.getOwnedElement();
    }
}