package org.andromda.metafacades.uml14;

import org.andromda.core.translation.ExpressionKinds;
import org.andromda.core.translation.ExpressionTranslator;

import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ConstraintFacade.
 *
 * @see org.andromda.metafacades.uml.ConstraintFacade
 */
public class ConstraintFacadeLogicImpl
       extends ConstraintFacadeLogic
       implements org.andromda.metafacades.uml.ConstraintFacade
{
    // ---------------- constructor -------------------------------

    public ConstraintFacadeLogicImpl (org.omg.uml.foundation.core.Constraint metaObject, java.lang.String context)
    {
        super (metaObject, context);
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
        if (elements != null && !elements.isEmpty()) {
            element = elements.get(0);
        }
        return element;
    }

    public boolean handleIsInvariant()
    {
        return getBody().matches(".*\\s" + ExpressionKinds.INV + "\\s*.*");
    }

    public boolean handleIsPreCondition()
    {
        return getBody().matches(".*\\s" + ExpressionKinds.PRE + "\\s*.*");
    }

    public boolean handleIsPostCondition()
    {
        return getBody().matches(".*\\s" + ExpressionKinds.POST + "\\s*.*");
    }

    public boolean handleIsDefinition()
    {
        return getBody().matches(".*\\s" + ExpressionKinds.DEF + "\\s*.*");
    }

    public boolean handleIsBodyExpression()
    {
        return getBody().matches(".*\\s" + ExpressionKinds.BODY + "\\s*.*");
    }

    public String handleGetTranslation(String language)
    {
        return ExpressionTranslator.instance().translate(
                language, getContextElement(), getBody()).getTranslatedExpression();
    }
}
