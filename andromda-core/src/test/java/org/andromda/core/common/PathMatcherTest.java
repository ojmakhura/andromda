package org.andromda.core.common;

import junit.framework.TestCase;


/**
 * JUnit tests for {@link org.andromda.core.common}
 *
 * @author Chad Brandon
 */
public class PathMatcherTest
    extends TestCase
{
    public void testWildcardMatch()
    {
        String path = "org/andromda/some/file/Test.java";
        assertTrue(PathMatcher.wildcardMatch(path, "**/*.java"));
        assertFalse(PathMatcher.wildcardMatch(path, "*.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "*/*/*/*/*.java"));
        assertFalse(PathMatcher.wildcardMatch(path, "*/*/*.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "**/*Test.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "**/*Tes*.java"));
        assertFalse(PathMatcher.wildcardMatch(path, "**/*TestFile*.java"));
        assertFalse(PathMatcher.wildcardMatch(path, "**/.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "org/andromda/**/*"));

        path = "Test.java";
        assertTrue(PathMatcher.wildcardMatch(path, "*.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "**/*.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "**/*"));

        assertTrue(PathMatcher.wildcardMatch(path, "*.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "**.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "**/*"));
        assertFalse(PathMatcher.wildcardMatch(path, "***/*.java"));

        path = "org/Test.java";
        assertFalse(PathMatcher.wildcardMatch(path, "*.java"));
        assertTrue(PathMatcher.wildcardMatch(path, "**/*"));
    }
}