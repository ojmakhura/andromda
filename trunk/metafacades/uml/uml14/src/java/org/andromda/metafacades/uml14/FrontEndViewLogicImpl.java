package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndView.
 *
 * @see org.andromda.metafacades.uml.FrontEndView
 */
public class FrontEndViewLogicImpl
    extends FrontEndViewLogic
{
    public FrontEndViewLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#isFrontEndView()
     */
    protected boolean handleIsFrontEndView()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_VIEW);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
     */
    protected List handleGetActions()
    {
        final List actions = new ArrayList();
        final Collection outgoing = getOutgoing();

        for (final Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof FrontEndForward)
            {
                actions.add(object);
            }
        }

        return actions;
    }
}