package org.andromda.metafacades.uml14;

import org.andromda.core.translation.ExpressionKinds;
import org.andromda.core.translation.ExpressionTranslator;
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
        implements org.andromda.metafacades.uml.ConstraintFacade
{
    // ---------------- constructor -------------------------------

    public ConstraintFacadeLogicImpl(org.omg.uml.foundation.core.Constraint metaObject,
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

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isInvariant()
     */
    public boolean handleIsInvariant()
    {
        return UMLMetafacadeUtils.isConstraintKind(getBody(),
                ExpressionKinds.INV);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPreCondition()
     */
    public boolean handleIsPreCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(getBody(),
                ExpressionKinds.PRE);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isPostCondition()
     */
    public boolean handleIsPostCondition()
    {
        return UMLMetafacadeUtils.isConstraintKind(getBody(),
                ExpressionKinds.POST);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isDefinition()
     */
    public boolean handleIsDefinition()
    {
        return UMLMetafacadeUtils.isConstraintKind(getBody(),
                ExpressionKinds.DEF);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#isBodyExpression()
     */
    public boolean handleIsBodyExpression()
    {
        return UMLMetafacadeUtils.isConstraintKind(getBody(),
                ExpressionKinds.BODY);
    }

    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getTranslation(java.lang.String)
     */
    public String handleGetTranslation(String language)
    {
        return ExpressionTranslator.instance().translate(language,
                getContextElement(),
                getBody()).getTranslatedExpression();
    }

    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        String documentation = super.getDocumentation(indent, lineLength, htmlStyle);
        boolean isBlank = false;

        if (htmlStyle)
        {
            String plainDocumentation = super.getDocumentation(indent, lineLength, false);
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