package org.andromda.translation.testsuite;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.translation.BaseTranslator;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.core.translation.Translator;
import org.andromda.core.translation.TranslatorException;
import org.apache.log4j.Logger;

/**
 * This class allows us to trace the parsing of the expression. It is
 * reflectively extended by Javaassist to allow the inclusion of all "inA" and
 * "outA" methods produced by the SableCC parser. This allows us to dynamically
 * include all handling code in each method without having to manual code each
 * one. It used used during development of Translators since it allows you to
 * see the execution of each node.
 * 
 * @author Chad Brandon
 */
public class TraceTranslator
    extends BaseTranslator
{

    private static final Logger logger = Logger
        .getLogger(TraceTranslator.class);

    private static final String INA_PREFIX = "inA";
    private static final String OUTA_PREFIX = "outA";
    private static final String CASE_PREFIX = "case";

    private Map methods = new HashMap();

    /**
     * This is added to the adapted class and then checked to see if it exists
     * to determine if we need to adapt the class.
     */
    private static final String FIELD_ADAPTED = "adapted";

    private ClassPool pool;

    /**
     * Constructs an instance of TraceTranslator.
     */
    public TraceTranslator()
    {}

    /**
     * Creates and returns an new Instance of this ExpressionTranslator as a
     * Translator object. The first time this method is called this class will
     * dynamically be adapted to handle all parser calls.
     * 
     * @return Translator
     */
    public static Translator getInstance()
    {
        final String debugMethodName = "TraceTranslator.getInstance";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + debugMethodName);
        }
        try
        {
            TraceTranslator oclTranslator = new TraceTranslator();
            Translator translator = oclTranslator;
            if (oclTranslator.needsAdaption())
            {

                if (logger.isInfoEnabled())
                {
                    logger
                        .info(" OCL Translator has not been adapted --> adapting");
                }
                translator = (Translator)oclTranslator
                    .getAdaptedTranslationClass().newInstance();
            }
            return translator;
        }
        catch (Exception ex)
        {
            String errMsg = "Error performing " + debugMethodName;
            logger.error(errMsg, ex);
            throw new TranslatorException(errMsg, ex);
        }
    }

    /**
     * @see org.andromda.core.translation.Translator#translate(java.lang.String,
     *      java.lang.Object, java.lang.String)
     */
    public Expression translate(
        String translationName,
        Object contextElement,
        String expression)
    {
        if (logger.isInfoEnabled())
        {
            logger
                .info("======================== Tracing Expression ========================");
            logger.info(TranslationUtils.removeExtraWhitespace(expression));
            logger
                .info("======================== ================== ========================");
        }
        Expression expressionObj = super.translate(
            translationName,
            contextElement,
            expression);
        if (logger.isInfoEnabled())
        {
            logger
                .info("========================  Tracing Complete  ========================");
        }
        return expressionObj;
    }

    /**
     * Checks to see if this class needs to be adapated If it has the "adapted"
     * field then we know it already has been adapted.
     * 
     * @return true/false, true if it needs be be adapted.
     */
    protected boolean needsAdaption()
    {
        boolean needsAdaption = false;
        try
        {
            this.getClass().getDeclaredField(FIELD_ADAPTED);
        }
        catch (NoSuchFieldException ex)
        {
            needsAdaption = true;
        }
        return needsAdaption;
    }

    /**
     * Creates and returns the adapted translator class.
     * 
     * @return Class the new Class instance.
     * @throws NotFoundException
     * @throws CannotCompileException
     * @throws IOException
     */
    protected Class getAdaptedTranslationClass()
        throws NotFoundException,
            CannotCompileException,
            IOException
    {

        Class thisClass = this.getClass();
        this.pool = TranslatorClassPool.getPool(thisClass.getClassLoader());

        CtClass ctTranslatorClass = pool.get(thisClass.getName());

        CtField adaptedField = new CtField(
            CtClass.booleanType,
            FIELD_ADAPTED,
            ctTranslatorClass);

        ctTranslatorClass.addField(adaptedField);

        //get the "inA" methods from the analysisClass
        CtMethod[] analysisMethods = ctTranslatorClass.getMethods();

        if (analysisMethods != null)
        {

            int methodNum = analysisMethods.length;

            for (int ctr = 0; ctr < methodNum; ctr++)
            {
                CtMethod method = analysisMethods[ctr];
                String methodName = method.getName();

                if (methodName.startsWith(INA_PREFIX))
                {
                    // add the new overriden "inA" methods
                    this.methods.put(method, this.getInAMethodBody(method));
                }
                else if (methodName.startsWith(OUTA_PREFIX))
                {
                    // add the new overriden "outA" methods
                    this.methods.put(method, this.getOutAMethodBody(method));
                }
                else if (methodName.startsWith(CASE_PREFIX))
                {
                    // add the new overridden "case" methods
                    this.methods.put(method, this.getCaseMethodBody(method));
                }
            }

            //now add all the methods to the class
            Iterator allMethods = this.methods.keySet().iterator();
            while (allMethods.hasNext())
            {
                CtMethod method = (CtMethod)allMethods.next();
                CtMethod newMethod = new CtMethod(
                    method,
                    ctTranslatorClass,
                    null);
                String methodBody = (String)this.methods.get(method);
                newMethod.setBody(methodBody);
                ctTranslatorClass.addMethod(newMethod);
            }

        }
        this.writeAdaptedClass();
        return ctTranslatorClass.toClass();
    }

    /**
     * Writes the class to the directory found by the class loader (since the
     * class is a currently existing class)
     */
    protected void writeAdaptedClass()
    {
        final String methodName = "TraceTranslator.writeAdaptedClass";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + methodName);
        }
        try
        {
            String className = this.getClass().getName();
            File dir = this.getAdaptedClassOutputDirectory();
            if (logger.isDebugEnabled())
            {
                logger.debug("writing className '" + className
                    + "' to directory --> " + "'" + dir + "'");
            }
            this.pool.writeFile(this.getClass().getName(), dir.toString());
        }
        catch (Exception ex)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, ex);
            throw new TranslatorException(errMsg, ex);
        }
    }

    /**
     * Retrieves the output directory which the adapted class will be written
     * to.
     * 
     * @return
     */
    protected File getAdaptedClassOutputDirectory()
    {
        final String methodName = "TraceTranslator.getAdaptedClassOutputDirectory";
        Class thisClass = this.getClass();
        URL classAsResource = ResourceUtils.getClassResource(thisClass
            .getName());
        File file = new File(classAsResource.getFile());
        File dir = file.getParentFile();
        if (dir == null)
        {
            throw new TranslatorException(methodName
                + " - can not retrieve directory for file '" + file + "'");
        }
        String className = thisClass.getName();
        int index = className.indexOf('.');
        String basePackage = null;
        if (index != -1)
        {
            basePackage = className.substring(0, index);
        }
        if (basePackage != null)
        {
            while (!dir.toString().endsWith(basePackage))
            {
                dir = dir.getParentFile();
            }
            dir = dir.getParentFile();
        }
        return dir;
    }

    /**
     * Creates and returns the method body for each "caseA" method
     * 
     * @param method
     * @return String the <code>case</code> method body
     */
    protected String getCaseMethodBody(CtMethod method)
    {
        final String methodDebugName = "TraceTranslator.getCaseAMethodBody";
        ExceptionUtils.checkNull(methodDebugName, "method", method);
        StringBuffer methodBody = new StringBuffer("{");
        String methodName = method.getName();
        methodBody.append("String methodName = \"" + methodName + "\";");
        methodBody.append(this.getMethodTrace(method));
        //add the call of the super class method, so that any methods in sub
        // classes
        //can provide functionality
        methodBody.append("super." + methodName + "($1);");
        methodBody.append("}");
        return methodBody.toString();
    }

    /**
     * Creates and returns the method body for each "inA" method
     * 
     * @param method
     * @return String the <code>inA</code> method body
     */
    protected String getInAMethodBody(CtMethod method)
    {
        final String methodDebugName = "TraceTranslator.getInAMethodBody";
        ExceptionUtils.checkNull(methodDebugName, "method", method);
        StringBuffer methodBody = new StringBuffer("{");
        String methodName = method.getName();
        methodBody.append("String methodName = \"" + methodName + "\";");
        methodBody.append(this.getMethodTrace(method));
        //add the call of the super class method, so that any methods in sub
        // classes
        //can provide functionality
        methodBody.append("super." + methodName + "($1);");
        methodBody.append("}");
        return methodBody.toString();
    }

    /**
     * Creates and returns the method body for each "inA" method
     * 
     * @param method
     * @return String the <code>outA</code> method body.
     */
    protected String getOutAMethodBody(CtMethod method)
    {
        final String methodDebugName = "TraceTranslator.getOutAMethodBody";
        ExceptionUtils.checkNull(methodDebugName, "method", method);
        StringBuffer methodBody = new StringBuffer("{");
        String methodName = method.getName();
        methodBody.append("String methodName = \"" + methodName + "\";");
        methodBody.append(this.getMethodTrace(method));
        //add the call of the super class method, so that any methods in sub
        // classes
        //can provide functionality
        methodBody.append("super." + methodName + "($1);");
        methodBody.append("}");
        return methodBody.toString();
    }

    /**
     * Returns the OCL fragment name that must have a matching name in the
     * library translation template in order to be placed into the Expression
     * translated expression buffer.
     * 
     * @param method
     * @return String
     */
    protected String getOclFragmentName(CtMethod method)
    {
        final String methodName = "TraceTranslator.getOclFragmentName";
        ExceptionUtils.checkNull(methodName, "method", method);
        String fragment = method.getName();
        String prefix = this.getMethodPrefix(method);
        int index = fragment.indexOf(prefix);
        if (index != -1)
        {
            fragment = fragment.substring(index + prefix.length(), fragment
                .length());
        }
        return fragment;
    }

    /**
     * Returns the prefix for the method (inA or outA)
     * 
     * @param method
     * @return
     */
    protected String getMethodPrefix(CtMethod method)
    {
        final String methodName = "TraceTranslator.getMethodPrefix";
        ExceptionUtils.checkNull(methodName, "method", method);
        String mName = method.getName();
        String prefix = INA_PREFIX;
        if (mName.startsWith(OUTA_PREFIX))
        {
            prefix = OUTA_PREFIX;
        }
        return prefix;
    }

    /**
     * Creates the debug statement that will be output for each method
     * 
     * @param method
     * @return @throws NotFoundException
     */
    protected String getMethodTrace(CtMethod method)
    {
        final String methodName = "TraceTranslator.getMethodDebug";
        ExceptionUtils.checkNull(methodName, "method", method);
        StringBuffer buf = new StringBuffer(
            "if (logger.isInfoEnabled()) {logger.info(\"");
        buf.append("\" + methodName + \" --> ");
        //javaassist names the arguments $1,$2,$3, etc.
        buf
            .append("'\" + org.andromda.core.translation.TranslationUtils.trimToEmpty($1) + \"'\");}");
        return buf.toString();
    }

    /**
     * Extends the Javaassist class pool so that we can define our own
     * ClassLoader to use from which to find, load and modify and existing
     * class.
     * 
     * @author Chad Brandon
     */
    private static class TranslatorClassPool
        extends ClassPool
    {

        private static Logger logger = Logger
            .getLogger(TranslatorClassPool.class);

        protected TranslatorClassPool()
        {
            super(ClassPool.getDefault());
            if (logger.isInfoEnabled())
            {
                logger.debug("instantiating new TranslatorClassPool");
            }
        }

        /**
         * Retrieves an instance of this TranslatorClassPool using the loader to
         * find/load any classes.
         * 
         * @param loader
         * @return
         */
        protected static ClassPool getPool(ClassLoader loader)
        {
            if (loader == null)
            {
                loader = Thread.currentThread().getContextClassLoader();
            }
            TranslatorClassPool pool = new TranslatorClassPool();
            pool.insertClassPath(new LoaderClassPath(loader));
            return pool;
        }

        /**
         * Returns a <code>java.lang.Class</code> object. It calls
         * <code>write()</code> to obtain a class file and then loads the
         * obtained class file into the JVM. The returned <code>Class</code>
         * object represents the loaded class.
         * <p>
         * To load a class file, this method uses an internal class loader.
         * Thus, that class file is not loaded by the system class loader, which
         * should have loaded this <code>AspectClassPool</code> class. The
         * internal class loader loads only the classes explicitly specified by
         * this method <code>writeAsClass()</code>. The other classes are
         * loaded by the parent class loader (the sytem class loader) by
         * delegation. Thus, if a class <code>X</code> loaded by the internal
         * class loader refers to a class <code>Y</code>, then the class
         * <code>Y</code> is loaded by the parent class loader.
         * 
         * @param classname a fully-qualified class name.
         * @return Class the Class it writes.
         * @throws NotFoundException
         * @throws IOException
         * @throws CannotCompileException
         */
        public Class writeAsClass(String classname)
            throws NotFoundException,
                IOException,
                CannotCompileException
        {
            try
            {
                return classLoader.loadClass(classname, write(classname));
            }
            catch (ClassFormatError e)
            {
                throw new CannotCompileException(e, classname);
            }
        }

        /**
         * LocalClassLoader which allows us to dynamically construct classes on
         * the fly using Javassist.
         */
        static class LocalClassLoader
            extends ClassLoader
        {
            /**
             * Constructs an instance of LocalClassLoader.
             * 
             * @param parent
             */
            public LocalClassLoader(
                ClassLoader parent)
            {
                super(parent);
            }

            /**
             * Loads a class.
             * 
             * @param name the name
             * @param classfile the bytes of the class.
             * @return Class
             * @throws ClassFormatError
             */
            public Class loadClass(String name, byte[] classfile)
                throws ClassFormatError
            {
                Class c = defineClass(name, classfile, 0, classfile.length);
                resolveClass(c);
                return c;
            }
        }

        /**
         * Create the LocalClassLoader and specify the ClassLoader for this
         * class as the parent ClassLoader. This allows classes defined outside
         * this LocalClassLoader to be loaded (i.e. classes that already exist,
         * and aren't being dynamically created
         */
        private static LocalClassLoader classLoader = new LocalClassLoader(
            LocalClassLoader.class.getClassLoader());
    }

    /**
     * This method is called by the main method during the build process, to
     * "adapt" the class to the OCL parser.
     */
    protected static void adaptClass()
    {
        if (logger.isInfoEnabled())
        {
            logger.info("adapting class for OCL parser");
        }
        TraceTranslator translator = new TraceTranslator();
        if (translator.needsAdaption())
        {
            try
            {
                translator.getAdaptedTranslationClass();
            }
            catch (Throwable th)
            {
                logger.error(th);
            }
        }
    }

    /**
     * This main method is called during the build process, to "adapt" the class
     * to the OCL parser.
     * 
     * @param args
     */
    public static void main(String args[])
    {
        try
        {
            AndroMDALogger.configure();
            TraceTranslator.adaptClass();
        }
        catch (Throwable th)
        {
            logger.error(th);
        }
    }

}