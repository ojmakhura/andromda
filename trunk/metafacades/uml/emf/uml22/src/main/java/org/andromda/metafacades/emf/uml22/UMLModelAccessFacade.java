package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.Filters;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * Model access facade implementation for the EMF/UML2 metafacades
 *
 * @author Steve Jerman
 * @author Chad Brandon
 * @author Bob Fields (Multiple model support)
 */
public class UMLModelAccessFacade
    implements ModelAccessFacade
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(UMLModelAccessFacade.class);

    /**
     * Protected to improve performance.
     */
    protected Filters modelPackages = new Filters();

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setModel(Object)
     */
    public void setModel(final Object modelIn)
    {
        ExceptionUtils.checkNull(
            "model",
            modelIn);
        ExceptionUtils.checkAssignable(
            ArrayList.class,
            "modelElement",
            modelIn.getClass());
        if (this.model==null)
        {
            this.model = new ArrayList<UMLResource>();
        }
        for (UMLResource modelResource : (ArrayList<UMLResource>)modelIn)
        {
            if (!this.model.contains(modelResource))
            {
                this.model.add(modelResource);
            }
        }
        // TODO: - clear the meta objects cache (yes this is a performance
        //       hack that at some point should be improved).
        UmlUtilities.clearAllMetaObjectsCache();
        // TODO: - clears out the foreign key cache (again probably not
        //         the cleanest way, but the easiest at this point).
        EntityMetafacadeUtils.clearForeignKeyConstraintNameCache();
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModel()
     */
    public Object getModel()
    {
        return this.model;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getName(Object)
     */
    public String getName(final Object modelElement)
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
     * @see org.andromda.core.metafacade.ModelAccessFacade#getPackageName(Object)
     */
    public String getPackageName(final Object modelElement)
    {
        ExceptionUtils.checkNull(
            "modelElement",
            modelElement);
        ExceptionUtils.checkAssignable(
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        final ModelElementFacade modelElementFacade = (ModelElementFacade)modelElement;
        final StringBuilder packageName = new StringBuilder(modelElementFacade.getPackageName(true));

        // - if the model element is a package then the package name will be the
        // name of the package with its package name
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
     * The actual underlying model instance.
     */
    private List<UMLResource> model;

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setPackageFilter(org.andromda.core.configuration.Filters)
     */
    public void setPackageFilter(final Filters modelPackagesIn)
    {
        this.modelPackages = modelPackagesIn;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getStereotypeNames(Object)
     */
    public Collection<String> getStereotypeNames(final Object modelElement)
    {
        ExceptionUtils.checkNull(
            "modelElement",
            modelElement);

        Collection<String> stereotypeNames = Collections.EMPTY_LIST;
        if (modelElement instanceof Element)
        {
            Element element = (Element)modelElement;
            stereotypeNames = UmlUtilities.getStereotypeNames(element);
        }
        else if (modelElement instanceof ModelElementFacade)
        {
            stereotypeNames = ((ModelElementFacade)modelElement).getStereotypeNames();
        }
        return stereotypeNames;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#findByStereotype(String)
     */
    public Collection<NamedElement> findByStereotype(final String name)
    {
        final List<NamedElement> elements = new ArrayList();
        for (UMLResource modelResource : this.model)
        {
            // TODO UmlUtilities.findModel() can return null. Check for null return value.
            for (TreeIterator<EObject> iterator = UmlUtilities.findModel(modelResource).eAllContents(); iterator.hasNext();)
            {
                final EObject object = (EObject)iterator.next();
                if (object instanceof NamedElement)
                {
                    final NamedElement element = (NamedElement)object;
                    if (UmlUtilities.containsStereotype(
                            element,
                            name))
                    {
                        elements.add(element);
                    }
                }
            }
        }
        int elementSize = elements.size();
        logger.debug("Finding stereotype <<" + name + ">> in elements " +
                elements.size());
        // - filter any elements before we turn them into metafacades (for performance reasons)
        this.filterElements(elements);
        if (elements.size()<elementSize)
        {
            logger.debug("Filtered out " + (elementSize-elements.size()) + " elements");
        }
        MetafacadeFactory.getInstance().createMetafacades(elements);
        return elements;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModelElements()
     */
    public Collection<NamedElement> getModelElements()
    {
        final List<NamedElement> elements = new ArrayList();

        for (UMLResource modelResource : this.model)
        {
            // TODO UmlUtilities.findModel() can return null. Check for null return value.
            for (final Iterator<EObject> iterator = UmlUtilities.findModel(modelResource).eAllContents(); iterator.hasNext();)
            {
                final EObject object = (EObject)iterator.next();
                if (object instanceof NamedElement)
                {
                    elements.add((NamedElement) object);
                }
            }
        }

        // - filter any elements before we turn them into metafacades (for performance reasons)
        this.filterElements(elements);

        // Don't forget to transform properties
        CollectionUtils.transform(
            elements,
            UmlUtilities.ELEMENT_TRANSFORMER);

        final Collection metafacades;

        if (elements.isEmpty())
        {
            metafacades = Collections.EMPTY_LIST;
        }
        else
        {
            metafacades = MetafacadeFactory.getInstance().createMetafacades(elements);
        }

        return metafacades;
    }

    /**
     * Filters out those metafacades which <strong>should</strong> be
     * processed.
     *
     * @param metafacades the Collection of metafacades.
     */
    private void filterElements(final Collection metafacades)
    {
        if (this.modelPackages != null && !this.modelPackages.isEmpty())
        {
            CollectionUtils.filter(
                metafacades,
                new Predicate()
                {
                    public boolean evaluate(final Object element)
                    {
                        boolean valid = false;
                        if (element instanceof NamedElement)
                        {
                            final NamedElement modelElement = (NamedElement)element;
                            final StringBuilder packageName =
                                new StringBuilder(
                                    UmlUtilities.getPackageName(
                                        modelElement,
                                        MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                                        true));

                            // - if the model element is a package then the package name will be the name
                            // of the package with its package name
                            if (element instanceof Package)
                            {
                                final String name = modelElement.getName();
                                if (StringUtils.isNotBlank(name))
                                {
                                    packageName.append(MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
                                    packageName.append(name);
                                }
                            }
                            valid = UMLModelAccessFacade.this.modelPackages.isApply(packageName.toString());
                        }

                        return valid;
                    }
                });
        }
    }
}
