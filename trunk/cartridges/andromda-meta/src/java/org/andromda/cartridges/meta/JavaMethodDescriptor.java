package org.andromda.cartridges.meta;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Represents a method in Java.
 * 
 * @since 10.12.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class JavaMethodDescriptor extends MethodDescriptor
{

    /**
     * Constructs a descriptor for a Java method.
     * 
     * @param m the method as described in java.lang.reflect
     * @param interfaceName the name of the parent interface of the method
     */
    public JavaMethodDescriptor(Method m, String interfaceName)
    {
        super(buildMethodData(m, interfaceName));
    }

    /**
     * Builds a data structure representing this method.
     * @param m the method
     * @return MethodData the declaration
     */
    private static MethodData buildMethodData(
        Method m,
        String interfaceName)
    {
        Class returntype = m.getReturnType();
        Class parameters[] = m.getParameterTypes();
        Class exceptions[] = m.getExceptionTypes();

        int modifiers = m.getModifiers();
        String returnTypeName =
            ((returntype != null) ? typename(returntype) : "");
        MethodData md =
            new MethodData(
                interfaceName,
                visibility(modifiers),
                Modifier.isAbstract(modifiers),
                returnTypeName,
                m.getName());

        for (int i = 0; i < parameters.length; i++)
        {
            md.addArgument(
                new ArgumentData(typename(parameters[i]), "p" + i));
        }

        for (int i = 0; i < exceptions.length; i++)
        {
            md.addException(typename(exceptions[i]));
        }

        return md;
    }

    /** Return the name of an interface or primitive type, handling arrays. */
    private static String typename(Class t)
    {
        String brackets = "";
        while (t.isArray())
        {
            brackets += "[]";
            t = t.getComponentType();
        }
        return t.getName() + brackets;
    }

    /** Return a string version of the visibility */
    private static String visibility(int m)
    {
        if (Modifier.isPublic(m))
            return "public";
        if (Modifier.isProtected(m))
            return "protected";
        if (Modifier.isPrivate(m))
            return "private";
        return "";
    }
}
