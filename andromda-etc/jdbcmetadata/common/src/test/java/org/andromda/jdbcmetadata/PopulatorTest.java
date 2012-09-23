/**
 *
 */
package org.andromda.jdbcmetadata;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class PopulatorTest
{
    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link org.andromda.jdbcmetadata.Populator#main(String[])}.
     */
    @Test
    @Ignore
    public void testMain()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.andromda.jdbcmetadata.Populator#populate()}.
     */
    @Test
    public void testPopulate()
    {
        Populator pop = new Populator();
        pop.setConfiguration();
        pop.populate();
    }

    /**
     * Test method for {@link org.andromda.jdbcmetadata.Populator#setConfiguration()}.
     */
    @Test
    @Ignore
    public void testSetConfiguration()
    {
        fail("Not yet implemented");
    }
}
