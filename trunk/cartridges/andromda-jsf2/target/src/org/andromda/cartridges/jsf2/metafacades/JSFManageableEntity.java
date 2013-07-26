// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.metafacades.uml.Role;

/**
 * TODO: Model Documentation for org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFManageableEntity
    extends ManageableEntity
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFManageableEntityMetaType();

    /**
     * The name of the action class that executes the manageable actions.
     * @return String
     */
    public String getActionClassName();

    /**
     * The fully qualified path to the action class that execute the manageable actions.
     * @return String
     */
    public String getActionFullPath();

    /**
     * The path to the action class that execute the manageable actions.
     * @return String
     */
    public String getActionPath();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.getActionRoles
     * @return String
     */
    public String getActionRoles();

    /**
     * The calcuated serial version UID for this manageable actions.
     * @return String
     */
    public String getActionSerialVersionUID();

    /**
     * The fully qualified name of the action class that execute the manageable actions.
     * @return String
     */
    public String getActionType();

    /**
     * The bean name of this manageable controller (this is what is stored in the JSF configuration
     * file).
     * @return String
     */
    public String getControllerBeanName();

    /**
     * Full path of this manageable controller.
     * @return String
     */
    public String getControllerFullPath();

    /**
     * Manageable controller class name.
     * @return String
     */
    public String getControllerName();

    /**
     * Fully qualified name of this manageable controller.
     * @return String
     */
    public String getControllerType();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.converterClassName
     * @return String
     */
    public String getConverterClassName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.converterFullPath
     * @return String
     */
    public String getConverterFullPath();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.converterType
     * @return String
     */
    public String getConverterType();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.exceptionKey
     * @return String
     */
    public String getExceptionKey();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.exceptionPath
     * @return String
     */
    public String getExceptionPath();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanClassName
     * @return String
     */
    public String getFormBeanClassName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanFullPath
     * @return String
     */
    public String getFormBeanFullPath();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanName
     * @return String
     */
    public String getFormBeanName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.formBeanType
     * @return String
     */
    public String getFormBeanType();

    /**
     * The calcuated serial version UID for this action's form.
     * @return String
     */
    public String getFormSerialVersionUID();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.listGetterName
     * @return String
     */
    public String getListGetterName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.listName
     * @return String
     */
    public String getListName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.listSetterName
     * @return String
     */
    public String getListSetterName();

    /**
     * returns all editable attributes
     * @return Collection
     */
    public Collection getManageableEditAttributes();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.manageableSearchAssociationEnds
     * @return Collection
     */
    public Collection getManageableSearchAssociationEnds();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.manageableSearchAttributes
     * @return Collection
     */
    public Collection getManageableSearchAttributes();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.messageKey
     * @return String
     */
    public String getMessageKey();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.messageValue
     * @return String
     */
    public String getMessageValue();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.odsExportFullPath
     * @return String
     */
    public String getOdsExportFullPath();

    /**
     * The full path to this entity's online help action. The returned String does not have a suffix
     * such as '.do'.
     * @return String
     */
    public String getOnlineHelpActionPath();

    /**
     * The key to lookup the online help documentation.
     * @return String
     */
    public String getOnlineHelpKey();

    /**
     * The full path to this entitiy's online help page. The returned String does not have a suffix
     * such as '.jsp'.
     * @return String
     */
    public String getOnlineHelpPagePath();

    /**
     * The online help documentation. The format is HTML without any style.
     * @return String
     */
    public String getOnlineHelpValue();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.populatorFullPath
     * @return String
     */
    public String getPopulatorFullPath();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.populatorName
     * @return String
     */
    public String getPopulatorName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.populatorType
     * @return String
     */
    public String getPopulatorType();

    /**
     * Represents a role a user may play within a system.  Provides access to things such as
     * services and
     * service operations.
     * @return Collection<Role>
     */
    public Collection<Role> getRoles();

    /**
     * Full path of this manageable search filter.
     * @return String
     */
    public String getSearchFilterFullPath();

    /**
     * Search filter class name.
     * @return String
     */
    public String getSearchFilterName();

    /**
     * The calculated serial version UID for this controller.
     * @return String
     */
    public String getSearchFilterSerialVersionUID();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanClassName
     * @return String
     */
    public String getSearchFormBeanClassName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanFullPath
     * @return String
     */
    public String getSearchFormBeanFullPath();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanName
     * @return String
     */
    public String getSearchFormBeanName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.searchFormBeanType
     * @return String
     */
    public String getSearchFormBeanType();

    /**
     * Tthe available types of export in a single String instance.
     * @return String
     */
    public String getTableExportTypes();

    /**
     * The maximum number of rows to be displayed in the table at the same time. This is also known
     * as the page size. A value of zero or less will display all data in the same table (therefore
     * also on the same page).
     * @return int
     */
    public int getTableMaxRows();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.valueObjectClassName
     * @return String
     */
    public String getValueObjectClassName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewFullPath
     * @return String
     */
    public String getViewFullPath();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewName
     * @return String
     */
    public String getViewName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewTitleKey
     * @return String
     */
    public String getViewTitleKey();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.viewTitleValue
     * @return String
     */
    public String getViewTitleValue();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.needsFileUpload
     * @return boolean
     */
    public boolean isNeedsFileUpload();

    /**
     * Returns true if the user needs to modify the standard behavior and a Impl.java file will be
     * created in web/src/main/java.
     * @return boolean
     */
    public boolean isNeedsImplementation();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.needsUserInterface
     * @return boolean
     */
    public boolean isNeedsUserInterface();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.preload
     * @return boolean
     */
    public boolean isPreload();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.isSearchable
     * @param element Object
     * @return boolean
     */
    public boolean isSearchable(Object element);

    /**
     * True if it is possible to export the table data to XML, CSV, PDF or Excel format.
     * @return boolean
     */
    public boolean isTableExportable();

    /**
     * True if it is possible to sort the columns of the table.
     * @return boolean
     */
    public boolean isTableSortable();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntity.validationRequired
     * @return boolean
     */
    public boolean isValidationRequired();
}