package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.BindingFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EnumerationLiteralFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.RedefinableTemplateSignatureFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateArgumentFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.translation.ocl.ExpressionKinds;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Feature;
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
    private static final long serialVersionUID = 34L;
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
        String packageName = UML14MetafacadeUtils.getPackageName(
            this.metaObject,
            this.getNamespaceScope(modelName),
            modelName);
        
        //package names are treated differently so we have to apply the name mask here
        //since there isn't a packageNameMask, we're using the modelElementNameMask
        String nameMask = null;
        try
        {
            nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.MODEL_ELEMENT_NAME_MASK));
        }
        catch (Exception ignore)
        {
            logger.warn("modelElementNameMask not found in " + this.toString() + " (getPackageName)");
            nameMask = "none";
        }

        return NameMasker.mask(packageName, nameMask);
        
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName(boolean)
     */
    @Override
    protected String handleGetPackageName(boolean modelName)
    {
        String packageName = this.handleGetPackageName();
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
        return handleGetBindedFullyQualifiedName(modelName, Collections.<BindingFacade>emptyList());
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
        return this.handleGetFullyQualifiedName(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValues(String)
     */
    @Override
    protected Collection<Object> handleFindTaggedValues(String name)
    {
        final Collection<Object> values = new ArrayList<Object>();

        // only search a tagged value when it actually has a name
        if (StringUtils.isNotBlank(name))
        {
            // trim the name, we don't want leading/trailing spaces
            name = StringUtils.trimToEmpty(name);

            for (final TaggedValueFacade taggedValue: this.getTaggedValues())
            {
                // does this name match the argument tagged value name ?
                // Check both the UML14 format name @andromda.value and EMF Format andromda_whatever
                String tagName = taggedValue.getName();
                if (name.equals(tagName) || MetafacadeUtils.getEmfTaggedValue(name).equals(tagName)
                    || MetafacadeUtils.getUml14TaggedValue(name).equals(tagName))
                {
                    for (final Object value : taggedValue.getValues())
                    {
                        // only process tagged values when they actually have a non-empty value
                        if (value != null)
                        {
                            // if an enumeration literal is referenced we assume its name
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
        return this.handleGetStereotypeNames().contains(StringUtils.trimToEmpty(stereotypeName));
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
        Collection<String> stereotypeNames = new ArrayList<String>();

        Collection<ModelElement> stereotypes = metaObject.getStereotype();
        for (final ModelElement stereotype : stereotypes)
        {
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
            this.handleGetFullyQualifiedName(),
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
            this.handleGetPackageName(),
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
            100 - indent.length());
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
        final StringBuilder documentation = new StringBuilder();
        
        if (lineLength < 1)
        {
            lineLength = Integer.MAX_VALUE;
        }

        final Collection<Comment> comments = this.metaObject.getComment();
        if (comments != null && !comments.isEmpty())
        {
            for (Comment comment : comments)
            {
                String commentString = StringUtils.trimToEmpty(comment.getBody());

                // if there isn't anything in the body, try the name
                if (StringUtils.isBlank(commentString))
                {
                    commentString = StringUtils.trimToEmpty(comment.getName());
                }
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
        // Add toDoTag if doc is empty: make modelers do the documentation.
        if (StringUtils.isEmpty(documentation.toString()))
        {
            if (Boolean.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_FOR_MISSING_DOCUMENTATION)))
            {
                String todoTag = (String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_TAG);
                documentation.append(todoTag).append(": Model Documentation for " + this.handleGetFullyQualifiedName());
            }
        }
        String trimmed = StringUtils.trimToEmpty(documentation.toString());
        if(lineLength > trimmed.length()) {
        	lineLength = 100 - indent.length();
        }
        // Only add paragraph tags if doc is not empty
        String rtn = StringUtilsHelper.format(
        	trimmed,
            indent,
            lineLength,
            htmlStyle);

        return rtn;
    }

    /**
     * If documentation is present, i.e. to add toDoTag or skip a line if not
     * @return true is documentation comment or Documentation stereotype is present
     * @see org.andromda.metafacades.uml.ModelElementFacade#isDocumentationPresent()
     */
    @Override
    protected boolean handleIsDocumentationPresent()
    {
        boolean rtn = false;
        final Collection<Comment> comments = this.metaObject.getComment();
        if (comments != null && !comments.isEmpty())
        {
            for (Comment comment : comments)
            {
                // if there isn't anything in the body, try the name
                if (StringUtils.isNotBlank(comment.getBody())|| StringUtils.isNotBlank(comment.getName()))
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
        String name=metaObject.getName();
        
        if(name != null)
        {
            String nameMask = null;
            try
            {
                nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.MODEL_ELEMENT_NAME_MASK));
            }
            catch (Exception ignore)
            {
                logger.warn("modelElementNameMask not found in " + this.toString());
                nameMask = "none";
            }
            name = NameMasker.mask(name, nameMask);
        }

        return name;
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
                final String message = "Error getting '" + propertyName + "' --> '" + uri + '\'';
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
     * @return Feature owner, or namespace
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    //@Override
    protected Object handleGetOwner()
    {
        if (this.metaObject instanceof Feature)
        {
            Feature feature = (Feature)this.metaObject;
            return feature.getOwner();
        }
        return metaObject.getNamespace();
    }

    /**
     * @return Collection of Features or Namespace elements
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    //@Override
    protected Collection handleGetOwnedElements()
    {
        if (this.metaObject instanceof Classifier)
        {
            Classifier classifier = (Classifier)this.metaObject;
            return classifier.getFeature();
        }
        if (this.metaObject instanceof Namespace)
        {
            Namespace namespace = (Namespace)this.metaObject;
            return namespace.getOwnedElement();
        }
        return null;
    }

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
                ConstraintFacade constraint = constraintIt.next();
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
        for (final StateMachine machine : machines)
        {
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
        final StringBuilder validationName = new StringBuilder();
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
        if (StringUtils.isNotBlank(this.handleGetName()))
        {
            validationName.append(this.handleGetName());
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#isReservedWord()
     */
    @Override
    protected boolean handleIsReservedWord()
    {
        return UMLMetafacadeUtils.isReservedWord(metaObject.getName());
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
        if (StringUtils.isNotBlank(parameterName))
        {
            parameterName = StringUtils.trimToEmpty(parameterName);
            final Collection<TemplateParameterFacade> parameters = this.getTemplateParameters();
            if (parameters != null && !parameters.isEmpty())
            {
                for (final TemplateParameterFacade currentTemplateParameter : parameters)
                {
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
     * {@inheritDoc}
     */
    @Override
    protected String handleGetBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        // This cast is not safe yet, but will be as soon as the filtering will be done
        @SuppressWarnings("unchecked")
        final Collection<BindingFacade> bindingFacades = new ArrayList(bindedElement.getSourceDependencies());
        CollectionUtils.filter(
            bindingFacades,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof BindingFacade;
                }
            });
        return handleGetBindedFullyQualifiedName(false, bindingFacades);
    }

    /**
     * <p>
     * Returns the fully qualified name of the model element. The fully
     * qualified name includes complete package qualified name of the
     * underlying model element.  If modelName is true, then the
     * original name of the model element (the name contained within
     * the model) will be the name returned, otherwise a name from a
     * language mapping will be returned. Moreover use the given collection
     * of {@link BindingFacade} to bind templates parameters to their actual
     * type.
     * </p>
     * @param modelName boolean
     * @param bindingFacades Collection
     * @return String
     */
    private String handleGetBindedFullyQualifiedName(boolean modelName, Collection<BindingFacade> bindingFacades)
    {
        String fullName = StringUtils.trimToEmpty(this.handleGetName());
        final String packageName = this.handleGetPackageName(true);
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
            // Retrieve all template parameters
            final Collection<TemplateParameterFacade> templateParameters = this.getTemplateParameters();

            // Construct a map of the TemplateParameterFacade to replace
            Map<TemplateParameterFacade, ModelElementFacade> bindedParameters = new HashMap<TemplateParameterFacade, ModelElementFacade>();
            for(BindingFacade bindingFacade : bindingFacades)
            {
                ModelElementFacade targetElement = bindingFacade.getTargetElement();
                if(targetElement instanceof RedefinableTemplateSignatureFacade)
                {
                    targetElement = ((RedefinableTemplateSignatureFacade) targetElement).getClassifier();
                }
                if(this.equals(targetElement))
                {
                    final Collection<TemplateArgumentFacade> arguments = bindingFacade.getArguments();
                    if(arguments.size() != getTemplateParameters().size())
                    {
                        throw new IllegalStateException("The size of the arguments of the BindingFacace must be equals to the size of the TemplateParameter collection of this element.");
                    }

                    Iterator<TemplateParameterFacade> templateParametersIterator = templateParameters.iterator();
                    Iterator<TemplateArgumentFacade> templateArgumentsIterator = arguments.iterator();

                    while(templateParametersIterator.hasNext())
                    {
                        final TemplateParameterFacade templateParameter = templateParametersIterator.next();
                        final TemplateArgumentFacade templateArgument =  templateArgumentsIterator.next();
                        bindedParameters.put(templateParameter, templateArgument.getElement());
                    }
                }
            }
            if(bindedParameters.isEmpty())
            {
                for(TemplateParameterFacade templateParameterFacade : templateParameters)
                {
                    bindedParameters.put(templateParameterFacade, templateParameterFacade.getParameter());
                }
            }

            // we'll be constructing the parameter list in this buffer
            // add the name we've constructed so far
            final StringBuilder buffer = new StringBuilder(fullName + '<');

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
            for (final Iterator<TemplateParameterFacade> parameterIterator = templateParameters.iterator(); parameterIterator.hasNext();)
            {
                final ModelElementFacade modelElement = bindedParameters.get(parameterIterator.next());

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
            buffer.append('>');

            // we have constructed the full name in the buffer
            fullName = buffer.toString();
        }

        return fullName;
    }

    // Valid identifier starts with alphanum or _$ and contains only alphanum or _$ or digits
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z_$][a-zA-Z\\d_$]*");
    /**
     * Return true if name matches the pattern [a-zA-Z_$][a-zA-Z\\d_$]*
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleIsValidIdentifierName()
     */
    @Override
    protected boolean handleIsValidIdentifierName()
    {
        final String name = this.handleGetName();
        return IDENTIFIER_PATTERN.matcher(name).matches();
    }

    @Override
    protected Collection<String> handleGetAdditionalAnnotations() {
        HashSet<String> annotations = new HashSet<String>();
        for (Object o : this.findTaggedValues(UMLProfile.TAGGEDVALUE_ADDITIONAL_ANNOTATION))
        {
            annotations.add(o.toString());
        }
        return annotations;
    }

    @Override
    public Collection<String> handleGetAdditionalExtends() {
        
        HashSet<String> extensions = new HashSet<String>();
        for (Object o : this.findTaggedValues(UMLProfile.TAGGEDVALUE_ADDITIONAL_EXTENDS))
        {
            extensions.add(o.toString());
        }
        return extensions;
    }

    @Override
    public Collection<String> handleGetAdditionalImplements() {
        HashSet<String> implementations = new HashSet<String>();
        for (Object o : this.findTaggedValues(UMLProfile.TAGGEDVALUE_ADDITIONAL_IMPLEMENTS))
        {
            implementations.add(o.toString());
        }
        return implementations;
    }
}
