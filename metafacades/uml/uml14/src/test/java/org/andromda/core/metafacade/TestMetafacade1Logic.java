package org.andromda.core.metafacade;



public class TestMetafacade1Logic extends MetafacadeBase {
    
    protected java.lang.Object metaObject;

    public TestMetafacade1Logic (java.lang.Object metaObject, String context) {
        super (metaObject, context);
        this.metaObject = metaObject;
    }
    
    // calls getContext from MetafacadeBase
    public String getContext() {
        return super.getContext();
    }
    
}


