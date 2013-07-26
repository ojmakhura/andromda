// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.FrontEndAction;

/**
 * Represents an action taken during a "front-end" event execution on a JSF application.
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFAction
    extends FrontEndAction
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFActionMetaType();

    /**
     * The name of the action class that executes this action.
     * @return String
     */
    public String getActionClassName();

    /**
     * The name of the action on the controller that executions this action.
     * @return String
     */
    public String getControllerAction();

    /**
     * A resource message key suited for the action''s documentation.
     * @return String
     */
    public String getDocumentationKey();

    /**
     * The resource messsage value suited for the action''s documentation.
     * @return String
     */
    public String getDocumentationValue();

    /**
     * The name of the bean under which the form is stored.
     * @return String
     */
    public String getFormBeanName();

    /**
     * The signature of the accessor method that returns the form implementation instance.
     * @return String
     */
    public String getFormImplementationGetter();

    /**
     * A comma separated list of all the form interfaces which the form implementation implements.
     * @return String
     */
    public String getFormImplementationInterfaceList();

    /**
     * The name of the form implementation.
     * @return String
     */
    public String getFormImplementationName();

    /**
     * The key that stores the form in which information is passed from one action to another.
     * @return String
     */
    public String getFormKey();

    /**
     * The scope of the JSF form (request, session,application,etc).
     * @return String
     */
    public String getFormScope();

    /**
     * The calcuated serial version UID for this action's form.
     * @return String
     */
    public String getFormSerialVersionUID();

    /**
     * The name that corresponds to the from-outcome in an navigational rule.
     * @return String
     */
    public String getFromOutcome();

    /**
     * The fully qualified name of the action class that execute this action.
     * @return String
     */
    public String getFullyQualifiedActionClassName();

    /**
     * The fully qualified path to the action class that execute this action.
     * @return String
     */
    public String getFullyQualifiedActionClassPath();

    /**
     * The fully qualified name of the form implementation.
     * @return String
     */
    public String getFullyQualifiedFormImplementationName();

    /**
     * The fully qualified path of the form implementation.
     * @return String
     */
    public String getFullyQualifiedFormImplementationPath();

    /**
     * All parameters that are of hidden input type.
     * @return List<JSFParameter>
     */
    public List<JSFParameter> getHiddenParameters();

    /**
     * The default resource message key for this action.
     * @return String
     */
    public String getMessageKey();

    /**
     * The path to this action.
     * @return String
     */
    public String getPath();

    /**
     * The path's root.
     * @return String
     */
    public String getPathRoot();

    /**
     * Messages used to indicate successful execution.
     * @return Map
     */
    public Map getSuccessMessages();

    /**
     * The name of the column targetted by this action.
     * @return String
     */
    public String getTableLinkColumnName();

    /**
     * The name of the table link specified for this action.
     * @return String
     */
    public String getTableLinkName();

    /**
     * If the action is a table link then this property represents the table to which is being
     * linked.
     * @return JSFParameter
     */
    public JSFParameter getTableLinkParameter();

    /**
     * The name of the method to be executed when this action is triggered.
     * @return String
     */
    public String getTriggerMethodName();

    /**
     * The name of the trigger that triggers that action.
     * @return String
     */
    public String getTriggerName();

    /**
     * The path to the view fragment corresponding to this action
     * @return String
     */
    public String getViewFragmentPath();

    /**
     * Any messages used to indicate a warning.
     * @return Map
     */
    public Map getWarningMessages();

    /**
     * Indicates if this action forwards to a dialog (use case runs in different conversation
     * scope). Only applied when the target is a final state pointing to another use case.
     * @return boolean
     */
    public boolean isDialog();

    /**
     * Indicates whether or not a final state is the target of this action.
     * @return boolean
     */
    public boolean isFinalStateTarget();

    /**
     * Whether or not the entire form should be reset (all action parameters on the form).
     * @return boolean
     */
    public boolean isFormReset();

    /**
     * Indicates if at least one parameter on the form requires being reset.
     * @return boolean
     */
    public boolean isFormResetRequired();

    /**
     * Indicates whether or not this action is represented by clicking on a hyperlink.
     * @return boolean
     */
    public boolean isHyperlink();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFAction.needsFileUpload
     * @return boolean
     */
    public boolean isNeedsFileUpload();

    /**
     * Indicates if this action forwards to a popup. Only applied when the target is a final state
     * pointing to another use case.
     * @return boolean
     */
    public boolean isPopup();

    /**
     * Indicates whether or not the values passed along with this action can be reset or not.
     * @return boolean
     */
    public boolean isResettable();

    /**
     * Indicates whether or not any success messags are present.
     * @return boolean
     */
    public boolean isSuccessMessagesPresent();

    /**
     * Indicates that this action works on all rows of the table from the table link relation.
     * @return boolean
     */
    public boolean isTableAction();

    /**
     * Indicates if a table link name has been specified and it properly targets a table
     * page-variable from the input page.
     * @return boolean
     */
    public boolean isTableLink();

    /**
     * Indicates whether or not at least one parameter on this action requires validation.
     * @return boolean
     */
    public boolean isValidationRequired();

    /**
     * Whether or not any warning messages are present.
     * @return boolean
     */
    public boolean isWarningMessagesPresent();
}