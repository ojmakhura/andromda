package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;

import org.andromda.metafacades.uml.BindingFacade;
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
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.CallConcurrencyKind;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Type;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.OperationFacade.
 *
 * @see org.andromda.metafacades.uml.OperationFacade
 * @author Bob Fields
 */
public class OperationFacadeLogicImpl
    extends OperationFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(OperationFacadeLogicImpl.class);

    /**
     * @param metaObject
     * @param context
     */
    public OperationFacadeLogicImpl(
        final Operation metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * Not yet implemented, always returns null. To implement: walk through the
     * related elements from the Sequence Diagram or OCL Body in the UML model to produce compilable code.
     * @return null
     * @see org.andromda.metafacades.uml.OperationFacade#getMethodBody()
     */
    @Override
    protected String handleGetMethodBody()
    {
        return null;
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
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
    @Override
    protected String handleGetSignature()
    {
        return this.getSignature(true);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getCall()
     */
    @Override
    protected String handleGetCall()
    {
        return this.getCall(this.handleGetName());
    }

    /**
     * Constructs the operation call with the given <code>name</code>
     *
     * @param name
     *            the name form which to construct the operation call.
     * @return the operation call.
     */
    private String getCall(final String name)
    {
        final StringBuilder buffer = new StringBuilder(name);
        buffer.append('(');
        buffer.append(this.getArgumentNames());
        buffer.append(')');
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList()
     */
    @Override
    protected String handleGetTypedArgumentList()
    {
        return this.getTypedArgumentList(true);
    }

    private String getTypedArgumentList(final boolean withArgumentNames)
    {
        // TODO: Possible covariant of the method 'getTypedArgumentList' defined in the class 'OperationFacadeLogic'
        return MetafacadeUtils.getTypedArgumentList(
            this.getArguments(),
            withArgumentNames,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isStatic()
     */
    @Override
    protected boolean handleIsStatic()
    {
        return this.metaObject.isStatic();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isAbstract()
     */
    @Override
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
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
     * Finds both exceptions and exception dependency references
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptions()
     */
    @Override
    protected Collection handleGetExceptions()
    {
        final Collection<DependencyFacade> exceptions = new LinkedHashSet<DependencyFacade>();

        // finds both exceptions and exception references
        final class ExceptionFilter
                implements Predicate
        {
            public boolean evaluate(final Object object)
            {
                boolean hasException = object instanceof DependencyFacade;
                if (hasException)
                {
                    final DependencyFacade dependency = (DependencyFacade)object;
                    // first check for exception references
                    hasException = dependency.hasStereotype(UMLProfile.STEREOTYPE_EXCEPTION_REF);

                    // if there wasn't any exception reference
                    // now check for actual exceptions
                    if (!hasException)
                    {
                        final ModelElementFacade targetElement = dependency.getTargetElement();
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
            public Object transform(final Object object)
            {
                if (object == null) return null;
                return ((DependencyFacade)object).getTargetElement();
            }
        });

        // finally add in any members of the UML2 RaisedException list
        // (the 'proper' UML2 way of doing exceptions .. or at least one way).
        final Collection<Type> raisedExceptions = this.metaObject.getRaisedExceptions();
        exceptions.addAll(this.shieldedElements(raisedExceptions));

        return exceptions;
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
            hasReturnType = !("void".equalsIgnoreCase(StringUtils.trimToEmpty(this.getReturnType().getFullyQualifiedName()))
                || StringUtils.trimToEmpty(this.getReturnType().getFullyQualifiedName(true)).equals(UMLProfile.VOID_TYPE_NAME));
        }
        if (LOGGER.isDebugEnabled())
        {
            final String rtnFQN = this.getReturnType().getFullyQualifiedName(true);
            final boolean voidType = "void".equalsIgnoreCase(StringUtils.trimToEmpty(this.getReturnType().getFullyQualifiedName()));
            final String voidRtn = this.getReturnType().getFullyQualifiedName();
            LOGGER.debug("OperationFacadeLogicImpl.handleIsReturnTypePresent rtnFQN=" + rtnFQN + " voidType=" + voidType + " voidRtn=" + voidRtn + " hasReturnType=" + hasReturnType);
        }
        return hasReturnType;
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
     * @see org.andromda.metafacades.uml.OperationFacade#getArgumentNames()
     */
    @Override
    protected String handleGetArgumentNames()
    {
        final StringBuilder buffer = new StringBuilder();

        final Iterator<Parameter> iterator = this.metaObject.getOwnedParameters().iterator();

        boolean commaNeeded = false;
        final String comma = ", ";
        while (iterator.hasNext())
        {
            final Parameter parameter = iterator.next();
            if (!parameter.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL))
            {
                if (commaNeeded)
                {
                    buffer.append(comma);
                }
                final ParameterFacade facade = (ParameterFacade)this.shieldedElement(parameter);
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
        final StringBuilder buffer = new StringBuilder();

        final Iterator<Parameter> iterator = this.metaObject.getOwnedParameters().iterator();

        boolean commaNeeded = false;
        while (iterator.hasNext())
        {
            final Parameter parameter = iterator.next();

            if (!parameter.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL))
            {
                if (commaNeeded)
                {
                    buffer.append(", ");
                }
                final ParameterFacade facade = (ParameterFacade)this.shieldedElement(parameter);
                buffer.append(facade.getType().getFullyQualifiedName());
                commaNeeded = true;
            }
        }
        return buffer.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isQuery()
     */
    @Override
    protected boolean handleIsQuery()
    {
        return this.metaObject.isQuery();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getConcurrency()
     */
    @Override
    protected String handleGetConcurrency()
    {
        String concurrency = null;

        final CallConcurrencyKind concurrencyKind = this.metaObject.getConcurrency();
        if (concurrencyKind == null || concurrencyKind.equals(CallConcurrencyKind.CONCURRENT_LITERAL))
        {
            concurrency = "concurrent";
        }
        else if (concurrencyKind.equals(CallConcurrencyKind.GUARDED_LITERAL))
        {
            concurrency = "guarded";
        }
        else// CallConcurrencyKindEnum.CCK_SEQUENTIAL
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
    @Override
    protected String handleGetPreconditionName()
    {
        return this.getPreconditionPattern().replaceAll(
            "\\{0\\}",
            this.handleGetName());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPostconditionName()
     */
    @Override
    protected String handleGetPostconditionName()
    {
        return this.getPostconditionPattern().replaceAll(
            "\\{0\\}",
            this.handleGetName());
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditionSignature()
     */
    @Override
    protected String handleGetPreconditionSignature()
    {
        return MetafacadeUtils.getSignature(
            this.getPreconditionName(),
            this.getArguments(),
            true,
            null);
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
     * @see org.andromda.metafacades.uml.OperationFacade#findTaggedValue(String,
     *      boolean)
     */
    @Override
    protected Object handleFindTaggedValue(
        final String name,
        final boolean follow)
    {
        final String trimName = StringUtils.trimToEmpty(name);
        Object value = this.findTaggedValue(trimName);
        if (follow)
        {
            ClassifierFacade type = this.getReturnType();
            while (value == null && type != null)
            {
                value = type.findTaggedValue(trimName);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList(String)
     */
    @Override
    protected String handleGetExceptionList(String initialExceptions)
    {
        initialExceptions = StringUtils.trimToEmpty(initialExceptions);
        final StringBuilder exceptionList = new StringBuilder(initialExceptions);
        // TODO getExceptions = Collection<ClassifierFacade> or <ModelElementFacade>?
        final Collection exceptions = this.getExceptions();
        if (exceptions != null && !exceptions.isEmpty())
        {
            if (StringUtils.isNotBlank(initialExceptions))
            {
                exceptionList.append(", ");
            }
            final Iterator exceptionIt = exceptions.iterator();
            while (exceptionIt.hasNext())
            {
                final ModelElementFacade exception = (ModelElementFacade)exceptionIt.next();
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
    @Override
    protected String handleGetSignature(final boolean withArgumentNames)
    {
        Collection<BindingFacade> dependencies = MetafacadeUtils.getBindingDependencies(this);

        return MetafacadeUtils.getSignature(
            this.handleGetName(),
            this.getArguments(),
            withArgumentNames,
            dependencies,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList(String)
     */
    @Override
    protected String handleGetTypedArgumentList(final String modifier)
    {
        return MetafacadeUtils.getTypedArgumentList(
            this.getArguments(),
            true,
            modifier);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(String)
     */
    @Override
    protected String handleGetSignature(final String argumentModifier)
    {
        Collection<BindingFacade> dependencies = MetafacadeUtils.getBindingDependencies(this);
        return MetafacadeUtils.getSignature(
            this.handleGetName(),
            this.getArguments(),
            true,
            dependencies,
            argumentModifier);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getOwner()
     */
    @Override
    protected Object handleGetOwner()
    {
        Object obj = null;
        // Fix from UML2, no longer calls getOwner to get the owning Class
        if (this.metaObject.getInterface()!=null)
        {
            obj = this.metaObject.getInterface();
        }
        else if (this.metaObject.getDatatype()!=null)
        {
            obj = this.metaObject.getDatatype();
        }
        else
        {
            obj = this.metaObject.getClass_();
        }
        return obj;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getParameters()
     */
    @Override
    protected Collection<Parameter> handleGetParameters()
    {
        final Collection<Parameter> params = new ArrayList<Parameter>(this.metaObject.getOwnedParameters());
        params.add(this.metaObject.getReturnResult());
        CollectionUtils.filter(
            params,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return object != null && !((Parameter)object).isException();
                }
            });
        return params;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getReturnType()
     */
    @Override
    protected Type handleGetReturnType()
    {
        // this.metaObject.getReturnResult().get;
        
        return this.metaObject.getType();
    }

    /**
     * @return this.getReturnType().getFullyQualifiedName()
     * @see org.andromda.metafacades.uml.OperationFacade#getGetterSetterReturnTypeName()
     */
    protected String handleGetGetterSetterReturnTypeName()
    {
        String name = null;
        final ClassifierFacade returnType = this.getReturnType();

        if (returnType!=null && (this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED))
        {
            final TypeMappings mappings = this.getLanguageMappings();
            name =
                this.handleIsOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME)
                                 : mappings.getTo(UMLProfile.COLLECTION_TYPE_NAME);

            // set this attribute's type as a template parameter if required
            if (BooleanUtils.toBoolean(
                    Objects.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING), "")))
            {
                String type = returnType.getFullyQualifiedName();
                if (returnType.isPrimitive())
                {
                    // Can't template primitive values, Objects only. Convert to wrapped.
                    type = this.getReturnType().getWrapperName();
                }

                // type = parameter.getGetterSetterTypeName();
                    // String[] sp = type.split("<");
                    // type = sp[0] + returnType.getGenericTypeString();

                // Allow List<Type[]> implementations.
                /*// Don't apply templating to modeled array types
                if (returnType.isArrayType())
                {
                    type = type.substring(0, type.length()-2);
                }*/
                /*Collection<GeneralizableElementFacade> specializations = returnType.getAllSpecializations();
                if ((specializations != null && !specializations.isEmpty()))
                {
                    name += "<? extends " + type + '>';
                }
                else
                {*/
                name += '<' + type + '>';
                //}
            }
        }
        if (name == null && returnType != null)
        {
            name = returnType.getFullyQualifiedName();
            // Special case: lower bound overrides primitive/wrapped type declaration
            // TODO Apply to all primitive types, not just booleans. This is a special case because of is/get Getters.
            if (this.getReturnType().isBooleanType())
            {
                if (this.getReturnType().isPrimitive() && (this.getLower() < 1))
                {
                    // Type is optional, should not be primitive
                    name = StringUtils.capitalize(name);
                }
                else if (!this.getReturnType().isPrimitive() && this.getLower() > 0)
                {
                    // Type is required, should not be wrapped
                    name = StringUtils.uncapitalize(name);
                }
            }

            
        }

        if(StringUtils.isNotBlank(this.getReturnParameter().getGenericTypeString())) {
            String[] sp = name.split("<");
            name = sp[0] + this.getReturnParameter().getGenericTypeString();
        }
        
        return name;
    }

    /**
     * @return this.metaObject.isOrdered()
     * @see org.andromda.metafacades.uml.OperationFacade#isOrdered()
     */
    protected boolean handleIsOrdered()
    {
        return this.metaObject.isOrdered();
    }

    /**
     * @return this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED
     * @see org.andromda.metafacades.uml.OperationFacade#isMany()
     */
    protected boolean handleIsMany()
    {
        // Because of MD11.5 (their multiplicity are String), we cannot use
        // isMultiValued()
        // RJF3 True if either the operation is many or the return parameter is many
        final ParameterFacade returnParameter = this.getReturnParameter();
        // Parameter or parameter type may be null during model validation
        final boolean returnMany = returnParameter!=null && returnParameter.getType()!=null &&
           (returnParameter.getUpper() > 1 ||
            returnParameter.getUpper() == LiteralUnlimitedNatural.UNLIMITED
            || returnParameter.getType().isArrayType()
            || returnParameter.getType().isCollectionType());
        return returnMany || this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getArguments()
     */
    @Override
    protected Collection<Parameter> handleGetArguments()
    {
        final Collection<Parameter> arguments = new ArrayList<Parameter>(this.metaObject.getOwnedParameters());
        CollectionUtils.filter(
            arguments,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final Parameter param = (Parameter)object;
                    return !param.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL) && !param.isException();
                }
            });
        return arguments;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPreconditions()
     */
    @Override
    protected Collection<ConstraintFacade> handleGetPreconditions()
    {
        return this.handleGetConstraints(ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getPostconditions()
     */
    @Override
    protected Collection<ConstraintFacade> handleGetPostconditions()
    {
        return this.handleGetConstraints(ExpressionKinds.POST);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#findParameter(String)
     */
    @Override
    protected ParameterFacade handleFindParameter(final String name)
    {
        return (ParameterFacade)this.shieldedElement(this.metaObject.getOwnedParameter(name, null));
    }

    /**
     * Get the UML upper multiplicity Not implemented for UML1.4
     * @return multiplicity upperBound
     */
    @Override
    protected int handleGetUpper()
    {
        // MD11.5 Exports multiplicity as String
        return this.metaObject.getUpper();
        //return UmlUtilities.parseMultiplicity(this.metaObject.getUpper(), 1);
    }

    /**
     * Get the UML lower multiplicity Not implemented for UML1.4
     * @return multiplicity lowerBound
     */
    @Override
    protected int handleGetLower()
    {
        // MD11.5 Exports multiplicity as String
        return this.metaObject.getLower();
        //return UmlUtilities.parseLowerMultiplicity(this.metaObject.getLower(),
        //    (ClassifierFacade) this.getReturnType(), "0");
            // ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_MULTIPLICITY)));
    }

    /**
     * Get the first UML2 ReturnResult parameter. Not implemented for UML1.4
     * @return ReturnResult parameter
     */
    @Override
    public ParameterFacade handleGetReturnParameter()
    {
        return (ParameterFacade)this.shieldedElement(this.metaObject.getReturnResult());
    }

    /*
     * Override to change private to public, since it makes no sense to have private operations in generated code
     * Allows for protected, package level visibility of operations in the model
     * @return String visibility
    @Override
    protected String handleGetVisibility()
    {
        String visibility = super.handleGetVisibility();
        if (visibility==null || visibility.equals("private"))
        {
            visibility = "public";
        }
        return visibility;
    }
     */

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#isOverriding()
     */
    @Override
    protected boolean handleIsOverriding()
    {
        return this.getOverriddenOperation() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getOverriddenOperation()
     */
    @Override
    protected OperationFacade handleGetOverriddenOperation()
    {
        OperationFacade overriddenOperation = null;

        final String signature = this.getSignature(false);

        ClassifierFacade ancestor = this.getOwner().getSuperClass();
        while (overriddenOperation == null && ancestor != null)
        {
            for (final Iterator<OperationFacade> operationIterator = ancestor.getOperations().iterator();
                 overriddenOperation == null && operationIterator.hasNext();)
            {
                final OperationFacade ancestorOperation = operationIterator.next();
                if (signature.equals(ancestorOperation.getSignature(false)))
                {
                    overriddenOperation = ancestorOperation;
                }
            }

            ancestor = ancestor.getSuperClass();
        }

        return overriddenOperation;
    }

    /**
     * @return this.metaObject.isLeaf()
     * @see org.andromda.metafacades.uml.OperationFacade#isLeaf()
     */
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }

    /**
     * @return this.metaObject.isUnique()
     * @see org.andromda.metafacades.uml.OperationFacade#isUnique()
     */
    protected boolean handleIsUnique()
    {
        return this.metaObject.isUnique();
    }

    @Override
    protected String handleGetTestName() {

        String value = (String)findTaggedValue(UMLProfile.TAGGEDVALUE_OPERATION_TEST_NAME);

        if (StringUtils.isBlank(value)) {
            value = "test" + StringUtilsHelper.capitalize(getName());
        }

        return value;
    }

    @Override
    protected String handleGetTestDisplayName() {
        String value = (String)findTaggedValue(UMLProfile.TAGGEDVALUE_OPERATION_TEST_DISPLAY_NAME);

        if (StringUtils.isBlank(value)) {
            value = "Test " + StringUtilsHelper.toPhrase(getName());
        }

        return value;
    }
}
