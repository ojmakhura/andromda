package org.andromda.core.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import org.apache.log4j.Logger;

/**
 * Finds and loads file resources from the current classpath.
 * 
 * @author Chad Brandon
 */
public class ResourceFinder {
	
	private static Logger logger = Logger.getLogger(ResourceFinder.class);

	private ResourceFinder() {}

	/**
	 * Returns a URL[] containing the URL of each
	 * resource and the File which represents the library
	 * the resource was found in.
	 *
	 * @param resource the resource to find
	 * 
	 * @return a <code>array of resource URLs<code>
	 */
	public static URL[] findResources(String resource) {
		final String methodName = "ResourceFinder.findResource";
		if (logger.isDebugEnabled())
			logger.debug("performing " + methodName);
		ExceptionUtils.checkEmpty(methodName, "resource", resource);

		URL[] resourceUrls;
		
		try {
		
			Collection resources = new ArrayList();
		
			Enumeration resourceEnum = 
				Thread.currentThread().getContextClassLoader().getResources(
					resource);
			while (resourceEnum.hasMoreElements()) {
				resources.add(resourceEnum.nextElement());
			}
			
			resourceUrls = (URL[])resources.toArray(new URL[0]);
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName;
			logger.error(errMsg, ex);
			throw new ResourceFinderException(errMsg, ex);
		}

		return resourceUrls;
	}

}
