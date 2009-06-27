package org.andromda.scriptwrappers;

import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.Primitive;


/**
 * This is a wrapper class for a BeanShell script. The generated Java classes contain a private
 * final copy of this wrapper and delegates all methods to this class so that their BeanShell-counterparts
 * can be executed.
 *
 * @author Chad Brandon
 */
public class BshScriptWrapper
{
    private final Interpreter interpreter;
    private String scriptPath;
    private Object stub;

    /**
     * StubClass is always the generated class (not any subclasses),
     * while stub may be an instance of a subclassed scripted class.
     */
    public BshScriptWrapper(
        Object stub,
        String scriptPath)
        throws java.lang.InstantiationError
    {
        this.stub = stub;
        this.scriptPath = scriptPath;
        this.interpreter = initialize(
                stub,
                scriptPath);
    }

    private BshScriptWrapper()
    {
        this.interpreter = null;
    }

    /**
     * Initializes the interpreter.
     *
     * @param stub the stub class.
     * @param scriptPath the path to the script file.
     * @return the initialized interpreter.
     */
    private final Interpreter initialize(
        Object stub,
        String scriptPath)
    {
        final Interpreter interpreter = new Interpreter();
        interpreter.setClassLoader(stub.getClass().getClassLoader());
        return interpreter;
    }

    /**
     * Invokes the method with the given <code>methodName</code> on the  instance.
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
            try
            {
                final Class stubClass = stub.getClass();
                this.interpreter.source(scriptPath);
                this.interpreter.set(
                    "instance",
                    interpreter.eval(" new " + stubClass.getName() + "();"));
                this.interpreter.set(
                    "stub",
                    stub);

                // - copy any properties
                this.interpreter.eval(BshScriptWrapper.class.getName() + ".copyProperties(stub, instance);");
            }
            catch (final Exception exception)
            {
                exception.printStackTrace();
                throw new InstantiationError("Problems instantiating script '" + scriptPath + "':" + exception);
            }
            final StringBuffer arguments = new StringBuffer();
            if (args != null)
            {
                for (int ctr = 1; ctr <= args.length; ctr++)
                {
                    final String argument = "$" + ctr;
                    this.interpreter.set(
                        argument,
                        args[ctr - 1]);
                    arguments.append(argument);
                    if (ctr != args.length)
                    {
                        arguments.append(", ");
                    }
                }
            }

            Object returnValue = this.interpreter.eval("instance." + methodName + "(" + arguments + ");");

            if (returnValue instanceof bsh.Primitive)
            {
                returnValue = Primitive.unwrap(returnValue);
            }

            return returnValue;
        }
        catch (EvalError exception)
        {
            throw new java.lang.RuntimeException(exception);
        }
    }

    /**
     * Copies all properties from the given <code>from</code> instance to the given
     * <code>to</code> instance.
     *
     * @param from the instance from which to copy all properties.
     * @param to the instance of which to copy all properties.
     * @throws Exception
     */
    protected static void copyProperties(
        final Object from,
        final Object to)
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
                    setterMethod.invoke(
                        to,
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