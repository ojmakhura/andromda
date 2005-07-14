package org.andromda.metafacades.uml14;

import org.andromda.core.translation.Expression;
import org.andromda.core.translation.ExpressionTranslator;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.translation.ocl.ExpressionKinds;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;


/**
 * Metafacade implementation for org.andromda.metafacades.uml.ConstraintFacade.
 *
 * @see org.andromda.metafacades.uml.ConstraintFacade
 */
public class ConstraintFacadeLogicImpl
    extends ConstraintFacadeLogic
{
    public ConstraintFacadeLogicImpl(
        org.omg.uml.foundation.core.Constraint metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getBody()
     */
    public java.lang.String handleGetBody()
    {
        String body = null;
        if (this.metaObject.getBody() != null)
        {
            body = this.metaObject.getBody().getBody();
        }
        return body;
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getContextElement()
     */
    public java.lang.Object handleGetContextElement()
    {
        Object element = null;
        final List elements = this.metaObject.getConstrainedElement();
        if (elements != null && !elements.isEmpty())
        {
            element = elements.get(0);
        }
        return element;
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isInvariant()
     */
    public boolean handleIsInvariant()
    {
        return UMLMetafacadeUtils.isConstraintKind(this.getBody(), ExpressionKinds.INV);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPreCondition()
     */
    public boolean handleIsPreCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(this.getBody(), ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPostCondition()
     */
    public boolean handleIsPostCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(this.getBody(), ExpressionKinds.POST);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isDefinition()
     */
    public boolean handleIsDefinition()
    {
        return UMLMetafacadeUtils.isConstraintKind(this.getBody(), ExpressionKinds.DEF);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isBodyExpression()
     */
    public boolean handleIsBodyExpression()
    {
        return UMLMetafacadeUtils.isConstraintKind(this.getBody(), ExpressionKinds.BODY);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getTranslation(java.lang.String)
     */
    public String handleGetTranslation(String language)
    {
        final Expression expression =
                ExpressionTranslator.instance().translate(language, this.getBody(), this.getContextElement());
        return (expression == null) ? null : expression.getTranslatedExpression();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String, int, boolean)
     */
    public String getDocumentation(
        String indent,
        int lineLength,
        boolean htmlStyle)
    {
        String documentation = super.getDocumentation(indent, lineLength, htmlStyle);
        boolean isBlank = false;

        if (htmlStyle)
        {
            final String plainDocumentation = super.getDocumentation(indent, lineLength, false);
            isBlank = StringUtils.isBlank(plainDocumentation);
        }
        else
        {
            isBlank = StringUtils.isBlank(documentation);
        }

        if (isBlank)
        {
            documentation = "An undocumented constraint has been violated: " + StringEscapeUtils.escapeJava(getBody());
        }
        return documentation;
    }
}