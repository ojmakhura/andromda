package org.andromda.cartridges.meta.tests;

/**
 * This is a class which serves as a test fixture for MethodDescriptorTest.
 * 
 * @see org.andromda.cartridges.meta.tests.MethodDescriptorTest
 */
public abstract class SampleClassForMethodDescriptorTest
{
	public void method0(String a1, String a2)
	{
	}
	
	public boolean method1(String a1, int a2)
	{
		return false;
	}

	public abstract boolean method2(int a1, Long a2) throws SampleExceptionForMethodDescriptorTest;

	public abstract boolean method3() throws SampleExceptionForMethodDescriptorTest;
}
