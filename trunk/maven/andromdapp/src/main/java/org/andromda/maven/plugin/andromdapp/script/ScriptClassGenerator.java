package org.andromda.maven.plugin.andromdapp.script;

import java.io.File;

import java.net.URL;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * This class instruments a given class file in order for it be scripted.  A class modified
 * by this script generator can have its methods edited and the logic available without having
 * to redeploy or compile the class.
 *
 * @author Chad Brandon
 */
public class ScriptClassGenerator
{
    /**
     * The shared instance of this class.
     */
    private static ScriptClassGenerator instance;

    /**
     * The name of the script wrapper to use.
     */
    private String scriptWrapperName;

    /**
     * Retrieves an instance of this class and uses the given script wrapper with
     * the given <code>scriptWrapperName</code>.
     *
     * @param scriptWrapperName the fully qualified name of the script wrapper class to use.
     * @return the instance of this class.
     */
    public static final ScriptClassGenerator getInstance(final String scriptWrapperName)
    {
        ExceptionUtils.checkEmpty(
            "scriptWrapperName",
            scriptWrapperName);
        instance = new ScriptClassGenerator();
        instance.scriptWrapperName = scriptWrapperName;
        return instance;
    }

    private ScriptClassGenerator()
    {
        // - do not allow instantiation
    }

    /**
     * Modifies the <code>existingClass</code> (basically inserts the script wrapper class into
     * the class).
     * @param scriptDirectory the directory in which to find the script.
     * @param existingClass the class to modify.
     */
    public void modifyClass(
        final String scriptDirectory,
        final Class existingClass)
    {
        try
        {
            final String className = existingClass.getName();

            final ClassPool pool = ClassPool.getDefault();
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null)
            {
                pool.insertClassPath(new LoaderClassPath(contextClassLoader));
            }
            final CtClass ctClass = pool.get(className);
            
            // - make sure the class isn't frozen
            ctClass.defrost();

            final String scriptWrapperFieldName = "scriptWrapper";
            try
            {
                ctClass.getField(scriptWrapperFieldName);
            }
            catch (Exception exception)
            {
                final CtField scriptWrapper =
                    new CtField(
                        convert(
                            pool,
                            this.scriptWrapperName),
                        scriptWrapperFieldName,
                        ctClass);
                scriptWrapper.setModifiers(Modifier.PRIVATE + Modifier.FINAL);
                ctClass.addField(
                    scriptWrapper,
                    getScriptWrapperInitialization(
                        scriptDirectory,
                        className));
            }

            final CtMethod[] existingMethods = ctClass.getDeclaredMethods();
            for (int ctr = 0; ctr < existingMethods.length; ctr++)
            {
                final CtMethod method = existingMethods[ctr];
                if (!Modifier.isStatic(method.getModifiers()))
                {
                    final CtClass returnType = method.getReturnType();
                    String methodBody;
                    if (returnType.equals(CtClass.voidType))
                    {
                        methodBody =
                                '{' + contructArgumentString(method) + "scriptWrapper.invoke(\"" + method.getName() +
                            "\", arguments);}";
                    }
                    else
                    {
                        if (returnType.isPrimitive())
                        {
                            methodBody =
                                    '{' + contructArgumentString(method) + " return ((" + getWrapperTypeName(returnType) +
                                ")scriptWrapper.invoke(\"" + method.getName() + "\", arguments))." +
                                returnType.getName() + "Value();}";
                        }
                        else
                        {
                            methodBody =
                                    '{' + contructArgumentString(method) + " return (" + method.getReturnType().getName() +
                                ")scriptWrapper.invoke(\"" + method.getName() + "\", arguments);}";
                        }
                    }
                    method.setBody(methodBody);
                }
            }

            final File directory = getClassOutputDirectory(existingClass);

            pool.writeFile(className,
                directory != null ? directory.getAbsolutePath() : "");
        }
        catch (final Throwable throwable)
        {
            throwable.printStackTrace();
            throw new ScriptClassGeneratorException(throwable);
        }
    }

    /**
     * Retrieves the output directory which the adapted class will be written to.
     *
     * @return the output directory
     */
    private File getClassOutputDirectory(final Class existingClass)
    {
        final String className = existingClass.getName();
        final String classResourcePath = '/' + className.replace(
            '.',
            '/') + ".class";
        final URL classResource = existingClass.getResource(classResourcePath);
        if (classResource == null)
        {
            throw new ScriptClassGeneratorException("Could not find the class resource '" + classResourcePath + '\'');
        }
        final String file = classResource.getFile().replaceAll(".*(\\\\|//)", "/");
        return new File(StringUtils.replace(file, classResourcePath, ""));
    }

    private String contructArgumentString(final CtMethod method)
        throws NotFoundException
    {
        CtClass[] argumentTypes = method.getParameterTypes();
        final int argumentNumber = argumentTypes.length;
        final StringBuffer arguments =
            new StringBuffer("final Object[] arguments = new Object[" + argumentNumber + "];");
        for (int ctr = 1; ctr <= argumentNumber; ctr++)
        {
            final CtClass argumentType = argumentTypes[ctr - 1];
            arguments.append("arguments[").append(ctr - 1).append("] = ");
            if (argumentType.isPrimitive())
            {
                arguments.append("new java.lang.").append(getWrapperTypeName(argumentType)).append("($").append(ctr).append(");");
            }
            else
            {
                arguments.append('$').append(ctr).append(';');
            }
        }
        return arguments.toString();
    }

    private String getWrapperTypeName(CtClass ctClass)
    {
        final String typeName = ctClass.getName();
        StringBuffer name = new StringBuffer(typeName);
        if ("int".equalsIgnoreCase(typeName))
        {
            name.append("eger");
        }
        return StringUtils.capitalize(name.toString());
    }

    private String getScriptWrapperInitialization(
        final String directory,
        final String className)
    {
        return "new " + this.scriptWrapperName + "(this, \"" +
        new File(
            directory,
            className.replace(
                '.',
                '/')).getAbsolutePath().replace(
            '\\',
            '/') + ".java" + "\");";
    }

    /**
     * Converts the given <code>clazz</code> to a CtClass instances.
     *
     * @param pool the pool from which to retrieve the CtClass instance.
     * @param clazz the class to convert.
     * @return the CtClass instances.
     * @throws NotFoundException
     */
    private CtClass convert(
        final ClassPool pool,
        final String className)
        throws NotFoundException
    {
        CtClass ctClass = null;
        if (className != null)
        {
            ctClass = pool.get(className);
        }
        return ctClass;
    }
}