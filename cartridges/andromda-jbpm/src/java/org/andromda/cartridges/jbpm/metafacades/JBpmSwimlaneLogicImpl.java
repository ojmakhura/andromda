package org.andromda.cartridges.jbpm.metafacades;

import java.util.Collection;

import org.andromda.cartridges.jbpm.JBpmProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmSwimlane.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlane
 */
public class JBpmSwimlaneLogicImpl
    extends JBpmSwimlaneLogic
{
    public JBpmSwimlaneLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetAssignmentExpressions()
    {
        return findTaggedValues(JBpmProfile.TAGGEDVALUE_ASSIGNMENT_EXPRESSION);
    }
}