package org.andromda.cartridges.bpm4struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Contains utilities for bpm4struts.
 * 
 * @author Wouter Zoons
 */
public class Bpm4StrutsUtils
{
    /**
     * Creates and returns a List from an <code>enumeration</code>.
     * 
     * @param enumeration the enumeration from which to create the List.
     * 
     * @return the new List.
     */
    public static List listEnumeration(Enumeration enumeration)
    {
        return Collections.list(enumeration);
    }

    private static final Pattern VALIDATOR_TAGGEDVALUE_PATTERN = Pattern.compile("\\w+(\\(\\w+=[^,)]*(,\\w+=[^,)]*)*\\))?");

    /**
     * Reads the validator arguments from the the given tagged value.
     *
     * @return never null, returns a list of String instances
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List parseValidatorArgs(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: "+validatorTaggedValue);
        }

        final List validatorArgs = new ArrayList();

        // only keep what is between parentheses (if any)
        int left = validatorTaggedValue.indexOf('(');
        if (left > -1)
        {
            int right = validatorTaggedValue.indexOf(')');
            validatorTaggedValue = validatorTaggedValue.substring(left+1,right);

            String[] pairs = validatorTaggedValue.split(",");
            for (int i = 0; i < pairs.length; i++)
            {
                String pair = pairs[i];
                int equalsIndex = pair.indexOf('=');
                // it's possible the argument is the empty string
                if (equalsIndex < pair.length()-1)
                {
                    validatorArgs.add(pair.substring(equalsIndex+1));
                }
                else
                {
                    validatorArgs.add("");
                }
            }
        }
        return validatorArgs;
    }

    /**
     * Reads the validator variable names from the the given tagged value.
     *
     * @return never null, returns a list of String instances
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List parseValidatorVars(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: "+validatorTaggedValue);
        }

        final List validatorVars = new ArrayList();

        // only keep what is between parentheses (if any)
        int left = validatorTaggedValue.indexOf('(');
        if (left > -1)
        {
            int right = validatorTaggedValue.indexOf(')');
            validatorTaggedValue = validatorTaggedValue.substring(left+1,right);

            String[] pairs = validatorTaggedValue.split(",");
            for (int i = 0; i < pairs.length; i++)
            {
                String pair = pairs[i];
                int equalsIndex = pair.indexOf('=');
                validatorVars.add(pair.substring(0,equalsIndex));
            }
        }
        return validatorVars;
    }

    /**
     * Parses the validator name for a tagged value.
     *
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static String parseValidatorName(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: "+validatorTaggedValue);
        }

        int leftParen = validatorTaggedValue.indexOf('(');
        return (leftParen==-1) ? validatorTaggedValue : validatorTaggedValue.substring(0,leftParen);
    }
}
