package org.andromda.core.metadecorators.uml14;

import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Dependency;
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

    // ------------------------------------------------------------

}
