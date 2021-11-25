package org.andromda.cartridges.thymeleaf.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafFinalState.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafFinalState
 */
public class ThymeleafFinalStateLogicImpl
    extends ThymeleafFinalStateLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafFinalStateLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }
    
    private ThymeleafManageableEntity findManageableByName(String name)
    {
        for(ClassifierFacade clazz: getModel().getAllClasses())
        {
            if(clazz instanceof ThymeleafManageableEntity && 
               (clazz.getName().equals(name) || clazz.getFullyQualifiedName().equals(name)))
            {
                return (ThymeleafManageableEntity)clazz;
            }
        }
        return null;
    }

    /**
     * @return fullPath
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafFinalState#getPath()
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
                fullPath = ((ThymeleafManageableEntity)getTargetElement()).getActionFullPath();
            }
            
        }
        else if (useCase instanceof ThymeleafUseCase)
        {
            fullPath = ((ThymeleafUseCase)useCase).getPath();
        }

        return fullPath;
    }

    /**
     * @return the target controller
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafFinalState#getTargetControllerFullyQualifiedName()
     */
    @Override
    protected String handleGetTargetControllerFullyQualifiedName() {
        String result=null;
        
        if(getTargetElement() instanceof ThymeleafUseCase)
        {
            result=((ThymeleafUseCase)getTargetElement()).getController().getFullyQualifiedName();
        }
        else if(getTargetElement() instanceof ThymeleafManageableEntity)
        {
            result=((ThymeleafManageableEntity)getTargetElement()).getControllerType();
        }
        
        return result;
    }

    /**
     * @return the target controller bean name
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafFinalState#getTargetControllerBeanName()
     */
    @Override
    protected String handleGetTargetControllerBeanName() {
        String result=null;
        
        if(getTargetElement() instanceof ThymeleafUseCase)
        {
            result=((ThymeleafController)((ThymeleafUseCase)getTargetElement()).getController()).getBeanName();
        }
        else if(getTargetElement() instanceof ThymeleafManageableEntity)
        {
            result=((ThymeleafManageableEntity)getTargetElement()).getControllerBeanName();
        }
        
        return result;
    }

    /**
     * @return the target element (use case or manageable class)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafFinalState#getTargetElement()
     */
    @Override
    protected ModelElementFacade handleGetTargetElement() {
        ModelElementFacade targetElement=getTargetUseCase();
        
        if(targetElement == null)
        {
            String nameParts[] = getName().split(" ");
            if(nameParts.length >= 2 && nameParts[0].equalsIgnoreCase("Manage"))
            {
                ThymeleafManageableEntity manageable=findManageableByName(nameParts[1]);
                if(manageable != null)
                {
                    return targetElement=manageable;
                }
            }
        }
        
        return targetElement;
    }
}