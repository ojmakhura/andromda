// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;

/**
 * Represents the portlet preferences for a given use case (assuming the JSF application being
 * generated is a portlet).
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFPortletPreferences
    extends ClassifierFacade
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFPortletPreferencesMetaType();

    /**
     * The use case to which the portlet preferences belongs.
     * @return JSFUseCase
     */
    public JSFUseCase getUseCase();
}