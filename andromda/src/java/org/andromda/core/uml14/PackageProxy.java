package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * @author Anthony Mowers
 *
 */
public class PackageProxy 
    extends ModelElementProxy 
    implements UMLPackage 
{

    public static UmlPackage newInstance(
        UMLScriptHelper scriptHelper,
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
            new PackageProxy(umlPackage, scriptHelper));
    }


    
    private PackageProxy(
        UmlPackage umlPackage,
        UMLScriptHelper scriptHelper)
    {
        super(umlPackage,scriptHelper);
    }

	/**
	 * @see org.andromda.core.uml14.UMLPackage#getClasses()
	 */
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
                o = ClassifierProxy.newInstance(scriptHelper,(Classifier)o);
                classProxies.add(o);
            }
        }
           
		return classProxies;
	}

}
