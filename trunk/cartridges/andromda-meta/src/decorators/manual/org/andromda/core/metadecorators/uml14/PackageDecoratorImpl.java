package org.andromda.core.metadecorators.uml14;

import org.omg.uml.foundation.core.UmlClass;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.modelmanagement.UmlPackage
 *
 *
 */
public class PackageDecoratorImpl extends PackageDecorator
{
    // ---------------- constructor -------------------------------

    public PackageDecoratorImpl(
        org.omg.uml.modelmanagement.UmlPackage metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class PackageDecorator ...

    // ------------- relations ------------------

    /**
     *
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

    // ------------------------------------------------------------

}
