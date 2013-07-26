// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.ModelElementFacade;

/**
 * Represents a final state in a JSF use case.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFFinalState
    extends FrontEndFinalState
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFFinalStateMetaType();

    /**
     * The path to which this final state points.
     * @return String
     */
    public String getPath();

    /**
     * The controller bean name to which this final state points.
     * @return String
     */
    public String getTargetControllerBeanName();

    /**
     * The target controller to which this final state points.
     * @return String
     */
    public String getTargetControllerFullyQualifiedName();

    /**
     * The element to which this final state points.
     * @return ModelElementFacade
     */
    public ModelElementFacade getTargetElement();
}