package org.andromda.metafacades.uml14;

import java.util.List;

import org.andromda.core.translation.ExpressionKinds;
import org.andromda.core.translation.ExpressionTranslator;

/**
 * Metafacade implementation for org.andromda.metafacades.uml.ConstraintFacade.
 * 
 * @see org.andromda.metafacades.uml.ConstraintFacade
 */
public class ConstraintFacadeLogicImpl
    extends ConstraintFacadeLogic
    implements org.andromda.metafacades.uml.ConstraintFacade
{
    // ---------------- constructor -------------------------------

    public ConstraintFacadeLogicImpl(
        org.omg.uml.foundation.core.Constraint metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ConstraintFacade ...

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getBody()
     */
    public java.lang.String handleGetBody()
    {
        return this.metaObject.getBody().getBody();
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getContextElement()
     */
    public java.lang.Object handleGetContextElement()
    {
        Object element = null;
        List elements = this.metaObject.getConstrainedElement();
        if (elements != null && !elements.isEmpty())
        {
            element = elements.get(0);
        }
        return element;
    }

    private static final String KIND_PREFIX_PATTERN = 
        UMLMetafacadeGlobals.CONSTRAINT_KIND_PREFIX_PATTERN;

    private static final String KIND_SUFFIX_PATTERN = 
        UMLMetafacadeGlobals.CONSTRAINT_KIND_SUFFIX_PATTERN;

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isInvariant()
     */
    public boolean handleIsInvariant()
    {
        return getBody().matches(
            KIND_PREFIX_PATTERN + ExpressionKinds.INV + KIND_SUFFIX_PATTERN);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPreCondition()
     */
    public boolean handleIsPreCondition()
    {
        return getBody().matches(
            KIND_PREFIX_PATTERN + ExpressionKinds.PRE + KIND_SUFFIX_PATTERN);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPostCondition()
     */
    public boolean handleIsPostCondition()
    {
        return getBody().matches(
            KIND_PREFIX_PATTERN + ExpressionKinds.POST + KIND_SUFFIX_PATTERN);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isDefinition()
     */
    public boolean handleIsDefinition()
    {
        return getBody().matches(
            KIND_PREFIX_PATTERN + ExpressionKinds.DEF + KIND_SUFFIX_PATTERN);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isBodyExpression()
     */
    public boolean handleIsBodyExpression()
    {
        return getBody().matches(
            KIND_PREFIX_PATTERN + ExpressionKinds.BODY + KIND_SUFFIX_PATTERN);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getTranslation(java.lang.String)
     */
    public String handleGetTranslation(String language)
    {
        return ExpressionTranslator.instance().translate(
            language,
            getContextElement(),
            getBody()).getTranslatedExpression();
    }
}