package org.andromda.core.uml14;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.omg.uml.UmlPackage;

/**
 * @author amowers
 *.
 */
public class ModelProxy 
    implements 
        UMLModel,
        java.lang.reflect.InvocationHandler 
{
    
    private UmlPackage model;
    private UMLScriptHelper scriptHelper;

	/**
	 * @see org.andromda.core.uml14.UMLModel#getPackages()
	 */
	public Collection getPackages() {
        Collection packages = 
            model.getModelManagement().getUmlPackage().refAllOfType();
        Collection packageProxies = new Vector();
        
        for (Iterator i= packages.iterator();i.hasNext();)
        {
            org.omg.uml.modelmanagement.UmlPackage o = 
                (org.omg.uml.modelmanagement.UmlPackage)i.next();
            o = PackageProxy.newInstance(scriptHelper,o);
            packageProxies.add(o);
        }
        
        return packageProxies;
	}

    public static UmlPackage newInstance(
        UMLScriptHelper scriptHelper,
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
            new ModelProxy(model, scriptHelper));
    }


    
    private ModelProxy(
        UmlPackage model,
        UMLScriptHelper scriptHelper)
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
