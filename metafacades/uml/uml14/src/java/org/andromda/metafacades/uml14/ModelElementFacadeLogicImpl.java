package org.andromda.metafacades.uml14;

import org.andromda.core.common.HTMLAnalyzer;
import org.andromda.core.common.HTMLParagraph;
import org.andromda.core.mapping.Mappings;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.translation.ExpressionKinds;
import org.andromda.metafacades.uml.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.modelmanagement.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Metaclass facade implementation.
 */
public class ModelElementFacadeLogicImpl
        extends ModelElementFacadeLogic
        implements org.andromda.metafacades.uml.ModelElementFacade
{
    // ---------------- constructor -------------------------------

    public ModelElementFacadeLogicImpl(org.omg.uml.foundation.core.ModelElement metaObject,
                                       String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetTaggedValues()
     */
    public java.util.Collection handleGetTaggedValues()
    {
        return metaObject.getTaggedValue();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    public String handleGetPackageName()
    {
        String packageName = "";

        for (ModelElement namespace = metaObject.getNamespace(); (namespace instanceof org.omg.uml.modelmanagement.UmlPackage)
                && !(namespace instanceof Model); namespace = namespace
                        .getNamespace())
        {
            packageName = "".equals(packageName)
                    ? namespace.getName()
                    : namespace.getName() + UMLMetafacadeGlobals.PACKAGE_SEPERATOR
                    + packageName;
        }

        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName(boolean)
     */
    public java.lang.String handleGetFullyQualifiedName(boolean modelName)
    {
        String fullName = StringUtils.trimToEmpty(this.getName());
        String packageName = getPackageName();

        if (StringUtils.isNotEmpty(packageName))
        {
            fullName = packageName + UMLMetafacadeGlobals.PACKAGE_SEPERATOR
                    + fullName;
        }

        if (!modelName)
        {
            if (this.getLanguageMappings() != null)
            {
                fullName = StringUtils.deleteWhitespace(this
                        .getLanguageMappings().getTo(fullName));
            }
        }
        return fullName;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public java.lang.String handleGetFullyQualifiedName()
    {
        return this.getFullyQualifiedName(false);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(java.lang.String)
     */
    public Object handleFindTaggedValue(String name)
    {
        name = StringUtils.trimToEmpty(name);
        Collection taggedValues = this.getTaggedValues();
        Object value = null;
        for (Iterator taggedValueIt = taggedValues.iterator(); taggedValueIt
                .hasNext();)
        {
            TaggedValueFacade taggedValue = (TaggedValueFacade) taggedValueIt
                    .next();
            String tagName = taggedValue.getName();
            if (tagName.equals(name))
            {
                value = taggedValue.getValue();
                break;
            }
        }

        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasStereotype(java.lang.String)
     */
    public boolean handleHasStereotype(final String stereotypeName)
    {
        Collection stereotypes = this.getStereotypes();

        boolean hasStereotype = StringUtils.isNotBlank(stereotypeName)
                && stereotypes != null && !stereotypes.isEmpty();

        if (hasStereotype)
        {
            class StereotypeFilter
                    implements Predicate
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = false;
                    StereotypeFacade stereotype = (StereotypeFacade) object;
                    String name = StringUtils.trimToEmpty(stereotype.getName());
                    valid = stereotypeName.equals(name);
                    while (!valid && stereotype != null)
                    {
                        stereotype = (StereotypeFacade) stereotype
                                .getGeneralization();
                        valid = stereotype != null
                                && StringUtils.trimToEmpty(stereotype.getName())
                                .equals(stereotypeName);
                    }
                    return valid;
                }
            }
            hasStereotype = CollectionUtils.find(this.getStereotypes(),
                    new StereotypeFilter()) != null;
        }
        return hasStereotype;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(java.lang.String)
     */
    public boolean handleHasExactStereotype(String stereotypeName)
    {
        return this.getStereotypeNames().contains(StringUtils.trimToEmpty(stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    public String handleGetVisibility()
    {
        StringBuffer visibility = new StringBuffer();
        VisibilityKind visibilityKind = metaObject.getVisibility();
        if (visibilityKind != null)
        {
            String visibilityString = visibilityKind.toString();
            visibility.append(visibilityString.substring(3, visibilityString
                    .length()));
            if (this.getLanguageMappings() != null)
            {
                visibility = new StringBuffer(this.getLanguageMappings().getTo(visibility.toString()));
            }
        }
        return visibility.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    public java.util.Collection handleGetStereotypeNames()
    {
        Collection stereotypeNames = new ArrayList();

        Collection stereotypes = metaObject.getStereotype();
        for (Iterator stereotypeIt = stereotypes.iterator(); stereotypeIt
                .hasNext();)
        {
            ModelElement stereotype = (ModelElement) stereotypeIt.next();
            stereotypeNames.add(StringUtils.trimToEmpty(stereotype.getName()));
        }
        return stereotypeNames;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String handleGetFullyQualifiedNamePath()
    {
        return this.getFullyQualifiedName().replace(UMLMetafacadeGlobals.PACKAGE_SEPERATOR,
                '/');
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackagePath()
     */
    public String handleGetPackagePath()
    {
        return this.getPackageName().replace(UMLMetafacadeGlobals.PACKAGE_SEPERATOR,
                '/');
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String)
     */
    public String handleGetDocumentation(String indent)
    {
        return getDocumentation(indent, 64);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String,
            *      int)
     */
    public String handleGetDocumentation(String indent, int lineLength)
    {
        return getDocumentation(indent, lineLength, true);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String,
            *      int, boolean)
     */
    public String handleGetDocumentation(String indent,
                                         int lineLength,
                                         boolean htmlStyle)
    {
        if (StringUtils.isEmpty(indent))
        {
            indent = "";
        }
        StringBuffer documentation = new StringBuffer();
        Collection comments = this.metaObject.getComment();
        if (comments != null && !comments.isEmpty())
        {
            Iterator commentIt = comments.iterator();
            while (commentIt.hasNext())
            {
                Comment comment = (Comment) commentIt.next();
                String commentString = StringUtils.trimToEmpty(comment
                        .getBody());
                //if there isn't anything in the body, try the name
                if (StringUtils.isEmpty(commentString))
                {
                    commentString = StringUtils.trimToEmpty(comment.getName());
                }
                documentation.append(StringUtils.trimToEmpty(commentString));
            }
        }
        // if there still isn't anything, try a tagged value
        if (StringUtils.isEmpty(documentation.toString()))
        {
            documentation.append(StringUtils.trimToEmpty((String) this
                    .findTaggedValue(UMLProfile.TAGGEDVALUE_DOCUMENTATION)));
        }
        try
        {
            String newLine = "\n";
            String startParaTag = (htmlStyle) ? "<p>" : "";
            String endParaTag = (htmlStyle) ? "</p>" : "";
            Collection paragraphs = new HTMLAnalyzer(lineLength)
                    .htmlToParagraphs(documentation.toString());
            if (paragraphs != null && !paragraphs.isEmpty())
            {
                documentation = new StringBuffer();
                for (Iterator paragraphIt = paragraphs.iterator(); paragraphIt
                        .hasNext();)
                {
                    HTMLParagraph paragraph = (HTMLParagraph) paragraphIt.next();
                    documentation.append(indent + startParaTag + newLine);
                    Collection lines = paragraph.getLines();
                    if (lines != null && !lines.isEmpty())
                    {
                        Iterator lineIt = lines.iterator();
                        while (lineIt.hasNext())
                        {
                            documentation.append(indent + lineIt.next()
                                    + newLine);
                        }
                    }
                    documentation.append(indent + endParaTag);
                    if (paragraphIt.hasNext())
                    {
                        documentation.append(newLine);
                    }
                }
            }
            else
            {
                documentation.append(indent);
            }
        }
        catch (Throwable th)
        {
            final String errMsg = "Error performing ModelElementFacadeImpl.getDocumentation";
            logger.error(errMsg, th);
        }
        return documentation.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String handleGetName()
    {
/*
        String name = null;
        try
        {
            Object maxFileNameLengthObject = this.getConfiguredProperty("maxFileNameLength");
            int maxFileNameLength = Integer.parseInt(String.valueOf(maxFileNameLengthObject));
            if (maxFileNameLength > 0)
            {
                name = StringUtilsHelper.crunch(metaObject.getName(), maxFileNameLength);
                name = MetafacadeUtils.createUniqueName(name);
            }
            else
            {
                name = metaObject.getName();
            }
        }
        catch (Exception e)
        {
            name = metaObject.getName();
        }
        return name;
*/
        return metaObject.getName();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    public Mappings handleGetLanguageMappings()
    {
        final String propertyName = "languageMappingsUri";
        Object property = this.getConfiguredProperty(propertyName);
        Mappings mappings = null;
        String uri = null;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String) property;
            try
            {
                mappings = Mappings.getInstance(uri);
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '"
                        + uri + "'";
                logger.error(errMsg, th);
                //don't throw the exception
            }
        }
        else
        {
            mappings = (Mappings) property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    protected Object handleGetPackage()
    {
        return metaObject.getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getRootPackage()
     */
    protected Object handleGetRootPackage()
    {
        return UMLMetafacadeUtils.getRootPackage();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDependencies()
     */
    protected java.util.Collection handleGetDependencies()
    {
        return new FilteredCollection(metaObject.getClientDependency())
        {
            public boolean evaluate(Object object)
            {
                return (object instanceof Dependency)
                        && !(object instanceof Abstraction);
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypes()
     */
    protected java.util.Collection handleGetStereotypes()
    {
        return this.metaObject.getStereotype();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getNameSpace()
     */
    protected Object handleGetNameSpace()
    {
        return metaObject.getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacadeLogic#getModel()
     */
    protected Object handleGetModel()
    {
        return MetafacadeFactory.getInstance().getModel().getModel();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacadeLogic#getConstraints()
     */
    protected Collection handleGetConstraints()
    {
        return this.metaObject.getConstraint();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints(java.lang.String)
     */
    public Collection handleGetConstraints(final String kind)
    {
        final Collection filteredConstraints = CollectionUtils.select(getConstraints(),
                new Predicate()
                {
                    public boolean evaluate(Object o)
                    {
                        if (o instanceof ConstraintFacade)
                        {
                            ConstraintFacade constraint = (ConstraintFacade) o;
                            return ((ExpressionKinds.BODY.equals(kind) && constraint
                                    .isBodyExpression())
                                    || (ExpressionKinds.DEF.equals(kind) && constraint
                                    .isDefinition())
                                    || (ExpressionKinds.INV.equals(kind) && constraint
                                    .isInvariant())
                                    || (ExpressionKinds.PRE.equals(kind) && constraint
                                    .isPreCondition()) || (ExpressionKinds.POST
                                    .equals(kind) && constraint.isPostCondition()));
                        }
                        return false;
                    }
                });
        return filteredConstraints;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraint(java.lang.String,
            *      java.lang.String)
     */
    public String handleTranslateConstraint(final String name,
                                            String translation)
    {
        String translatedExpression = "";
        ConstraintFacade constraint = (ConstraintFacade) CollectionUtils.find(this.getConstraints(),
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        ConstraintFacade constraint = (ConstraintFacade) object;
                        return StringUtils.trimToEmpty(constraint.getName())
                                .equals(StringUtils.trimToEmpty(name));
                    }
                });

        if (constraint != null)
        {
            translatedExpression = constraint.getTranslation(translation);
        }
        return translatedExpression;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String)
     */
    public java.lang.String[] handleTranslateConstraints(String translation)
    {
        return this.translateConstraints(this.getConstraints(), translation);
    }

    /**
     * Private helper that translates all the expressions contained in the
     * <code>constraints</code>, and returns an array of the translated
     * expressions.
     *
     * @param constraints the constraints to translate
     * @param translation the translation to transate <code>to</code>.
     * @return String[] the translated expressions, or null if no constraints
     *         were found
     */
    private String[] translateConstraints(Collection constraints,
                                          String translation)
    {
        String[] translatedExpressions = null;
        if (constraints != null && !constraints.isEmpty())
        {
            translatedExpressions = new String[constraints.size()];
            Iterator constraintIt = constraints.iterator();
            for (int ctr = 0; constraintIt.hasNext(); ctr++)
            {
                ConstraintFacade constraint = (ConstraintFacade) constraintIt
                        .next();
                translatedExpressions[ctr] = constraint
                        .getTranslation(translation);
            }
        }
        return translatedExpressions;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String,
            *      java.lang.String)
     */
    public java.lang.String[] handleTranslateConstraints(final String kind,
                                                         String translation)
    {
        Collection constraints = this.getConstraints();
        CollectionUtils.filter(constraints, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                ConstraintFacade constraint = (ConstraintFacade) object;
                return UMLMetafacadeUtils.isConstraintKind(constraint.getBody(),
                        kind);
            }
        });
        return this.translateConstraints(constraints, translation);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer toString = new StringBuffer(this.getClass().getName());
        String name = this.getFullyQualifiedName(true);
        if (StringUtils.isNotEmpty(name))
        {
            toString.append("[");
            toString.append(name);
            toString.append("]");
        }
        return toString.toString();
    }
}