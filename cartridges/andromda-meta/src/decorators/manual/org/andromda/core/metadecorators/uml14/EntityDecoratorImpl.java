package org.andromda.core.metadecorators.uml14;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class EntityDecoratorImpl extends EntityDecorator
{
    // ---------------- constructor -------------------------------
    
    public EntityDecoratorImpl (org.omg.uml.foundation.core.Classifier metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EntityDecorator ...

    // ------------- relations ------------------
    
   /**
    *
    */
    public org.omg.uml.foundation.core.ModelElement handleGetPrimaryKeyAttribute()
    {
        // TODO: add your implementation here!
        return null;
    }

    // ------------------------------------------------------------

}
