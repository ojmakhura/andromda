package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate
 */
public class JBpmPseudostateLogicImpl
    extends JBpmPseudostateLogic
{

    public JBpmPseudostateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    public String getName()
    {
        String name = super.getName();
        if (StringUtils.isBlank(name))
        {
            name = "start";
        }
        return name;
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getStateMachine() instanceof ActivityGraphFacade
                && ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getClassName()
     */
    protected java.lang.String handleGetClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName());
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getSwimlane()
     */
    protected java.lang.Object handleGetSwimlane()
    {
        return this.getPartition();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getBeforeSignal()
     */
    protected java.util.List handleGetBeforeSignal()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getAfterSignal()
     */
    protected java.util.List handleGetAfterSignal()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getNodeEnter()
     */
    protected java.util.List handleGetNodeEnter()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getNodeLeave()
     */
    protected java.util.List handleGetNodeLeave()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    protected java.util.List handleGetTimers()
    {
        // maybe one day UML will support events on pseudostates
        return Collections.EMPTY_LIST;
    }

    protected String handleGetDecisionHandlerPackageName()
    {
        String packageName = null;

        final StateMachineFacade stateMachine = this.getStateMachine();
        if (stateMachine instanceof ActivityGraphFacade)
        {
            final UseCaseFacade useCase = ((ActivityGraphFacade)stateMachine).getUseCase();
            if (useCase != null)
            {
                packageName = useCase.getPackageName();
            }
        }

        return packageName;
    }

    protected String handleGetDecisionHandlerFullPath()
    {
        return StringUtils.replace(this.getClazz(), ".", "/");
    }

    protected String handleGetDecisionHandlerClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    protected String handleGetClazz()
    {
        String decisionHandlerClass = null;

        if (this.isDecisionPoint())
        {
            final StringBuffer clazzBuffer = new StringBuffer();
            if (StringUtils.isNotBlank(this.getDecisionHandlerPackageName()))
            {
                clazzBuffer.append(this.getDecisionHandlerPackageName());
                clazzBuffer.append('.');
            }
            clazzBuffer.append(this.getDecisionHandlerClassName());
            decisionHandlerClass = clazzBuffer.toString();
        }

        return decisionHandlerClass;
    }

    protected String handleGetNodeClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName()) + "Node";
    }

    protected String handleGetNodePackageName()
    {
        return (this.getProcessDefinition() == null) ? null : this.getProcessDefinition().getPackageName();
    }

    protected Object handleGetProcessDefinition()
    {
        Object processDefinition = null;

        final StateMachineFacade stateMachine = this.getStateMachine();
        if (stateMachine instanceof ActivityGraphFacade)
        {
            processDefinition = ((ActivityGraphFacade)stateMachine).getUseCase();
        }

        return (processDefinition instanceof JBpmProcessDefinition) ? processDefinition : null;
    }
}