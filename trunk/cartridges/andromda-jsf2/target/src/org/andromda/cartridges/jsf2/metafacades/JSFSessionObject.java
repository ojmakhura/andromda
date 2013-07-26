// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;

/**
 * Represents a JSF session object (a session object is an object that is stored in the session
 * during application execution).
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFSessionObject
    extends ClassifierFacade
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFSessionObjectMetaType();

    /**
     * The full path to the session object file name.
     * @return String
     */
    public String getFullPath();
}