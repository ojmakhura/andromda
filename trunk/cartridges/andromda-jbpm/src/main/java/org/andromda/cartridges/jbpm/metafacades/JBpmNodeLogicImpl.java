package org.andromda.cartridges.jbpm.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmNode.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode
 */
public class JBpmNodeLogicImpl
    extends JBpmNodeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JBpmNodeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return !this.getTasks().isEmpty()
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#isTaskNode()
     */
    protected boolean handleIsTaskNode()
    {
        return !this.getTasks().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNodeLogic#handleGetSwimlane()
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getSwimlane()
     */
    protected Object handleGetSwimlane()
    {
        return this.getPartition();
    }

    //TODO Generation from UML2 Metafacade model does not put these methods in *Logic generated class.
    // Implementation copied from UML14 generated version, to avoid compiler error on missing method implementation from abstract method.
    /**
     *
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodeClassName()
     */
    //@Override
    public String getNodeClassName()
    {
        return this.getSuperJBpmEventState().getNodeClassName();
    }

    /**
     *
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodePackageName()
     */
    //@Override
    public String getNodePackageName()
    {
        return this.getSuperJBpmEventState().getNodePackageName();
    }

    /**
     *
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getProcessDefinition()
     */
    //@Override
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
    //@Override
    public boolean isContainedInBusinessProcess()
    {
        return this.getSuperJBpmEventState().isContainedInBusinessProcess();
    }
}
