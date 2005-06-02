package org.andromda.cartridges.meta.metafacades;

import org.andromda.cartridges.meta.MetaProfile;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization.
 *
 * @see org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization
 */
public class MetafacadeGeneralizationLogicImpl
    extends MetafacadeGeneralizationLogic
{
    public MetafacadeGeneralizationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.MetafacadeGeneralization#getPrecedence()
     */
    protected java.lang.Integer handleGetPrecedence()
    {
        Integer precedence = new Integer(999999999);
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