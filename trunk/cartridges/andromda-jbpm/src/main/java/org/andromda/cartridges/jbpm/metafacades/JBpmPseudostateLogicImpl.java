package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate
 */
public class JBpmPseudostateLogicImpl
    extends JBpmPseudostateLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public JBpmPseudostateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostateLogic#getName()
     */
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostateLogic#handleGetClassName()
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getClassName()
     */
    protected String handleGetClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName());
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostateLogic#handleGetSwimlane()
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostate#getSwimlane()
     */
    protected Object handleGetSwimlane()
    {
        return this.getPartition();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostateLogic#handleGetDecisionHandlerPackageName()
     */
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

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostateLogic#handleGetDecisionHandlerFullPath()
     */
    protected String handleGetDecisionHandlerFullPath()
    {
        return StringUtils.replace(this.getClazz(), ".", "/");
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostateLogic#handleGetDecisionHandlerClassName()
     */
    protected String handleGetDecisionHandlerClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmPseudostateLogic#handleGetClazz()
     */
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