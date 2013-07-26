// license-header java merge-point
//
// Attention: generated code (by Metafacade.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;

/**
 * TODO: Model Documentation for
 * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd
 *
 * Metafacade interface to be used by AndroMDA cartridges.
 */
public interface JSFManageableEntityAssociationEnd
    extends ManageableEntityAssociationEnd
{
    /**
     * Indicates the metafacade type (used for metafacade mappings).
     *
     * @return boolean always <code>true</code>
     */
    public boolean isJSFManageableEntityAssociationEndMetaType();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.backingListName
     * @return String
     */
    public String getBackingListName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.labelListName
     * @return String
     */
    public String getLabelListName();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.messageKey
     * @return String
     */
    public String getMessageKey();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.messageValue
     * @return String
     */
    public String getMessageValue();

    /**
     * The key to lookup the online help documentation. This documentation is gathered from the
     * documentation entered by the user, as well as analyzing the model.
     * @return String
     */
    public String getOnlineHelpKey();

    /**
     * The online help documentation. This documentation is gathered from the documentation entered
     * by the user, as well as analyzing the model. The format is HTML without any style.
     * @return String
     */
    public String getOnlineHelpValue();

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.valueListName
     * @return String
     */
    public String getValueListName();
}