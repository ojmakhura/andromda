package org.andromda.core.common;

/**
 * Used to specify which packages should
 * or should not be processed within the model. 
 * This is useful if you need to reference stereotyped model
 * elements from other packages but you don't want
 * to generate elements from them. 
 * 
 * @author Chad Brandon
 * 
 * @see org.andromda.core.common.ModelPackages
 */
public class ModelPackage {

	private String name;
	private boolean shouldProcess;

	/**
	 * Gets the name of this ModelPackage.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this ModelPackage.
	 * 
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Whether or not this ModelPackage
	 * should be processed.
	 * 
	 * @return Returns the shouldProcess.
	 */
	public boolean isShouldProcess() {
		return shouldProcess;
	}

	/**
	 * Sets whether or not this ModelPackage should
	 * be processed.
	 * 
	 * @param shouldProcess The shouldProcess to set.
	 */
	public void setShouldProcess(boolean shouldProcess) {
		this.shouldProcess = shouldProcess;
	}

}
