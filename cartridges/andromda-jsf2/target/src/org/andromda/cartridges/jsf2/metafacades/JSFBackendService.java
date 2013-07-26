// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.Service;

/**
 * Represents a back-end service that can be accessed by a JSF controller.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFBackendService
    extends Service
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFBackendServiceMetaType();

    /**
     * The implementation to use in a controller in order to get an instance of a back end service.
     * @return String
     */
    public String getAccessorImplementation();
}