package org.andromda.cartridges.jsf.validator;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.MissingResourceException;

import javax.faces.context.FacesContext;

import org.andromda.cartridges.jsf.Messages;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;


/**
 * Retrieves and formats the validation messages.
 *
 * @author Chad Brandon
 */
public class ValidatorMessages
{
    /**
     * Gets the <code>message</code> based on the <code>action</code>
     * message and the <code>field</code>'s arg objects.
     *
     * @param action Validator action
     * @param args any message arguments to be substituted.
     * @param context the faces context
     * @return action message
     */
    public static String getMessage(
        final ValidatorAction action,
        final String[] args,
        final FacesContext context)
    {
        final Locale locale = context.getViewRoot().getLocale();
        String message = null;
        final String messageKey = action.getMsg();
        if (message == null)
        {
            try
            {
                message = Messages.get(
                        messageKey,
                        args);
            }
            catch (final MissingResourceException exception)
            {
                message = messageKey;
            }
        }
        message = new MessageFormat(
                message,
                locale).format(args);
        return message;
    }

    /**
     * Gets the message given the action, field and faces context.
     *
     * @param action the validator action instance.
     * @param field the field.
     * @param context the faces context.
     * @return the message
     */
    public static String getMessage(
        final ValidatorAction action,
        final Field field,
        final FacesContext context)
    {
        return getMessage(
            action,
            getArgs(
                action.getName(),
                field),
            context);
    }

    /**
     * Gets the message arguments based on the given
     * validator <code>action</code> and <code>field</code>.
     * @param action action name
     * @param field the validator field
     * @return message arguments
     */
    public static String[] getArgs(
        final String action,
        final Field field)
    {
        final Arg[] args = field.getArgs(action);
        final String[] argMessages = new String[args.length];
        for (int ctr = 0; ctr < args.length; ctr++)
        {
            final Arg arg = args[ctr];
            if (arg != null)
            {
                if (arg.isResource())
                {
                    argMessages[ctr] = Messages.get(
                            arg.getKey(),
                            null);
                }
                else
                {
                    argMessages[ctr] = arg.getKey();
                }
            }
        }
        return argMessages;
    }
}