// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import java.util.List;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FrontEndController;

/**
 * Represents a controller for a JSF application.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFController
    extends FrontEndController
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFControllerMetaType();

    /**
     * All services the controller needs.
     * @return Collection
     */
    public Collection getAllServices();

    /**
     * The bean name of this controller (this is what is stored in the JSF configuration file).
     * @return String
     */
    public String getBeanName();

    /**
     * The calculated serial version UID for this controller.
     * @return String
     */
    public String getControllerSerialVersionUID();

    /**
     * The fully qualified implementation name of this controller.
     * @return String
     */
    public String getFullyQualifiedImplementationName();

    /**
     * The fully qualified path to the controller implemention file.
     * @return String
     */
    public String getFullyQualifiedImplementationPath();

    /**
     * The implementation name of this controller.
     * @return String
     */
    public String getImplementationName();

    /**
     * References to packages containing services this controller depends
     * @return List<DependencyFacade>
     */
    public List<DependencyFacade> getServicesPackagesReferences();

    /**
     * All dependencies to VOs to be kept in the session.
     * @return List<DependencyFacade>
     */
    public List<DependencyFacade> getSessionObjectReferences();
}