package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.omg.uml.foundation.core.Classifier;

/**
 * @author Anthony Mowers
 *
 * This script helper simulates the old-style UML2EJB SimpleOO based
 * script helper.  It will be deprecated as soon as a new script helper API
 * is designed.
 * 
 */
public class SimpleOOHelper extends UMLScriptHelper 
{
    public Object getModel()
    {
        return ModelProxy.newInstance(this,this.model);
    }
    
    public Collection getModelElements()
    {
        Collection elements = new Vector();
        for (Iterator i= super.getModelElements().iterator();i.hasNext();)
        {
            Object o = i.next();
            if (o instanceof Classifier )
            {
                o = ClassifierProxy.newInstance(this,(Classifier)o);
            }
            elements.add(o);
        }
        
        return elements;
    }
}
