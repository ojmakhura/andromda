package org.andromda.translation.ocl.syntax;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * An implementation of the ocl OperationDeclaration facade.
 *
 * @author Chad Brandon
 * @see org.andromda.translation.ocl.syntax.OperationDeclaration
 */
public class OperationDeclarationImpl
        implements OperationDeclaration
{

    private String name;
    private String returnType;
    private VariableDeclaration[] arguments = new VariableDeclaration[0];

    /**
     * Constructs a new OperationDeclarationImpl
     *
     * @param name       the name of the Operation
     * @param returnType the returnType of the operation
     * @param arguments  the arguments of the operation.
     */
    public OperationDeclarationImpl(String name, String returnType, VariableDeclaration[] arguments)
    {
        ExceptionUtils.checkEmpty("name", name);
        this.name = StringUtils.trimToEmpty(name);
        this.returnType = StringUtils.trimToEmpty(returnType);
        this.arguments = arguments;
    }

    /**
     * @see org.andromda.translation.ocl.syntax.OperationDeclaration#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @see org.andromda.translation.ocl.syntax.OperationDeclaration#getReturnType()
     */
    public String getReturnType()
    {
        return this.returnType;
    }

    /**
     * @see org.andromda.translation.ocl.syntax.OperationDeclaration#getArguments()
     */
    public VariableDeclaration[] getArguments()
    {
        return arguments;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuffer toString = new StringBuffer(this.getName());
        toString.append("(");
        if (this.getArguments().length > 0)
        {
            toString.append(StringUtils.join(this.getArguments(), ','));
        }
        toString.append(")");
        if (StringUtils.isNotEmpty(this.getReturnType()))
        {
            toString.append("::");
            toString.append(this.getReturnType());
        }
        return toString.toString();
    }

}