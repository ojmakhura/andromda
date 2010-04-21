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
    public JBpmSwimlaneLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getActivityGraph().getUseCase() instanceof JBpmProcessDefinition;
    }

    protected String handleGetAssignmentExpression()
    {
        return (String)findTaggedValue(JBpmProfile.TAGGEDVALUE_ASSIGNMENT_EXPRESSION);
    }

    protected String handleGetClazz()
    {
        String assignmentHandlerClass = null;

        final StringBuffer clazzBuffer = new StringBuffer();
        if (StringUtils.isNotBlank(this.getAssignmentHandlerPackageName()))
        {
            clazzBuffer.append(this.getAssignmentHandlerPackageName());
            clazzBuffer.append('.');
        }
        clazzBuffer.append(this.getAssignmentHandlerClassName());
        assignmentHandlerClass = clazzBuffer.toString();

        return assignmentHandlerClass;
    }

    protected String handleGetAssignmentHandlerClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName() + "Assignment");
    }

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

    protected String handleGetAssignmentHandlerFullPath()
    {
        return StringUtils.replace(this.getClazz(), ".", "/");
    }
}