package org.andromda.cartridges.jsf2.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf2.metafacades.JSFFinalState.
 *
 * @see org.andromda.cartridges.jsf2.metafacades.JSFFinalState
 */
public class JSFFinalStateLogicImpl
    extends JSFFinalStateLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFFinalStateLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }
    
    private JSFManageableEntity findManageableByName(String name)
    {
        for(ClassifierFacade clazz: getModel().getAllClasses())
        {
            if(clazz instanceof JSFManageableEntity && 
               (clazz.getName().equals(name) || clazz.getFullyQualifiedName().equals(name)))
            {
                return (JSFManageableEntity)clazz;
            }
        }
        return null;
    }

    /**
     * @return fullPath
     * @see org.andromda.cartridges.jsf2.metafacades.JSFFinalState#getPath()
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
                fullPath = ((JSFManageableEntity)getTargetElement()).getActionFullPath();
            }
            
        }
        else if (useCase instanceof JSFUseCase)
        {
            fullPath = ((JSFUseCase)useCase).getPath();
        }

        return fullPath;
    }

    /**
     * @return the target controller
     * @see org.andromda.cartridges.jsf2.metafacades.JSFFinalState#getTargetControllerFullyQualifiedName()
     */
    @Override
    protected String handleGetTargetControllerFullyQualifiedName() {
        String result=null;
        
        if(getTargetElement() instanceof JSFUseCase)
        {
            result=((JSFUseCase)getTargetElement()).getController().getFullyQualifiedName();
        }
        else if(getTargetElement() instanceof JSFManageableEntity)
        {
            result=((JSFManageableEntity)getTargetElement()).getControllerType();
        }
        
        return result;
    }

    /**
     * @return the target controller bean name
     * @see org.andromda.cartridges.jsf2.metafacades.JSFFinalState#getTargetControllerBeanName()
     */
    @Override
    protected String handleGetTargetControllerBeanName() {
        String result=null;
        
        if(getTargetElement() instanceof JSFUseCase)
        {
            result=((JSFController)((JSFUseCase)getTargetElement()).getController()).getBeanName();
        }
        else if(getTargetElement() instanceof JSFManageableEntity)
        {
            result=((JSFManageableEntity)getTargetElement()).getControllerBeanName();
        }
        
        return result;
    }

    /**
     * @return the target element (use case or manageable class)
     * @see org.andromda.cartridges.jsf2.metafacades.JSFFinalState#getTargetElement()
     */
    @Override
    protected ModelElementFacade handleGetTargetElement() {
        ModelElementFacade targetElement=getTargetUseCase();
        
        if(targetElement == null)
        {
            String nameParts[] = getName().split(" ");
            if(nameParts.length >= 2 && nameParts[0].equalsIgnoreCase("Manage"))
            {
                JSFManageableEntity manageable=findManageableByName(nameParts[1]);
                if(manageable != null)
                {
                    return targetElement=manageable;
                }
            }
        }
        
        return targetElement;
    }
}