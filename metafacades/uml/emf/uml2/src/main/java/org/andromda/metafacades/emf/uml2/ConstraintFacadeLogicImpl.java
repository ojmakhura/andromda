package org.andromda.metafacades.emf.uml2;

import java.util.List;

import org.andromda.core.translation.Expression;
import org.andromda.core.translation.ExpressionTranslator;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.translation.ocl.ExpressionKinds;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ConstraintFacade.
 *
 * @see org.andromda.metafacades.uml.ConstraintFacade
 */
public class ConstraintFacadeLogicImpl
    extends ConstraintFacadeLogic
{
    public ConstraintFacadeLogicImpl(
        org.eclipse.uml2.Constraint metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getBody()
     */
    protected java.lang.String handleGetBody()
    {
        String body = null;
        if (this.metaObject.getSpecification() != null)
        {
            body = this.metaObject.getSpecification().stringValue();
        }
        return body;
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isInvariant()
     */
    protected boolean handleIsInvariant()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.INV);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPreCondition()
     */
    protected boolean handleIsPreCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPostCondition()
     */
    protected boolean handleIsPostCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.POST);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isDefinition()
     */
    protected boolean handleIsDefinition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.DEF);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isBodyExpression()
     */
    protected boolean handleIsBodyExpression()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.BODY);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getTranslation(java.lang.String)
     */
    protected java.lang.String handleGetTranslation(java.lang.String language)
    {
        final Expression expression =
            ExpressionTranslator.instance().translate(
                language,
                this.getBody(),
                this.getContextElement());
        return expression == null ? null : expression.getTranslatedExpression();
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getContextElement()
     */
    protected java.lang.Object handleGetContextElement()
    {
        Object element = null;
        final List elements = this.metaObject.getConstrainedElements();
        if (elements != null && !elements.isEmpty())
        {
            element = elements.get(0);
        }
        return element;
    }
}