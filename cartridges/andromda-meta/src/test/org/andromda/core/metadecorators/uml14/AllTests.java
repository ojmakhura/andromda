/*
 * 
 * @since 29.11.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
package org.andromda.core.metadecorators.uml14;

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
            new TestSuite("Test for org.andromda.core.metadecorators.uml14");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(DecoratorFactoryTest.class));
        suite.addTest(new TestSuite(DecoratorSmallTest1.class));
        //$JUnit-END$
        return suite;
    }
}
