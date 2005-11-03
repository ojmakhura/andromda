package org.andromda.cartridges.aspdotnet.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.aspdotnet.metafacades.AspUseCase.
 *
 * @see org.andromda.cartridges.aspdotnet.metafacades.AspUseCase
 */
public class AspUseCaseLogicImpl
    extends AspUseCaseLogic
{

    public AspUseCaseLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

	/**protected Object handleGetController() 
	{
		AspActivityGraph graph = (AspActivityGraph)this.getActivityGraph();
	    return (graph == null) ? null : graph.getController();
	}*/

	protected String handleGetControllerName() 
	{
		return (getController() == null) ? null : ((AspController)getController()).getName();
	}

	protected Collection handleGetWebForms() 
	{
		Collection webForms = new LinkedList();
		AspActivityGraph graph = (AspActivityGraph)this.getActivityGraph();
		if(graph != null)
		{
			Collection actionStates = graph.getActionStates();
			for (Iterator iterator = actionStates.iterator(); iterator.hasNext();) 
			{
				Object obj = iterator.next();
				if(obj instanceof AspWebForm)
					webForms.add(obj);				
			}
		}
		return webForms;
	}

	/**protected Object handleGetActivityGraph() 
	{
		Collection ownedElements = getOwnedElements();
	    for (Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
	    {
	    	Object obj = iterator.next();
	        if (obj instanceof AspActivityGraph) 
	        	return obj;
	    }
	    return null;
	    
	}*/

	protected Object handleGetStartView() 
	{
		AspActivityGraph activityGraph = (AspActivityGraph)this.getActivityGraph();
		PseudostateFacade initialState = (PseudostateFacade)activityGraph.getInitialStates().iterator().next();
		AspTransition transition = (AspTransition)initialState.getOutgoing().iterator().next();
		return transition.getTarget();
	}

	protected String handleGetNormalizedName() 
	{
		return StringUtils.deleteWhitespace(this.getName());
	}

	protected boolean handleIsFrontEndApplication() 
	{
		Collection stereotypes = this.getStereotypes();
		for (Iterator iter = stereotypes.iterator(); iter.hasNext();) 
		{
			StereotypeFacade stereotype = (StereotypeFacade)iter.next();
			if(stereotype.getName().equals("FrontEndApplication"))
				return true;
		}
		return false;
	}

	protected String handleGetFullyQualifiedPath() 
	{
		return StringUtils.replaceChars(this.getPackageName(),'.','/');
	}

}