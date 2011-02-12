package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmSwimlane.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlane
 */
public class JBpmSwimlaneLogicImpl
    extends JBpmSwimlaneLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JBpmSwimlaneLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlaneLogic#handleIsContainedInBusinessProcess()
     */
    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getActivityGraph().getUseCase() instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlaneLogic#handleGetAssignmentExpression()
     */
    protected String handleGetAssignmentExpression()
    {
        return (String)findTaggedValue(JBpmProfile.TAGGEDVALUE_ASSIGNMENT_EXPRESSION);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlaneLogic#handleGetClazz()
     */
    protected String handleGetClazz()
    {
        String assignmentHandlerClass = null;

        final StringBuilder clazzBuffer = new StringBuilder();
        if (StringUtils.isNotBlank(this.getAssignmentHandlerPackageName()))
        {
            clazzBuffer.append(this.getAssignmentHandlerPackageName());
            clazzBuffer.append('.');
        }
        clazzBuffer.append(this.getAssignmentHandlerClassName());
        assignmentHandlerClass = clazzBuffer.toString();

        return assignmentHandlerClass;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlaneLogic#handleGetAssignmentHandlerClassName()
     */
    protected String handleGetAssignmentHandlerClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName() + "Assignment");
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlaneLogic#handleGetAssignmentHandlerPackageName()
     */
    protected String handleGetAssignmentHandlerPackageName()
    {
        String packageName = null;

        final UseCaseFacade useCase = this.getActivityGraph().getUseCase();
        if (useCase != null)
        {
            packageName = useCase.getPackageName();
        }

        return packageName;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmSwimlaneLogic#handleGetAssignmentHandlerFullPath()
     */
    protected String handleGetAssignmentHandlerFullPath()
    {
        return StringUtils.replace(this.getClazz(), ".", "/");
    }
}