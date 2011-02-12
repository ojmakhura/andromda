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
}