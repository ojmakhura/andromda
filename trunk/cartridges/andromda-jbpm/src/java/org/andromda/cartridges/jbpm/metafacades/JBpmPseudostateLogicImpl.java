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
}