package org.andromda.cartridges.aspdotnet.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.CallEventFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.aspdotnet.metafacades.AspTransition.
 *
 * @see org.andromda.cartridges.aspdotnet.metafacades.AspTransition
 */
public class AspTransitionLogicImpl
    extends AspTransitionLogic
{
	private StringBuffer navigation;
	private Collection operations;
    public AspTransitionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.cartridges.aspdotnet.metafacades.AspTransition#getParameters()
     */
    protected java.util.Collection handleGetParameters()
    {
    	if(this.getTrigger()!=null)
    		return this.getTrigger().getParameters();
    	return null;
    }
	protected String handleGetSignalName() 
	{
		if(this.getTrigger()!=null)
    		return this.getTrigger().getName();
    	return null;
	}
	protected Collection handleGetTextBoxes() 
	{
		Collection textBoxes = new LinkedList();
		for (Iterator iterator = this.getParameters().iterator(); iterator.hasNext();) 
		{
			AspParameter  parameter = (AspParameter) iterator.next();
			if(parameter.getViewType().equals("TextBox"))
				textBoxes.add(parameter);
		}
		return textBoxes;
	}
	protected Collection handleGetDropDownLists() 
	{
		Collection dropDownLists = new LinkedList();
		for (Iterator iterator = this.getParameters().iterator(); iterator.hasNext();) 
		{
			AspParameter  parameter = (AspParameter) iterator.next();
			if(parameter.getViewType().equals("DropDownList"))
				dropDownLists.add(parameter);
		}
		return dropDownLists;
	}
	protected Collection handleGetRadioButtons() 
	{
		Collection radioButtons = new LinkedList();
		for (Iterator iterator = this.getParameters().iterator(); iterator.hasNext();) 
		{
			AspParameter  parameter = (AspParameter) iterator.next();
			if(parameter.getViewType().equals("RadioButton"))
				radioButtons.add(parameter);
		}
		return radioButtons;
	}
	protected Collection handleGetCheckBoxes() 
	{
		Collection checkBoxes = new LinkedList();
		for (Iterator iterator = this.getParameters().iterator(); iterator.hasNext();) 
		{
			AspParameter  parameter = (AspParameter) iterator.next();
			if(parameter.getViewType().equals("CheckBox"))
				checkBoxes.add(parameter);
		}
		return checkBoxes;
	}
	public String handleGetNavigation() 
	{
		navigation = new StringBuffer();
		createNavigation(this);
		return navigation.toString();
	}
	
	private void createNavigation(AspTransition transition)
	{
		if(transition.getTarget() instanceof AspWebForm)		
		{
			navigation.append("Navigate(");
			navigation.append('"');
			navigation.append(((AspWebForm)transition.getTarget()).getNormalizedName());
			navigation.append('"');
			navigation.append(");");
		}
		else if(transition.getTarget() instanceof AspFinalState)
		{
			navigation.append("UIPManager.StartNavigationTask(");
			navigation.append('"');
			navigation.append(StringUtils.deleteWhitespace(((AspFinalState)transition.getTarget()).getName()));
			navigation.append('"');
			navigation.append(");");
		}
		else if(transition.getTarget() instanceof PseudostateFacade)
		{
			PseudostateFacade decisionPoint = (PseudostateFacade)transition.getTarget();
			Collection transitions = decisionPoint.getOutgoing();
			for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
			{
				AspTransition nextTransition = (AspTransition) iterator.next();
				if(nextTransition.getGuard().getBody().equals("true"))
				{
					navigation.append("if (");
					navigation.append(((CallEventFacade)transition.getTrigger()).getOperation().getName());
					navigation.append("()){");
					createNavigation(nextTransition);
					navigation.append("}");
					
				}
			}
			for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
			{
				AspTransition nextTransition = (AspTransition) iterator.next();
				if(nextTransition.getGuard().getBody().equals("false"))
				{
					navigation.append("else {");
					createNavigation(nextTransition);
					navigation.append("}");
				}
			}			
		}
		else if(transition.getTarget() instanceof ActionStateFacade)
		{
			Collection deferrableEvents = ((ActionStateFacade)transition.getTarget()).getDeferrableEvents();
			Iterator iterator = deferrableEvents.iterator();
			while(iterator.hasNext())
			{
				CallEventFacade callEvent = (CallEventFacade)iterator.next();
				navigation.append(callEvent.getOperation().getName());
				navigation.append("();");			
			}
			AspTransition nextTransition = (AspTransition)((ActionStateFacade)transition.getTarget()).getOutgoing().iterator().next();
			createNavigation(nextTransition);
		}		
	}
	protected Collection handleGetOperations() 
	{
		operations = new LinkedList();
		addOperations(this);
		return operations;
	}
	
	private void addOperations(AspTransition transition)
	{
		if(transition.getTarget() instanceof PseudostateFacade)
		{
			operations.add(((CallEventFacade)transition.getTrigger()).getOperation());
			PseudostateFacade decisionPoint = (PseudostateFacade)transition.getTarget();
			Collection transitions = decisionPoint.getOutgoing();
			for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
			{
				addOperations((AspTransition)iterator.next());
			}	
		}
		else if(transition.getTarget() instanceof ActionStateFacade)
		{
			Collection deferrableEvents = ((ActionStateFacade)transition.getTarget()).getDeferrableEvents();
			Iterator iterator = deferrableEvents.iterator();
			while(iterator.hasNext())
			{
				CallEventFacade callEvent = (CallEventFacade)iterator.next();
				operations.add(callEvent.getOperation().getName());			
			}
			AspTransition nextTransition = (AspTransition)((ActionStateFacade)transition.getTarget()).getOutgoing().iterator().next();
			addOperations(nextTransition);
		}		
	}

}