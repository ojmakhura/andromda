package org.andromda.core.metadecorators.uml14;

import java.util.Collection;

/**
 * Base class for all metaclass decorators.
 */
public class DecoratorBase
{
    private Object metaObject;

    public DecoratorBase (Object metaObject)
    {
        this.metaObject = metaObject;
    }
    
   /**
    * Returns a collection of decorators for a collection
    * of metaobjects. Contacts the DecoratorFactory to manufacture
    * the proper decorators.
    * @see DecoratorFactory
    * 
    * @param metaobjects 
    * @return Collection of DecoratorBase-derived objects
    */
    public static Collection decoratedElements(Collection metaobjects)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
