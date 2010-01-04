package org.andromda.cartridges.ejb.metafacades;

import java.text.MessageFormat;

import java.util.StringTokenizer;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.ValueObjectFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.ValueObjectFacade
 */
public class ValueObjectFacadeLogicImpl
    extends ValueObjectFacadeLogic
{
    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public ValueObjectFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Using <code>/</code> in the configuredProperty <code>valueObjectPackage</code> can remove the previous package
     * name.
     *
     * @see org.andromda.cartridges.ejb.metafacades.ValueObjectFacade#getPackageName()
     */
    public String getPackageName()
    {
        final String packageName =
            MessageFormat.format(
                this.getConfiguredProperty("valueObjectPackage").toString(),
                new String[] {StringUtils.trimToEmpty(super.getPackageName())});
        StringBuffer buffer = new StringBuffer();
        for (final StringTokenizer tokenizer = new StringTokenizer(packageName, "."); tokenizer.hasMoreTokens();)
        {
            String token = tokenizer.nextToken();
            if (token.indexOf('/') < 0)
            {
                buffer.append(token).append('.');
            }
        }
        final String name = buffer.toString();
        return name.endsWith(".") ? name.substring(0, name.length() - 1) : name;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.ValueObjectFacade#getName()
     */
    public String getName()
    {
        return MessageFormat.format(
            this.getConfiguredProperty("valueObjectName").toString(),
            new Object[] {StringUtils.trimToEmpty(super.getName())});
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        final String name = this.getPackageName();
        return name == null || "".equalsIgnoreCase(name) ? getName() : name + '.' + getName();
    }

    /**
     * If we're using inheritance to build up a value object with all model elements of an entity, we're returning no
     * superclass.
     * @return super.getGeneralization() if generalization.hasStereotype(EJBProfile.STEREOTYPE_ENTITY)
     */
    public GeneralizableElementFacade getGeneralization()
    {
        GeneralizableElementFacade generalization = super.getGeneralization();
        return generalization == null || generalization.hasStereotype(EJBProfile.STEREOTYPE_ENTITY) ? null
                                                                                                    : generalization;
    }
}