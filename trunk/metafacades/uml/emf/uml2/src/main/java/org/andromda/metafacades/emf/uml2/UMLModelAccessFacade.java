package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.Filters;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;

/**
 * Model access facade implementation for the EMF/UML2
 * metafacades
 * 
 * @author Steve Jerman
 * @author Chad Brandon
 */
public class UMLModelAccessFacade
    implements ModelAccessFacade
{
    /**
     * Protected to improve performance.
     */
    protected Filters modelPackages = new Filters();

    /**
     * Set the current model.
     */
    public void setModel(Object model)
    {
        ExceptionUtils.checkNull(
            "model",
            model);
        ExceptionUtils.checkAssignable(
            Model.class,
            "modelElement",
            model.getClass());
        this.theModel = (Model)model;

        if (model instanceof Model)
        {
            theModel = (Model)model;
        }
        else
        {
            throw new MetafacadeException("Object is not an instance of org.eclipse.uml2.Model");
        }
    }

    /**
     * Get a wrapped version of the model.
     */
    public Object getModel()
    {
        return theModel;
    }

    /**
     * Get the name of the passed ModelElement
     */
    public String getName(Object modelElement)
    {
        ExceptionUtils.checkNull(
            "modelElement",
            modelElement);
        ExceptionUtils.checkAssignable(
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        return ((ModelElementFacade)modelElement).getName();
    }

    /**
     * Get the name of the enclosing package
     */
    public String getPackageName(Object modelElement)
    {
        ExceptionUtils.checkNull(
            "modelElement",
            modelElement);
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
     * Package filter set up... ignored for now.
     */
    public void setPackageFilter(Filters modelPackages)
    {
        this.modelPackages = modelPackages;
    }

    /**
     *
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        if (!(modelElement instanceof NamedElement))
        {
            throw new MetafacadeException("argument is not a org.eclipse.uml2.Element : " + modelElement);
        }
        NamedElement el = (NamedElement)modelElement;
        Collection names = UmlUtilities.getStereotypeNames(el);
        return names;
    }

    /**
     * find by stereotype ... case insensitive
     */
    public Collection findByStereotype(String name)
    {
        final ArrayList elements = new ArrayList();
        for (TreeIterator iterator = theModel.eAllContents(); iterator.hasNext();)
        {
            EObject object = (EObject)iterator.next();
            if (object instanceof NamedElement)
            {
                final NamedElement element = (NamedElement)object;
                if (UmlUtilities.containsStereotype(
                        element,
                        name))
                {
                    elements.add(MetafacadeFactory.getInstance().createMetafacade(element));
                }
            }
        }
        return elements;
    }

    /**
     * Return a collection of modelelements filtered by filterpackages
     */
    public Collection getModelElements()
    {
        Collection metafacades = Collections.EMPTY_LIST;
        ArrayList elements = new ArrayList();
        for (TreeIterator i = theModel.eAllContents(); i.hasNext();)
        {
            EObject e = (EObject)i.next();
            if (e instanceof NamedElement)
            {
                elements.add(e);
            }
        }
        if (elements != null)
        {
            metafacades = MetafacadeFactory.getInstance().createMetafacades(elements);
            this.filterMetafacades(metafacades);
        }
        return metafacades;
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

    Model theModel;
}