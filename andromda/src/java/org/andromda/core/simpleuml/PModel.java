package org.andromda.core.simpleuml;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.UmlPackage;

/**
 * dynamic proxy for a Model: dynamically supports the UMLModel, 
 * and org.omg.uml.UmlPackage interfaces.
 * 
 * @author Anthony Mowers
 *.
 */
public class PModel 
    implements 
        UMLModel,
        java.lang.reflect.InvocationHandler 
{
    
    private UmlPackage model;
    private UMLStaticHelper scriptHelper;

	public Collection getPackages() {
        Collection packages = 
            model.getModelManagement().getUmlPackage().refAllOfType();
        Collection packageProxies = new Vector();
        
        for (Iterator i= packages.iterator();i.hasNext();)
        {
            org.omg.uml.modelmanagement.UmlPackage o = 
                (org.omg.uml.modelmanagement.UmlPackage)i.next();
            o = PPackage.newInstance(scriptHelper,o);
            packageProxies.add(o);
        }
        
        return packageProxies;
	}

    public static UmlPackage newInstance(
        UMLStaticHelper scriptHelper,
        UmlPackage model)
    {
        Class[] interfaces = new Class[]
            {
            UMLModel.class,
            UmlPackage.class
            };

        return (UmlPackage)java.lang.reflect.Proxy.newProxyInstance(
            model.getClass().getClassLoader(),
            interfaces,
            new PModel(model, scriptHelper));
    }


    
    private PModel(
        UmlPackage model,
        UMLStaticHelper scriptHelper)
    {
        this.model = model;
        this.scriptHelper = scriptHelper;
    }

    public Object invoke(Object proxy, Method m, Object[] args)
        throws Throwable
    {
        if (m.getDeclaringClass().isAssignableFrom(this.getClass()))
        {
            return m.invoke(this, args);
        }

        return m.invoke(model, args);
    }
}
