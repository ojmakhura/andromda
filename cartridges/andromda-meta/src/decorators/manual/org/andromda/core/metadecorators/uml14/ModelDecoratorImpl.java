package org.andromda.core.metadecorators.uml14;

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

    /**
     *
     */
    public java.util.Collection handleGetPackages()
    {
        return metaObject
            .getModelManagement()
            .getUmlPackage()
            .refAllOfType();
    }

    // ------------------------------------------------------------

}
