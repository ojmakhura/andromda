package org.andromda.core.translation.syntax.impl;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.syntax.OperationDeclaration;
import org.andromda.core.translation.syntax.VariableDeclaration;
import org.apache.commons.lang.StringUtils;

/**
 * An implementation of the ocl OperationDeclaration facade.
 * 
 * @see org.andromda.core.translation.syntax.OperationDeclaration
 * 
 * @author Chad Brandon
 */
public class OperationDeclarationImpl implements OperationDeclaration {

	private String name;
	private String returnType;
	private VariableDeclaration[] arguments = new VariableDeclaration[0];

	/**
	 * Constructs a new OperationDeclarationImpl
	 * 
	 * @param name the name of the Operation
	 * @param returnType the returnType of the operation
	 * @param arguments the arguments of the operation.
	 */
	public OperationDeclarationImpl(
		String name, 
		String returnType, 
		VariableDeclaration[] arguments) {
		final String methodName = "OperationDeclarationImpl";
		ExceptionUtils.checkEmpty(methodName, "name", name);
		this.name = StringUtils.trimToEmpty(name);
		this.returnType = StringUtils.trimToEmpty(returnType);
		this.arguments = arguments;
	}
	
    /**
     * @see org.andromda.core.translation.concretesyntax.OperationDeclaration#getName()
     */
	public String getName() {
		return this.name;
	}
	
    /**
     * @see org.andromda.core.translation.concretesyntax.OperationDeclaration#getReturnType()
     */
	public String getReturnType() {
		return this.returnType;
	}

    /**
     * @see org.andromda.core.translation.concretesyntax.OperationDeclaration#getArguments()
     */
	public VariableDeclaration[] getArguments() {
		return arguments;
	}

    /**
     * @see java.lang.Object#toString()
     */
	public String toString() { 
        StringBuffer toString = new StringBuffer(this.getName());
        toString.append("(");
        if (this.getArguments().length > 0) {
            toString.append(StringUtils.join(this.getArguments(), ','));
        }
        toString.append(")");
        if (StringUtils.isNotEmpty(this.getReturnType())) {
            toString.append(":");
            toString.append(this.getReturnType());
        }
		return toString.toString();
	}

}
