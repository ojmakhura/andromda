package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.cartridges.web.CartridgeWebGlobals;
import org.andromda.cartridges.web.CartridgeWebProfile;
import org.andromda.cartridges.web.CartridgeWebUtils;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.utils.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForward.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForward
 */
public class ThymeleafForwardLogicImpl
    extends ThymeleafForwardLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafForwardLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        StringBuilder name = new StringBuilder(super.getName());
        final Object target = this.getTarget();
        if (target instanceof ThymeleafFinalState)
        {
            name.append(CartridgeWebGlobals.USECASE_FORWARD_NAME_SUFFIX);
        }
        else
        {
            name.insert(0, this.getUseCase().getName() + "-");
        }
        return CartridgeWebUtils.toWebResourceName(name.toString());
    }

    /**
     * @return forwardPath
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForward#getPath()
     */
    protected String handleGetPath()
    {
        String forwardPath = null;
        final StateVertexFacade target = getTarget();
        if (this.isEnteringView())
        {
            forwardPath = ((ThymeleafView)target).getPath();
        }
        else if (this.isEnteringFinalState())
        {
            forwardPath = ((ThymeleafFinalState)target).getPath();
        }

        return forwardPath;
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForwardLogic#handleIsSuccessMessagesPresent()
     */
    protected boolean handleIsSuccessMessagesPresent()
    {
        return !this.getSuccessMessages().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForwardLogic#handleIsWarningMessagesPresent()
     */
    protected boolean handleIsWarningMessagesPresent()
    {
        return !this.getWarningMessages().isEmpty();
    }

    /**
     * Collects specific messages in a map.
     *
     * @param taggedValue the tagged value from which to read the message
     * @return maps message keys to message values, but only those that match the arguments
     *         will have been recorded
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getMessages(String taggedValue)
    {
        Map<String, String> messages;

        final Collection taggedValues = this.findTaggedValues(taggedValue);
        if (taggedValues.isEmpty())
        {
            messages = Collections.EMPTY_MAP;
        }
        else
        {
            messages = new LinkedHashMap<String, String>(); // we want to keep the order

            for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                final String value = (String)iterator.next();
                messages.put(StringUtilsHelper.toResourceMessageKey(value), value);
            }
        }

        return messages;
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForwardLogic#handleGetSuccessMessages()
     */
    protected Map handleGetSuccessMessages()
    {
        return this.getMessages(CartridgeWebProfile.TAGGEDVALUE_ACTION_SUCCESS_MESSAGE);
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForwardLogic#handleGetWarningMessages()
     */
    protected Map handleGetWarningMessages()
    {
        return this.getMessages(CartridgeWebProfile.TAGGEDVALUE_ACTION_WARNING_MESSAGE);
    }

    /**
     * @return getTarget() instanceof ThymeleafFinalState
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForward#isFinalStateTarget()
     */
    protected boolean handleIsFinalStateTarget()
    {
        return this.getTarget() instanceof ThymeleafFinalState;
    }

    /**
     * @return getName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafForward#getFromOutcome()
     */
    protected String handleGetFromOutcome()
    {
        return this.getName();
    }
}
