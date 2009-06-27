package org.andromda.metafacades.uml14;

import java.util.List;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.ExpressionTranslator;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.translation.ocl.ExpressionKinds;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Constraint;

/**
 * Metafacade implementation for org.andromda.metafacades.uml.ConstraintFacade.
 *
 * @see org.andromda.metafacades.uml.ConstraintFacade
 * @author Bob Fields
 */
public class ConstraintFacadeLogicImpl
    extends ConstraintFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ConstraintFacadeLogicImpl(
        Constraint metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getBody()
     */
    @Override
    public String handleGetBody()
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
    @Override
    public Object handleGetContextElement()
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
    @Override
    public boolean handleIsInvariant()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.INV);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPreCondition()
     */
    @Override
    public boolean handleIsPreCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPostCondition()
     */
    @Override
    public boolean handleIsPostCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.POST);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isDefinition()
     */
    @Override
    public boolean handleIsDefinition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.DEF);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isBodyExpression()
     */
    @Override
    public boolean handleIsBodyExpression()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.BODY);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getTranslation(String)
     */
    @Override
    public String handleGetTranslation(String language)
    {
        final Expression expression =
            ExpressionTranslator.instance().translate(
                language,
                this.getBody(),
                this.getContextElement());
        return expression == null ? null : expression.getTranslatedExpression();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(String, int, boolean)
     */
    public String getDocumentation(
        String indent,
        int lineLength,
        boolean htmlStyle)
    {
        String documentation = super.getDocumentation(indent, lineLength, htmlStyle);
        boolean isBlank;

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