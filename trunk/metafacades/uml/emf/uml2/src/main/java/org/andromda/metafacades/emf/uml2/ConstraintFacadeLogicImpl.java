package org.andromda.metafacades.emf.uml2;

import java.util.List;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.ExpressionTranslator;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.translation.ocl.ExpressionKinds;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ConstraintFacade.
 *
 * @see org.andromda.metafacades.uml.ConstraintFacade
 */
public class ConstraintFacadeLogicImpl
    extends ConstraintFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ConstraintFacadeLogicImpl(
        final org.eclipse.uml2.Constraint metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getSpecification()
     * @see org.andromda.metafacades.uml.ConstraintFacade#getBody()
     */
    protected String handleGetBody()
    {
        String body = null;
        if (this.metaObject.getSpecification() != null)
        {
            body = this.metaObject.getSpecification().stringValue();
        }
        return body;
    }

    /**
     * @return isConstraintKind(ExpressionKinds.INV)
     * @see org.andromda.metafacades.uml.ConstraintFacade#isInvariant()
     */
    protected boolean handleIsInvariant()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.INV);
    }

    /**
     * @return isConstraintKind(ExpressionKinds.PRE)
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPreCondition()
     */
    protected boolean handleIsPreCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.PRE);
    }

    /**
     * @return isConstraintKind(ExpressionKinds.POST)
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPostCondition()
     */
    protected boolean handleIsPostCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.POST);
    }

    /**
     * @return isConstraintKind(ExpressionKinds.DEF)
     * @see org.andromda.metafacades.uml.ConstraintFacade#isDefinition()
     */
    protected boolean handleIsDefinition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.DEF);
    }

    /**
     * @return isConstraintKind(ExpressionKinds.BODY)
     * @see org.andromda.metafacades.uml.ConstraintFacade#isBodyExpression()
     */
    protected boolean handleIsBodyExpression()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.BODY);
    }

    /**
     * @param language
     * @return getTranslatedExpression
     * @see org.andromda.metafacades.uml.ConstraintFacade#getTranslation(String)
     */
    protected String handleGetTranslation(final String language)
    {
        final Expression expression =
            ExpressionTranslator.instance().translate(
                language,
                this.getBody(),
                this.getContextElement());
        return expression == null ? null : expression.getTranslatedExpression();
    }

    /**
     * @return getConstrainedElements.get(0)
     * @see org.andromda.metafacades.uml.ConstraintFacade#getContextElement()
     */
    protected Object handleGetContextElement()
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