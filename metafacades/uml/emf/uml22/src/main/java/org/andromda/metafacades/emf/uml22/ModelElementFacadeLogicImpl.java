package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.BindingFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Manifestation;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.TemplateableElement;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ModelElementFacade.
 *
 * @see org.andromda.metafacades.uml.ModelElementFacade
 * @author Bob Fields
 */
public class ModelElementFacadeLogicImpl
    extends ModelElementFacadeLogic
{
    /**
     * Helps to get the xmi:id value from the model
     */
    static XMIHelperImpl xmiHelper = new XMIHelperImpl();

    /**
     * @param metaObject
     * @param context
     */
    public ModelElementFacadeLogicImpl(
        final Element metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ModelElementFacadeLogicImpl.class);

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    @Override
    protected String handleGetVisibility()
    {
        if (this.metaObject instanceof NamedElement)
        {
            final NamedElement element = (NamedElement)this.metaObject;
            final VisibilityKind kind = element.getVisibility();
            String visibility = null;
            if (kind.equals(VisibilityKind.PACKAGE_LITERAL))
            {
                visibility = "package";
            }
            if (kind.equals(VisibilityKind.PRIVATE_LITERAL))
            {
                visibility = "private";
            }
            if (kind.equals(VisibilityKind.PROTECTED_LITERAL))
            {
                visibility = "protected";
            }
            if (kind.equals(VisibilityKind.PUBLIC_LITERAL))
            {
                visibility = "public";
            }
            final TypeMappings languageMappings = this.getLanguageMappings();
            if (languageMappings != null)
            {
                visibility = languageMappings.getTo(visibility);
            }
            return visibility;
        }
        return null;
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        // In UML2, model elements need not have a name,
        // only when they are an instance of NamedElement.
        if (this.metaObject instanceof NamedElement)
        {
            NamedElement namedElement = (NamedElement) this.metaObject;
            return namedElement.getName();

        }
        return "";
    }

    /**
     * Gets the appropriate namespace property for retrieve the namespace scope
     * operation (depending on the given <code>modelName</code> flag.
     *
     * @param modelName
     *            whether or not the scope operation for the model should be
     *            retrieved as opposed to the mapped scope operator.
     * @return the scope operator.
     */
    private String getNamespaceScope(boolean modelName)
    {
        return modelName
            ? MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR
            : ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    @Override
    protected String handleGetPackageName()
    {
        return UmlUtilities.getPackageName(this.metaObject, this.getNamespaceScope(false), false);
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
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
     */
    protected String getArraySuffix()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    @Override
    protected org.andromda.metafacades.uml.TypeMappings handleGetLanguageMappings()
    {
        final String propertyName = UMLMetafacadeProperties.LANGUAGE_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            String uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                mappings.setArraySuffix(this.getArraySuffix());
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                ModelElementFacadeLogicImpl.logger.error(
                    errMsg,
                    th);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * @return Collection String of org.eclipse.uml2.uml.Stereotype.getName()
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    @Override
    protected Collection<String> handleGetStereotypeNames()
    {
        return UmlUtilities.getStereotypeNames(this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getId()
     */
    @Override
    protected String handleGetId()
    {
        return xmiHelper.getID(this.metaObject);
    }

    /**
     * @return isReservedWord
     * @see org.andromda.metafacades.uml.ModelElementFacade#isReservedWord()
     */
    //@Override
    protected boolean handleIsReservedWord()
    {
        return UMLMetafacadeUtils.isReservedWord(this.getName());
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(String)
     */
    @Override
    protected Object handleFindTaggedValue(final String name)
    {
        Collection taggedValues = this.findTaggedValues(name);
        return taggedValues.isEmpty() ? null : taggedValues.iterator().next();
    }

    /**
     * Assumes no stereotype inheritance
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasStereotype(String)
     */
    @Override
    protected boolean handleHasStereotype(final String stereotypeName)
    {
        return UmlUtilities.containsStereotype(
            this.metaObject,
            stereotypeName);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(String)
     */
    @Override
    protected String handleGetDocumentation(final String indent)
    {
        return this.getDocumentation(
            indent,
            64);
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
            final TypeMappings languageMappings = this.getLanguageMappings();
            if (languageMappings != null)
            {
                fullName = StringUtils.trimToEmpty(languageMappings.getTo(fullName));

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
            // add the name we've constructed so far
            final StringBuilder buffer = new StringBuilder(fullName + "<");

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
            final Collection<TemplateParameterFacade> templateParameters = this.getTemplateParameters();
            for (Iterator<TemplateParameterFacade> parameterIterator = templateParameters.iterator(); parameterIterator.hasNext();)
            {
                final ModelElementFacade modelElement =
                    ((TemplateParameterFacade)parameterIterator.next()).getParameter();

                // TODO: UML14 returns ParameterFacade, UML2 returns ModelElementFacade, so types are wrong from fullyQualifiedName
                // Mapping from UML2 should return ParameterFacade, with a getType method.
                // Add TemplateParameterFacade.getType method - need to access this in vsl templates.
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(String,
     *      int)
     */
    @Override
    protected String handleGetDocumentation(
        final String indent,
        final int lineLength)
    {
        return this.getDocumentation(
            indent,
            lineLength,
            true);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(String)
     */
    @Override
    protected boolean handleHasExactStereotype(final String stereotypeName)
    {
        return this.getStereotypeNames().contains(StringUtils.trimToEmpty(stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraint(String,
     *      String)
     */
    @Override
    protected String handleTranslateConstraint(
        final String name,
        final String translation)
    {
        String translatedExpression = "";
        ConstraintFacade constraint =
            (ConstraintFacade)CollectionUtils.find(
                this.getConstraints(),
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        final ConstraintFacade constraintEval = (ConstraintFacade)object;
                        return StringUtils.trimToEmpty(constraintEval.getName()).equals(StringUtils.trimToEmpty(name));
                    }
                });

        if (constraint != null)
        {
            translatedExpression = constraint.getTranslation(translation);
        }
        return translatedExpression;
    }

    /**
     * Private helper that translates all the expressions contained in the
     * <code>constraints</code>, and returns an array of the translated
     * expressions.
     *
     * @param constraints
     *            the constraints to translate
     * @param translation
     *            the translation to translate <code>to</code>.
     * @return String[] the translated expressions, or null if no constraints
     *         were found
     */
    // TODO: Possible covariant of the method 'translateConstraints' defined in the class 'ModelElementFacadeLogic'
    private String[] translateConstraints(
        final Collection<ConstraintFacade> constraints,
        final String translation)
    {
        String[] translatedExpressions = null;
        if (constraints != null && !constraints.isEmpty())
        {
            translatedExpressions = new String[constraints.size()];
            Iterator<ConstraintFacade> constraintIt = constraints.iterator();
            for (int ctr = 0; constraintIt.hasNext(); ctr++)
            {
                ConstraintFacade constraint = constraintIt.next();
                translatedExpressions[ctr] = constraint.getTranslation(translation);
            }
        }
        return translatedExpressions;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(String,
     *      String)
     */
    @Override
    protected String[] handleTranslateConstraints(
        final String kind,
        final String translation)
    {
        Collection<ConstraintFacade> constraints = this.getConstraints();
        CollectionUtils.filter(
            constraints,
            new Predicate()
            {
                public boolean evaluate(final Object object)
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(String)
     */
    @Override
    protected String[] handleTranslateConstraints(final String translation)
    {
        return this.translateConstraints(
            this.getConstraints(),
            translation);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints(String)
     */
    @Override
    protected Collection<ConstraintFacade> handleGetConstraints(final String kind)
    {
        return CollectionUtils.select(
            this.getConstraints(),
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
                final TaggedValueFacade taggedValue = (TaggedValueFacade)taggedValueIterator.next();

                // does this name match the argument tagged value name ?
                if (UmlUtilities.doesTagValueNameMatch(name, taggedValue.getName()))
                {
                    // 'tagged values' can have arrays of strings as well as
                    // strings as values.
                    /* http://galaxy.andromda.org/jira/browse/UMLMETA-89
                    Object value = taggedValue.getValue();
                    if (value instanceof Collection)
                    {
                        values.addAll((Collection) taggedValue.getValue());
                    }
                    else
                    {
                        values.add(taggedValue.getValue());
                    } */
                    //
                    if (taggedValue.getValues() != null && taggedValue.getValues().size() > 0)
                    {
                        values.addAll(taggedValue.getValues());
                    }
                    // If it matches this taggedValue, and taggedValues are unique, no need to check the rest.
                    break;
                }
            }
        }
        return values;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(String,
     *      int, boolean)
     */
    @Override
    protected String handleGetDocumentation(
        final String indent,
        int lineLength,
        final boolean htmlStyle)
    {
        final StringBuilder documentation = new StringBuilder();

        if (lineLength < 1)
        {
            lineLength = Integer.MAX_VALUE;
        }

        final Collection<Comment> comments = this.metaObject.getOwnedComments();
        if (comments != null && !comments.isEmpty())
        {
            for (Comment comment : comments)
            {
                String commentString = StringUtils.trimToEmpty(comment.getBody());

                // Comment.toString returns org.eclipse.uml2.uml.internal.impl.CommentImpl@95c90f4 (body: )
                /*if (StringUtils.isBlank(commentString))
                {
                    commentString = StringUtils.trimToEmpty(comment.toString());
                }*/
                documentation.append(StringUtils.trimToEmpty(commentString));
                documentation.append(SystemUtils.LINE_SEPARATOR);
            }
        }

        // if there still isn't anything, try a tagged value
        if (StringUtils.isBlank(documentation.toString()))
        {
            documentation.append(
                StringUtils.trimToEmpty((String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_DOCUMENTATION)));
        }

        // TODO: Optional generation of TODO tags for missing documentation
        // if there still isn't anything, create a todo tag
        if (StringUtils.isEmpty(documentation.toString()))
        {
            /*if (Boolean.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_FOR_MISSING_DOCUMENTATION)))
            {
                String todoTag = (String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_TAG);
                documentation.append(todoTag).append(": Model Documentation for " + this.getFullyQualifiedName());
            }*/
        }

        return StringUtilsHelper.format(
            StringUtils.trimToEmpty(documentation.toString()),
            indent,
            lineLength,
            htmlStyle);
    }

    /**
     * If documentation is present, i.e. to add toDoTag or skip a line if not
     * @return true is documentation comment or Documentation stereotype is present
     * @see org.andromda.metafacades.uml.ModelElementFacade#isDocumentationPresent()
     */
    //@Override
    protected boolean handleIsDocumentationPresent()
    {
        boolean rtn = false;
        final Collection<Comment> comments = this.metaObject.getOwnedComments();
        if (comments != null && !comments.isEmpty())
        {
            for (Comment comment : comments)
            {
                String commentString = StringUtils.trimToEmpty(comment.getBody());

                // if there isn't anything in the body, try the name
                if (StringUtils.isNotBlank(commentString))
                {
                    rtn = true;
                    break;
                }
            }
        }

        if (!rtn && StringUtils.isNotBlank((String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_DOCUMENTATION)))
        {
            rtn = true;
        }

        return rtn;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName(boolean)
     */
    @Override
    protected String handleGetPackageName(final boolean modelName)
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTaggedValues()
     */
    @Override
    protected Collection<TagDefinition> handleGetTaggedValues()
    {
        // TODO: UmlUtilities returns TagDefinition, not TaggedValueFacade
        return UmlUtilities.getTaggedValue(this.metaObject);
    }

    /*
     * @see org.andromda.metafacades.uml.ModelElementFacade#getOwnedElements()
    protected Collection<Element> handleGetOwnedElements()
    {
        return this.metaObject.getOwnedElements();
    }
     */

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    @Override
    protected Package handleGetPackage()
    {
        return this.metaObject.getNearestPackage();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getRootPackage()
     */
    @Override
    protected Model handleGetRootPackage()
    {
        // Be careful here, UML2 Model is mapped to a PackageFacade -
        // RootPackage. Need to map Model to UMLResource, not to modelFacade, in metafacade.xml
        return (Model) UmlUtilities.findModel(this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTargetDependencies()
     */
    @Override
    protected Collection<DirectedRelationship> handleGetTargetDependencies()
    {
        ArrayList<DirectedRelationship> dependencies = new ArrayList<DirectedRelationship>();
        dependencies.addAll(UmlUtilities.getAllMetaObjectsInstanceOf(
                DirectedRelationship.class,
                this.metaObject.getModel()));
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    DirectedRelationship relation = (DirectedRelationship) object;
                    if(isAUml14Dependency(relation))
                    {
                        // we only check first, see dependency facade for more detail.
                        return ModelElementFacadeLogicImpl.this.metaObject.equals(relation.getTargets().get(0));
                    }
                    return false;
                }
            });
        return dependencies;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModel()
     */
    @Override
    protected Resource handleGetModel()
    {
        // Be careful here, Model Facade is mapped to resource
        // TODO map ModelFacade to UML2 Model or Package?
        Resource resource = this.metaObject.getModel().eResource();
        if (resource==null)
        {
            logger.error("ModelElementFacadeLogicImpl.handleGetModel: " + this.metaObject + " Model: " + this.metaObject.getModel());
            resource = (Resource) this.metaObject.getModel();
        }
        return resource;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypes()
     */
    @Override
    protected Collection<Stereotype> handleGetStereotypes()
    {
        // TODO: Convert EList to Collection<org.andromda.metafacades.uml.StereotypeFacade>
        return this.metaObject.getAppliedStereotypes();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints()
     */
    @Override
    protected Collection<Constraint> handleGetConstraints()
    {
        ArrayList<Constraint> constraints = new ArrayList<Constraint>();
        constraints.addAll(UmlUtilities.getAllMetaObjectsInstanceOf(Constraint.class, this.metaObject.getModel()));

        CollectionUtils.filter(
                constraints,
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        Constraint constraint = (Constraint) object;
                        return constraint.getConstrainedElements().contains(ModelElementFacadeLogicImpl.this.metaObject);
                    }
                });
        return constraints;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getSourceDependencies()
     */
    @Override
    protected Collection<DirectedRelationship> handleGetSourceDependencies()
    {
        // A more efficient implementation of this would have been to use getClientDependencies() and getTemplateBindings()
        // But it would have required the same filtering
        // This way, the code is the "same" as getTargettingDependencies
        ArrayList<DirectedRelationship> dependencies = new ArrayList<DirectedRelationship>();
        dependencies.addAll(UmlUtilities.getAllMetaObjectsInstanceOf(
                DirectedRelationship.class,
                this.metaObject.getModel()));
        CollectionUtils.filter(
            dependencies,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    DirectedRelationship relation = (DirectedRelationship) object;
                    if(relation != null && isAUml14Dependency(relation) && relation.getSources() != null && relation.getSources().size()>0)
                    {
                        // we only check first, see dependency facade for more detail.
                           return ModelElementFacadeLogicImpl.this.metaObject.equals(relation.getSources().get(0));
                    }
                    return false;
                }
            });
        return dependencies;
    }

    /**
     * This function tests if the given relation is a dependency in UML1.4 sense of term.
     * @param relation The relation to test
     * @return instanceof Abstraction, Deployment, Manifestation, realization, Substitution, Usage
     */
    static boolean isAUml14Dependency(DirectedRelationship relation)
    {
        // this ensure that this relation is either a dependency or a template binding
        boolean isAUml14Dependency = (relation instanceof Dependency) || (relation instanceof TemplateBinding);

        // but we don't want subclass of dependency
        isAUml14Dependency = isAUml14Dependency && !(relation instanceof Abstraction); // present in uml 1.4 (but filter in uml14 facade)
        isAUml14Dependency = isAUml14Dependency && !(relation instanceof Deployment);
        //isAUml14Dependency = isAUml14Dependency && !(relation instanceof Implementation);
        isAUml14Dependency = isAUml14Dependency && !(relation instanceof Manifestation);
        //isAUml14Dependency = isAUml14Dependency && !(relation instanceof Permission); // present in uml 1.4
        isAUml14Dependency = isAUml14Dependency && !(relation instanceof Realization);
        isAUml14Dependency = isAUml14Dependency && !(relation instanceof Substitution);
        isAUml14Dependency = isAUml14Dependency && !(relation instanceof Usage);// present in uml 1.4

        return isAUml14Dependency;
    }

    /**
     * @return stateMachine org.eclipse.uml2.uml.StateMachine
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStateMachineContext()
     */
    @Override
    protected StateMachine handleGetStateMachineContext()
    {
        // TODO: What should this method return ?
        // As I've seen in uml1.4 impl, it should return the statemachine which this element is the context for.
        // Let's say for UML2: Return the owner if the latter is a StateMachine
        StateMachine stateMachine = null;
        Element owner = this.metaObject.getOwner();
        if(owner instanceof StateMachine)
        {
            stateMachine = (StateMachine) owner;
        }
        return (StateMachine)stateMachine;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        final StringBuilder validationName = new StringBuilder();
        final Object seperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        for (NamedElement namespace = (NamedElement)this.metaObject.getOwner(); namespace != null;
            namespace = (NamedElement)namespace.getOwner())
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
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleIsBindingDependenciesPresent()
     */
    @Override
    protected boolean handleIsBindingDependenciesPresent()
    {
        final Collection<DependencyFacade> dependencies = new ArrayList<DependencyFacade>(this.getSourceDependencies());
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
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleIsTemplateParametersPresent()
     */
    @Override
    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO: Be sure it works with RSM / MD11.5
        final Collection<TemplateParameterFacade> params = this.getTemplateParameters();
        return params != null && !params.isEmpty();
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleCopyTaggedValues(org.andromda.metafacades.uml.ModelElementFacade)
     */
    @Override
    protected void handleCopyTaggedValues(final ModelElementFacade element)
    {
        // TODO What to do with this ?
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetTemplateParameter(java.lang.String)
     */
    @Override
    protected Object handleGetTemplateParameter(String parameterName)
    {
        // TODO: Be sure it works with RSM / MD11.5
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

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetTemplateParameters()
     */
    @Override
    protected Collection<TemplateParameter> handleGetTemplateParameters()
    {
        // TODO: Be sure it works with RSM / MD11.5
        Collection<TemplateParameter> templateParameters = new ArrayList<TemplateParameter>();
        if (this.metaObject instanceof TemplateableElement)
        {
            TemplateableElement templateableElement = (TemplateableElement)this.metaObject;
            TemplateSignature templateSignature = templateableElement.getOwnedTemplateSignature();
            if (templateSignature != null)
            {
                templateParameters.addAll(templateSignature.getParameters());
            }
        }
        return templateParameters;
    }

    /**
     * @return this.metaObject.getKeywords()
     * @see org.andromda.metafacades.uml.ModelElementFacade#getKeywords()
     */
    @Override
    protected Collection<String> handleGetKeywords()
    {
        return this.metaObject.getKeywords();
    }

    /**
     * @return element.getLabel()
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLabel()
     */
    @Override
    protected String handleGetLabel()
    {
        final NamedElement element = (NamedElement)this.metaObject;
        return element.getLabel();
    }

    /**
     * @return element.getNamespace()
     */
    // * @see org.andromda.metafacades.uml.ModelElementFacade#getNamespace()
    //@Override
    protected ModelElementFacade handleGetNamespace()
    {
        final NamedElement element = (NamedElement)this.metaObject;
        //return (ModelElementFacade) element.getNamespace();
        return (ModelElementFacade)this.shieldedElement(element.getNamespace());
    }

    /**
     * @return this.metaObject.getOwner()
     * @see org.andromda.metafacades.uml.ModelElementFacade#getOwner()
    //@Override
    protected Element handleGetOwner()
    {
        return this.metaObject.getOwner();
    }
     */

    /**
     * @return element.getQualifiedName()
     * @see org.andromda.metafacades.uml.ModelElementFacade#getQualifiedName()
     */
    protected String handleGetQualifiedName()
    {
        final NamedElement element = (NamedElement)this.metaObject;
        return element.getQualifiedName();
    }

    /**
     * @param keywordName
     * @return this.metaObject.hasKeyword(keywordName)
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasKeyword(String)
     */
    //@Override
    protected boolean handleHasKeyword(String keywordName)
    {
        return this.metaObject.hasKeyword(keywordName);
    }
}
