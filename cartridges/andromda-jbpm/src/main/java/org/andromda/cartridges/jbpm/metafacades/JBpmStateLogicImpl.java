package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;
import org.andromda.metafacades.uml.EventFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmState.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmState
 */
public class JBpmStateLogicImpl
    extends JBpmStateLogic
{

    public JBpmStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected boolean handleIsTaskNode()
    {
        return hasStereotype(JBpmProfile.STEREOTYPE_TASK);
    }

    protected Object handleGetSwimlane()
    {
        return this.getPartition();
    }
}