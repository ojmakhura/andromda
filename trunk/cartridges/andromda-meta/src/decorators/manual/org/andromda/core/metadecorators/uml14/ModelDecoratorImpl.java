package org.andromda.core.metadecorators.uml14;

import java.util.Collection;

import org.omg.uml.foundation.core.ModelElement;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.UmlPackage
 *
 *
 */
public class ModelDecoratorImpl extends ModelDecorator
{
    // ---------------- constructor -------------------------------

    public ModelDecoratorImpl(org.omg.uml.UmlPackage metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ModelDecorator ...

    // ------------- relations ------------------

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelDecorator#handleGetRootPackage()
     */
    protected ModelElement handleGetRootPackage()
    {
        Collection rootPackages =
            metaObject.getModelManagement().getModel().refAllOfType();
        return (ModelElement) rootPackages.iterator().next();
    }

    // ------------------------------------------------------------

}
