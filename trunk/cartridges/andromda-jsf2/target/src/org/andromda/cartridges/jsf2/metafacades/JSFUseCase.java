// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.FrontEndUseCase;

/**
 * Represents a JSF use case.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFUseCase
    extends FrontEndUseCase
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFUseCaseMetaType();

    /**
     * The name of the action class that forwards to this use case.
     * @return String
     */
    public String getActionClassName();

    /**
     * All forwards in this use case that are represented as actions.
     * @return List<JSFAction>
     */
    public List<JSFAction> getActionForwards();

    /**
     * TODO: Model Documentation for
     * JSFUseCase.getActionRoles
     * @return String
     */
    public String getActionRoles();

    /**
     * Constains all forwards includes regular FrontEndForwards and all actiion forwards.
     * @return List
     */
    public List getAllForwards();

    /**
     * A map with keys sorted alphabetically, normalized across all different use-cases, views, etc.
     * @return Map
     */
    public Map getAllMessages();

    /**
     * All views for the application (not just the ones belonging to this use case).
     * @return Collection<JSFView>
     */
    public Collection<JSFView> getAllViews();

    /**
     * The name of the action on the controller that executions this use case.
     * @return String
     */
    public String getControllerAction();

    /**
     * The key under which to store the action form passed along in this in this use-case.
     * @return String
     */
    public String getFormKey();

    /**
     * The name that will cause a forward to use case.
     * @return String
     */
    public String getForwardName();

    /**
     * All forwards contained in this use case.
     * @return List<JSFForward>
     */
    public List<JSFForward> getForwards();

    /**
     * The name of the class that stores all the forwards paths.
     * @return String
     */
    public String getForwardsClassName();

    /**
     * The fully qualified name of the action class that forwards to this use case.
     * @return String
     */
    public String getFullyQualifiedActionClassName();

    /**
     * The fully qualified path to the action class that forwards to this use case.
     * @return String
     */
    public String getFullyQualifiedActionClassPath();

    /**
     * The path of the initial target going into this use case.
     * @return String
     */
    public String getInitialTargetPath();

    /**
     * TODO: Model Documentation for
     * JSFUseCase.getNavigationChildren
     * @return Collection
     */
    public Collection getNavigationChildren();

    /**
     * TODO: Model Documentation for
     * JSFUseCase.getNavigationParents
     * @return Collection
     */
    public Collection getNavigationParents();

    /**
     * Retrieves all navigation rules for the faces-config.xml
     * @return Collection
     */
    public Collection getNavigationRules();

    /**
     * The path to which this use case points.
     * @return String
     */
    public String getPath();

    /**
     * The root path for this use case (this is the path the directory containing the use case's
     * resources).
     * @return String
     */
    public String getPathRoot();

    /**
     * The forward name for the portlet 'edit' page.
     * @return String
     */
    public String getPortletEditForwardName();

    /**
     * The path to the portlet 'edit' page.
     * @return String
     */
    public String getPortletEditPath();

    /**
     * The forward name for the portlet 'help' page.
     * @return String
     */
    public String getPortletHelpForwardName();

    /**
     * The path to the 'help' page of the portlet.
     * @return String
     */
    public String getPortletHelpPath();

    /**
     * The forward name for the portlet 'view' page.
     * @return String
     */
    public String getPortletViewForwardName();

    /**
     * The path to the portlet 'view' page.
     * @return String
     */
    public String getPortletViewPath();

    /**
     * Any portlet preferences associated to this use case.
     * @return JSFPortletPreferences
     */
    public JSFPortletPreferences getPreferences();

    /**
     * All use cases that are labled as registration use cases.
     * @return List<JSFUseCase>
     */
    public List<JSFUseCase> getRegistrationUseCases();

    /**
     * The title message key for this use-case.
     * @return String
     */
    public String getTitleKey();

    /**
     * The title message value for this use-case.
     * @return String
     */
    public String getTitleValue();

    /**
     * Indicates that at least one client/server parameter found in the collection of existing
     * use-cases requires validation.
     * @return boolean
     */
    public boolean isApplicationValidationRequired();

    /**
     * Indicates whether or not the initial target of this use case is a view or not.
     * @return boolean
     */
    public boolean isInitialTargetView();

    /**
     * Indicates whether or not this is a front-end registration use case.  Only one use case can be
     * labeled as a 'registration' use case.
     * @return boolean
     */
    public boolean isRegistrationUseCase();

    /**
     * Indicates whether or not at least one parameter in this use-case require validation.
     * @return boolean
     */
    public boolean isValidationRequired();

    /**
     * Indicates whether or not at least one view in the use case has the same name as this use
     * case.
     * @return boolean
     */
    public boolean isViewHasNameOfUseCase();
}