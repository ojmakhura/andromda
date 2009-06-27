package org.andromda.core.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Contains utilities for dealing with classes.
 *
 * @author Chad Brandon
 * @author Bob Fields
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
     */
    public static Class loadClass(String className)
    {
        ExceptionUtils.checkEmpty(
            "className",
            className);
        className = StringUtils.trimToNull(className);

        // get rid of any array notation
        className = StringUtils.replace(
                className,
                "[]",
                "");

        final ClassLoader loader = getClassLoader();
        Class loadedClass;
        try
        {
            // check and see if its a primitive and if so convert it
            if (ClassUtils.isPrimitiveType(className))
            {
                loadedClass = getPrimitiveClass(
                        className,
                        loader);
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
    public static ClassLoader getClassLoader()
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
    protected static Class getPrimitiveClass(
        final String name,
        final ClassLoader loader)
    {
        ExceptionUtils.checkEmpty(
            "name",
            name);
        ExceptionUtils.checkNull(
            "loader",
            loader);

        Class primitiveClass = null;
        if (isPrimitiveType(name) && !name.equals("void"))
        {
            final String className;
            if ("char".equals(name))
            {
                className = "Character";
            }
            else if ("int".equals(name))
            {
                className = "Integer";
            }
            else
            {
                className = StringUtils.capitalize(name);
            }

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
     * Returns a collection of all static fields values for the given
     * <code>clazz</code> and <code>type</code> of field.
     *
     * @param clazz  the Class from which to retrieve the static fields
     * @param type the type of static fields to retrieve, if null all are retrieved
     * @return Collection the collection of static field values.
     * @throws IllegalAccessException - if some aspect of this static field prevents it from being added to this collection.
     */
    public static Collection getStaticFieldValues(
        final Class clazz,
        final Class type)
        throws IllegalAccessException
    {
        ExceptionUtils.checkNull(
            "clazz",
            clazz);
        final Field[] fields = clazz.getFields();
        int fieldsNum = fields.length;

        final List values = new ArrayList();
        Field field;
        int modifiers;
        for (int ctr = 0; ctr < fieldsNum; ctr++)
        {
            field = fields[ctr];
            modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers))
            {
                if (type != null)
                {
                    if (type == field.getType())
                    {
                        values.add(fields[ctr].get(null));
                    }
                }
                else
                {
                    values.add(fields[ctr].get(null));
                }
            }
        }
        return values;
    }

    /**
     * Retrieves all interfaces for the given <code>className</code> (including <code>className</code>
     * itself, assuming it's an interface as well).
     *
     * @param className the root interface className
     * @return a list containing all interfaces ordered from the root down.
     */
    public static List getInterfaces(final String className)
    {
        final List interfaces = new ArrayList();
        if (className != null && className.trim().length() > 0)
        {
            interfaces.addAll(getInterfaces(ClassUtils.loadClass(className.trim())));
        }
        return interfaces;
    }

    /**
     * Retrieves all interfaces for the given <code>clazz</code> (including <code>clazz</code>
     * itself, assuming it's an interface as well).
     *
     * @param clazz the root interface class
     * @return a list containing all interfaces ordered from the root down.
     */
    public static List getInterfaces(final Class clazz)
    {
        final List interfaces = new ArrayList();
        if (clazz != null)
        {
            interfaces.addAll(ClassUtils.getAllInterfaces(clazz));
            if (clazz.isInterface())
            {
                interfaces.add(
                    0,
                    clazz);
            }
        }
        return interfaces;
    }

    /**
     * Gets the interfaces for the given <code>className</code> in reverse order.
     *
     * @param className the name of the class for which to retrieve the interfaces
     * @return the array containing the reversed interfaces.
     */
    public static Class[] getInterfacesReversed(final String className)
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
    protected static boolean isPrimitiveType(final String name)
    {
        return ("void".equals(name) || "char".equals(name) || "byte".equals(name) || "short".equals(name) ||
        "int".equals(name) || "long".equals(name) || "float".equals(name) || "double".equals(name) ||
        "boolean".equals(name));
    }

    /**
     * The suffix for class files.
     */
    public static final String CLASS_EXTENSION = ".class";

    /**
     * Searches the contents of the <code>directoryUri</code> and returns the first
     * Class found that is of the given <code>type</code>.
     *
     * @param directoryUris the URIs to search, ie. directories or archives.
     * @param type the type to find.
     * @return the class or null if not found.
     */
    public static Class findClassOfType(
        final URL directoryUris[],
        final Class type)
    {
        Class found = null;
        if (directoryUris != null && directoryUris.length > 0)
        {
            final int numberOfDirectoryUris = directoryUris.length;
            for (int ctr = 0; ctr < numberOfDirectoryUris; ctr++)
            {
                final URL directoryUri = directoryUris[ctr];
                final List contents = ResourceUtils.getDirectoryContents(
                        directoryUri,
                        false,
                        null);
                for (final Iterator iterator = contents.iterator(); iterator.hasNext();)
                {
                    final String path = (String)iterator.next();
                    if (path.endsWith(CLASS_EXTENSION))
                    {
                        final String typeName =
                            StringUtils.replace(
                                ResourceUtils.normalizePath(path).replace(
                                    '/',
                                    '.'),
                                CLASS_EXTENSION,
                                "");
                        try
                        {
                            final Class loadedClass = getClassLoader().loadClass(typeName);
                            if (type.isAssignableFrom(loadedClass))
                            {
                                found = loadedClass;                          
                                break;
                            }
                        }
                        catch (final ClassNotFoundException exception)
                        {
                            // - ignore, means the file wasn't a class
                        }
                    }
                }
            }
        }
        return found;
    }
    
    /**
     * Loads all methods from the given <code>clazz</code> (this includes
     * all super class methods, public, private and protected).
     *
     * @param clazz the class to retrieve the methods.
     * @return the loaded methods.
     */
    public static List getAllMethods(final Class clazz)
    {
        final Set methods = new LinkedHashSet();
        loadMethods(clazz, methods);
        return new ArrayList(methods);
    }
    
    /**
     * Loads all methods from the given <code>clazz</code> (this includes
     * all super class methods).
     *
     * @param methods the list to load full of methods.
     * @param clazz the class to retrieve the methods.
     */
    private static void loadMethods(
        final Class clazz,
        final Set methods)
    {
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        if (clazz.getSuperclass() != null)
        {
            loadMethods(
                    clazz.getSuperclass(),
                    methods);
        }
    }
    
    /**
     * Indicates whether or not a class of the given <code>type</code>
     * is present in one of the given <code>directoryUris</code>.
     * 
     * @param directoryUris the URIs to search, ie. directories or archives.
     * @param type the type to check.
     * @return true/false.
     */
    public static boolean isClassOfTypePresent(
        final URL directoryUris[],
        final Class type)
    {
        return ClassUtils.findClassOfType(directoryUris, type) != null;
    }
}