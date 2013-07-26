// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.FrontEndControllerOperation;

/**
 * Represents an operation for a JSF controller.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFControllerOperation
    extends FrontEndControllerOperation
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFControllerOperationMetaType();

    /**
     * The operation call that takes the appropriate form as an argument.
     * @return String
     */
    public String getFormCall();

    /**
     * The form name the corresponds to this controller operation.
     * @return String
     */
    public String getFormName();

    /**
     * The controller operation signature that takes the appropriate form (if this operation has at
     * least one form field) as an argument.
     * @return String
     */
    public String getFormSignature();

    /**
     * The fully qualified form name.
     * @return String
     */
    public String getFullyQualifiedFormName();

    /**
     * The fully qualified path of the form file.
     * @return String
     */
    public String getFullyQualifiedFormPath();

    /**
     * The controller implementation operation signature that takes the appropriate form (if this
     * operation has at least one form field) as an argument.
     * @return String
     */
    public String getImplementationFormSignature();
}