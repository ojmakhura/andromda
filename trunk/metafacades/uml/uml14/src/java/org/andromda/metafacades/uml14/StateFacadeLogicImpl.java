package org.andromda.metafacades.uml14;



/**
 * Metaclass facade implementation.
 */
public class StateFacadeLogicImpl
    extends StateFacadeLogic
    implements org.andromda.metafacades.uml.StateFacade
{
    // ---------------- constructor -------------------------------

    public StateFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.State metaObject,
        String context)
    {
        super(metaObject, context);
    }
}
