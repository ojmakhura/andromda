package org.andromda.core.common;

import java.util.Collection;

import org.andromda.cartridges.interfaces.OutletDictionary;
import org.apache.velocity.app.VelocityEngine;

/**
 * Conext passed from the core to a cartridge
 * when code has to be generated.
 * 
 * @since 28.07.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class CodeGenerationContext
{
    private RepositoryFacade repository = null;
    private ScriptHelper scriptHelper = null;
    private DbMappingTable typeMappings = null;
    private OutletDictionary outletDictionary = null;
    private boolean lastModifiedCheck = false;
    private Collection userProperties = null;

    public CodeGenerationContext(
        RepositoryFacade rf,
        ScriptHelper sh,
        DbMappingTable typeMappings,
        OutletDictionary outletDictionary,
        boolean lastModifiedCheck,
        Collection userPropeties)
    {
        this.repository = rf;
        this.scriptHelper = sh;
        this.typeMappings = typeMappings;
        this.outletDictionary = outletDictionary;
        this.lastModifiedCheck = lastModifiedCheck;
        this.userProperties = userPropeties;
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
     * Returns the scriptHelper.
     * @return ScriptHelper
     */
    public ScriptHelper getScriptHelper()
    {
        return scriptHelper;
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
     * Sets the scriptHelper.
     * @param scriptHelper The scriptHelper to set
     */
    public void setScriptHelper(ScriptHelper scriptHelper)
    {
        this.scriptHelper = scriptHelper;
    }

    /**
     * Returns the typeMappings.
     * @return DbMappingTable
     */
    public DbMappingTable getTypeMappings()
    {
        return typeMappings;
    }

    /**
     * Sets the typeMappings.
     * @param typeMappings The typeMappings to set
     */
    public void setTypeMappings(DbMappingTable typeMappings)
    {
        this.typeMappings = typeMappings;
    }

    /**
     * Returns the outletDictionary.
     * @return OutletDictionary
     */
    public OutletDictionary getOutletDictionary()
    {
        return outletDictionary;
    }

    /**
     * Sets the outletDictionary.
     * @param outletDictionary The outletDictionary to set
     */
    public void setOutletDictionary(OutletDictionary outletDictionary)
    {
        this.outletDictionary = outletDictionary;
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
     * Returns the userProperties.
     * @return Collection
     */
    public Collection getUserProperties()
    {
        return userProperties;
    }

    /**
     * Sets the userProperties.
     * @param userProperties The userProperties to set
     */
    public void setUserProperties(Collection userProperties)
    {
        this.userProperties = userProperties;
    }
}
