package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndActivityGraph.
 *
 * @see org.andromda.metafacades.uml.FrontEndActivityGraph
 */
public class FrontEndActivityGraphLogicImpl
    extends FrontEndActivityGraphLogic
{
    public FrontEndActivityGraphLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getController()
     */
    protected java.lang.Object handleGetController()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndActivityGraph#getInitialAction()
     */
    protected java.lang.Object handleGetInitialAction()
    {
        // TODO: add your implementation here!
        return null;
    }
}