package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
 */
public class StrutsUserLogicImpl
        extends StrutsUserLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
{
    // ---------------- constructor -------------------------------

    public StrutsUserLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
    }
    
    protected java.util.Collection handleGetGeneralizedUsers()
    {
        List generalizedUsers = new ArrayList();

        final Collection parentActors = getGeneralizedActors();
        for (Iterator iterator = parentActors.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsUser)
            {
                generalizedUsers.add(object);
            }
        }
        return parentActors;
    }

    protected java.util.Collection handleGetGeneralizedByUsers()
    {
        List generalizedByUsers = new ArrayList();

        final Collection parentActors = getGeneralizedByActors();
        for (Iterator iterator = parentActors.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsUser)
            {
                generalizedByUsers.add(object);
            }
        }
        return parentActors;
    }
}
