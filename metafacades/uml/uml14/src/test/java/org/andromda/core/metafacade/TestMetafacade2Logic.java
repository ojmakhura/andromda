package org.andromda.core.metafacade;



public class TestMetafacade2Logic extends MetafacadeBase {
    
    public static final String CONTEXT_NAME = "org.andromda.core.metafacade.TestMetafacade2";
    
    protected java.lang.Object metaObject;
    private TestMetafacade1Logic super_;

    public TestMetafacade2Logic (java.lang.Object metaObject, String contextName) {  
        super (metaObject, getContextName(contextName));
        this.super_ = (TestMetafacade1Logic)
            org.andromda.core.metafacade.MetafacadeFactory
                .getInstance()
                .createFacadeImpl(
                    "org.andromda.core.metafacade.TestMetafacade1",
                    metaObject,
                    getContextName(contextName));
        this.metaObject = metaObject;
    }
    
    private static String getContextName(String contextName) {
        if (contextName == null) {
            contextName = CONTEXT_NAME;
        }
        return contextName;
    }
    
    // from org.andromda.core.metafacade.TestMetafacade1Logic
    public String getContext() {
    	return this.super_.getContext();
    }
    
}
