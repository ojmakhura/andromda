package org.andromda.cartridges.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.andromda.core.simpleuml.SimpleOOHelper;

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
     * Returns a collection of all the methods of a given interface
     * and its super-interfaces. Duplicate methods from more than one
     * super-interface are merged into one. Methods from the interface
     * <code>ModelElement</code> are <b>not included</b> since the decorator
     * class is supposed to derive from ModelElementDecorator.
     * </p>
     * 
     * <p>
     * The return value implements the interface Collection so that
     * the script code can iterate over the elements.
     * </p>
     * 
     * @param interfaceName the FQCN of the interface
     * @return a collection of MethodDescriptors
     * @throws ClassNotFoundException
     */
    public static Collection getMethods(String interfaceName)
        throws ClassNotFoundException
    {
        boolean allowModelElementInterfaces =
            interfaceName.equals(modelElementInterfaceName);

        Collection c =
            getMethodsInternal(interfaceName, allowModelElementInterfaces)
                .values();
        ArrayList result = new ArrayList();
        result.addAll(c);
        Collections.sort(result);
        return result;
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
                new MethodDescriptor(methods[i], interfaceName);
            String decl = mdesc.getDeclaration(false);
            // the HashMap.put() will eliminate duplicate declarations
            // when the same method is declared in more than one interface.
            result.put(decl, mdesc);
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
}
