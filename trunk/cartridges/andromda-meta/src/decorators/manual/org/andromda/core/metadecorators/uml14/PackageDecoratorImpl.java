package org.andromda.core.metadecorators.uml14;

import java.util.Collection;

import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.UmlPackage;

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

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.PackageDecorator#handleGetPackages()
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

    // ------------------------------------------------------------

}
