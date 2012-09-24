package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.omg.uml.foundation.core.Comment;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TemplateParameter;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.TemplateParameterFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateParameterFacade
 * @author Bob Fields
 */
public class TemplateParameterFacadeLogicImpl
    extends TemplateParameterFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public TemplateParameterFacadeLogicImpl (TemplateParameter metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getParameter()
     */
    @Override
    protected ModelElement handleGetParameter()
    {
        return metaObject.getParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getDefaultElement
     */
    @Override
    protected ModelElement handleGetDefaultElement()
    {
        return metaObject.getDefaultElement();
    }

    @Override
    protected ClassifierFacade handleGetType()
    {
        final ParameterFacadeLogicImpl parameter = (ParameterFacadeLogicImpl)this.getParameter();
        return parameter.getType();
    }

    @Override
    protected String handleGetGetterSetterTypeName()
    {
        if (this.handleGetType()==null)
        {
            return "";
        }
        else
        {
            // Multiplicity in return type is only supported in UML2
            return this.getType().getFullyQualifiedName();
        }
    }

    @Override
    protected String handleGetName()
    {
        return metaObject.getParameter().getName();
    }

    @Override
    protected String handleGetFullyQualifiedName()
    {
        final ModelElementFacade parameter = this.getParameter();
        return parameter.getFullyQualifiedName();
    }

    @Override
    protected String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    @Override
    protected String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * UML2 only, UML14 has no concept of constrainingClassifier on the TemplateParameter
     * @see org.andromda.metafacades.uml14.TemplateParameterFacadeLogic#handleGetConstrainingClassifiers()
     */
    @Override
    protected Collection<ClassifierFacade> handleGetConstrainingClassifiers()
    {
        return new ArrayList<ClassifierFacade>();
    }

    @Override
    protected ModelElementFacade handleGetOwner()
    {
        return (ModelElementFacade) this.shieldedElement(this.metaObject.getTemplate().getNamespace());
    }

    @Override
    protected String handleGetDocumentation(String indent)
    {
        return getDocumentation(
                indent,
                80 - indent.length());
    }

    @Override
    protected String handleGetDocumentation(String indent, int lineLength)
    {
        return getDocumentation(
                indent,
                lineLength,
                true);
    }

    /**
     * Duplicated from ModelElementFacade because there is no inheritance relationship
     * @see org.andromda.metafacades.uml14.TemplateParameterFacadeLogic#handleGetDocumentation(java.lang.String, int, boolean)
     */
    @Override
    protected String handleGetDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        final StringBuilder documentation = new StringBuilder();

        if (lineLength < 1)
        {
            lineLength = Integer.MAX_VALUE;
        }

        final Collection<Comment> comments = this.metaObject.getParameter().getComment();
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

        // Add toDoTag if doc is empty: make modelers do the documentation.
        if (StringUtils.isEmpty(documentation.toString()))
        {
            if (Boolean.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_FOR_MISSING_DOCUMENTATION)))
            {
                String todoTag = (String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_TAG);
                documentation.append(todoTag).append(": Model Documentation for " + this.getFullyQualifiedName());
            }
        }

        // Only add paragraph tags if doc is not empty
        String rtn = StringUtilsHelper.format(
            StringUtils.trimToEmpty(documentation.toString()),
            indent,
            lineLength,
            htmlStyle);

        return rtn;
    }
    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName
     */
    @Override
    public String getValidationName()
    {
        return metaObject.getParameter().getName();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner
     */
    @Override
    public ModelElement getValidationOwner()
    {
        return metaObject.getTemplate();
    }
}
