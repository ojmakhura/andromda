package org.andromda.core.translation.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.TranslationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * Represents a translation XML template found within a translation library.
 * 
 * @author Chad Brandon
 */
public class Translation {
	
	private static final Logger logger = Logger.getLogger(Translation.class);
	
	private String name;
	
	private Map fragments;
    
    private Collection ignorePatterns;
    
    private Collection validatePatterns;
	
	/**
	 * Constructs an instance of Translation.
	 */
	public Translation() {
		this.fragments = new HashMap();
        this.ignorePatterns = new ArrayList();
	}
	
	/**
	 * The library translation to which this translation belongs.
	 */
	private LibraryTranslation libraryTranslation;
	
	/**
	 * Gets the LibraryTranslation to which this 
	 * Translation belongs.
	 * @return LibraryTranslation
	 */
	protected LibraryTranslation getLibraryTranslation() {
		final String methodName = "Translation.getLibraryTranslation";
		//should never happen, but it doesn't hurt to be safe
		if (this.libraryTranslation == null) {
			throw new LibraryException(methodName
				+ " - libraryTranslation can not be null");
		}
		return libraryTranslation;
	}

	/**
	 * Sets the LibraryTranslation to which this Translation
	 * belongs.
	 * 
	 * @param translation the LibraryTranslation to which this Translation belongs.
	 */
	protected void setLibraryTranslation(LibraryTranslation translation) {
		libraryTranslation = translation;
	}
	
	/**
     * Gets the fragment matching (using regular expressions) the specified name.
     * 
     * @param name the name of the fragment to retrieve.
     * 
	 * @return Fragment
	 */
	protected Fragment getFragment(String name) {
        Fragment fragment = null;
        Iterator names = fragments.keySet().iterator();
        //search through the names and the first name that matches
        //one of the names return the value of that name.
        while (names.hasNext()) {
            String nextName = (String)names.next(); 
        	if (name.matches(nextName)) {
                fragment = (Fragment)fragments.get(nextName);
            }
        }
        //if the fragment is null, and the name isn't in an ignorePattern
        //element, then give an error
        if (fragment == null && !this.isIgnorePattern(name)) {
            // TODO: make this work correctly with unsupported functions.
            /*logger.error("ERROR! expression fragment '" 
                + name 
                + "' is not currently supported --> add a <fragment/> with "
                + " a name that matches this expression to your translation file "
                + "'" + this.getLibraryTranslation().getFile()
                + "' to enable support");  */   
        }
		return fragment;
	}

	/**
	 * Adds a new Translation fragment to the Translation.
	 * 
	 * @param fragment
	 */
	public void addFragment(Fragment fragment) {
		final String methodName = "Translation.addFragment";
		ExceptionUtils.checkNull(methodName, "fragment", fragment);
		fragment.setTranslation(this);
		this.fragments.put(fragment.getName(), fragment);
	}

	/**
	 * Gets the name of this Translation.
	 * @return String
	 */
	protected String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	protected void setName(String name) {
		this.name = name;
	}
    
    /**
     * Adds an <code>ignorePattern</code> to 
     * the Collection of ignorePatterns.
     * 
     * @param ignorePattern the pattern to ignore.
     */
    public void addIgnorePattern(String ignorePattern) {
        this.ignorePatterns.add(StringUtils.trimToEmpty(ignorePattern));
    }
    
    /**
     * Adds an <code>validatePattern</code> to 
     * the Collection of validatePatterns.
     * 
     * @param validatePattern the pattern to validate.
     */
    public void addValidatePattern(String validatePattern) {
        this.validatePatterns.add(StringUtils.trimToEmpty(validatePattern));
    }
    
    /**
     * Checks to see if the pattern is an ignore pattern.
     * What this means is that if if this pattern matches
     * on a regular expression found in the collection of ignore patterns
     * then the TranslationLibrary won't complain if it doesn't match 
     * a fragment name.
     * @param pattern
     * @return boolean <code>true</code> if its an ignore pattern, <code>false</code> otherwise.
     */
    public boolean isIgnorePattern(String pattern) {
        boolean isIgnorePattern = false;
        pattern = StringUtils.trimToEmpty(pattern);
        Iterator ignorePatterns = this.ignorePatterns.iterator();
        //search through the ignorePatterns and see if one
        //of them matches the passed in pattern.
        while (ignorePatterns.hasNext()) {
            String nextIgnorePattern =
                StringUtils.trimToEmpty((String)ignorePatterns.next()); 
            isIgnorePattern = pattern.matches(nextIgnorePattern);
            if (isIgnorePattern) {
            	break;
            }
        }
        return isIgnorePattern;
    }
	
	/**
	 * Gets the "translated" value of this Fragment if it
	 * exists.  That is, it retrieves the fragment
	 * body for the name of this fragment and replaces
	 * any fragment references with other fragment bodies
	 * (if they exist)
	 * @param name the name of the fragment.
	 * @param kind the kind of the fragment.
	 * @return String the translated body of the fragment kind.
	 */
	protected String getTranslated(String name, String kind) {
		final String methodName = "Translation.getTranslated";
		if (logger.isDebugEnabled()) 
			logger.debug("performing " + methodName + 
				" with name '" + name + "' and kind '" + kind + "'");
		
		//clean the strings first
		name = StringUtils.trimToEmpty(name);
		kind = StringUtils.trimToEmpty(kind);
		
		ExceptionUtils.checkEmpty(methodName, "name", name);
		
		Fragment fragment = this.getFragment(name);
		String translated = "";
		if (fragment != null) {
			translated = fragment.getKind(kind);
			String begin = "fragment{";
			int beginLength = begin.length();
			String end = "}";
			for (int beginIndex = translated.indexOf(begin); 
				 beginIndex != -1; 
				 beginIndex = translated.indexOf(begin)) {
				String fragmentName = 
					translated.substring(beginIndex + beginLength, translated.length());
				int endIndex = fragmentName.indexOf(end);
				if (endIndex != -1) {
					fragmentName = fragmentName.substring(0, endIndex);
				}
				StringBuffer toReplace = new StringBuffer(begin);
				toReplace.append(fragmentName);
				toReplace.append(end);
				translated = StringUtils.replace(
					translated,
					toReplace.toString(), 
					this.getTranslated(fragmentName, kind));
			}
 		}
 		//TODO: make output more readable than everything on one line
		return TranslationUtils.removeExtraWhitespace(translated);
	}
	
    /**
     * @see java.lang.Object#toString()
     */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
    
}
