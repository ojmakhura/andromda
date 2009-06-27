package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Dependency;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class DependencyFacadeLogicImpl
        extends DependencyFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public DependencyFacadeLogicImpl(Dependency metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Gets the name in the following manner. <ol> <li>If the dependency has a name return it.</li> <li>If the
     * dependency does <strong>NOT </strong> have a name, get the target element's and return its name
     * uncapitalized.</li> </ol>
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    public String handleGetName()
    {
        String name = super.handleGetName();
        if (StringUtils.isBlank(name) && this.getTargetElement() != null)
        {
            name = StringUtils.uncapitalize(this.getTargetElement().getName());
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getGetterName()
     */
    @Override
    public String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSetterName()
     */
    @Override
    public String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getTargetElement()
     */
    @Override
    public Object handleGetTargetElement()
    {
        Object targetElement = null;
        Collection suppliers = UML14MetafacadeUtils.getCorePackage().getASupplierSupplierDependency().getSupplier(
                this.metaObject);
        if (!suppliers.isEmpty())
        {
            targetElement = suppliers.iterator().next();
        }
        return targetElement;
    }

    /**
     * @see org.andromda.metafacades.uml.DependencyFacade#getSourceElement()
     */
    @Override
    protected Object handleGetSourceElement()
    {
        Object sourceElement = null;
        Collection clients = UML14MetafacadeUtils.getCorePackage().getAClientClientDependency().getClient(
                this.metaObject);
        if (!clients.isEmpty())
        {
            sourceElement = clients.iterator().next();
        }
        return sourceElement;
    }

}