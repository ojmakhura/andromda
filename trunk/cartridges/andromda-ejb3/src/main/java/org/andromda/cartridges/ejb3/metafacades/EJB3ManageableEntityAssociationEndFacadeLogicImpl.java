package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAssociationEndFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAssociationEndFacade
 */
public class EJB3ManageableEntityAssociationEndFacadeLogicImpl
    extends EJB3ManageableEntityAssociationEndFacadeLogic
{
    
    public EJB3ManageableEntityAssociationEndFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAssociationEndFacadeLogic#handleGetLabelName()
     */
    protected String handleGetLabelName()
    {
        String labelNamePattern = (this.isMany() ? 
                (String)this.getConfiguredProperty(EJB3Globals.LABEL_COLLECTION_NAME_PATTERN) :
                    (String)this.getConfiguredProperty(EJB3Globals.LABEL_SINGLE_NAME_PATTERN));

        return MessageFormat.format(
                labelNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAssociationEndFacadeLogic#handleGetGetterLabelName()
     */
    protected String handleGetGetterLabelName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtils.capitalize(this.getLabelName());
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAssociationEndFacadeLogic#handleGetSetterLabelName()
     */
    protected String handleGetSetterLabelName()
    {
        return "set" + StringUtils.capitalize(this.getLabelName());
    }
}