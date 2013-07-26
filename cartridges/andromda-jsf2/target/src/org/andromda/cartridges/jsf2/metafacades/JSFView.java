// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.List;
import org.andromda.metafacades.uml.FrontEndView;

/**
 * Represents a JSF view for a front-end application.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFView
    extends FrontEndView
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFViewMetaType();

    /**
     * All those forwards that are actions.
     * @return List<JSFAction>
     */
    public List<JSFAction> getActionForwards();

    /**
     * All variables that have backing value.
     * @return List<JSFParameter>
     */
    public List<JSFParameter> getBackingValueVariables();

    /**
     * A resource message key suited for the page's documentation.
     * @return String
     */
    public String getDocumentationKey();

    /**
     * A resource message value suited for the view's documentation.
     * @return String
     */
    public String getDocumentationValue();

    /**
     * All actions that have forms associated with them.
     * @return List<JSFAction>
     */
    public List<JSFAction> getFormActions();

    /**
     * The key that stores the form in which information is passed from one action to another.
     * @return String
     */
    public String getFormKey();

    /**
     * Gets the forwards which can be targgeted from this view.
     * @return List<JSFForward>
     */
    public List<JSFForward> getForwards();

    /**
     * The name that corresponds to the from-outcome in an navigational rule.
     * @return String
     */
    public String getFromOutcome();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.fullyQualifiedPageObjectClassName
     * @return String
     */
    public String getFullyQualifiedPageObjectClassName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.fullyQualifiedPageObjectClassPath
     * @return String
     */
    public String getFullyQualifiedPageObjectClassPath();

    /**
     * The fully qualified name of this view's form populator.
     * @return String
     */
    public String getFullyQualifiedPopulator();

    /**
     * The default resource message key for this view.
     * @return String
     */
    public String getMessageKey();

    /**
     * A displayable version of this view's name.
     * @return String
     */
    public String getMessageValue();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.pageObjectBeanName
     * @return String
     */
    public String getPageObjectBeanName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.pageObjectClassName
     * @return String
     */
    public String getPageObjectClassName();

    /**
     * The full path of the view resources (i.e. the JSP page).
     * @return String
     */
    public String getPath();

    /**
     * The name of the form populator for this view.
     * @return String
     */
    public String getPopulator();

    /**
     * The path to the form populator.
     * @return String
     */
    public String getPopulatorPath();

    /**
     * A resource message key suited for the view's title.
     * @return String
     */
    public String getTitleKey();

    /**
     * A default resource message value suited for the page's title.
     * @return String
     */
    public String getTitleValue();

    /**
     * Indicates whether or not this view has the same name as the use case in which it is
     * contained.
     * @return boolean
     */
    public boolean isHasNameOfUseCase();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFView.needsFileUpload
     * @return boolean
     */
    public boolean isNeedsFileUpload();

    /**
     * Indicates whether or not any non-table view variables are present in this view.
     * @return boolean
     */
    public boolean isNonTableVariablesPresent();

    /**
     * Indicates if a populator is required for this view.
     * @return boolean
     */
    public boolean isPopulatorRequired();

    /**
     * Indicates if this view represents a popup.
     * @return boolean
     */
    public boolean isPopup();

    /**
     * Indicates whether or not at least one parameter of an outgoing action in this view requires
     * validation.
     * @return boolean
     */
    public boolean isValidationRequired();
}