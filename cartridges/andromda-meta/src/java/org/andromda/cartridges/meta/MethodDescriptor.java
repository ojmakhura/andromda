package org.andromda.cartridges.meta;


/**
 * Represents a method in a programming language.
 */
public class MethodDescriptor implements Comparable
{
    private MethodData method;
	
    /**
     * Creates a method descriptor from the properties of a method. 
     */
    public MethodDescriptor(MethodData m)
    {
        this.method = m;
    }

	/**
	 * @return a string representing a declaration for this method
	 */
	public String getDeclaration(boolean suppressAbstractDeclaration)
	{
		return method.buildMethodDeclaration(suppressAbstractDeclaration);
	}

	/**
	 * Indicates if method has a return type other than "void".
	 * @return boolean
	 */
	public boolean hasReturnType()
	{
		String returnTypeName = method.getReturnTypeName();
        return (returnTypeName != null)
			&& !("void".equals(returnTypeName));
	}

	/**
	 * Returns a string representing a call to this method.
	 * @return call string
	 */
	public String getMethodCall()
	{
		return method.buildMethodCall();
	}

    /**
     * Returns a signature which can be used as a key into a map.
     * Consists of the return type, the name and the f.q. types
     * of the arguments.
     * 
     * @return String the key that identifies this method
     */
    public String getCharacteristicKey()
    {
        return method.buildCharacteristicKey();
    }
    
	/**
	 * Returns the name of the interface in which the method was declared.
     * @return name
     */
    public String getParentInterfaceName()
	{
		return method.getInterfaceName();
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
