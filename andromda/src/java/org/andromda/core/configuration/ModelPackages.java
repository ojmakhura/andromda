package org.andromda.core.configuration;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Stores information about all ModelPackage instances that should or should not be processed. This is useful if you
 * need to reference stereotyped model elements from other packages but you don't want to generate elements from them.
 *
 * @author Chad Brandon
 * @see org.andromda.core.configuration.ModelPackage
 */
public class ModelPackages
    implements Serializable
{
    /**
     * The flag indicating whether or not all model packages
     * should be processed.
     */
    private boolean processAll = true;

    /**
     * Stores the model packages.
     */
    private Map modelPackages;

    /**
     * Sets whether or not AndroMDA should process all packages on a model. If this is set to true, then package elements should be
     * specified if you want to keep certain packages from being processed. If this is set to false, then you would want
     * to define package elements to specify which packages <strong>SHOULD BE </strong> processed. This is useful if you
     * need to reference model elements from other packages but you don't want to perform any generation from them. The
     * default is <strong>true </strong>.
     *
     * @param processAll whether or not we should process all packages true/false
     */
    public void setProcessAll(final boolean processAll)
    {
        this.processAll = processAll;
    }

    /**
     * Stores the packages as they're added.
     */
    private final Collection packages = new ArrayList();

    /**
     * Sets the packageName and whether or not it should be processed.
     *
     * @param modelPackage the ModelPackage instance.
     */
    public void addPackage(final ModelPackage modelPackage)
    {
        this.packages.add(modelPackage);
    }

    /**
     * Adds all ModelPackages in the given <code>modelPackages</code> to this ModelPackages instance.
     *
     * @param modelPackages the ModelPackages instance to add.
     */
    public void addPackages(final ModelPackages modelPackages)
    {
        if (modelPackages != null)
        {
            this.initialize();
            modelPackages.initialize();
            this.modelPackages.putAll(modelPackages.modelPackages);
        }
    }

    /**
     * Determines whether or not the <code>packageName</code> should be processed. If
     * <code>processAllModelPackages</code> is true, then this method will return false only if the ModelPackage
     * corresponding to the <code>packageName</code> has shouldProcess set to false.
     *
     * @param packageName the name of the model package to check.
     * @return boolean
     */
    public boolean isProcess(final String packageName)
    {
        boolean shouldProcess = this.processAll;
        this.initialize();
        Boolean process = (Boolean)modelPackages.get(packageName);
        if (process != null)
        {
            shouldProcess = process.booleanValue();
        }
        return shouldProcess;
    }

    /**
     * This method normally be unnecessary. It is here because of the way Ant behaves. Ant calls
     * addModelPackage(ModelPackage) before the ModelPackage is fully initialized (therefore the 'name' isn't set). So
     * we kept the modelPackages in an ArrayList that we have to copy into the modelPackages Map.
     */
    private final void initialize()
    {
        if (this.modelPackages == null)
        {
            this.modelPackages = new HashMap();
            for (final Iterator iterator = packages.iterator(); iterator.hasNext();)
            {
                ModelPackage modelPackage = (ModelPackage)iterator.next();
                this.modelPackages.put(
                    modelPackage.getName(),
                    new Boolean(modelPackage.isProcess()));
            }
        }
    }
}