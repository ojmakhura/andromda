package org.andromda.core.metadecorators.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.omg.uml.foundation.core.ModelElement;

/**
 * Base class for all metaclass decorators.
 */
public class DecoratorBase
{
    private   Object metaObject;
    protected Logger logger;

    public DecoratorBase(Object metaObject)
    {
        this.metaObject = metaObject;
    }

    /**
     * Returns a collection of decorators for a collection
     * of metaobjects. Contacts the DecoratorFactory to manufacture
     * the proper decorators.
     * @see DecoratorFactory
     * 
     * @param metaobjects the objects to decorate
     * @return Collection of DecoratorBase-derived objects
     */
    public static Collection decoratedElements(Collection metaobjects)
    {
        ArrayList result = new ArrayList(metaobjects.size());
        DecoratorFactory df = DecoratorFactory.getInstance();

        for (Iterator iter = metaobjects.iterator(); iter.hasNext();)
        {
            ModelElement element = (ModelElement) iter.next();
            result.add(df.createDecoratorObject(element));
        }
        return result;
    }

    /**
     * Returns one decorator for a particular metaobject. Contacts 
     * the DecoratorFactory to manufacture the proper decorator.
     * 
     * @see DecoratorFactory
     * @param metaObject the object to decorate
     * @return DecoratorBase the decorator
     */
    public static DecoratorBase decoratedElement(ModelElement metaObject)
    {
        return DecoratorFactory.getInstance().createDecoratorObject(
            metaObject);
    }
    
    /**
     * Package-local setter, called by decorator factory.
     * Sets the logger to use inside the decorator's code.
     * @param l the logger to set
     */
    void setLogger(Logger l)
    {
        logger = l;
    }
}
