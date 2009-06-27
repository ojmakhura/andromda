package org.andromda.scriptwrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;


/**
 * This is a wrapper class for a Groovy script. The generated Java classes contain a private
 * final copy of this wrapper and delegates all methods to this class so that their Groovy-counterparts
 * can be executed.
 *
 * @author Chad Brandon
 */
public class GroovyScriptWrapper
{
    private String scriptPath;
    private Object stub;

    /**
     * StubClass is always the generated class (not any subclasses),
     * while stub may be an instance of a subclassed scripted class.
     */
    public GroovyScriptWrapper(
        Object stub,
        String scriptPath)
        throws java.lang.InstantiationError
    {
        this.stub = stub;
        this.scriptPath = scriptPath;
    }

    /**
     * Invokes the method with the given <code>methodName</code> on the instance.
     *
     * @param methodName the name of the method to invoke.
     * @param args the arguments to pass to the method.
     * @return the return result of invoking the operation.
     */
    public Object invoke(
        String methodName,
        Object[] args)
    {
        try
        {
            final GroovyClassLoader grooveyClassLoader = new GroovyClassLoader(this.getClassLoader());
            final Class groovyClass =
                grooveyClassLoader.parseClass(
                    this.getContents(new File(this.scriptPath)),
                    scriptPath);
            final GroovyObject groovyObject = (GroovyObject)groovyClass.newInstance();

            this.copyProperties(
                this.stub,
                groovyObject);
            return groovyObject.invokeMethod(
                methodName,
                args);
        }
        catch (final Throwable throwable)
        {
            throw new java.lang.RuntimeException(throwable);
        }
    }
    
    /**
     * Retrieves the appropriate class loader instance as the parent of the groovyClassLoader.
     * 
     * @return the class loader instance.
     */
    private ClassLoader getClassLoader()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null)
        {
            classLoader = this.getClass().getClassLoader();
        }
        return classLoader;
    }

    /**
     * The line separator.
     */
    private static final char LINE_SEPARATOR = '\n';

    /**
     * Loads the resource and returns the contents as a String.
     *
     * @param resource the name of the resource.
     * @return the contents of the resource as a string.
     * @throws FileNotFoundException
     */
    private String getContents(final File file)
        throws FileNotFoundException
    {
        Reader resource = new FileReader(file);
        final StringBuffer contents = new StringBuffer();
        try
        {
            if (resource != null)
            {
                BufferedReader resourceInput = new BufferedReader(resource);
                for (String line = resourceInput.readLine(); line != null; line = resourceInput.readLine())
                {
                    contents.append(line + LINE_SEPARATOR);
                }
                resourceInput.close();
                resourceInput = null;
            }
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }

        // - return the contents and remove any throws clauses (since groovy doesn't support those)
        return contents.toString().trim().replaceAll(
            "\\s+throws\\s+.+\\s+",
            " ");
    }

    /**
     * Copies all properties from the given <code>from</code> instance to the given
     * <code>to</code> instance.
     *
     * @param from the instance from which to copy all properties.
     * @param to the instance of which to copy all properties.
     * @throws Exception
     */
    private void copyProperties(
        final Object from,
        final GroovyObject to)
        throws Exception
    {
        final Set methods = new LinkedHashSet();
        loadSuperMethods(
            from.getClass(),
            methods);
        for (final Iterator iterator = methods.iterator(); iterator.hasNext();)
        {
            final Method method = (Method)iterator.next();

            final String methodName = method.getName();
            final String getPrefix = "get";
            if (methodName.startsWith(getPrefix) && method.getParameterTypes().length == 0)
            {
                String propertyName = methodName.replaceAll(
                        getPrefix,
                        "");

                Method setterMethod = null;
                try
                {
                    setterMethod =
                        from.getClass().getMethod(
                            "set" + propertyName,
                            new Class[] {method.getReturnType()});
                }
                catch (final Exception exception)
                {
                    // - ignore
                }
                if (setterMethod != null)
                {
                    method.setAccessible(true);
                    final Object value = method.invoke(
                            from,
                            null);
                    to.invokeMethod(
                        setterMethod.getName(),
                        new Object[] {value});
                }
            }
        }
    }

    /**
     * Loads all methods from the clazz's super classes.
     *
     * @param methods the list to load full of methods.
     * @param clazz the class to retrieve the methods.
     * @return the loaded methods.
     */
    private static Set loadSuperMethods(
        final Class clazz,
        final Set methods)
    {
        if (clazz.getSuperclass() != null)
        {
            methods.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredMethods()));
            methods.addAll(loadSuperMethods(
                    clazz.getSuperclass(),
                    methods));
        }
        return methods;
    }
}