package org.andromda.core.common;

import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;

/**
 * Conext passed from the core to a cartridge
 * when code has to be generated.
 * 
 * @since 28.07.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 *
 */
public class CodeGenerationContext
{
    private RepositoryFacade repository = null;
    private boolean lastModifiedCheck = false;
    private ModelPackages modelPackages;

    public CodeGenerationContext(
        RepositoryFacade rf,
        boolean lastModifiedCheck,
		ModelPackages modelPackages)
    {
        this.repository = rf;
        this.lastModifiedCheck = lastModifiedCheck;
        this.modelPackages = modelPackages;
    }

    /**
     * Returns the repository.
     * @return RepositoryFacade
     */
    public RepositoryFacade getRepository()
    {
        return repository;
    }

    /**
     * Sets the repository.
     * @param repository The repository to set
     */
    public void setRepository(RepositoryFacade repository)
    {
        this.repository = repository;
    }

    /**
     * Returns the model facade for this code generation step.
     * @return the model facade
     */
    public ModelAccessFacade getModelFacade()
    {
        return this.getRepository().getModel();
    }

    /**
     * Returns the lastModifiedCheck.
     * @return boolean
     */
    public boolean isLastModifiedCheck()
    {
        return lastModifiedCheck;
    }

    /**
     * Sets the lastModifiedCheck.
     * @param lastModifiedCheck The lastModifiedCheck to set
     */
    public void setLastModifiedCheck(boolean lastModifiedCheck)
    {
        this.lastModifiedCheck = lastModifiedCheck;
    }
    
	/**
	 * Gets the model packages that should/shouldn't
	 * be processed.  
	 * 
	 * @return Returns the modelPackages.
	 */
	public ModelPackages getModelPackages() 
	{
		return this.modelPackages;
	}

	/**
	 * Sets modelPackages.
	 * 
	 * @param modelPackages The modelPackages to set.
	 */
	public void setModelPackages(ModelPackages modelPackages) 
	{
		this.modelPackages = modelPackages;
	}

}
