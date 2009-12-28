package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.translation.ocl.ExpressionKinds;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.CallConcurrencyKind;
import org.omg.uml.foundation.datatypes.CallConcurrencyKindEnum;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class OperationFacadeLogicImpl
        extends OperationFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public OperationFacadeLogicImpl(Operation metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(OperationFacadeLogicImpl.class);

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.OPERATION_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public ClassifierFacade getValidationOwner()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature()
     */
    @Override
    protected String handleGetSignature()
    {
        return this.getSignature(true);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(boolean)
     */
    @Override
    protected String handleGetSignature(boolean withArgumentNames)
    {
        return MetafacadeUtils.getSignature(this.getName(), this.getArguments(), withArgumentNames, null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList()
     */
    @Override
    protected String handleGetTypedArgumentList()
    {
        return this.getTypedArgumentList(true);
    }

    private String getTypedArgumentList(boolean withArgumentNames)
    {
        return this.getTypedArgumentList(withArgumentNames, null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getCall()
     */
    @Override
    protected String handleGetCall()
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
        buffer.append('(');
        buffer.append(this.getArgumentNames());
        buffer.append(')');
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getArgumentNames()
     */
    @Override
    protected String handleGetArgumentNames()
    {
        StringBuffer buffer = new StringBuffer();

        Iterator iterator = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (iterator.hasNext())
        {
            Parameter parameter = (Parameter)iterator.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(parameter.getKind()))
            {
                if (commaNeeded)
                {
                    buffer.append(", ");
                }
                ParameterFacade facade = (ParameterFacade)this.shieldedElement(parameter);
                buffer.append(facade.getName());
                commaNeeded = true;
            }
        }
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getArgumentTypeNames()
     */
    @Override
    protected String handleGetArgumentTypeNames()
    {
        StringBuffer buffer = new StringBuffer();

        Iterator iterator = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (iterator.hasNext())
        {
            Parameter parameter = (Parameter)iterator.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(parameter.getKind()))
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
     * @return fully qualified return type, including multiplicity
     * @see org.andromda.metafacades.uml.OperationFacade#getGetterSetterReturnTypeName()
     */
    @Override
    protected String handleGetGetterSetterReturnTypeName()
    {
        // Multiplicity in return type is only supported in UML2
        return getReturnType().getFullyQualifiedName();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getReturnType()
     */
    @Override
    protected Classifier handleGetReturnType()
    {
        Classifier type = null;
        final Collection<Parameter> parms = metaObject.getParameter();
        for (final Parameter parameter : parms)
        {
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(parameter.getKind()))
            {
                type = parameter.getType();
                break;
            }
        }
        return type;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getArguments()
     */
    @Override
    protected Collection<Parameter> handleGetArguments()
    {
        final Collection<Parameter> arguments = new ArrayList<Parameter>(metaObject.getParameter());
        CollectionUtils.filter(arguments, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return !ParameterDirectionKindEnum.PDK_RETURN.equals(((Parameter)object).getKind());
            }
        });
        return arguments;
    }

    /**
     * Not yet implemented, always returns null. To implement: walk through the
     * related elements from the Sequence Diagram in the UML model to produce compilable code.
     * @return method body
     * @see org.andromda.metafacades.uml.OperationFacade#getMethodBody()
     */
    //@Override
    protected String handleGetMethodBody()
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getOwner()
     */
    @Override
    protected Classifier handleGetOwner()
    {
        return this.metaObject.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getParameters()
     */
    @Override
    protected Collection<Parameter> handleGetParameters()
    {
        return metaObject.getParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#findTaggedValue(String, boolean)
     */
    @Override
    protected Object handleFindTaggedValue(String name, boolean follow)
    {
        name = StringUtils.trimToEmpty(name);
        Object value = findTaggedValue(name);
        if (follow)
        {
            ClassifierFacade type = this.getReturnType();
            while (value == null && type != null)
            {
                value = type.findTaggedValue(name);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isStatic()
     */
    @Override
    protected boolean handleIsStatic()
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isAbstract()
     */
    @Override
    protected boolean handleIsAbstract()
    {
        return metaObject.isAbstract();
    }

    /**
     * @return metaObject.isLeaf()
     * @see org.andromda.metafacades.uml.OperationFacade#isLeaf()
     */
    @Override
    protected boolean handleIsLeaf()
    {
        return metaObject.isLeaf();
    }

    /**
     * @return false always
     * @see org.andromda.metafacades.uml.OperationFacade#isMany()
     */
    //@Override
    protected boolean handleIsMany()
    {
        return false;
    }

    /** 
     * @return false always
     * @see org.andromda.metafacades.uml.OperationFacade#isOrdered()
     */
    //@Override
    protected boolean handleIsOrdered()
    {
        return false;
    }

    /** 
     * @return false always
     * @see org.andromda.metafacades.uml.OperationFacade#isOrdered()
     */
    //@Override
    protected boolean handleIsUnique()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isQuery()
     */
    @Override
    protected boolean handleIsQuery()
    {
        return metaObject.isQuery();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isExceptionsPresent()
     */
    @Override
    protected boolean handleIsExceptionsPresent()
    {
        return !this.getExceptions().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptions()
     */
    @Override
    protected Collection<DependencyFacade> handleGetExceptions()
    {
        Collection <DependencyFacade>exceptions = new LinkedHashSet<DependencyFacade>();

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
                    hasException = dependency.hasStereotype(UMLProfile.STEREOTYPE_EXCEPTION_REF);

                    // if there wasn't any exception reference
                    // now check for actual exceptions
                    if (!hasException)
                    {
                        ModelElementFacade targetElement = dependency.getTargetElement();
                        hasException = targetElement != null && targetElement.hasStereotype(
                                UMLProfile.STEREOTYPE_EXCEPTION);
                    }
                }
                return hasException;
            }
        }

        // first get any dependencies on this operation's
        // owner (because these will represent the default exception(s))
        final Collection<DependencyFacade> ownerDependencies = new ArrayList<DependencyFacade>(this.getOwner().getSourceDependencies());
        if (!ownerDependencies.isEmpty())
        {
            CollectionUtils.filter(ownerDependencies, new ExceptionFilter());
            exceptions.addAll(ownerDependencies);
        }

        final Collection<DependencyFacade> operationDependencies = new ArrayList<DependencyFacade>(this.getSourceDependencies());
        // now get any exceptions directly on the operation
        if (!operationDependencies.isEmpty())
        {
            CollectionUtils.filter(operationDependencies, new ExceptionFilter());
            exceptions.addAll(operationDependencies);
        }

        // now transform the dependency(s) to the actual exception(s)
        CollectionUtils.transform(exceptions, new Transformer()
        {
            public ModelElementFacade transform(Object object)
            {
                return ((DependencyFacade)object).getTargetElement();
            }
        });
        return exceptions;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList()
     */
    @Override
    protected String handleGetExceptionList()
    {
        return this.getExceptionList(null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isReturnTypePresent()
     */
    @Override
    protected boolean handleIsReturnTypePresent()
    {
        boolean hasReturnType = false;
        if (this.getReturnType() != null)
        {
            hasReturnType = !("void".equalsIgnoreCase(StringUtils.trimToEmpty(
                    this.getReturnType().getFullyQualifiedName()))
              || StringUtils.trimToEmpty(
                this.getReturnType().getFullyQualifiedName(true)).equals(UMLProfile.VOID_TYPE_NAME));
        }
        return hasReturnType;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList(String)
     */
    @Override
    protected String handleGetExceptionList(String initialExceptions)
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
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList(String)
     */
    @Override
    protected String handleGetTypedArgumentList(String modifier)
    {
        return this.getTypedArgumentList(true, modifier);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(String)
     */
    @Override
    protected String handleGetSignature(String argumentModifier)
    {
        return MetafacadeUtils.getSignature(this.getName(), this.getArguments(), true, argumentModifier);
    }

    private String getTypedArgumentList(boolean withArgumentNames, String modifier)
    {
        final StringBuffer buffer = new StringBuffer();
        final Iterator parameterIterator = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (parameterIterator.hasNext())
        {
            Parameter paramter = (Parameter)parameterIterator.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(paramter.getKind()))
            {
                String type = null;
                if (paramter.getType() == null)
                {
                    OperationFacadeLogicImpl.logger.error(
                            "ERROR! No type specified for parameter --> '" + paramter.getName() +
                            "' on operation --> '" +
                            this.getName() +
                            "', please check your model");
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
                    buffer.append(' ');
                }
                buffer.append(type);
                if (withArgumentNames)
                {
                    buffer.append(' ');
                    buffer.append(paramter.getName());
                }
                commaNeeded = true;
            }
        }
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getConcurrency()
     */
    @Override
    protected String handleGetConcurrency()
    {
        String concurrency;

        final CallConcurrencyKind concurrencyKind = metaObject.getConcurrency();
        if (concurrencyKind == null || CallConcurrencyKindEnum.CCK_CONCURRENT.equals(concurrencyKind))
        {
            concurrency = "concurrent";
        }
        else if (CallConcurrencyKindEnum.CCK_GUARDED.equals(concurrencyKind))
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
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditionName()
     */
    @Override
    protected String handleGetPreconditionName()
    {
        return this.getPreconditionPattern().replaceAll("\\{0\\}", this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPostconditionName()
     */
    @Override
    protected String handleGetPostconditionName()
    {
        return this.getPostconditionPattern().replaceAll("\\{0\\}", this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditionSignature()
     */
    @Override
    protected String handleGetPreconditionSignature()
    {
        return MetafacadeUtils.getSignature(this.getPreconditionName(), this.getArguments(), true, null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditionCall()
     */
    @Override
    protected String handleGetPreconditionCall()
    {
        return this.getCall(this.getPreconditionName());
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
     * @see org.andromda.metafacades.uml.OperationFacade#isPreconditionsPresent()
     */
    @Override
    protected boolean handleIsPreconditionsPresent()
    {
        final Collection<ConstraintFacade> preconditions = this.getPreconditions();
        return preconditions != null && !preconditions.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isPostconditionsPresent()
     */
    @Override
    protected boolean handleIsPostconditionsPresent()
    {
        final Collection<ConstraintFacade> postconditions = this.getPostconditions();
        return postconditions != null && !postconditions.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditions()
     */
    @Override
    protected Collection<ConstraintFacade> handleGetPreconditions()
    {
        return this.getConstraints(ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPostconditions()
     */
    @Override
    protected Collection<ConstraintFacade> handleGetPostconditions()
    {
        return this.getConstraints(ExpressionKinds.POST);
    }
    
    /**
     * @see  org.andromda.metafacades.uml.OperationFacade#findParameter(String)
     */
    @Override
    protected ParameterFacade handleFindParameter(final String name)
    {
        return (ParameterFacade)CollectionUtils.find(
            this.getParameters(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    final ParameterFacade parameter = (ParameterFacade)object;
                    return StringUtils.trimToEmpty(parameter.getName()).equals(name);
                }
            });
    }

    /**
     * Get the UML upper multiplicity
     * @return -1 (UnlimitedNatural) is isMany, otherwise 1
     */
    @Override
    protected int handleGetUpper()
    {
        if (this.isMany())
        {
            return -1;
        }
        return 1;
     }

    /**
     * Get the UML lower multiplicity
     * @return 1 if primitive, otherwise 0
     */
    @Override
    protected int handleGetLower()
    {
        if (this.getReturnType().isPrimitive())
        {
            return 1;
        }
        return 0;
    }

    /**
     * @see org.andromda.metafacades.uml14.OperationFacadeLogic#handleGetReturnParameter()
     */
    @Override
    public ParameterFacade handleGetReturnParameter()
    {
        //throw new UnsupportedOperationException("ReturnResults is not a UML1.4 feature");
        ParameterFacade facade = null;
        final Collection<Parameter> parms = metaObject.getParameter();
        for (final Parameter parameter : parms)
        {
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(parameter.getKind()))
            {
                facade = (ParameterFacade) shieldedElement(parameter);
                break;
            }
        }
        return facade;
    }

    protected boolean handleIsOverriding()
    {
        return this.getOverriddenOperation() != null;
    }

    protected OperationFacade handleGetOverriddenOperation()
    {
        OperationFacade overriddenOperation = null;

        final String signature = this.getSignature(false);

        ClassifierFacade ancestor = this.getOwner().getSuperClass();
        while (overriddenOperation == null && ancestor != null)
        {
            for (Iterator operationIterator = ancestor.getOperations().iterator();
                 overriddenOperation == null && operationIterator.hasNext();)
            {
                final OperationFacade ancestorOperation = (OperationFacade)operationIterator.next();
                if (signature.equals(ancestorOperation.getSignature(false)))
                {
                    overriddenOperation = ancestorOperation;
                }
            }

            ancestor = ancestor.getSuperClass();
        }

        return overriddenOperation;
    }
}
