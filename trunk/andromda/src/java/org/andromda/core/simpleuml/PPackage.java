package org.andromda.core.simpleuml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * dynamic proxy for a Package: dynamically supports the UMLPackage, 
 * and org.omg.uml.modelmanagement.UmlPackage interfaces.
 * 
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class PPackage 
    extends PModelElement 
    implements UMLPackage 
{

    public static UmlPackage newInstance(
        UMLStaticHelper scriptHelper,
        UmlPackage umlPackage)
    {
        Class[] interfaces = new Class[]
            {
            UMLPackage.class,
            UmlPackage.class
            };

        return (UmlPackage)java.lang.reflect.Proxy.newProxyInstance(
            umlPackage.getClass().getClassLoader(),
            interfaces,
            new PPackage(umlPackage, scriptHelper));
    }


    
    private PPackage(
        UmlPackage umlPackage,
        UMLStaticHelper scriptHelper)
    {
        super(umlPackage,scriptHelper);
    }

	public Collection getClasses() 
    {
        UmlPackage umlPackage = (UmlPackage)modelElement;
        Collection contents = umlPackage.getOwnedElement();
        Collection classProxies = new Vector();
        
        for (Iterator i = contents.iterator(); i.hasNext(); )
        {
            Object o = i.next();
            if (o instanceof UmlClass)
            {
                o = PClassifier.newInstance(scriptHelper,(Classifier)o);
                classProxies.add(o);
            }
        }
           
		return classProxies;
	}

}
