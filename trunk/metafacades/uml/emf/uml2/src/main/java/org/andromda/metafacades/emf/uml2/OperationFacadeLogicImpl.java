package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.translation.ocl.ExpressionKinds;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.CallConcurrencyKind;
import org.eclipse.uml2.Parameter;
import org.eclipse.uml2.ParameterDirectionKind;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.OperationFacade.
 *
 * @see org.andromda.metafacades.uml.OperationFacade
 */
public class OperationFacadeLogicImpl
    extends OperationFacadeLogic
{
    public OperationFacadeLogicImpl(
        org.eclipse.uml2.Operation metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.OPERATION_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature()
     */
    protected java.lang.String handleGetSignature()
    {
        return this.getSignature(true);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getCall()
     */
    protected java.lang.String handleGetCall()
    {
        return this.getCall(this.getName());
    }

    /**
     * Constructs the operation call with the given <code>name</code>
     *
     * @param name the name form which to construct the operation call.
     * @return the operation call.
     */
    private String getCall(String name)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name);
        buffer.append("(");
        buffer.append(this.getArgumentNames());
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList()
     */
    protected java.lang.String handleGetTypedArgumentList()
    {
        return this.getTypedArgumentList(true);
    }

    private String getTypedArgumentList(boolean withArgumentNames)
    {
        return this.getTypedArgumentList(
            withArgumentNames,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isStatic()
     */
    protected boolean handleIsStatic()
    {
        return metaObject.isStatic();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isAbstract()
     */
    protected boolean handleIsAbstract()
    {
        return metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList()
     */
    protected java.lang.String handleGetExceptionList()
    {
        return this.getExceptionList(null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptions()
     */
    protected java.util.Collection handleGetExceptions()
    {
        Collection exceptions = new HashSet();

        // finds both exceptions and exception references
        final class ExceptionFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                boolean hasException = object instanceof DependencyFacade;
                if (hasException)
                {
                    DependencyFacade dependency = (DependencyFacade)object;

                    // first check for exception references
                    hasException = dependency.hasStereotype("Exception");

                    // if there wasn't any exception reference
                    // now check for actual exceptions
                    if (!hasException)
                    {
                        ModelElementFacade targetElement = dependency.getTargetElement();
                        hasException = targetElement != null && targetElement.hasStereotype(("Exception"));
                    }
                }
                return hasException;
            }
        }

        // first get any dependencies on this operation's
        // owner (because these will represent the default exception(s))
        Collection ownerDependencies = this.getOwner().getSourceDependencies();
        if (ownerDependencies != null && !ownerDependencies.isEmpty())
        {
            CollectionUtils.filter(
                ownerDependencies,
                new ExceptionFilter());
            exceptions.addAll(ownerDependencies);
        }

        Collection operationDependencies = this.getSourceDependencies();

        // now get any exceptions directly on the operation
        if (operationDependencies != null && !operationDependencies.isEmpty())
        {
            CollectionUtils.filter(
                operationDependencies,
                new ExceptionFilter());
            exceptions.addAll(operationDependencies);
        }

        // now transform the dependency(s) to the actual exception(s)
        CollectionUtils.transform(
            exceptions,
            new Transformer()
            {
                public Object transform(Object object)
                {
                    return ((DependencyFacade)object).getTargetElement();
                }
            });
        return exceptions;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isReturnTypePresent()
     */
    protected boolean handleIsReturnTypePresent()
    {
        boolean hasReturnType = true;
        if (this.getReturnType() != null)
        {
            hasReturnType = !StringUtils.trimToEmpty(this.getReturnType().getFullyQualifiedName()).equals("void");
        }
        return hasReturnType;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isExceptionsPresent()
     */
    protected boolean handleIsExceptionsPresent()
    {
        return !this.getExceptions().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getArgumentNames()
     */
    protected java.lang.String handleGetArgumentNames()
    {
        StringBuffer buffer = new StringBuffer();

        Iterator iterator = metaObject.getOwnedParameters().iterator();

        boolean commaNeeded = false;
        while (iterator.hasNext())
        {
            Parameter parameter = (Parameter)iterator.next();

            if (!parameter.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL))
            {
                if (commaNeeded)
                {
                    buffer.append(", ");
                }
                buffer.append(parameter.getName());
                commaNeeded = true;
            }
        }
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getArgumentTypeNames()
     */
    protected java.lang.String handleGetArgumentTypeNames()
    {
        StringBuffer buffer = new StringBuffer();

        Iterator iterator = metaObject.getOwnedParameters().iterator();

        boolean commaNeeded = false;
        while (iterator.hasNext())
        {
            Parameter parameter = (Parameter)iterator.next();

            if (!parameter.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL))
            {
                if (commaNeeded)
                {
                    buffer.append(", ");
                }
                ParameterFacade facade = (ParameterFacade)shieldedElement(parameter);
                buffer.append(facade.getType().getFullyQualifiedName());
                commaNeeded = true;
            }
        }
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isQuery()
     */
    protected boolean handleIsQuery()
    {
        return metaObject.isQuery();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getConcurrency()
     */
    protected java.lang.String handleGetConcurrency()
    {
        String concurrency = null;

        final CallConcurrencyKind concurrencyKind = metaObject.getConcurrency();
        if (concurrencyKind == null || concurrencyKind.equals(CallConcurrencyKind.CONCURRENT_LITERAL))
        {
            concurrency = "concurrent";
        }
        else if (concurrencyKind.equals(CallConcurrencyKind.GUARDED_LITERAL))
        {
            concurrency = "guarded";
        }
        else // CallConcurrencyKindEnum.CCK_SEQUENTIAL
        {
            concurrency = "sequential";
        }

        final TypeMappings languageMappings = this.getLanguageMappings();
        if (languageMappings != null)
        {
            concurrency = languageMappings.getTo(concurrency);
        }

        return concurrency;
    }

    /**
     * Gets the pattern for constructing the precondition name.
     *
     * @return the precondition pattern.
     */
    private String getPreconditionPattern()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.PRECONDITION_NAME_PATTERN));
    }

    /**
     * Gets the pattern for constructing the postcondition name.
     *
     * @return the postcondition pattern.
     */
    private String getPostconditionPattern()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.POSTCONDITION_NAME_PATTERN));
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditionName()
     */
    protected java.lang.String handleGetPreconditionName()
    {
        return this.getPreconditionPattern().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPostconditionName()
     */
    protected java.lang.String handleGetPostconditionName()
    {
        return this.getPostconditionPattern().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditionSignature()
     */
    protected java.lang.String handleGetPreconditionSignature()
    {
        return this.getSignature(
            this.getPreconditionName(),
            true,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditionCall()
     */
    protected java.lang.String handleGetPreconditionCall()
    {
        return this.getCall(this.getPreconditionName());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isPreconditionsPresent()
     */
    protected boolean handleIsPreconditionsPresent()
    {
        final Collection preconditions = this.getPreconditions();
        return preconditions != null && !preconditions.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isPostconditionsPresent()
     */
    protected boolean handleIsPostconditionsPresent()
    {
        final Collection postconditions = this.getPostconditions();
        return postconditions != null && !postconditions.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#findTaggedValue(java.lang.String, boolean)
     */
    protected java.lang.Object handleFindTaggedValue(
        java.lang.String name,
        boolean follow)
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList(java.lang.String)
     */
    protected java.lang.String handleGetExceptionList(java.lang.String initialExceptions)
    {
        initialExceptions = StringUtils.trimToEmpty(initialExceptions);
        StringBuffer exceptionList = new StringBuffer(initialExceptions);
        Collection exceptions = this.getExceptions();
        if (exceptions != null && !exceptions.isEmpty())
        {
            if (StringUtils.isNotEmpty(initialExceptions))
            {
                exceptionList.append(", ");
            }
            Iterator exceptionIt = exceptions.iterator();
            while (exceptionIt.hasNext())
            {
                ModelElementFacade exception = (ModelElementFacade)exceptionIt.next();
                exceptionList.append(exception.getFullyQualifiedName());
                if (exceptionIt.hasNext())
                {
                    exceptionList.append(", ");
                }
            }
        }

        return exceptionList.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(boolean)
     */
    protected java.lang.String handleGetSignature(boolean withArgumentNames)
    {
        return this.getSignature(
            this.getName(),
            withArgumentNames,
            null);
    }

    private String getSignature(
        final String name,
        final boolean withArgumentNames,
        final String argumentModifier)
    {
        StringBuffer signature = new StringBuffer(name);
        signature.append("(");
        signature.append(this.getTypedArgumentList(
                withArgumentNames,
                argumentModifier));
        signature.append(")");
        return signature.toString();
    }

    private String getTypedArgumentList(
        boolean withArgumentNames,
        String modifier)
    {
        StringBuffer buffer = new StringBuffer();
        Iterator parameterIterator = metaObject.getOwnedParameters().iterator();

        boolean commaNeeded = false;
        while (parameterIterator.hasNext())
        {
            Parameter paramter = (Parameter)parameterIterator.next();

            if (!paramter.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL))
            {
                String type = null;
                if (paramter.getType() == null)
                {
                    this.logger.error(
                        "ERROR! No type specified for parameter --> '" + paramter.getName() + "' on operation --> '" +
                        this.getName() + "', please check your model");
                }
                else
                {
                    type = ((ClassifierFacade)this.shieldedElement(paramter.getType())).getFullyQualifiedName();
                }

                if (commaNeeded)
                {
                    buffer.append(", ");
                }
                if (StringUtils.isNotBlank(modifier))
                {
                    buffer.append(modifier);
                    buffer.append(" ");
                }
                buffer.append(type);
                if (withArgumentNames)
                {
                    buffer.append(" ");
                    buffer.append(paramter.getName());
                }
                commaNeeded = true;
            }
        }
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList(java.lang.String)
     */
    protected java.lang.String handleGetTypedArgumentList(java.lang.String modifier)
    {
        return this.getTypedArgumentList(
            true,
            modifier);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(java.lang.String)
     */
    protected java.lang.String handleGetSignature(java.lang.String argumentModifier)
    {
        return this.getSignature(
            this.getName(),
            true,
            argumentModifier);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getOwner()
     */
    protected java.lang.Object handleGetOwner()
    {
        return this.metaObject.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getParameters()
     */
    protected java.util.Collection handleGetParameters()
    {
        return metaObject.getOwnedParameters();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getReturnType()
     */
    protected java.lang.Object handleGetReturnType()
    {
        return metaObject.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getArguments()
     */
    protected java.util.Collection handleGetArguments()
    {
        final Collection arguments = new ArrayList(metaObject.getOwnedParameters());
        CollectionUtils.filter(
            arguments,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return !((Parameter)object).getDirection().equals(ParameterDirectionKind.RETURN_LITERAL);
                }
            });
        return arguments;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditions()
     */
    protected java.util.Collection handleGetPreconditions()
    {
        return this.getConstraints(ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPostconditions()
     */
    protected java.util.Collection handleGetPostconditions()
    {
        return this.getConstraints(ExpressionKinds.POST);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.OperationFacade#findParameter(java.lang.String)
     */
    protected ParameterFacade handleFindParameter(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }
}