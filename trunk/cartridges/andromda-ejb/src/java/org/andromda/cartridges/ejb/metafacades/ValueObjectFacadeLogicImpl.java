package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.StringTokenizer;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.ValueObjectFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.ValueObjectFacade
 */
public class ValueObjectFacadeLogicImpl
        extends ValueObjectFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ValueObjectFacadeLogicImpl(Object metaObject, String context)
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
        String packageName = MessageFormat.format(this.getConfiguredProperty("valueObjectPackage").toString(), new String[]{
            StringUtils.trimToEmpty(super.getPackageName())});

        StringTokenizer st = new StringTokenizer(packageName, ".");
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens())
        {
            String token = st.nextToken();
            if (token.indexOf("/") < 0)
            {
                sb.append(token).append(".");
            }
        }
        String pn = sb.toString();
        return (pn.endsWith(".")) ? pn.substring(0, pn.length() - 1) : pn;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.ValueObjectFacade#getName()
     */
    public String getName()
    {
        return MessageFormat.format(this.getConfiguredProperty("valueObjectName").toString(), new Object[]{
            StringUtils.trimToEmpty(super.getName())});
    }

    public String getFullyQualifiedName()
    {
        String fqn = getPackageName();
        return (fqn == null || fqn.equalsIgnoreCase("")) ? getName() : fqn + "." + getName();
    }

    /**
     * If we're using inheritance to build up a value object with all model elements of an entity, we're returning no
     * superclass.
     */
    public GeneralizableElementFacade getGeneralization()
    {
        GeneralizableElementFacade gef = super.getGeneralization();
        return (gef == null || gef.hasStereotype(EJBProfile.STEREOTYPE_ENTITY)) ? null : gef;
    }
}