package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

/**
 * Metaclass facade implementation.
 */
public class DependencyFacadeLogicImpl
    extends DependencyFacadeLogic
    implements org.andromda.metafacades.uml.DependencyFacade
{
    // ---------------- constructor -------------------------------

    public DependencyFacadeLogicImpl(
        org.omg.uml.foundation.core.Dependency metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Gets the name in the following manner.
     * <ol>
     * <li>If the dependency has a name return it.</li>
     * <li>If the dependency does <strong>NOT </strong> have a name, get the
     * target element's and return its name uncapitalized.</li>
     * </ol>
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        String name = super.getName();
        if (StringUtils.isBlank(name) && this.getTargetElement() != null)
        {
            name = StringUtils.uncapitalize(this.getTargetElement().getName());
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getGetterName()
     */
    public java.lang.String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSetterName()
     */
    public java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getTargetElement()
     */
    public Object handleGetTargetElement()
    {
        Object targetElement = null;
        Collection suppliers = UMLMetafacadeUtils.getCorePackage()
            .getASupplierSupplierDependency().getSupplier(this.metaObject);
        if (!suppliers.isEmpty())
        {
            targetElement = suppliers.iterator().next();
        }
        return targetElement;
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSourceElement()
     */
    protected Object handleGetSourceElement()
    {
        Object sourceElement = null;
        Collection clients = UMLMetafacadeUtils.getCorePackage()
            .getAClientClientDependency().getClient(this.metaObject);
        if (!clients.isEmpty())
        {
            sourceElement = clients.iterator().next();
        }
        return sourceElement;
    }

}