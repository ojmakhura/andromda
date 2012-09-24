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
    private static final long serialVersionUID = 34L;
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

    /**
     *
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodeClassName()
     */
    @Override
    public String getNodeClassName()
    {
        return this.getSuperJBpmEventState().getNodeClassName();
    }

    /**
     *
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodePackageName()
     */
    @Override
    public String getNodePackageName()
    {
        return this.getSuperJBpmEventState().getNodePackageName();
    }

    /**
     *
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getProcessDefinition()
     */
    @Override
    public JBpmProcessDefinition getProcessDefinition()
    {
        return this.getSuperJBpmEventState().getProcessDefinition();
    }

    /**
     * <p>
     * True if this element is part of a business process usecase.
     * </p>
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#isContainedInBusinessProcess()
     */
    @Override
    public boolean isContainedInBusinessProcess()
    {
        return this.getSuperJBpmEventState().isContainedInBusinessProcess();
    }
}