package org.andromda.core.common;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains utilities for dealing with classes.
 * 
 * @author Chad Brandon   
 */
public class ClassUtils extends org.apache.commons.lang.ClassUtils {
	
	private static Logger logger = Logger.getLogger(ClassUtils.class);
	
	/**
	 * Loads and returns the class having the className. Will load
	 * but normal classes and the classes representing primatives. 
	 * 
	 * @param className the name of the class to load.
	 * @return Class the loaded class
	 * @throws ClassNotFoundException if the class can not be found
	 * @throws NoSuchFieldException if the className is a primative and its corresponding
	 *         type field can not be retrieved.
	 * @throws IllegalAccessException if the className is a primative and its corresponding
	 *         type field is illegal accessed.
	 */
	public static Class loadClass(String className) 
		throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
		final String methodName = "ClassUtils.loadClass";
		ExceptionUtils.checkEmpty(methodName, "className", className);
		className = StringUtils.trimToNull(className);
		//get rid of any array notation
		className = StringUtils.replace(className, "[]", "");
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Class loadedClass  = null;
		//check and see if its a primitive and if so convert it
		if (ClassUtils.isPrimitiveType(className)) {
			loadedClass = getPrimitiveClass(className, loader);
		} else {
			loadedClass = loader.loadClass(className);
		}
		
		if (loader == null) {
			loader = ClassUtils.class.getClassLoader();
			Thread.currentThread().setContextClassLoader(loader);
		}
		
		return loadedClass;
	}
	
	/**
	 * <p>Returns the type class name for a Java primitive.</p>
	 *
	 * @param name a <code>String</code> with the name of the type
	 * @param loader the loader to use.
	 * @return a <code>String</code> with the name of the
	 *         corresponding java.lang wrapper class if
	 *         <code>name</code> is a Java primitive type;
	 *         <code>false</code> if not
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	protected static Class getPrimitiveClass(String name, ClassLoader loader)
		throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
		final String methodName = "ClassUtils.getPrimitiveClass";
		ExceptionUtils.checkEmpty(methodName, "name", name);
		ExceptionUtils.checkNull(methodName, "loader", loader);
		
		Class primitiveClass = null;
		if (isPrimitiveType(name) && !name.equals("void")) {
			String className = null;
			if ("char".equals(name)) {
				className = "java.lang.Character";
			}
			if ("int".equals(name)) {
				className = "java.lang.Integer";
			}

			className = "java.lang." + StringUtils.capitalize(name);
			
			try {
				if (StringUtils.isNotEmpty(className)) {
					Field field = loader.loadClass(className).getField("TYPE");
					primitiveClass = (Class) field.get(null);
				}
			} catch (Exception ex) {
				String errMsg = "Error performing " + methodName;
				logger.error(errMsg, ex);
			}
		}
		return primitiveClass;
	}
	
	/**
	 * <p>Checks if a given type name is a Java primitive type.</p>
	 *
	 * @param name a <code>String</code> with the name of the type
	 * @return <code>true</code> if <code>name</code> is a Java
	 *         primitive type; <code>false</code> if not
	 */
	protected static boolean isPrimitiveType(String name) {
		return (
			"void".equals(name)
				|| "char".equals(name)
				|| "byte".equals(name)
				|| "short".equals(name)
				|| "int".equals(name)
				|| "long".equals(name)
				|| "float".equals(name)
				|| "double".equals(name)
				|| "boolean".equals(name));
	}
	
}
