package org.andromda.core.translation.syntax;

/**
 * Represents a variable declaration within an OCL
 * expression.
 * 
 * @author Chad Brandon
 */
public interface VariableDeclaration {
	
	/**
	 * The variable declaration name 
	 * 
	 * @return String the name of the variable declaration.
	 */
	public String getName();
	
	/**
	 * The variable declaration type.
	 * 
	 * @return String the type of the variable declaration.
	 */
	public String getType();
	
	/**
	 * The variable declaration initial value.
	 * 
	 * @return String the initial value.
	 */
	public String getValue();
	
}
