package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.translation.ocl.ExpressionKinds;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Namespace;
import org.eclipse.uml2.VisibilityKind;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ModelElementFacade.
 *
 * @see org.andromda.metafacades.uml.ModelElementFacade
 */
public class ModelElementFacadeLogicImpl
    extends ModelElementFacadeLogic
{
    static XMIHelperImpl xmiHelper = new XMIHelperImpl();

    public ModelElementFacadeLogicImpl(
        org.eclipse.uml2.Element metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    protected java.lang.String handleGetVisibility()
    {
        if (metaObject instanceof NamedElement)
        {
            final NamedElement element = (NamedElement)metaObject;
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
    protected java.lang.String handleGetPackagePath()
    {
        return StringUtils.replace(
            this.getPackageName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR)),
            "/");
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected java.lang.String handleGetName()
    {
        return ((NamedElement)metaObject).getName();
    }

    /**
     * Constructs the package name for the given <code>metaObject</code>, seperating the package name by the given
     * <code>separator</code>.
     *
     * @param metaObject the Model Element
     * @param separator the PSM namespace separator
     * @param modelName true/false on whether or not to get the model package name instead
     *        of the PSM package name.
     * @return the package name.
     */
    private String getPackageName(
        NamedElement metaObject,
        String separator,
        boolean modelName)
    {
        String packageName = "";
        for (NamedElement namespace = (NamedElement)metaObject.getOwner(); !(namespace instanceof Model);
            namespace = (NamedElement)namespace.getOwner())
        {
            packageName = packageName.equals("") ? namespace.getName() : namespace.getName() + separator + packageName;
        }
        if (modelName && StringUtils.isNotBlank(packageName))
        {
            packageName = StringUtils.replace(
                    packageName,
                    separator,
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
        }
        return packageName;
    }

    /**
     * Gets the appropriate namespace property for retrieve the namespace scope operation (dependng on the given
     * <code>modelName</code> flag.
     *
     * @param modelName whether or not the scope operation for the model should be retrieved as oppposed to the mapped
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    protected java.lang.String handleGetPackageName()
    {
        final boolean modelName = false;
        final String packageName =
            this.getPackageName(
                (NamedElement)metaObject,
                this.getNamespaceScope(modelName),
                modelName);
        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    protected java.lang.String handleGetFullyQualifiedName()
    {
        return this.getFullyQualifiedName(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedNamePath()
     */
    protected java.lang.String handleGetFullyQualifiedNamePath()
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
    private final String getArraySuffix()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    protected org.andromda.metafacades.uml.TypeMappings handleGetLanguageMappings()
    {
        final String propertyName = UMLMetafacadeProperties.LANGUAGE_MAPPINGS_URI;
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri = null;
        if (String.class.isAssignableFrom(property.getClass()))
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
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                logger.error(
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    protected java.util.Collection handleGetStereotypeNames()
    {
        return UmlUtilities.getStereotypeNames((NamedElement)metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getId()
     */
    protected java.lang.String handleGetId()
    {
        return xmiHelper.getID(metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#isConstraintsPresent()
     */
    protected boolean handleIsConstraintsPresent()
    {
        return this.getConstraints() != null && !this.getConstraints().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(java.lang.String)
     */
    protected java.lang.Object handleFindTaggedValue(java.lang.String name)
    {
        Collection taggedValues = findTaggedValues(name);
        return taggedValues.isEmpty() ? null : taggedValues.iterator().next();
    }

    /**
     * Assumes no stereotype inheritance
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasStereotype(java.lang.String)
     */
    protected boolean handleHasStereotype(java.lang.String stereotypeName)
    {
        return UmlUtilities.containsStereotype(
            (NamedElement)metaObject,
            stereotypeName);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String)
     */
    protected java.lang.String handleGetDocumentation(java.lang.String indent)
    {
        return getDocumentation(
            indent,
            64);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName(boolean)
     */
    protected java.lang.String handleGetFullyQualifiedName(boolean modelName)
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
                fullName = StringUtils.deleteWhitespace(this.getLanguageMappings().getTo(fullName));

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
        return fullName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String, int)
     */
    protected java.lang.String handleGetDocumentation(
        java.lang.String indent,
        int lineLength)
    {
        return getDocumentation(
            indent,
            lineLength,
            true);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(java.lang.String)
     */
    protected boolean handleHasExactStereotype(java.lang.String stereotypeName)
    {
        return UmlUtilities.containsStereotype(
            (NamedElement)metaObject,
            stereotypeName);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraint(java.lang.String, java.lang.String)
     */
    protected java.lang.String handleTranslateConstraint(
        java.lang.String name,
        java.lang.String translation)
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * Private helper that translates all the expressions contained in the <code>constraints</code>, and returns an
     * array of the translated expressions.
     *
     * @param constraints the constraints to translate
     * @param translation the translation to transate <code>to</code>.
     * @return String[] the translated expressions, or null if no constraints were found
     */
    private String[] translateConstraints(
        Collection constraints,
        String translation)
    {
        String[] translatedExpressions = null;
        if (constraints != null && !constraints.isEmpty())
        {
            translatedExpressions = new String[constraints.size()];
            Iterator constraintIt = constraints.iterator();
            for (int ctr = 0; constraintIt.hasNext(); ctr++)
            {
                ConstraintFacade constraint = (ConstraintFacade)constraintIt.next();
                translatedExpressions[ctr] = constraint.getTranslation(translation);
            }
        }
        return translatedExpressions;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String, java.lang.String)
     */
    protected java.lang.String[] handleTranslateConstraints(
        final java.lang.String kind,
        java.lang.String translation)
    {
        Collection constraints = this.getConstraints();
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String)
     */
    protected java.lang.String[] handleTranslateConstraints(java.lang.String translation)
    {
        return this.translateConstraints(
            this.getConstraints(),
            translation);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints(java.lang.String)
     */
    protected java.util.Collection handleGetConstraints(final java.lang.String kind)
    {
        final Collection filteredConstraints =
            CollectionUtils.select(
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
        return filteredConstraints;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValues(java.lang.String)
     */
    protected java.util.Collection handleFindTaggedValues(java.lang.String name)
    {
        final Collection values = new ArrayList();

        // only search a tagged value when it actually has a name
        if (StringUtils.isNotBlank(name))
        {
            // trim the name, we don't want leading/trailing spaces
            name = StringUtils.trimToEmpty(name);

            // loop over the tagged values
            final Collection taggedValues = this.getTaggedValues();
            for (final Iterator taggedValueIterator = taggedValues.iterator(); taggedValueIterator.hasNext();)
            {
                TagDefinition taggedValue = (TagDefinition)taggedValueIterator.next();

                // does this name match the argument tagged value name ?
                if (name.equals(taggedValue.getName()))
                {
                    values.add(taggedValue.getValue());
                }
            }
        }
        return values;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String, int, boolean)
     */
    protected java.lang.String handleGetDocumentation(
        java.lang.String indent,
        int lineLength,
        boolean htmlStyle)
    {
        final StringBuffer documentation = new StringBuffer();

        if (lineLength < 1)
        {
            lineLength = Integer.MAX_VALUE;
        }

        final Collection comments = metaObject.getOwnedComments();
        if (comments != null && !comments.isEmpty())
        {
            for (final Iterator commentIterator = comments.iterator(); commentIterator.hasNext();)
            {
                final Comment comment = (Comment)commentIterator.next();
                String commentString = StringUtils.trimToEmpty(comment.getBody());

                if (StringUtils.isEmpty(commentString))
                {
                    commentString = StringUtils.trimToEmpty(comment.toString());
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName(boolean)
     */
    protected java.lang.String handleGetPackageName(boolean modelName)
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
    protected java.util.Collection handleGetTaggedValues()
    {
        return UmlUtilities.getAndroMDATags(metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    protected java.lang.Object handleGetPackage()
    {
        return metaObject.getNearestPackage();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getRootPackage()
     */
    protected java.lang.Object handleGetRootPackage()
    {
        return metaObject.getModel();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTargetDependencies()
     */
    protected java.util.Collection handleGetTargetDependencies()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModel()
     */
    protected java.lang.Object handleGetModel()
    {
        return metaObject.getModel();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypes()
     */
    protected java.util.Collection handleGetStereotypes()
    {
        return metaObject.getAppliedStereotypes();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints()
     */
    protected java.util.Collection handleGetConstraints()
    {
        return ((Namespace)metaObject).getOwnedRules();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getSourceDependencies()
     */
    protected java.util.Collection handleGetSourceDependencies()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStateMachineContext()
     */
    protected java.lang.Object handleGetStateMachineContext()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    public String getValidationName()
    {
        final StringBuffer validationName = new StringBuffer();
        final Object seperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        for (NamedElement namespace = (NamedElement)metaObject.getOwner(); namespace != null;
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

    protected boolean handleIsBindingDependenciesPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected void handleCopyTaggedValues(ModelElementFacade element)
    {
        // TODO Auto-generated method stub
        
    }

    protected Object handleGetTemplateParameter(String parameterName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected Collection handleGetTemplateParameters()
    {
        // TODO Auto-generated method stub
        return null;
    }
}