package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.BindingFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EnumerationLiteralFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.translation.ocl.ExpressionKinds;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ModelElementFacadeLogicImpl
    extends ModelElementFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ModelElementFacadeLogicImpl(
        org.omg.uml.foundation.core.ModelElement metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ModelElementFacadeLogicImpl.class);

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTaggedValues()
     */
    @Override
    protected Collection handleGetTaggedValues()
    {
        return metaObject.getTaggedValue();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    @Override
    protected String handleGetPackageName()
    {
        final boolean modelName = false;
        return UML14MetafacadeUtils.getPackageName(
            this.metaObject,
            this.getNamespaceScope(modelName),
            modelName);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName(boolean)
     */
    @Override
    protected String handleGetPackageName(boolean modelName)
    {
        String packageName = this.getPackageName();
        if (modelName)
        {
            packageName =
                StringUtils.replace(
                    packageName,
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
        }
        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName(boolean)
     */
    @Override
    protected String handleGetFullyQualifiedName(boolean modelName)
    {
        String fullName = StringUtils.trimToEmpty(this.getName());
        final String packageName = this.getPackageName(true);
        final String metafacadeNamespaceScopeOperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        if (StringUtils.isNotBlank(packageName))
        {
            fullName = packageName + metafacadeNamespaceScopeOperator + fullName;
        }
        if (!modelName)
        {
            if (this.getLanguageMappings() != null)
            {
                fullName = StringUtils.trimToEmpty(this.getLanguageMappings().getTo(fullName));

                // now replace the metafacade scope operators
                // with the mapped scope operators
                final String namespaceScopeOperator =
                    String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR));
                fullName = StringUtils.replace(
                        fullName,
                        metafacadeNamespaceScopeOperator,
                        namespaceScopeOperator);
            }
        }

        if (this.isTemplateParametersPresent() &&
            BooleanUtils.toBoolean(
                ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING))))
        {
            // we'll be constructing the parameter list in this buffer
            final StringBuffer buffer = new StringBuffer();

            // add the name we've constructed so far
            buffer.append(fullName);

            // start the parameter list
            buffer.append("<");

            // loop over the parameters, we are so to have at least one (see outer condition)
            final Collection<TemplateParameterFacade> templateParameters = this.getTemplateParameters();
            for (Iterator<TemplateParameterFacade> parameterIterator = templateParameters.iterator(); parameterIterator.hasNext();)
            {
                final ModelElementFacade modelElement =
                    ((TemplateParameterFacade)parameterIterator.next()).getParameter();

                if (modelElement instanceof ParameterFacade)
                {
                    buffer.append(((ParameterFacade)modelElement).getType().getFullyQualifiedName());
                }
                else
                {
                    buffer.append(modelElement.getFullyQualifiedName());
                }

                if (parameterIterator.hasNext())
                {
                    buffer.append(", ");
                }
            }

            // we're finished listing the parameters
            buffer.append(">");

            // we have constructed the full name in the buffer
            fullName = buffer.toString();
        }

        return fullName;
    }

    /**
     * Gets the appropriate namespace property for retrieving the namespace scope operation (depending on the given
     * <code>modelName</code> flag.
     *
     * @param modelName whether or not the scope operation for the model should be retrieved as opposed to the mapped
     *                  scope operator.
     * @return the scope operator.
     */
    private String getNamespaceScope(boolean modelName)
    {
        String namespaceScope = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        if (!modelName)
        {
            namespaceScope =
                ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR));
        }
        return namespaceScope;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    @Override
    protected String handleGetFullyQualifiedName()
    {
        return this.getFullyQualifiedName(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValues(String)
     */
    @Override
    protected Collection handleFindTaggedValues(String name)
    {
        final Collection values = new ArrayList();

        // only search a tagged value when it actually has a name
        if (StringUtils.isNotBlank(name))
        {
            // trim the name, we don't want leading/trailing spaces
            name = StringUtils.trimToEmpty(name);

            // loop over the tagged values
            final Collection<TaggedValueFacade> taggedValues = this.getTaggedValues();
            for (final Iterator<TaggedValueFacade> taggedValueIterator = taggedValues.iterator(); taggedValueIterator.hasNext();)
            {
                TaggedValueFacade taggedValue = (TaggedValueFacade)taggedValueIterator.next();

                // does this name match the argument tagged value name ?
                // Check both the UML14 format name @andromda.value and EMF Format andromda_whatever
                String tagName = taggedValue.getName();
                if (name.equals(tagName) || MetafacadeUtils.getEmfTaggedValue(name).equals(tagName)
                    || MetafacadeUtils.getUml14TaggedValue(name).equals(tagName))
                {
                    for (final Iterator valueIterator = taggedValue.getValues().iterator(); valueIterator.hasNext();)
                    {
                        // only process tagged values when they actually have a
                        // non-empty value
                        final Object value = valueIterator.next();
                        if (value != null)
                        {
                            // if an enumeration literal is referenced we assume
                            // its name
                            if (value instanceof EnumerationLiteralFacade)
                            {
                                values.add(((EnumerationLiteralFacade)value).getValue(true));
                            }
                            else if (value instanceof String)
                            {
                                // only add String when they are not blank
                                String valueString = (String)value;
                                if (StringUtils.isNotBlank(valueString))
                                {
                                    values.add(value);
                                }
                            }
                            else
                            {
                                values.add(value);
                            }
                        }
                    }
                }
            }
        }
        return values;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(String)
     */
    @Override
    protected Object handleFindTaggedValue(String name)
    {
        Collection taggedValues = findTaggedValues(name);
        return taggedValues.isEmpty() ? null : taggedValues.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasStereotype(String)
     */
    @Override
    protected boolean handleHasStereotype(final String stereotypeName)
    {
        Collection<StereotypeFacade> stereotypes = this.getStereotypes();

        boolean hasStereotype = StringUtils.isNotBlank(stereotypeName) && stereotypes != null &&
            !stereotypes.isEmpty();

        if (hasStereotype)
        {
            class StereotypeFilter
                implements Predicate
            {
                public boolean evaluate(Object object)
                {
                    boolean valid;
                    StereotypeFacade facade = (StereotypeFacade)object;
                    String name = StringUtils.trimToEmpty(facade.getName());
                    valid = stereotypeName.equals(name);
                    while (!valid && facade != null)
                    {
                        facade = (StereotypeFacade)facade.getGeneralization();
                        valid = facade != null && StringUtils.trimToEmpty(facade.getName()).equals(stereotypeName);
                    }
                    return valid;
                }
            }
            hasStereotype = CollectionUtils.find(
                    this.getStereotypes(),
                    new StereotypeFilter()) != null;
        }
        return hasStereotype;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getId()
     */
    @Override
    protected String handleGetId()
    {
        return this.metaObject.refMofId();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(String)
     */
    @Override
    protected boolean handleHasExactStereotype(String stereotypeName)
    {
        return this.getStereotypeNames().contains(StringUtils.trimToEmpty(stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    @Override
    protected String handleGetVisibility()
    {
        String visibility;

        final VisibilityKind visibilityKind = metaObject.getVisibility();
        if (visibilityKind == null || VisibilityKindEnum.VK_PRIVATE.equals(visibilityKind))
        {
            visibility = "private";
        }
        else if (VisibilityKindEnum.VK_PROTECTED.equals(visibilityKind))
        {
            visibility = "protected";
        }
        else if (VisibilityKindEnum.VK_PUBLIC.equals(visibilityKind))
        {
            visibility = "public";
        }
        else // VisibilityKindEnum.VK_PACKAGE
        {
            visibility = "package";
        }

        final TypeMappings languageMappings = this.getLanguageMappings();
        if (languageMappings != null)
        {
            visibility = languageMappings.getTo(visibility);
        }

        return visibility;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    @Override
    protected Collection<String> handleGetStereotypeNames()
    {
        Collection<String> stereotypeNames = new ArrayList();

        Collection<ModelElement> stereotypes = metaObject.getStereotype();
        for (final Iterator<ModelElement> stereotypeIt = stereotypes.iterator(); stereotypeIt.hasNext();)
        {
            ModelElement stereotype = (ModelElement)stereotypeIt.next();
            if (stereotype != null)
            {
                stereotypeNames.add(StringUtils.trimToEmpty(stereotype.getName()));
            }
        }
        return stereotypeNames;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedNamePath()
     */
    @Override
    protected String handleGetFullyQualifiedNamePath()
    {
        return StringUtils.replace(
            this.getFullyQualifiedName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
            "/");
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackagePath()
     */
    @Override
    protected String handleGetPackagePath()
    {
        return StringUtils.replace(
            this.getPackageName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
            "/");
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(String)
     */
    @Override
    protected String handleGetDocumentation(String indent)
    {
        return getDocumentation(
            indent,
            64);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(String, int)
     */
    @Override
    protected String handleGetDocumentation(
        String indent,
        int lineLength)
    {
        return getDocumentation(
            indent,
            lineLength,
            true);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(String, int, boolean)
     */
    @Override
    protected String handleGetDocumentation(
        String indent,
        int lineLength,
        boolean htmlStyle)
    {
        final StringBuffer documentation = new StringBuffer();

        if (lineLength < 1)
        {
            lineLength = Integer.MAX_VALUE;
        }

        final Collection<Comment> comments = this.metaObject.getComment();
        if (comments != null && !comments.isEmpty())
        {
            for (final Iterator<Comment> commentIterator = comments.iterator(); commentIterator.hasNext();)
            {
                final Comment comment = (Comment)commentIterator.next();
                String commentString = StringUtils.trimToEmpty(comment.getBody());

                // if there isn't anything in the body, try the name
                if (StringUtils.isEmpty(commentString))
                {
                    commentString = StringUtils.trimToEmpty(comment.getName());
                }
                documentation.append(StringUtils.trimToEmpty(commentString));
                documentation.append(SystemUtils.LINE_SEPARATOR);
            }
        }

        // if there still isn't anything, try a tagged value
        if (StringUtils.isEmpty(documentation.toString()))
        {
            documentation.append(
                StringUtils.trimToEmpty((String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_DOCUMENTATION)));
        }

        return StringUtilsHelper.format(
            StringUtils.trimToEmpty(documentation.toString()),
            indent,
            lineLength,
            htmlStyle);
    }

    /**
     * @return NOT IMPLEMENTED: UML2 only
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLabel()
     */
    protected String handleGetLabel()
    {
        return null;
    }

    /**
     * @return NOT IMPLEMENTED: UML2 only
     * @see org.andromda.metafacades.uml.ModelElementFacade#getKeywords()
     */
    protected Collection<String> handleGetKeywords()
    {
        return new ArrayList<String>();
    }

    /*
     * @return NOT IMPLEMENTED: UML2 only
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModelNamespace()
    protected ModelElementFacade handleGetModelNamespace()
    {
        return null;
    }
     */

    /*
     * @return NOT IMPLEMENTED: UML2 only
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModelNamespace()
    protected ClassifierFacade handleGetOwner()
    {
        return null;
    }
     */

    /**
     * @return NOT IMPLEMENTED: UML2 only
     * @see org.andromda.metafacades.uml.ModelElementFacade#getQualifiedName()
     */
    protected String handleGetQualifiedName()
    {
        return null;
    }

    /**
     * @param keyword hasExactStereotype(keyword)
     * @return hasExactStereotype(keyword)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getQualifiedName()
     */
    protected boolean handleHasKeyword(String keyword)
    {
        return this.handleHasExactStereotype(keyword);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        return metaObject.getName();
    }

    /**
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
     */
    private String getArraySuffix()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    @Override
    protected TypeMappings handleGetLanguageMappings()
    {
        final String propertyName = UMLMetafacadeProperties.LANGUAGE_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri;
        if (property instanceof String)
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                mappings.setArraySuffix(this.getArraySuffix());
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (Throwable throwable)
            {
                final String message = "Error getting '" + propertyName + "' --> '" + uri + "'";
                logger.error(
                    message,
                    throwable);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /*
     * UML2 Only
     * @see org.andromda.metafacades.uml.ModelElementFacade#getOwnedMembers()
    protected Collection<ModelElementFacade> handleGetOwnedElements()
    {
        return new ArrayList<ModelElementFacade>();
    }
     */

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    @Override
    protected Namespace handleGetPackage()
    {
        return metaObject.getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getRootPackage()
     */
    @Override
    protected UmlPackage handleGetRootPackage()
    {
        return UML14MetafacadeUtils.getRootPackage();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getSourceDependencies()
     */
    @Override
    protected Collection<DependencyFacade> handleGetSourceDependencies()
    {
        Collection<DependencyFacade> dependencies = new ArrayList();
        Collection<DependencyFacade> clientDependencies =
            UML14MetafacadeUtils.getCorePackage().getAClientClientDependency().getClientDependency(this.metaObject);
        if (clientDependencies != null)
        {
            dependencies.addAll(clientDependencies);
        }
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Dependency) && !(object instanceof Abstraction);
                }
            });
        return dependencies;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTargetDependencies()
     */
    @Override
    protected Collection<DependencyFacade> handleGetTargetDependencies()
    {
        Collection<DependencyFacade> dependencies = new ArrayList();
        Collection<DependencyFacade> supplierDependencies =
            UML14MetafacadeUtils.getCorePackage().getASupplierSupplierDependency().getSupplierDependency(
                this.metaObject);
        if (supplierDependencies != null)
        {
            dependencies.addAll(supplierDependencies);
        }
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return (object instanceof Dependency) && !(object instanceof Abstraction);
                }
            });
        return dependencies;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypes()
     */
    @Override
    protected Collection<StereotypeFacade> handleGetStereotypes()
    {
        return this.metaObject.getStereotype();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModel()
     */
    @Override
    protected Object handleGetModel()
    {
        return MetafacadeFactory.getInstance().getModel().getModel();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints()
     */
    @Override
    protected Collection<ConstraintFacade> handleGetConstraints()
    {
        return this.metaObject.getConstraint();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints(String)
     */
    @Override
    protected Collection<ConstraintFacade> handleGetConstraints(final String kind)
    {
        return CollectionUtils.select(
            getConstraints(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    if (object instanceof ConstraintFacade)
                    {
                        ConstraintFacade constraint = (ConstraintFacade)object;
                        return ((ExpressionKinds.BODY.equals(kind) && constraint.isBodyExpression()) ||
                        (ExpressionKinds.DEF.equals(kind) && constraint.isDefinition()) ||
                        (ExpressionKinds.INV.equals(kind) && constraint.isInvariant()) ||
                        (ExpressionKinds.PRE.equals(kind) && constraint.isPreCondition()) ||
                        (ExpressionKinds.POST.equals(kind) && constraint.isPostCondition()));
                    }
                    return false;
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraint(String, String)
     */
    @Override
    protected String handleTranslateConstraint(
        final String name,
        String translation)
    {
        String translatedExpression = "";
        ConstraintFacade constraint =
            (ConstraintFacade)CollectionUtils.find(
                this.getConstraints(),
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        ConstraintFacade constraint = (ConstraintFacade)object;
                        return StringUtils.trimToEmpty(constraint.getName()).equals(StringUtils.trimToEmpty(name));
                    }
                });

        if (constraint != null)
        {
            translatedExpression = constraint.getTranslation(translation);
        }
        return translatedExpression;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(String)
     */
    @Override
    protected String[] handleTranslateConstraints(String translation)
    {
        return this.translateConstraints(
            this.getConstraints(),
            translation);
    }

    /**
     * Private helper that translates all the expressions contained in the <code>constraints</code>, and returns an
     * array of the translated expressions.
     *
     * @param constraints the constraints to translate
     * @param translation the translation to translate <code>to</code>.
     * @return String[] the translated expressions, or null if no constraints were found
     */
    private String[] translateConstraints(
        Collection<ConstraintFacade> constraints,
        String translation)
    {
        String[] translatedExpressions = null;
        if (constraints != null && !constraints.isEmpty())
        {
            translatedExpressions = new String[constraints.size()];
            Iterator<ConstraintFacade> constraintIt = constraints.iterator();
            for (int ctr = 0; constraintIt.hasNext(); ctr++)
            {
                ConstraintFacade constraint = (ConstraintFacade)constraintIt.next();
                translatedExpressions[ctr] = constraint.getTranslation(translation);
            }
        }
        return translatedExpressions;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(String, String)
     */
    @Override
    protected String[] handleTranslateConstraints(
        final String kind,
        String translation)
    {
        Collection<ConstraintFacade> constraints = this.getConstraints();
        CollectionUtils.filter(
            constraints,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    ConstraintFacade constraint = (ConstraintFacade)object;
                    return UMLMetafacadeUtils.isConstraintKind(
                        constraint.getBody(),
                        kind);
                }
            });
        return this.translateConstraints(
            constraints,
            translation);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStateMachineContext()
     */
    @Override
    protected StateMachine handleGetStateMachineContext()
    {
        StateMachine machineContext = null;

        final Collection<StateMachine> machines = UML14MetafacadeUtils.getModel().getStateMachines().getStateMachine().refAllOfType();
        for (final Iterator<StateMachine> machineIterator = machines.iterator(); machineIterator.hasNext();)
        {
            final StateMachine machine = (StateMachine)machineIterator.next();
            final ModelElement contextElement = machine.getContext();
            if (metaObject.equals(contextElement))
            {
                machineContext = machine;
            }
        }

        return machineContext;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTemplateParameters()
     */
    @Override
    protected Collection handleGetTemplateParameters()
    {
        return metaObject.getTemplateParameter();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    public String getValidationName()
    {
        final StringBuffer validationName = new StringBuffer();
        final String seperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        for (ModelElement namespace = metaObject.getNamespace(); namespace != null;
            namespace = namespace.getNamespace())
        {
            if (validationName.length() == 0)
            {
                validationName.append(namespace.getName());
            }
            else
            {
                validationName.insert(
                    0,
                    seperator);
                validationName.insert(
                    0,
                    namespace.getName());
            }
        }
        if (validationName.length() > 0)
        {
            validationName.append(seperator);
        }
        if (StringUtils.isNotEmpty(this.getName()))
        {
            validationName.append(this.getName());
        }
        else
        {
            validationName.append(this.getConfiguredProperty(UMLMetafacadeProperties.UNDEFINED_NAME));
        }
        return validationName.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#isConstraintsPresent()
     */
    @Override
    protected boolean handleIsConstraintsPresent()
    {
        return this.getConstraints() != null && !this.getConstraints().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#isBindingDependenciesPresent()
     */
    @Override
    protected boolean handleIsBindingDependenciesPresent()
    {
        Collection<DependencyFacade> dependencies = this.getSourceDependencies();
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof BindingFacade;
                }
            });
        return !dependencies.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#isTemplateParametersPresent()
     */
    @Override
    protected boolean handleIsTemplateParametersPresent()
    {
        final Collection<TemplateParameterFacade> params = this.getTemplateParameters();
        return params != null && !params.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#copyTaggedValues(org.andromda.metafacades.uml.ModelElementFacade)
     */
    @Override
    protected void handleCopyTaggedValues(ModelElementFacade element)
    {
        org.omg.uml.foundation.core.ModelElement elementMetaObject;
        if (element instanceof MetafacadeBase)
        {
            final MetafacadeBase metafacade = (MetafacadeBase)element;
            final Object metaObject = metafacade.getMetaObject();
            if (metaObject instanceof ModelElement)
            {
                elementMetaObject = (ModelElement)metaObject;
                this.metaObject.getTaggedValue().addAll(elementMetaObject.getTaggedValue());
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTemplateParameter(String)
     */
    @Override
    protected TemplateParameterFacade handleGetTemplateParameter(String parameterName)
    {
        TemplateParameterFacade templateParameter = null;
        if (StringUtils.isNotEmpty(parameterName))
        {
            parameterName = StringUtils.trimToEmpty(parameterName);
            final Collection<TemplateParameterFacade> parameters = this.getTemplateParameters();
            if (parameters != null && !parameters.isEmpty())
            {
                for (final Iterator<TemplateParameterFacade> iterator = parameters.iterator(); iterator.hasNext();)
                {
                    final TemplateParameterFacade currentTemplateParameter = (TemplateParameterFacade)iterator.next();
                    if (currentTemplateParameter.getParameter() != null)
                    {
                        final ModelElementFacade parameter = currentTemplateParameter.getParameter();

                        // there should not be two template parameters with the same parameter name, but nothing
                        // prevents the model from allowing that.  So return the first instance if found.
                        if (parameterName.equals(parameter.getName()))
                        {
                            templateParameter = currentTemplateParameter;
                            break;
                        }
                    }
                }
            }
        }

        return templateParameter;
    }
}