/*
 * 
 * @since 29.11.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
package org.andromda.metafacades.uml14;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @since 29.11.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class AllTests
{

    public static Test suite()
    {
        TestSuite suite =
            new TestSuite("Test for org.andromda.core.metafacade");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(MetafacadeFactoryTest.class));
        suite.addTest(new TestSuite(FacadeSmallTest1.class));
        //$JUnit-END$
        return suite;
    }
}
