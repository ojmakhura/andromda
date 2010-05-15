package org.andromda.maven.plugin.modelarchiver;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Simple test for replacement method
 *
 * @author Plushnikov Michail
 *         Date: 28.12.2009
 *         Time: 01:15:06
 */
public class SimpleTest
{
    private BaseArchiveMojo mojo = new XmiZipArchiverMojo();

    @Test
    public void testNoReplace()
    {
        final String version = mojo.escapePattern("3.4-SNAPSHOT");
        final String extension = mojo.escapePattern(".xml");

        final String extensionPattern = "((\\-" + version + ")?)" + extension + "(['\"|])";
        final String newExtension = "\\-" + version + extension + "$3";

        String before = "abcdefgh.xml.defoir";
        String after = before.replaceAll(extensionPattern, newExtension);

        assertEquals(before, after);
    }

    @Test
    public void testReplace_1()
    {
        final String version = mojo.escapePattern("3.4-SNAPSHOT");
        final String extension = mojo.escapePattern(".xml.zip");

        final String extensionPattern = "((\\-" + version + ")?)" + extension + "(['\"|])";
        final String newExtension = "\\-" + version + extension + "$3";

        String before = "<UML:Package href='andromda-profile-datatype.xml.zip'>";
        String after = before.replaceAll(extensionPattern, newExtension);

        assertNotSame(before, after);
    }

    @Test
    public void testReplace_2()
    {
        final String version = mojo.escapePattern("3.4-SNAPSHOT");
        final String extension = mojo.escapePattern(".xml.zip");

        final String extensionPattern = "((\\-" + version + ")?)" + extension + "(['\"|])";
        final String newExtension = "\\-" + version + extension + "$3";

        String before = "<UML:Package href='andromda-profile-datatype.xml.zip|_9_0_1fe00f9_1119336925546_238419_77'>";
        String after = before.replaceAll(extensionPattern, newExtension);

        assertNotSame(before, after);
    }
}
