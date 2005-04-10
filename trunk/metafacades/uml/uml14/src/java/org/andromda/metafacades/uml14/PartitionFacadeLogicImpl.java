package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.PartitionFacade.
 *
 * @see org.andromda.metafacades.uml.PartitionFacade
 */
public class PartitionFacadeLogicImpl extends PartitionFacadeLogic
{
    // ---------------- constructor -------------------------------

    public PartitionFacadeLogicImpl(org.omg.uml.behavioralelements.activitygraphs.Partition metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PartitionFacade#getActivityGraph()
     */
    protected java.lang.Object handleGetActivityGraph()
    {
        return metaObject.getActivityGraph();
    }

}
