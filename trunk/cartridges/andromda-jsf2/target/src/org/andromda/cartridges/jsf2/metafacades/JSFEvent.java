// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.FrontEndEvent;

/**
 * A front-end JSF even (like the pressing of a button, etc).
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFEvent
    extends FrontEndEvent
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFEventMetaType();

    /**
     * The resource message key for this trigger.
     * @return String
     */
    public String getMessageKey();

    /**
     * The resource message value  for this trigger, this would be the button label or hyperlink
     * name.
     * @return String
     */
    public String getMessageValue();

    /**
     * The resource message key for the reset button.
     * @return String
     */
    public String getResetMessageKey();

    /**
     * The default value for the reset button's message.
     * @return String
     */
    public String getResetMessageValue();
}