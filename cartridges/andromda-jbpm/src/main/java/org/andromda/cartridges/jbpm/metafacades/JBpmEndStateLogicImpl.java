package org.andromda.cartridges.jbpm.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmEndState.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmEndState
 */
public class JBpmEndStateLogicImpl
    extends JBpmEndStateLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JBpmEndStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    //TODO Generation from UML2 Metafacade model does not put these methods in *Logic generated class.
    // Implementation copied from UML14 generated version, to avoid compiler error on missing method implementation from abstract method.
    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodeClassName()
     */
    @Override
    public String getNodeClassName()
    {
        return this.getSuperJBpmEventState().getNodeClassName();
    }
    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getNodePackageName()
     */
    @Override
    public String getNodePackageName()
    {
        return this.getSuperJBpmEventState().getNodePackageName();
    }
    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#getProcessDefinition()
     */
    @Override
    public JBpmProcessDefinition getProcessDefinition()
    {
        return this.getSuperJBpmEventState().getProcessDefinition();
    }
    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmStateVertex#isContainedInBusinessProcess()
     */
    @Override
    public boolean isContainedInBusinessProcess()
    {
        return this.getSuperJBpmEventState().isContainedInBusinessProcess();
    }
}