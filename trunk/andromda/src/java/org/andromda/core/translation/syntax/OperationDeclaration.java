package org.andromda.core.translation.syntax;


/**
 * Represents an operation declaration within an OCL
 * expression.
 * 
 * @author Chad Brandon
 */
public interface OperationDeclaration {
	
	/**
	 * The declared operation name 
	 * 
	 * @return String the name of the operation.
	 */
	public String getName();
	
	/**
	 * The declared return type.
	 * 
	 * @return String the type of the operation .
	 */
	public String getReturnType();
	
	/**
	 * Returns a VariableDeclaration array representing
	 * the arguments of the operation.
	 * 
	 * @return VariableDeclaration[] an array containing the declared arguments.
	 */
	public VariableDeclaration[] getArguments();
	
}
