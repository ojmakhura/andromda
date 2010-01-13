package org.andromda.metafacades.emf.uml22;

import java.util.List;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.ExpressionTranslator;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.translation.ocl.ExpressionKinds;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;

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
        final Constraint metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getBody()
     */
    @Override
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
     * @see org.andromda.metafacades.uml.ConstraintFacade#isInvariant()
     */
    @Override
    protected boolean handleIsInvariant()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.INV);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPreCondition()
     */
    @Override
    protected boolean handleIsPreCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPostCondition()
     */
    @Override
    protected boolean handleIsPostCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.POST);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isDefinition()
     */
    @Override
    protected boolean handleIsDefinition()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.DEF);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isBodyExpression()
     */
    @Override
    protected boolean handleIsBodyExpression()
    {
        return UMLMetafacadeUtils.isConstraintKind(
            this.getBody(),
            ExpressionKinds.BODY);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getTranslation(String)
     */
    @Override
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
     * @see org.andromda.metafacades.uml.ConstraintFacade#getContextElement()
     */
    @Override
    protected Element handleGetContextElement()
    {
        Element element = null;
        final List<Element> elements = this.metaObject.getConstrainedElements();
        if (elements != null && !elements.isEmpty())
        {
            element = elements.get(0);
        }
        return element;
    }
}
