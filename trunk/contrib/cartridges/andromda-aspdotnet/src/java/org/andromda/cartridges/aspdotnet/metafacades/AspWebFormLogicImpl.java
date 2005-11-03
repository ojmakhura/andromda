package org.andromda.cartridges.aspdotnet.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.andromda.metafacades.uml.CallEventFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.aspdotnet.metafacades.AspWebForm.
 *
 * @see org.andromda.cartridges.aspdotnet.metafacades.AspWebForm
 */
public class AspWebFormLogicImpl
    extends AspWebFormLogic
{

    public AspWebFormLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

	protected String handleGetNormalizedName() 
	{
		return StringUtils.deleteWhitespace(this.getName());
	}

	protected String handleGetFullyQualifiedPath() 
	{
		return StringUtils.replaceChars(this.getUseCase().getActivityGraph().getUseCase().getPackageName(),'.','/');
	}

	protected Collection handleGetLoadOperations() 
	{
		Collection loadOperations = new LinkedList();
		Collection deferrableEvents = this.getDeferrableEvents();
		for (Iterator iterator = deferrableEvents.iterator(); iterator.hasNext();) 
		{
			Object obj = iterator.next();	
			if(obj instanceof CallEventFacade)
			{
				loadOperations.add(((CallEventFacade)obj).getOperation());
			}
		}
		return loadOperations;
	}

	protected String handleGetControllerName() 
	{
		return this.getUseCase().getController().getName();
	}

	protected Object handleGetController()
	{
		return this.getUseCase().getController();
	}

	protected boolean handleIsLoadOperation() 
	{
		return !this.getLoadOperations().isEmpty();
	}

	protected Collection handleGetNavigateValues() 
	{
		//todo make restriction on this method: it should return only concerned webform
		Collection navigateValues = new LinkedList();
		AspActivityGraph activityGraph = (AspActivityGraph)this.getUseCase().getActivityGraph();
		Collection actionStates = activityGraph.getActionStates();
		for (Iterator iterator = actionStates.iterator(); iterator.hasNext();) 
		{
			Object obj = iterator.next();
			if(obj instanceof AspWebForm)
			{
				navigateValues.add(obj);
			}
		}
		return navigateValues;
	}

	protected Collection handleGetTextBoxes() 
	{
		Collection textBoxes = new LinkedList();
		Collection transitions = this.getOutgoing();
		for (Iterator iter = transitions.iterator(); iter.hasNext();) 
		{
			AspTransition transition = (AspTransition) iter.next();
			textBoxes.addAll(transition.getTextBoxes());
		}
		return textBoxes;
	}

	protected Collection handleGetDropDownLists() 
	{
		Collection dropDownLists = new LinkedList();
		Collection transitions = this.getOutgoing();
		for (Iterator iter = transitions.iterator(); iter.hasNext();) 
		{
			AspTransition transition = (AspTransition) iter.next();
			dropDownLists.addAll(transition.getDropDownLists());
		}
		return dropDownLists;
	}

	protected Collection handleGetCheckBoxes() 
	{
		Collection checkBoxes = new LinkedList();
		Collection transitions = this.getOutgoing();
		for (Iterator iter = transitions.iterator(); iter.hasNext();) 
		{
			AspTransition transition = (AspTransition) iter.next();
			checkBoxes.addAll(transition.getCheckBoxes());
		}
		return checkBoxes;
	}

	protected Collection handleGetParameters() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected String handleGetNavigation() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Collection handleGetRadioButtons() 
	{
		Collection radioButtons = new LinkedList();
		Collection transitions = this.getOutgoing();
		for (Iterator iter = transitions.iterator(); iter.hasNext();) 
		{
			AspTransition transition = (AspTransition) iter.next();
			radioButtons.addAll(transition.getRadioButtons());
		}
		return radioButtons;
	}

	protected String handleGetCSSPath() 
	{
		StringBuffer path = new StringBuffer();
		char[] arrayPath = this.getFullyQualifiedPath().toCharArray();
		path.append("..");
		for (int i = 0; i < arrayPath.length; i++) 
		{
			if(arrayPath[i] == '/')
			{
				path.append("\\\\");
				if((i+1)<arrayPath.length)
					path.append("..");
			}			
		}
		return path.toString();
	}

	protected String handleGetMenuPath() 
	{
		return StringUtils.replace(this.getCSSPath(),"\\\\","\\");
	}
	
}