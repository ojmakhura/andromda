// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Map;
import org.andromda.metafacades.uml.FrontEndForward;

/**
 * Represents a forward within a JSF activity graph.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFForward
    extends FrontEndForward
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFForwardMetaType();

    /**
     * The name that corresponds to the from-outcome in an navigational rule.
     * @return String
     */
    public String getFromOutcome();

    /**
     * The path to which this forward points.
     * @return String
     */
    public String getPath();

    /**
     * Messages used to indicate successful execution.
     * @return Map
     */
    public Map getSuccessMessages();

    /**
     * Any messages used to indicate a warning.
     * @return Map
     */
    public Map getWarningMessages();

    /**
     * Indicates whether or not a final state is the target of this forward.
     * @return boolean
     */
    public boolean isFinalStateTarget();

    /**
     * Indicates whether or not any success messags are present.
     * @return boolean
     */
    public boolean isSuccessMessagesPresent();

    /**
     * Whether or not any warning messages are present.
     * @return boolean
     */
    public boolean isWarningMessagesPresent();
}