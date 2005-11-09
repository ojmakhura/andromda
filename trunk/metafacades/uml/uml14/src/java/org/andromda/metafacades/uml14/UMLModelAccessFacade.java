package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.Filters;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;


/**
 * Contains a UML model, follows the {@link ModelAccessFacade} interface and can therefore be processed by AndroMDA.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class UMLModelAccessFacade
    implements ModelAccessFacade
{
    private static final Logger logger = Logger.getLogger(UMLModelAccessFacade.class);
    private UmlPackage model;

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setModel(java.lang.Object)
     */
    public void setModel(final Object model)
    {
        ExceptionUtils.checkNull("model", model);
        ExceptionUtils.checkAssignable(
            UmlPackage.class,
            "modelElement",
            model.getClass());
        this.model = (UmlPackage)model;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModel()
     */
    public Object getModel()
    {
        return model;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getName(java.lang.Object)
     */
    public String getName(final Object modelElement)
    {
        ExceptionUtils.checkNull("modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        return ((ModelElementFacade)modelElement).getName();
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getPackageName(java.lang.Object)
     */
    public String getPackageName(final Object modelElement)
    {
        ExceptionUtils.checkNull("modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        final ModelElementFacade modelElementFacade = (ModelElementFacade)modelElement;
        final StringBuffer packageName = new StringBuffer(modelElementFacade.getPackageName(true));

        // - if the model element is a package then the package name will be the name 
        //   of the package with its package name
        if (modelElement instanceof PackageFacade)
        {
            final String name = modelElementFacade.getName();
            if (StringUtils.isNotBlank(name))
            {
                packageName.append(MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
                packageName.append(name);
            }
        }
        return packageName.toString();
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getStereotypeNames(java.lang.Object)
     */
    public Collection getStereotypeNames(final Object modelElement)
    {
        Collection stereotypeNames = new ArrayList();
        if (modelElement instanceof ModelElement)
        {
            ModelElement element = (ModelElement)modelElement;
            Collection stereotypes = element.getStereotype();
            for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
            {
                ModelElement stereotype = (ModelElement)iterator.next();
                stereotypeNames.add(stereotype.getName());
            }
        }
        else if (modelElement instanceof ModelElementFacade)
        {
            stereotypeNames = ((ModelElementFacade)modelElement).getStereotypeNames();
        }
        return stereotypeNames;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#findByStereotype(java.lang.String)
     */
    public Collection findByStereotype(String stereotype)
    {
        final String methodName = "UMLModelAccessFacade.findByStereotype";
        final Collection metafacades = new ArrayList();
        stereotype = StringUtils.trimToEmpty(stereotype);
        if (StringUtils.isNotEmpty(stereotype))
        {
            if (this.model != null)
            {
                final Collection underlyingElements = model.getCore().getModelElement().refAllOfType();
                if (underlyingElements != null || !underlyingElements.isEmpty())
                {
                    for (final Iterator iterator = underlyingElements.iterator(); iterator.hasNext();)
                    {
                        ModelElement element = (ModelElement)iterator.next();
                        Collection stereotypeNames = this.getStereotypeNames(element);
                        if (stereotypeNames != null && stereotypeNames.contains(stereotype))
                        {
                            metafacades.add(MetafacadeFactory.getInstance().createMetafacade(element));
                        }
                    }
                }
                if (logger.isDebugEnabled())
                {
                    logger.debug("completed " + methodName + " with " + metafacades.size() + " modelElements");
                }
            }
            this.filterMetafacades(metafacades);
        }
        return metafacades;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModelElements()
     */
    public Collection getModelElements()
    {
        Collection metafacades = Collections.EMPTY_LIST;
        if (this.model != null)
        {
            metafacades =
                MetafacadeFactory.getInstance().createMetafacades(
                    this.model.getCore().getModelElement().refAllOfType());
            this.filterMetafacades(metafacades);
        }
        return metafacades;
    }

    /**
     * Stores the package filter information.  Protected
     * visibility for better inner class access performance.
     */
    protected Filters modelPackages;

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setPackageFilter(org.andromda.core.configuration.Filters)
     */
    public void setPackageFilter(final Filters modelPackages)
    {
        this.modelPackages = modelPackages;
    }

    /**
     * Filters out those metafacades which <strong>should </strong> be processed.
     *
     * @param modelElements the Collection of modelElements.
     */
    private final void filterMetafacades(final Collection metafacades)
    {
        if (this.modelPackages != null && !this.modelPackages.isEmpty())
        {
            CollectionUtils.filter(
                metafacades,
                new Predicate()
                {
                    public boolean evaluate(final Object metafacade)
                    {
                        boolean valid = false;
                        if (metafacade instanceof MetafacadeBase)
                        {
                            final ModelElementFacade modelElementFacade = (ModelElementFacade)metafacade;
                            final StringBuffer packageName = new StringBuffer(modelElementFacade.getPackageName(true));

                            // - if the model element is a package then the package name will be the name 
                            //   of the package with its package name
                            if (metafacade instanceof PackageFacade)
                            {
                                final String name = modelElementFacade.getName();
                                if (StringUtils.isNotBlank(name))
                                {
                                    packageName.append(MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
                                    packageName.append(name);
                                }
                            }
                            valid = modelPackages.isApply(packageName.toString());
                        }
                        return valid;
                    }
                });
        }
    }
}