package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd
 */
public class SpringEntityAssociationEndLogicImpl
    extends SpringEntityAssociationEndLogic
    implements
    org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd
{
    // ---------------- constructor -------------------------------

    public SpringEntityAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd#isOne2OnePrimary()
     */
    public boolean handleIsOne2OnePrimary()
    {
        boolean primaryOne2One = super.isOne2One();
        SpringEntityAssociationEndLogicImpl otherEnd = (SpringEntityAssociationEndLogicImpl)this
            .getOtherEnd();
        if (primaryOne2One)
        {
            primaryOne2One = super.isAggregation() || this.isComposition();
        }
        // if the flag is false delegate to the super class
        if (!primaryOne2One)
        {
            primaryOne2One = super.isOne2One() && !otherEnd.isAggregation()
                && !otherEnd.isComposition();
        }
        return primaryOne2One;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        String getterSetterTypeName = super.getGetterSetterTypeName();
        if (!this.isMany())
        {
            ClassifierFacade type = this.getType();
            if (type != null
                && SpringEntity.class.isAssignableFrom(type.getClass()))
            {
                String typeName = ((SpringEntity)type)
                    .getFullyQualifiedEntityName();
                if (StringUtils.isNotEmpty(typeName))
                {
                    getterSetterTypeName = typeName;
                }
            }
        }
        return getterSetterTypeName;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isLazy()
     */
    protected boolean handleIsLazy()
    {
        String lazyString = (String)findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        boolean lazy;

        if (lazyString == null)
        {
            lazy = !isComposition();
        }
        else
        {
            lazy = Boolean.valueOf(lazyString).booleanValue();
        }

        return lazy;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd#isOne2OneSecondary()
     */
    protected boolean handleIsOne2OneSecondary()
    {
        boolean secondary = false;
        Object type = this.getType();
        Object otherType = this.getOtherEnd().getType();
        if (type != null && SpringEntity.class.isAssignableFrom(type.getClass()) &&
            otherType != null && SpringEntity.class.isAssignableFrom(otherType.getClass()))
        {
            SpringEntity entity = (SpringEntity)type;
            SpringEntity otherEntity = (SpringEntity)otherType;
            secondary = (this.isChild() && entity.isForeignHibernateGeneratorClass()) ||
                otherEntity.isForeignHibernateGeneratorClass() ||
                (!this.isNavigable() && this.getOtherEnd().isNavigable() && !this.isOne2OnePrimary());
        }
        return secondary;
    }
    
    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAssociationEnd#getHibernateCascade()
     */
    protected String handleGetHibernateCascade()
    {
        String cascade = SpringGlobals.HIBERNATE_CASCADE_DELETE;
        Object type = this.getType();
        if (type != null && SpringEntity.class.isAssignableFrom(type.getClass()))
        {
            SpringEntity entity = (SpringEntity)type;
            if (entity.getHibernateDefaultCascade().equalsIgnoreCase(SpringGlobals.HIBERNATE_CASCADE_SAVE_UPDATE) &&
                this.getOtherEnd().isMany())
            {
                cascade = SpringGlobals.HIBERNATE_CASCADE_ALL_DELETE_ORPHAN;                    
            }           
        }
        return cascade;
    }
    
    /*
#set ($cascade = "delete")
#if ($hibernateDefaultCascade.equalsIgnoreCase("save-update"))
#set ($cascade = "all-${cascade}-orphan")
     */
}
