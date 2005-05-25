package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
 */
public class StrutsFinalStateLogicImpl
        extends StrutsFinalStateLogic
{
    public StrutsFinalStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    public String getName()
    {
        String name = super.getName();

        if (name == null)
        {
            StrutsUseCase useCase = getTargetUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
            }
        }

        return name;
    }

    protected String handleGetFullPath()
    {
        String fullPath = null;

        StrutsUseCase useCase = getTargetUseCase();
        System.out.println("the target UC>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + this.getTargetUseCase());
        if (useCase == null)
        {
            // perhaps this final state links outside of the UML model
            final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_WEBPAGELINK);
            if (taggedValue == null)
            {
                String name = getName();
                if (name != null && (name.startsWith("/") || name.startsWith("http://")))
                {
                    fullPath = name;
                }
            }
            else
            {
                fullPath = String.valueOf(taggedValue);
            }
        }
        else
        {
            fullPath = useCase.getActionPath() + ".do";
        }

        return fullPath;
    }

    protected Object handleGetTargetUseCase()
    {
        Object targetUseCase = null;

        // first check if there is a hyperlink from this final state to a use-case
        // this works at least in MagicDraw
        final Object taggedValue = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK);
        System.out.println("the tagged value: " + taggedValue);
        if (taggedValue != null)
        {
            System.out.println("the tagged value: " + taggedValue);
            if (taggedValue instanceof StrutsActivityGraph)
            {
                targetUseCase = ((StrutsActivityGraph)taggedValue).getUseCase();
            }
            else if (taggedValue instanceof StrutsUseCase)
            {
                targetUseCase = taggedValue;
            }
        }
        else // maybe the name points to a use-case ?
        {
            final String name = super.getName();
            System.out.println("the super name: " +super.getName());
            if (StringUtils.isNotBlank(name))
            {
                UseCaseFacade useCase = getModel().findUseCaseByName(name);
                System.out.println("just found findUseCaseByName with '" + name + "' : " + useCase);
                if (useCase != null)
                {
                    System.out.println("the stereotypes: " + useCase.getStereotypeNames());
                }
                if (useCase instanceof StrutsUseCase)
                {
                    targetUseCase = useCase;
                }
            }
        }
        return targetUseCase;
    }

    protected List handleGetActions()
    {
        Set actions = new HashSet();
        Collection incoming = this.getIncoming();

        for (Iterator incomingIterator = incoming.iterator(); incomingIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward)incomingIterator.next();
            actions.addAll(forward.getActions());
        }
        return new ArrayList(actions);
    }
}
