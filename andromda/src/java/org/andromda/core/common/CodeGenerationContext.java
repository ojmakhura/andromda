package org.andromda.core.common;

import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;

/**
 * Conext passed from the core to a cartridge when code has to be generated.
 * 
 * @since 28.07.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class CodeGenerationContext
{
    private RepositoryFacade repository = null;
    long lastModified;
    private boolean lastModifiedCheck = false;
    private ModelPackages modelPackages;

    public CodeGenerationContext(
        RepositoryFacade repository,
        long lastModified,
        boolean lastModifiedCheck,
        ModelPackages modelPackages)
    {
        this.repository = repository;
        this.lastModifiedCheck = lastModifiedCheck;
        this.modelPackages = modelPackages;
    }

    /**
     * Gets <code>lastModified</code> for this context.
     * 
     * @return RepositoryFacade
     */
    public long getLastModified()
    {
        return this.lastModified;
    }

    /**
     * Returns the model facade for this code generation step.
     * 
     * @return the model facade
     */
    public ModelAccessFacade getModelFacade()
    {
        return this.repository.getModel();
    }

    /**
     * Returns the lastModifiedCheck.
     * 
     * @return boolean
     */
    public boolean isLastModifiedCheck()
    {
        return lastModifiedCheck;
    }

    /**
     * Gets the model packages that should/shouldn't be processed.
     * 
     * @return Returns the modelPackages.
     */
    public ModelPackages getModelPackages()
    {
        return this.modelPackages;
    }
}