package org.andromda.core.common;

import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;


/**
 * Provides wild card matching on file paths (i.e. Cartridge.java will match <code>*.java</code>, etc).
 *
 * @author Chad Brandon
 */
public class PathMatcher
{
    /**
     * Provides matching of simple wildcards. (i.e. '*.java' etc.)
     *
     * @param path the path to match against.
     * @param pattern the pattern to check if the path matches.
     * @return true if the <code>path</code> matches the given <code>pattern</code>, false otherwise.
     */
    public static boolean wildcardMatch(
        String path,
        String pattern)
    {
        path = StringUtils.trimToEmpty(path);
        boolean matches = false;
        final String doubleStar = "**/";
        final String slash = "/";
        pattern = StringUtils.replace(pattern, ".", "\\.");
        boolean matchAll = pattern.startsWith(doubleStar);
        if (matchAll)
        {
            String replacement = ".*/";
            if (path.indexOf(slash) == -1)
            {
                replacement = ".*";
            }
            pattern = StringUtils.replaceOnce(pattern, doubleStar, replacement);
        }
        pattern = StringUtils.replace(pattern, "*", ".*");
        try
        {
            matches = path.matches(pattern);
        }
        catch (final PatternSyntaxException exception)
        {
            matches = false;
        }
        if (!matchAll)
        {
            matches = matches && StringUtils.countMatches(pattern, slash) == StringUtils.countMatches(path, slash);
        }
        return matches;
    }
}