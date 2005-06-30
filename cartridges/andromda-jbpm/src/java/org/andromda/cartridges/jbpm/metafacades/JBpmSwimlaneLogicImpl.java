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
    public JBpmSwimlaneLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getActivityGraph().getUseCase() instanceof JBpmProcessDefinition;
    }


    protected Collection handleGetAssignmentExpressions()
    {
        return findTaggedValues(JBpmProfile.TAGGEDVALUE_ASSIGNMENT_EXPRESSION);
    }
}