package org.andromda.core.translation.syntax.impl;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.syntax.VariableDeclaration;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An implementation of the ocl VariableDeclaration.
 * 
 * @see org.andromda.core.translation.syntax.VariableDeclaration
 * 
 * @author Chad Brandon
 */
public class VariableDeclarationImpl implements VariableDeclaration {

	private String name;
	private String type;
	private String value;

	/**
	 * Constructs a new VariableDeclarationImpl
	 * 
	 * @param name the name of the VariableDeclaratiom
	 * @param type the type of the VariableDeclarationCS
	 */
	public VariableDeclarationImpl(String name, String type, String value) {
		String methodName = "VariableDeclarationImpl";
		ExceptionUtils.checkNull(methodName, "name", name);
		this.name = name;
		this.type = type;
		this.value = value;
	}
    
    /**
     * @see org.andromda.core.translation.concretesyntax.VariableDeclarationCS#getName()
     */
	public String getName() {
		return this.name;
	}
	
    /**
     * @see org.andromda.core.translation.concretesyntax.VariableDeclarationCS#getType()
     */
	public String getType() {
		return this.type;
	}
	
    /**
     * @see org.andromda.core.translation.concretesyntax.VariableDeclarationCS#getValue()
     */
	public String getValue() {
		return this.value;
	}

    /**
     * @see java.lang.Object#toString()
     */
	public String toString() { 
		return ToStringBuilder.reflectionToString(this);
	}

}
