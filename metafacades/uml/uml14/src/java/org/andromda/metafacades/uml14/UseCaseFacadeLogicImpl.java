package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.foundation.core.ModelElement;

/**
 * Metaclass facade implementation.
 */
public class UseCaseFacadeLogicImpl
        extends UseCaseFacadeLogic
{
    // ---------------- constructor -------------------------------

    public UseCaseFacadeLogicImpl(org.omg.uml.behavioralelements.usecases.UseCase metaObject, String context)
    {
        super(metaObject, context);
    }
    
    /**
     * @see org.andromda.metafacades.uml14.UseCaseFacadeLogic#handleGetFirstActivityGraph()
     */
    protected Object handleGetFirstActivityGraph()
    {
        ActivityGraph activityGraph = null;

        for (Iterator iterator = metaObject.getOwnedElement().iterator(); iterator.hasNext() && activityGraph == null;)
        {
            ModelElement modelElement = (ModelElement)iterator.next();
            if (modelElement instanceof ActivityGraph)
            {
                activityGraph = (ActivityGraph)modelElement;
            }
        }

        return activityGraph;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getPackage();
    }

    /**
     * @see org.andromda.metafacades.uml14.UseCaseFacade#getExtensionPoints()
     */
    protected Collection handleGetExtensionPoints()
    {
        return metaObject.getExtensionPoint();
    }

    /*** 
     * @see org.andromda.metafacades.uml14.UseCaseFacade#getExtends()
     */
    protected Collection handleGetExtends()
    {
        return metaObject.getExtend();
    }
}