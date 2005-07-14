package org.andromda.cartridges.jbpm.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmNode.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode
 */
public class JBpmNodeLogicImpl
    extends JBpmNodeLogic
{

    public JBpmNodeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#isTaskNode()
     */
    protected boolean handleIsTaskNode()
    {
        return !getTasks().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getSwimlane()
     */
    protected java.lang.Object handleGetSwimlane()
    {
        return this.getPartition();
    }
}