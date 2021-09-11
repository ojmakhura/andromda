package org.andromda.cartridges.jakarta.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaFinalState.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaFinalState
 */
public class JakartaFinalStateLogicImpl
    extends JakartaFinalStateLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaFinalStateLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }
    
    private JakartaManageableEntity findManageableByName(String name)
    {
        for(ClassifierFacade clazz: getModel().getAllClasses())
        {
            if(clazz instanceof JakartaManageableEntity && 
               (clazz.getName().equals(name) || clazz.getFullyQualifiedName().equals(name)))
            {
                return (JakartaManageableEntity)clazz;
            }
        }
        return null;
    }

    /**
     * @return fullPath
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaFinalState#getPath()
     */
    protected String handleGetPath()
    {
        String fullPath = null;

        FrontEndUseCase useCase = this.getTargetUseCase();
        if (useCase == null)
        {
            // - perhaps this final state links outside of the UML model
            final Object taggedValue = this.findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
            if (taggedValue == null)
            {
                String name = getName();
                if (name != null && (name.startsWith("/") || name.startsWith("http://") || name.startsWith("file:")))
                {
                    fullPath = name;
                }
            }
            else
            {
                fullPath = String.valueOf(taggedValue);
            }
            
            if(fullPath == null && getName() != null)
            {
                fullPath = ((JakartaManageableEntity)getTargetElement()).getActionFullPath();
            }
            
        }
        else if (useCase instanceof JakartaUseCase)
        {
            fullPath = ((JakartaUseCase)useCase).getPath();
        }

        return fullPath;
    }

    /**
     * @return the target controller
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaFinalState#getTargetControllerFullyQualifiedName()
     */
    @Override
    protected String handleGetTargetControllerFullyQualifiedName() {
        String result=null;
        
        if(getTargetElement() instanceof JakartaUseCase)
        {
            result=((JakartaUseCase)getTargetElement()).getController().getFullyQualifiedName();
        }
        else if(getTargetElement() instanceof JakartaManageableEntity)
        {
            result=((JakartaManageableEntity)getTargetElement()).getControllerType();
        }
        
        return result;
    }

    /**
     * @return the target controller bean name
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaFinalState#getTargetControllerBeanName()
     */
    @Override
    protected String handleGetTargetControllerBeanName() {
        String result=null;
        
        if(getTargetElement() instanceof JakartaUseCase)
        {
            result=((JakartaController)((JakartaUseCase)getTargetElement()).getController()).getBeanName();
        }
        else if(getTargetElement() instanceof JakartaManageableEntity)
        {
            result=((JakartaManageableEntity)getTargetElement()).getControllerBeanName();
        }
        
        return result;
    }

    /**
     * @return the target element (use case or manageable class)
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaFinalState#getTargetElement()
     */
    @Override
    protected ModelElementFacade handleGetTargetElement() {
        ModelElementFacade targetElement=getTargetUseCase();
        
        if(targetElement == null)
        {
            String nameParts[] = getName().split(" ");
            if(nameParts.length >= 2 && nameParts[0].equalsIgnoreCase("Manage"))
            {
                JakartaManageableEntity manageable=findManageableByName(nameParts[1]);
                if(manageable != null)
                {
                    return targetElement=manageable;
                }
            }
        }
        
        return targetElement;
    }
}