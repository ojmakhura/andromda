package org.andromda.core.metadecorators.uml14;

import java.util.Iterator;

import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class ClassifierDecoratorImpl extends ClassifierDecorator
{
    // ---------------- constructor -------------------------------

    public ClassifierDecoratorImpl(
        org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ClassifierDecorator ...

    // ------------- relations ------------------

    /**
     *
     */
    public java.util.Collection handleGetOperations()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            protected boolean accept(Object object)
            {
                return object instanceof Operation;
            }
        };
    }

    // ------------------------------------------------------------

    /**
     *
     */
    public java.util.Collection handleGetAttributes()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            protected boolean accept(Object object)
            {
                return (object instanceof Attribute);
            }
        };
    }

    // ------------------------------------------------------------

    /**
     *
     */
    public java.util.Collection handleGetDependencies()
    {
        return new FilteredCollection(metaObject.getClientDependency())
        {
            protected boolean accept(Object object)
            {
                return (object instanceof Dependency)
                    && !(object instanceof Abstraction);
            }
        };
    }

    // ------------------------------------------------------------

    /**
     *
     */
    public java.util.Collection handleGetAssociationEnds()
    {
        return DecoratorFactory
            .getInstance()
            .getModel()
            .getCore()
            .getAParticipantAssociation()
            .getAssociation(metaObject);
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#handleGetSuperclass()
     */
    protected ModelElement handleGetSuperclass()
    {
        Iterator i = getGeneralization().iterator();

        if (i.hasNext())
        {
            Generalization generalization = (Generalization) i.next();
            return generalization.getParent();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        String fullName = getName();

        if (isPrimitiveType())
        {
            return fullName;
        }

        String packageName = getPackageName();
        fullName =
            "".equals(packageName)
                ? fullName
                : packageName + "." + fullName;

        return fullName;
    }


    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#isPrimitiveType()
     */
    public boolean isPrimitiveType()
    {
        String name = getName();
        return (
            "void".equals(name)
                || "char".equals(name)
                || "byte".equals(name)
                || "short".equals(name)
                || "int".equals(name)
                || "long".equals(name)
                || "float".equals(name)
                || "double".equals(name)
                || "boolean".equals(name));
    }

    // ------------------------------------------------------------

}
