package org.andromda.metafacades.uml14;



/**
 * Metaclass facade implementation.
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
    implements org.andromda.metafacades.uml.FinalStateFacade
{
    // ---------------- constructor -------------------------------

    public FinalStateFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.FinalState metaObject,
        String context)
    {
        super(metaObject, context);
    }

    public Object getValidationOwner()
    {
        return getActivityGraph();
    }
}
