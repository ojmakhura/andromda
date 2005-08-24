package org.andromda.cartridges.bpm4jsf.components.validator;

import java.text.MessageFormat;

import org.andromda.cartridges.bpm4jsf.components.Messages;
import org.apache.commons.lang.StringUtils;
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
     * Gets the <code>message</code> based on the <code<action</code>
     * message and the <code>field</code>'s arg objects.
     * 
     * @param report the servlet report
     * @param action Validator action
     * @param field the validator Field
     */
    public static String getMessage(
        ValidatorAction action,
        Field field)
    {
        String args[] = getArgs(action.getName(), field);

        String message = field.getMsg(action.getName()) != null
            ? field.getMsg(action.getName())
            : action.getMsg();

        String resourceMessage = Messages.get(message, null); 
        if (StringUtils.isNotBlank(resourceMessage))
        {
            resourceMessage = MessageFormat.format(resourceMessage, args);
            message = resourceMessage;
        }

        return message;
    }
    
    /**
     * Gets the message arguments based on the given
     * validator <code>action</code> and <code>cield</code>.
     * @param action action name
     * @param field the validator field
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