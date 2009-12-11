package org.andromda.cartridges.jsf.metafacades;

import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFFinalState.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFFinalState
 */
public class JSFFinalStateLogicImpl
    extends JSFFinalStateLogic
{

    /**
     * Public constructor for JSFFinalStateLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.jsf.metafacades.JSFFinalState
     */
    public JSFFinalStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @return Path
     * @see org.andromda.cartridges.jsf.metafacades.JSFFinalState#getPath()
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
        }
        else if (useCase instanceof JSFUseCase)
        {
            fullPath = ((JSFUseCase)useCase).getPath();
        }

        return fullPath;
    }

}