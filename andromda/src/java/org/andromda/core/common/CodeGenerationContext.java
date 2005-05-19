package org.andromda.core.common;

import org.andromda.core.configuration.ModelPackages;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacade;

/**
 * Conext passed from the core to a cartridge when code has to be generated.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @since 28.07.2003
 */
public class CodeGenerationContext
{
    private RepositoryFacade repository = null;
    private ModelPackages modelPackages;

    public CodeGenerationContext(final RepositoryFacade repository, final ModelPackages modelPackages)
    {
        this.repository = repository;
        this.modelPackages = modelPackages;
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
     * Gets the model packages that should/shouldn't be processed.
     *
     * @return Returns the modelPackages.
     */
    public ModelPackages getModelPackages()
    {
        return this.modelPackages;
    }
}