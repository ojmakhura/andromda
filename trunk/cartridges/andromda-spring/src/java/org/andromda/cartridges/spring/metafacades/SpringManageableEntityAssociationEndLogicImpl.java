package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.utils.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd
 */
public class SpringManageableEntityAssociationEndLogicImpl
    extends SpringManageableEntityAssociationEndLogic
{

    /**
     * Public constructor for SpringManageableEntityAssociationEndLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd
     */
    public SpringManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return StringUtilsHelper.lowerCamelCaseName(this.getName()) + "Dao"
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd#getDaoName()
     */
    protected String handleGetDaoName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName()) + "Dao";
    }

    /**
     * @return getType().getBeanName(false)
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd#getDaoReferenceName()
     */
    protected String handleGetDaoReferenceName()
    {
        String referenceName = null;

        final ClassifierFacade type = this.getType();
        if (type instanceof SpringManageableEntity)
        {
            final SpringManageableEntity entity = (SpringManageableEntity)type;
            referenceName = entity.getBeanName(false);
        }

        return referenceName;
    }

    /**
     * @return getGetterName() + "Dao"
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd#getDaoGetterName()
     */
    protected String handleGetDaoGetterName()
    {
        return this.getGetterName() + "Dao";
    }

    /**
     * @return getSetterName() + "Dao"
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd#getDaoSetterName()
     */
    protected String handleGetDaoSetterName()
    {
        return this.getSetterName() + "Dao";
    }

}