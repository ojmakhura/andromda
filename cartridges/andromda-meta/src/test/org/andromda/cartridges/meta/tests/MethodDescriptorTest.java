package org.andromda.cartridges.meta.tests;

import java.lang.reflect.Method;

import org.andromda.cartridges.meta.MethodDescriptor;

import junit.framework.TestCase;

public class MethodDescriptorTest extends TestCase
{
    private Method[] methods;

    /**
     * Constructor for MethodDescriptorTest.
     * @param arg0
     */
    public MethodDescriptorTest(String arg0)
    {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        methods = SampleClassForMethodDescriptorTest.class.getMethods();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testGetDeclaration()
    {
		assertEquals(
			"public void method0(java.lang.String p0, java.lang.String p1)",
			new MethodDescriptor(methods[0], null).getDeclaration(true));
		assertEquals(
			"public void method0(java.lang.String p0, java.lang.String p1)",
			new MethodDescriptor(methods[0], null).getDeclaration(false));

        assertEquals(
            "public boolean method1(java.lang.String p0, int p1)",
            new MethodDescriptor(methods[1], null).getDeclaration(true));
		assertEquals(
			"public boolean method1(java.lang.String p0, int p1)",
			new MethodDescriptor(methods[1], null).getDeclaration(false));

		assertEquals(
			"public boolean method2(int p0, java.lang.Long p1) throws "
				+ SampleExceptionForMethodDescriptorTest.class.getName(),
			new MethodDescriptor(methods[2], null).getDeclaration(true));
		assertEquals(
			"public abstract boolean method2(int p0, java.lang.Long p1) throws "
				+ SampleExceptionForMethodDescriptorTest.class.getName(),
			new MethodDescriptor(methods[2], null).getDeclaration(false));

		assertEquals(
			"public boolean method3() throws "
				+ SampleExceptionForMethodDescriptorTest.class.getName(),
			new MethodDescriptor(methods[3], null).getDeclaration(true));
		assertEquals(
			"public abstract boolean method3() throws "
				+ SampleExceptionForMethodDescriptorTest.class.getName(),
			new MethodDescriptor(methods[3], null).getDeclaration(false));
    }

    public void testHasReturnType()
    {
		assertTrue("method0 should not have a return type", !new MethodDescriptor(methods[0], null).hasReturnType());
		assertTrue("method1 should have a return type", new MethodDescriptor(methods[1], null).hasReturnType());
		assertTrue("method2 should have a return type", new MethodDescriptor(methods[2], null).hasReturnType());
		assertTrue("method3 should have a return type", new MethodDescriptor(methods[3], null).hasReturnType());
    }

    public void testGetMethodCall()
    {
		assertEquals("method0 (p0, p1)", new MethodDescriptor(methods[0], null).getMethodCall());
		assertEquals("method1 (p0, p1)", new MethodDescriptor(methods[1], null).getMethodCall());
		assertEquals("method2 (p0, p1)", new MethodDescriptor(methods[2], null).getMethodCall());
		assertEquals("method3 ()", new MethodDescriptor(methods[3], null).getMethodCall());
    }

}
