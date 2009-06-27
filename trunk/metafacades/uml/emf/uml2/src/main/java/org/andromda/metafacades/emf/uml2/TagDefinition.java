package org.andromda.metafacades.emf.uml2;

import java.util.Collection;


/**
 * Represents a TagDefinition metaclass (was needed because it doesn't exist in
 * the uml2 metamodel).
 *
 * @author Steve Jerman
 */
public interface TagDefinition
    extends org.eclipse.uml2.NamedElement
{
    /**
     * Get the value of the tag.
     *
     * @return the object that is the value of the tag.
     */
    public Object getValue();

    /**
     * If the value is a collection return it as a collection rather than an
     * object.
     *
     * @return a collection of values.
     */
    public Collection getValues();
}