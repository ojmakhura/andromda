package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Stores information about all ModelPackage instances
 * that should or should not be processed.  This is useful 
 * if you need to reference stereotyped model
 * elements from other packages but you don't want
 * to generate elements from them. 
 * 
 * @author Chad Brandon
 * 
 * @see org.andromda.core.common.ModelPackage
 */
public class ModelPackages {
	
	private boolean processAllModelPackages = true;
	
	private Map modelPackages;
	
	private Collection initCollection = new ArrayList();
	
	/**
	 * This method normally be unnecessary. It is here because of the way Ant behaves.
	 * Ant calls addModelPackage(ModelPackage) before the ModelPackage is fully
	 * initialized (therefore the 'name' isn't set). So we kept the modelPackages in an 
	 * ArrayList that we have to copy into the modelPackages Map.
	 */
	private void init() {
		if (this.modelPackages == null) {
			this.modelPackages = new HashMap();
			for (Iterator iter = initCollection.iterator(); iter.hasNext();) {
				ModelPackage modelPackage = (ModelPackage)iter.next();
				this.modelPackages.put(
					modelPackage.getName(), 
					new Boolean(modelPackage.isShouldProcess()));
			}	
		}	
	}
	
	/**
	 * Set true/false whether all modelPackages should be 
	 * processed.
	 * 
	 * @param processAllModelPackages
	 */
	public void setProcessAllPackages(boolean processAllModelPackages) {
		this.processAllModelPackages = processAllModelPackages;
	}

	/**
	 * Sets the packageName and whether or not it should
	 * be processed.
	 *  
	 * @param packageName the name of the package.
	 * @param process true/false on whether or not it should
	 *        be processed.
	 */
	public void addPackage(ModelPackage modelPackage) {
		this.initCollection.add(modelPackage);
	}

	/**
	 * Determines whether or not the <code>packageName</code>
	 * should be processed.  If <code>processAllModelPackages</code>
	 * is true, then this method will return false only if the ModelPackage
	 * corresponding to the <code>packageName</code> has shouldProcess
	 * set to true. 
	 * 
	 * @param packageName the name of the model package to check.
	 * 
	 * @return boolean
	 */
	public boolean shouldProcess(String packageName) {
		boolean shouldProcess = this.processAllModelPackages;
		this.init();
		Boolean process = (Boolean)modelPackages.get(packageName);
		if (process != null) {
			shouldProcess = process.booleanValue();
		}
		return shouldProcess;
	}

}
