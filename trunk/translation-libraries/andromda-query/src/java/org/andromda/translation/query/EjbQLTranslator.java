package org.andromda.translation.query;

import java.util.HashMap;
import java.util.Map;

import org.andromda.core.translation.TranslationUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Performs translation to the following:   
 * <ul> 
 *     <li>
 *          EJB-QL
 *     </li>
 * </ul>
 * @author Chad Brandon
 */
public class EjbQLTranslator extends QueryTranslator {
    
    /**
     * Used to replace the 'counter' reference in the EJB-QL template
     */
    private static final String ARG_COUNTER = "counter";

	/**
	 * Used to represent an incrementing argument numbers.
	 */
	private short argCounter;
    
    /**
     * Holds the arguments which have previously been
     * used during translation. The key is the argument name
     * BEFORE translation, and the value is the argument name
     * AFTER translation.
     */
	private Map usedArguments = new HashMap();
    
	/**
	 * Called by super class to reset any objects.
	 */
	protected void preProcess() {
		super.preProcess();
        this.usedArguments.clear();
        this.resetArgCounter();
	}
    
    /**
     * Resets the argCounter variable
     * to its beginning value.
     */
    private void resetArgCounter() {
    	this.argCounter = 1;
    }

	/**
	 * Returns a String representing an incrementing number. It increments and
	 * returns the next value each time this method is called.
	 * 
	 * @return String the counter represented by a String.
	 */
	protected String getCounter() {
		return String.valueOf(argCounter++);
	}

	/**
	 * Checks to see if the replacement is an argument and if so replaces the
	 * {index} in the fragment with the 'argument' fragment from the template.
	 * Otherwise replaces the {index} with the passed in replacement value.
	 * 
	 * @param fragment
	 * @param replacement
	 * @param index
	 * @return String the fragment with any replacements.
	 */
	protected String replaceFragment(
		String fragment,
		String replacement,
		int index) {		
        if (this.isArgument(replacement)) {
            //get the used argument and if it exists, use that for the
            //replacement, otherwise use a new one.
            String usedArgument = (String)this.usedArguments.get(replacement);
            if (StringUtils.isEmpty(usedArgument)) {
                String argument = this.getTranslationFragment("argument");
                argument = this.replaceCounterPattern(argument);
                this.usedArguments.put(replacement, argument);
                replacement = argument;
            } else {
                replacement = usedArgument;
            }
        }
        fragment = super.replaceFragment(fragment, replacement, index);
		return fragment;
	}

	/**
	 * Handles the replacemenht of the references to 'counter' with the
	 * incrementing counter (currently just used for EJB-QL translation) -->
	 * may want to find a cleaner way to do this.
	 */
	protected String replaceCounterPattern(String fragment) {
		if (TranslationUtils.containsPattern(fragment, EjbQLTranslator.ARG_COUNTER)) {
			fragment =
				TranslationUtils.replacePattern(
					fragment,
					EjbQLTranslator.ARG_COUNTER,
					this.getCounter());
		}
		return fragment;
	}
}
