package org.andromda.core.common;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Contains utilities for dealing with classes.
 *
 * @author Chad Brandon
 */
public class ClassUtils
    extends org.apache.commons.lang.ClassUtils
{
    /**
     * Creates a new instance of the class having the given <code>className</code>.
     *
     * @param className the name of the class to instantiate.
     * @return Object the new instance
     */
    public static Object newInstance(final String className)
    {
        try
        {
            return loadClass(className).newInstance();
        }
        catch (final Throwable throwable)
        {
            throw new ClassUtilsException(throwable);
        }
    }
    
    /**
     * Creates a new instance of the class given the <code>type</code>.
     *
     * @param type the type from which to instantiate the new instance.
     * @return Object the new instance
     */
    public static Object newInstance(final Class type)
    {
        try
        {
            return type != null ? type.newInstance() : null;
        }
        catch (final Throwable throwable)
        {
            throw new ClassUtilsException(throwable);
        }        
    }

    /**
     * Loads and returns the class having the className. Will load but normal classes and the classes representing
     * primatives.
     *
     * @param className the name of the class to load.
     * @return Class the loaded class
     * @throws ClassNotFoundException if the class can not be found
     */
    public static Class loadClass(String className)
    {
        final String methodName = "ClassUtils.loadClass";
        ExceptionUtils.checkEmpty(methodName, "className", className);
        className = StringUtils.trimToNull(className);

        // get rid of any array notation
        className = StringUtils.replace(className, "[]", "");

        final ClassLoader loader = getClassLoader();
        Class loadedClass = null;
        try
        {
            // check and see if its a primitive and if so convert it
            if (ClassUtils.isPrimitiveType(className))
            {
                loadedClass = getPrimitiveClass(className, loader);
            }
            else
            {
                loadedClass = loader.loadClass(className);
            }
        }
        catch (final Throwable throwable)
        {
            throw new ClassUtilsException(throwable);
        }
        return loadedClass;
    }

    /**
     * Gets the appropriate class loader instance.
     *
     * @return the class loader.
     */
    public static final ClassLoader getClassLoader()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
        {
            loader = ClassUtils.class.getClassLoader();
            Thread.currentThread().setContextClassLoader(loader);
        }
        return loader;
    }

    /**
     * <p> Returns the type class name for a Java primitive.
     * </p>
     *
     * @param name a <code>String</code> with the name of the type
     * @param loader the loader to use.
     * @return a <code>String</code> with the name of the corresponding
     *         java.lang wrapper class if <code>name</code> is a Java
     *         primitive type; <code>false</code> if not
     */
    protected static final Class getPrimitiveClass(
        final String name,
        final ClassLoader loader)
    {
        final String methodName = "ClassUtils.getPrimitiveClass";
        ExceptionUtils.checkEmpty(methodName, "name", name);
        ExceptionUtils.checkNull(methodName, "loader", loader);

        Class primitiveClass = null;
        if (isPrimitiveType(name) && !name.equals("void"))
        {
            String className = null;
            if ("char".equals(name))
            {
                className = "java.lang.Character";
            }
            if ("int".equals(name))
            {
                className = "java.lang.Integer";
            }

            className = "java.lang." + StringUtils.capitalize(name);

            try
            {
                if (StringUtils.isNotEmpty(className))
                {
                    Field field = loader.loadClass(className).getField("TYPE");
                    primitiveClass = (Class)field.get(null);
                }
            }
            catch (final Exception exception)
            {
                throw new ClassUtilsException(exception);
            }
        }
        return primitiveClass;
    }

    /**
     * Retrieves all interfaces for the given <code>className</code> (including the interface for <code>className</code>
     * itself, assuming it's an interface itself).
     *
     * @param className the root interface className
     * @return a list containing all interfaces ordered from the root down.
     */
    public static final List getInterfaces(final String className)
    {
        final List interfaces = new ArrayList();
        if (StringUtils.isNotEmpty(className))
        {
            final Class interfaceClass = ClassUtils.loadClass(className);
            interfaces.addAll(ClassUtils.getAllInterfaces(interfaceClass));
            interfaces.add(0, interfaceClass);
        }
        return interfaces;
    }

    /**
     * Gets the interfaces for the given <code>className</code> in reverse order.
     *
     * @param className the name of the class for which to retrieve the interfaces
     * @return the array containing the reversed interfaces.
     */
    public static final Class[] getInterfacesReversed(final String className)
    {
        Class[] interfaces = (Class[])getInterfaces(className).toArray(new Class[0]);
        if (interfaces != null && interfaces.length > 0)
        {
            CollectionUtils.reverseArray(interfaces);
        }
        return interfaces;
    }

    /**
     * <p/>
     * Checks if a given type name is a Java primitive type. </p>
     *
     * @param name a <code>String</code> with the name of the type
     * @return <code>true</code> if <code>name</code> is a Java primitive type; <code>false</code> if not
     */
    protected static final boolean isPrimitiveType(final String name)
    {
        return ("void".equals(name) || "char".equals(name) || "byte".equals(name) || "short".equals(name) ||
        "int".equals(name) || "long".equals(name) || "float".equals(name) || "double".equals(name) ||
        "boolean".equals(name));
    }
}