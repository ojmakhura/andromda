package org.andromda.cartridges.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.simpleuml.SimpleOOHelper;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Stereotype;

/**
 * Script helper for the metaclass decorator cartridge.
 * It makes the Java reflection API accessible from within template code
 * and concentrates the necessary intelligence here, not in the templates!
 */
public class MetaScriptHelper extends SimpleOOHelper
{
    private static final String modelElementInterfaceName =
        "org.omg.uml.foundation.core.ModelElement";

    /**
     * <p>
     * Returns a Map of all the methods of a given interface
     * and its super-interfaces. Duplicate methods from more than one
     * super-interface are merged into one. Methods from the interface
     * <code>ModelElement</code> are <b>not included</b> since the decorator
     * class is supposed to derive from ModelElementDecorator.
     * </p>
     * 
     * @param interfaceName the FQCN of the interface
     * @return a Map of MethodDescriptors
     * @throws ClassNotFoundException
     */
    public static Map getMethodMap(String interfaceName)
        throws ClassNotFoundException
    {
        boolean allowModelElementInterfaces =
            interfaceName.equals(modelElementInterfaceName);

        return getMethodsInternal(
            interfaceName,
            allowModelElementInterfaces);
    }

    /**
     * Returns a collection of all the methods of a given interface
     * and its super-interfaces. Duplicate methods from more than one
     * super-interface are merged into one.
     * 
     * @param interfaceName the FQCN of the interface
     * @return a map of MethodDescriptors
     * @throws ClassNotFoundException
     */
    private static Map getMethodsInternal(
        String interfaceName,
        boolean allowModelElementInterfaces)
        throws ClassNotFoundException
    {

        Class c = Class.forName(interfaceName);
        HashMap result = new HashMap();
        Method[] methods = c.getDeclaredMethods(); // Look up methods.

        // convert them to source code.
        for (int i = 0; i < methods.length; i++)
        {
            MethodDescriptor mdesc =
                new JavaMethodDescriptor(methods[i], interfaceName);
            // the HashMap.put() will eliminate duplicate declarations
            // when the same method is declared in more than one interface.
            result.put(mdesc.getCharacteristicKey(), mdesc);
        }

        // add methods from all super-interfaces
        Class[] interfaces = c.getInterfaces();
        for (int i = 0; i < interfaces.length; i++)
        {
            String name = interfaces[i].getName();
            if (allowModelElementInterfaces
                || !name.equals(modelElementInterfaceName))
            {
                result.putAll(
                    getMethodsInternal(name, allowModelElementInterfaces));
            }
        }

        return result;
    }

    /**
     * Excludes a simple getter from the map of all methods of a decorated interface.
     * 
     * @param allMethods the methods
     * @param targetTypeName the target type of the getter
     * @param fieldName the field that the getter gets
     * @return the modified map
     */
    public static Map excludeSimpleGetter(
        Map allMethods,
        String targetTypeName,
        String fieldName)
    {
        SimpleGetterDescriptor sgd =
            new SimpleGetterDescriptor(targetTypeName, fieldName);
        allMethods.remove(sgd.getCharacteristicKey());
        return allMethods;
    }

    /**
     * Excludes a method that will be generated from a UML Operation.
     * 
     * @param allMethods the methods
     * @param o the operation to exclude
     * @return the modified map of methods
     */
    public static Map excludeUMLOperation(Map allMethods, Operation o)
    {
        UMLOperationDescriptor uod = new UMLOperationDescriptor(o);
        allMethods.remove(uod.getCharacteristicKey());
        return allMethods;
    }

    /**
     * <p>
     * Returns a flat, sorted collection of all methods in a Map.
     * The script code can iterate over that collection.
     * </p>
     * @param methods the methods inside a Map
     * @return a collection of MethodDescriptors
     * @throws ClassNotFoundException
     */
    public static Collection sortMethodsFlat(Map methods)
        throws ClassNotFoundException
    {
        ArrayList result = new ArrayList();
        result.addAll(methods.values());
        Collections.sort(result);
        return result;
    }

    /**
     * Returns the class tagged with &lt;&lt;metaclass&gt;&gt; that is
     * connected to cl via a dependency.
     * 
     * @param cl the source classifier
     * @return the metaclass-stereotyped classifier
     */
    public Classifier getMetaclass(GeneralizableElement cl)
    {
        for (Iterator iter = cl.getClientDependency().iterator();
            iter.hasNext();
            )
        {
            Object element = iter.next();
            if ((element instanceof Dependency) && !(element instanceof Abstraction))
            {
                Dependency dep = (Dependency)element;
                ModelElement supplier = (ModelElement)dep.getSupplier().iterator().next();
                Collection stereotypes = supplier.getStereotype();
                if (stereotypes != null)
                {
                    String stereotypeName = ((Stereotype)stereotypes.iterator().next()).getName();
                    if (stereotypeName.equals("metaclass")) {
                        return (Classifier)supplier;
                    }
                }
            }
        }
        GeneralizableElement superclass = getGeneralization(cl);
        return (superclass != null) ? getMetaclass(superclass) : null;
    }
}
