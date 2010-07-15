package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmState.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmState
 */
public class JBpmStateLogicImpl
    extends JBpmStateLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public JBpmStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return hasStereotype(JBpmProfile.STEREOTYPE_TASK)
     */
    protected boolean handleIsTaskNode()
    {
        return hasStereotype(JBpmProfile.STEREOTYPE_TASK);
    }

    /**
     * @return getPartition()
     * @see #getPartition()
     */
    protected Object handleGetSwimlane()
    {
        return this.getPartition();
    }
}