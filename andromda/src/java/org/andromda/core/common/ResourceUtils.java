package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Provides utilities for loading resources.
 * 
 * @author Chad Brandon
 */
public class ResourceUtils {
	
	private static final Logger logger = Logger.getLogger(ResourceUtils.class);
	
	/**
	 * Retrieves a resource from the class package
	 * @param resourceName the name of the resource
	 * @return java.net.URL
	 */
	public static URL getResource(String resourceName) {
		final String methodName = "ResourceUtils.getResource";
		if (logger.isDebugEnabled())
			logger.debug("performing '" + methodName 
				+ "' with resourceName '" + resourceName + "'");	
		ExceptionUtils.checkEmpty(methodName, "resourceName", resourceName);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();		
		URL resource = loader.getResource(resourceName);
		return resource;
	}	
    
    /**
     * Loads the file resource and returns the contents
     * as a String.
     * @param resourceName the name of the resource.
     * @return String
     */
    public static String getContents(URL resource) {
        final String methodName = "ResourceUtils.getContents";
        if (logger.isDebugEnabled())
            logger.debug("performing " + methodName
                + " with resource '" + resource + "'");
        StringBuffer contents = new StringBuffer();
        try {   
            if (resource != null) {
                BufferedReader in = 
                    new BufferedReader(
                        new InputStreamReader(resource.openStream()));
                String line;
                while ((line = in.readLine()) != null){
                    contents.append(line + "\n");
                }
                in.close();
            }
        } catch (Exception ex) {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, ex);
            throw new RuntimeException(errMsg, ex);
        }
        return contents.toString();
    }

	/**
	 * Loads the file resource and returns the contents
	 * as a String.
     * @param resourceName the name of the resource.
	 * @return String
	 */
	public static String getContents(String resourceName) {
		return getContents(getResource(resourceName));
	}
    /**
     * Takes a className as an argument and returns the URL for the 
     * class.  
     * @param className
     * @return java.net.URL
     */
    public static URL getClassResource(String className) {
        final String methodName = "ResourceUtils.getClassResource";
        ExceptionUtils.checkEmpty(methodName, "className", className);
        return getResource(getClassNameAsResource(className));
    }
    
    /**
     * Private helper method.
     * @param className
     * @return String
     */
    private static String getClassNameAsResource(String className) {
        return className.replace('.', '/') + ".class";
    }
    
}
