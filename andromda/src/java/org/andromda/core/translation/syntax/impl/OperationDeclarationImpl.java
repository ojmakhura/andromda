package org.andromda.core.translation.syntax.impl;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.syntax.Operation;
import org.andromda.core.translation.syntax.VariableDeclaration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An implementation of the ocl OperationDeclaration facade.
 * 
 * @see org.andromda.core.translation.syntax.Operation
 * 
 * @author Chad Brandon
 */
public class OperationDeclarationImpl implements Operation {

	private String name;
	private String returnType;
	private VariableDeclaration[] parameters = new VariableDeclaration[0];

	/**
	 * Constructs a new OperationDeclarationImpl
	 * 
	 * @param name the name of the Operation
	 * @param returnType the returnType of the operation
	 * @param parameters the parameters of the operation.
	 */
	public OperationDeclarationImpl(
		String name, 
		String returnType, 
		VariableDeclaration[] parameters) {
		String methodName = "OperationDeclarationImpl";
		ExceptionUtils.checkEmpty(methodName, "name", name);
		this.name = StringUtils.trimToEmpty(name);
		this.returnType = StringUtils.trimToEmpty(returnType);
		this.parameters = parameters;
	}
	
    /**
     * @see org.andromda.core.translation.concretesyntax.Operation#getName()
     */
	public String getName() {
		return this.name;
	}
	
    /**
     * @see org.andromda.core.translation.concretesyntax.Operation#getReturnType()
     */
	public String getReturnType() {
		return this.returnType;
	}

    /**
     * @see org.andromda.core.translation.concretesyntax.Operation#getParameters()
     */
	public VariableDeclaration[] getParameters() {
		return parameters;
	}

    /**
     * @see java.lang.Object#toString()
     */
	public String toString() { 
		return ToStringBuilder.reflectionToString(this);
	}

}
