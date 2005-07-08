package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndAction.
 *
 * @see org.andromda.metafacades.uml.FrontEndAction
 */
public class FrontEndActionLogicImpl
    extends FrontEndActionLogic
{
    public FrontEndActionLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getInput()
     */
    protected java.lang.Object handleGetInput()
    {
        Object input = null;
        final ModelElementFacade source = this.getSource();
        if (source instanceof PseudostateFacade)
        {
            final PseudostateFacade pseudostate = (PseudostateFacade)source;
            if (pseudostate.isInitialState())
            {
                input = source;
            }
        }
        else
        {
            if (source.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_VIEW))
            {
                input = source;
            }
        }
        return input;
    }
}