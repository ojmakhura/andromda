package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;

import java.lang.reflect.Method;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger
 */
public class StrutsTriggerLogicImpl
        extends StrutsTriggerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger
{
    // ---------------- constructor -------------------------------

    public StrutsTriggerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsTrigger ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getNotAllowedTitleKey()()
     */
    public java.lang.String handleGetNotAllowedTitleKey()
    {
        return getTitleKey() + ".notallowed";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTitleKey()()
     */
    public java.lang.String handleGetTitleKey()
    {
        return getTriggerKey() + ".title";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetTitleKey()()
     */
    public java.lang.String handleGetResetTitleKey()
    {
        return getTitleKey() + ".reset";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetNotAllowedTitleKey()()
     */
    public java.lang.String handleGetResetNotAllowedTitleKey()
    {
        return getResetTitleKey() + ".not.allowed";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTriggerKey()()
     */
    public java.lang.String handleGetTriggerKey()
    {
        return StringUtilsHelper.toResourceMessageKey(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTriggerValue()()
     */
    public java.lang.String handleGetTriggerValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getTitleValue()()
     */
    public java.lang.String handleGetTitleValue()
    {
        return getTriggerValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetTitleValue()()
     */
    public java.lang.String handleGetResetTitleValue()
    {
        return "Reset";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getResetNotAllowedTitleValue()()
     */
    public java.lang.String handleGetResetNotAllowedTitleValue()
    {
        return "You are not allowed to reset";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsTrigger#getNotAllowedTitleValue()()
     */
    public java.lang.String handleGetNotAllowedTitleValue()
    {
        return "You are not allowed to call this action";
    }

    // ------------- relations ------------------

    protected Object handleGetControllerCall()
    {
        /*
         * hack until I find a solution to workaround the JMI multiple inheritance (through interfaces)
         */
        try
        {
            Method method = metaObject.getClass().getMethod("getOperation", null);
            return method.invoke(metaObject, null);
        } catch (Exception ex)
        {
            return null;
        }
    }
}
