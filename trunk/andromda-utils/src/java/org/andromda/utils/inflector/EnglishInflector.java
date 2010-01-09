package org.andromda.utils.inflector;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Language utility for transforming English words.
 * See also <a href="http://www.csse.monash.edu.au/~damian/papers/HTML/Plurals.html">
 * http://www.csse.monash.edu.au/~damian/papers/HTML/Plurals.html</a>
 *
 * @author maetl@coretxt.net.nz
 * @author wouter@andromda.org
 * @author bob@andromda.org
 */
public class EnglishInflector
{
    /**
     * Converts an English word to plural form. Leaves it alone if already plural.
     *
     * @param word an English word
     * @return the pluralization of the argument English word, or the argument in case it is <code>null</code>
     */
    public static String pluralize(String word)
    {
        if (word == null) return null;

        final Map<String, String> rules = EnglishInflector.getPluralRules();
        for (final Map.Entry<String, String> rule : rules.entrySet())
        {
            final String pattern = rule.getKey();
            final String replace = rule.getValue();
            if (word.matches(pattern))
            {
                return word.replaceFirst(pattern, replace);
            }
        }
        return word.replaceFirst("([\\w]+)([^s])$", "$1$2s");
    }

    /**
     * Returns map of plural patterns
     */
    private static Map<String, String> getPluralRules()
    {
        // Rules are checked in order through LinkedHashMap.
        final Map<String, String> rules = new LinkedHashMap<String, String>();
        // Check first if special case word is already plural
        rules.put("(\\w*)people$", "$1people");
        rules.put("(\\w*)children$", "$1children");
        rules.put("(\\w*)feet$", "$1feet");
        rules.put("(\\w*)teeth$", "$1teeth");
        rules.put("(\\w*)men$", "$1men");
        rules.put("(\\w+)(es)$", "$1es");
        // Check exception special case words
        rules.put("(\\w*)person$", "$1people");
        rules.put("(\\w*)child$", "$1children");
        rules.put("(\\w*)foot$", "$1feet");
        rules.put("(\\w*)tooth$", "$1teeth");
        rules.put("(\\w*)bus$", "$1buses");
        rules.put("(\\w*)man$", "$1men");
        // Greek endings
        rules.put("(\\w+)(sis)$", "$1ses");
        // Old English. hoof -> hooves, leaf -> leaves
        rules.put("(\\w*)(fe)$", "$1ves");
        rules.put("(\\w*)(f)$", "$1ves");
        // Y preceeded by a consonant changes to ies
        rules.put("(\\w+)([^aeiou])y$", "$1$2ies");
        // Voiced consonants add es instead of s
        rules.put("(\\w+)(z|ch|ss|sh|x)$", "$1$2es");
        // If nothing else matches, and word ends in s, assume plural already
        rules.put("(\\w+)(s)$", "$1s");
        // Otherwise, just add s at the end in pluralize()
        return rules;
    }

    /**
     * Converts an English word to singular form. Leaves it alone if already singular.
     *
     * @param word an English word
     * @return the singularization of the argument English word, or the argument in case it is <code>null</code>
     */
    public static String singularize(String word)
    {
        if (word == null) return null;

        final Map<String, String> rules = EnglishInflector.getSingularRules();
        for (final Map.Entry<String, String> rule : rules.entrySet())
        {
            final String pattern = rule.getKey();
            final String replace = rule.getValue();
            if (word.matches(pattern))
            {
                return word.replaceFirst(pattern, replace);
            }
        }
        return word.replaceFirst("([\\w]+)s$", "$1");
    }

    /**
     * Returns map of singular patterns
     */
    private static Map<String, String> getSingularRules()
    {
        final Map<String, String> rules = new LinkedHashMap<String, String>();
        rules.put("(\\w*)people$", "$1person");
        rules.put("(\\w*)children$", "$1child");
        rules.put("(\\w*)series$", "$1series");
        rules.put("(\\w*)feet$", "$1foot");
        rules.put("(\\w*)teeth$", "$1tooth");
        rules.put("(\\w*)buses$", "$1bus");
        rules.put("(\\w*)men$", "$1man");
        rules.put("(\\w*)person$", "$1person");
        rules.put("(\\w*)child$", "$1child");
        rules.put("(\\w*)foot$", "$1foot");
        rules.put("(\\w*)tooth$", "$1tooth");
        rules.put("(\\w*)bus$", "$1bus");
        rules.put("(\\w*)man$", "$1man");
        rules.put("(\\w+)(sis)$", "$1sis");
        rules.put("(\\w+)([ll])f", "$1$2f");
        rules.put("(\\w+)([^l])fe", "$1$2fe");
        rules.put("(\\w+)(ses)$", "$1sis");
        rules.put("(\\w+)([ll])ves", "$1$2f");
        rules.put("(\\w+)([^l])ves", "$1$2fe");
        rules.put("(\\w+)([^aeiou])y", "$1$2y");
        rules.put("(\\w+)([^aeiou])ies", "$1$2y");
        rules.put("(\\w+)(z|ch|ss|sh|x)es$", "$1$2");
        return rules;
    }

}

