package org.andromda.core.metadecorators.uml14;

import java.util.Iterator;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class EntityDecoratorImpl extends EntityDecorator
{
    private final static String PRIMARY_KEY = "PrimaryKey";

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
        for (Iterator i = getAttributes().iterator(); i.hasNext();)
        {
            AttributeDecorator attribute = (AttributeDecorator) i.next();
            if (attribute.getStereotype().equals(PRIMARY_KEY))
            {
                return attribute;
            }
        }

        return null;
    }

    // ------------------------------------------------------------

}
