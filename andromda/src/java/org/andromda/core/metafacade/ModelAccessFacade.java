package org.andromda.core.metafacade;

import java.util.Collection;

/**
 * <p>
 *  Provides access to a model loaded by a Repository
 *  and made available to be used by metafacades.
 * </p>
 * 
 * <p>
 * Models can be instances of any metamodel. The most common models
 * will be UML models. This interface is an abstraction. Any model
 * that implements this interface can be used with AndroMDA. 
 * </p>
 * 
 * <p>
 * Design goal: This class should only contain the <b>minimum amount
 * of methods</b> that will be needed such that the AndroMDA core can
 * deal with it. All other stuff should be done in cartridge-specific
 * classes!!! So, please don't make this class grow!
 * </p>
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
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
     * Returns an object that represents the entire model.
     * Data type is defined by the implementor of this interface.
     * 
     * @return Object
     */
    public Object getModel();
    
    /**
     * Returns the name of a model element (whatever that means for a concrete model).
     * @param modelElement the model element
     * @return String containing the name
     */
    public String getName(Object modelElement);
    
    /**
     * Returns the package name of a model element (whatever that means for a concrete model).
     * @param modelElement the model element
     * @return String containing the name
     */
    public String getPackageName(Object modelElement);

    /**
     * Returns a collection of stereotype names for a model element
     * (whatever that means for a concrete model).
     * 
     * @param modelElement the model element
     * @return Collection of Strings with stereotype names
     */
    public Collection getStereotypeNames(Object modelElement);
    
    /**
     * Finds all the model elements that have the specified 
     * <code>stereotype</code>.
     * 
     * @param stereotype the name of the stereotype, they are matched
     *        without regard to case.
     * @return Collection
     */
    public Collection findByStereotype(String stereotype);

}
