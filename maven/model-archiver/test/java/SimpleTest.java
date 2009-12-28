import org.andromda.maven.plugin.modelarchiver.MojoUtils;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author: Overheat
 * Date: 28.12.2009
 * Time: 01:15:06
 */
public class SimpleTest
{
    @Test
    public void testNoReplace()
    {
        final String version = MojoUtils.escapePattern("3.4-SNAPSHOT");
        final String extension = MojoUtils.escapePattern(".xml");

        final String extensionPattern = "((\\-" + version + ")?)" + extension+"(['\"|])";
        final String newExtension = "\\-" + version + extension+"$3";

        String before = "abcdefgh.xml.defoir";
        String after  = before.replaceAll(extensionPattern, newExtension);

        assertEquals(before, after);
    }

    @Test
    public void testReplace_1()
    {
        final String version = MojoUtils.escapePattern("3.4-SNAPSHOT");
        final String extension = MojoUtils.escapePattern(".xml.zip");

        final String extensionPattern = "((\\-" + version + ")?)" + extension+"(['\"|])";
        final String newExtension = "\\-" + version + extension+"$3";

        String before = "<UML:Package href='andromda-profile-datatype.xml.zip'>";
        String after  = before.replaceAll(extensionPattern, newExtension);

        assertNotSame(before, after);
    }

    @Test
    public void testReplace_2()
    {
        final String version = MojoUtils.escapePattern("3.4-SNAPSHOT");
        final String extension = MojoUtils.escapePattern(".xml.zip");

        final String extensionPattern = "((\\-" + version + ")?)" + extension+"(['\"|])";
        final String newExtension = "\\-" + version + extension+"$3";

        String before = "<UML:Package href='andromda-profile-datatype.xml.zip|_9_0_1fe00f9_1119336925546_238419_77'>";
        String after  = before.replaceAll(extensionPattern, newExtension);

        assertNotSame(before, after);
    }
}
