package org.andromda.core.metafacade;

import java.util.Collection;

import org.andromda.core.configuration.ModelPackages;


/**
 * <p/>
 * Provides access to a model loaded by a Repository and made available to be used to retrieve information about
 * model elements and metafacades. </p>
 * <p/>
 * Models can be instances of any metamodel. The most common models will be UML models. This interface is an
 * abstraction. Any model that implements this interface can be used with AndroMDA. </p>
 * <p/>
 * Design goal: This class should only contain the <b>minimum amount of methods </b> that will be needed such that the
 * AndroMDA core can deal with it. All other stuff should be done in cartridge-specific classes!!! So, please don't make
 * this class grow! </p>
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public interface ModelAccessFacade
{
    /**
     * Sets the object that represents the entire model.
     *
     * @param model the model to set.
     */
    public void setModel(Object model);

    /**
     * Returns an object that represents the entire model. Data type is defined by the implementor of this interface.
     *
     * @return the metaclass model.
     */
    public Object getModel();

    /**
     * Returns the name of a metafacade (whatever that means for a concrete model).
     *
     * @param metafacade the metafacade from which to retrieve the name.
     * @return String containing the name
     */
    public String getName(Object metafacade);

    /**
     * Returns the package name of a model element (whatever that means for a concrete model).
     *
     * @param modelElement the model element
     * @return String containing the name
     */
    public String getPackageName(Object modelElement);

    /**
     * Sets the model packages instance which contains the information about what
     * packages should and should not be filtered out.  Ths model access facade
     * instance then uses this information to provide any filtering by package.
     *
     * @param modelPackages the model packages by which to filter.
     */
    public void setPackageFilter(ModelPackages modelPackages);

    /**
     * Returns a collection of stereotype names for a modelElement (whatever that means for a concrete model).
     *
     * @param modelElement the modelElement
     * @return Collection of Strings with stereotype names
     */
    public Collection getStereotypeNames(Object modelElement);

    /**
     * Finds all the model elements that have the specified <code>stereotype</code> (with any filtering
     * applied from the information provided by {@link #setPackageFilter(ModelPackages)}).
     *
     * @param stereotype the name of the stereotype, they are matched without regard to case.
     * @return Collection of model elements having the given stereotype
     */
    public Collection findByStereotype(String stereotype);

    /**
     * Returns all elements from the model (with any filtering
     * applied from the information provided by {@link #setPackageFilter(ModelPackages)}).
     *
     * @return Collection of all metafacades
     */
    public Collection getModelElements();
}