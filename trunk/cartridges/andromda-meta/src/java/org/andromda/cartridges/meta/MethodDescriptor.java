package org.andromda.cartridges.meta;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Represents a method in Java.
 */
public class MethodDescriptor implements Comparable
{
    private Method method;
	private String interfaceName;
	
    /**
     * Creates a method descriptor from the properties of a method. 
     */
    public MethodDescriptor(Method m, String interfaceName)
    {
        this.method = m;
        this.interfaceName = interfaceName;
    }

	/**
	 * @return a string representing a declaration for this method
	 */
	public String getDeclaration(boolean suppressAbstractDeclaration)
	{
		return buildMethodDeclaration(method, suppressAbstractDeclaration);
	}

	/**
	 * Indicates if method has a return type other than "void".
	 * @return boolean
	 */
	public boolean hasReturnType()
	{
		return (method.getReturnType() != null)
			&& !("void".equals(typename(method.getReturnType())));
	}

	/**
	 * Returns a string representing a call to this method.
	 * @return call string
	 */
	public String getMethodCall()
	{
		return buildMethodCall(method);
	}

	/**
	 * Returns the name of the interface in which the method was declared.
     * @return name
     */
    public String getParentInterfaceName()
	{
		return interfaceName;
	}
	
	
    /**
     * Builds a string representing a call to the method.
     * @param m the method
     * @return String how a call would look like
     */
    private String buildMethodCall(Method m)
    {
        Class returntype = m.getReturnType();
        Class parameters[] = m.getParameterTypes();
        Class exceptions[] = m.getExceptionTypes();

        String call = m.getName() + " (";

        for (int i = 0; i < parameters.length; i++)
        {
            if (i > 0)
                call += ", ";
            call += "p" + i;
        }
        call += ")";
        return call;
    }

    /**
     * Builds a string representing a declaration for this method.
     * @param m the method
     * @return String the declaration
     */
    private String buildMethodDeclaration(Method m, boolean suppressAbstractDeclaration)
    {
        Class returntype = m.getReturnType();
        Class parameters[] = m.getParameterTypes();
        Class exceptions[] = m.getExceptionTypes();

        String declaration =
            modifiers(m.getModifiers() & ~(suppressAbstractDeclaration ? Modifier.ABSTRACT : 0))
                + ((returntype != null) ? typename(returntype) + " " : "")
                + m.getName()
                + "(";

        for (int i = 0; i < parameters.length; i++)
        {
            if (i > 0)
                declaration += ", ";
            declaration += typename(parameters[i]);
            declaration += " p" + i;
        }
        declaration += ")";

        if (exceptions.length > 0)
        {
            declaration += " throws ";
            for (int i = 0; i < exceptions.length; i++)
            {
                if (i > 0)
                    declaration += ", ";
                declaration += typename(exceptions[i]);
            }
        }

        return declaration;
    }

    /** Return the name of an interface or primitive type, handling arrays. */
    private static String typename(Class t)
    {
        String brackets = "";
        while (t.isArray())
        {
            brackets += "[]";
            t = t.getComponentType();
        }
        return t.getName() + brackets;
    }

    /** Return a string version of modifiers, handling spaces nicely. */
    private static String modifiers(int m)
    {
        if (m == 0)
            return "";
        else
            return Modifier.toString(m) + " ";
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg)
    {
        MethodDescriptor other = (MethodDescriptor)arg;
        int diff = getParentInterfaceName().compareTo(other.getParentInterfaceName());
        if (diff != 0) {
        	return diff;
        }
        return method.getName().compareTo(other.method.getName());
    }

}
