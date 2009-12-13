package org.andromda.cartridges.meta.metafacades;

import java.util.Collection;
import org.andromda.cartridges.meta.MetaProfile;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization
 * @author Bob Fields
 */
public class MetafacadeGeneralizationLogicImpl
    extends MetafacadeGeneralizationLogic
{
    /**
     * @param metaObjectIn
     * @param context
     */
    public MetafacadeGeneralizationLogicImpl(
        Object metaObjectIn,
        String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization#getPrecedence()
     */
    @Override
    protected Integer handleGetPrecedence()
    {
        Integer precedence = 999999999;
        String value =
            ObjectUtils.toString(
                this.findTaggedValue(MetaProfile.TAGGEDVALUE_GENERALIZATION_PRECEDENCE));
        if (StringUtils.isNotBlank(value))
        {
            try
            {
                precedence = new Integer(value);
            }
            catch (NumberFormatException ex)
            {
                // ignore since we'll just take the large default.
            }
        }
        return precedence;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization#getGetterName()
     */
    @Override
    protected String handleGetGetterName()
    {
        String name = this.getName();
        if (StringUtils.isBlank(name))
        {
            if (this.getParent() != null)
            {
                name = this.getParent().getName();
            }
        }
        name = StringUtils.capitalize(name);
        return "get" + name;
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization#getGetterNameVisibility()
     */
    @Override
    protected String handleGetGetterNameVisibility()
    {
        String visibility = "private";
        GeneralizableElementFacade child = this.getChild();
        if (child != null)
        {
            // if we have more than one generalization for the metafacade
            // then we expose the super facade accessors.
            Collection generalizations = child.getGeneralizations();
            if ((generalizations != null) && (generalizations.size() > 1))
            {
                visibility = "protected";
            }
        }
        return visibility;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    public String getName()
    {
        String name = super.getName();
        if (StringUtils.isBlank(name) && (this.getParent() != null))
        {
            name = this.getParent().getName();
        }
        return ObjectUtils.toString(
            this.getConfiguredProperty(MetaGlobals.PROPERTY_GENERALIZATION_NAME_PATTERN))
                          .replaceAll("\\{0\\}", name);
    }
}