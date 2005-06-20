package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmSwimlane.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlane
 */
public class JBpmSwimlaneLogicImpl
    extends JBpmSwimlaneLogic
{

    public JBpmSwimlaneLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected Collection handleGetAssignmentExpressions()
    {
        return findTaggedValues(JBpmProfile.TAGGED_VALUE_ASSIGNMENT_EXPRESSION);
    }
}