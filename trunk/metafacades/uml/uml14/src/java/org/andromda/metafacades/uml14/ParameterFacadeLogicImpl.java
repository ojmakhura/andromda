package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.UMLProfile;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.ParameterDirectionKind;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;


/**
 * Metaclass facade implementation.
 */
public class ParameterFacadeLogicImpl
       extends ParameterFacadeLogic
       implements org.andromda.metafacades.uml.ParameterFacade
{
    // ---------------- constructor -------------------------------

    public ParameterFacadeLogicImpl (org.omg.uml.foundation.core.Parameter metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ParameterFacade#getDefaultValue()
     */
    public String handleGetDefaultValue()
    {
        final Expression expression = metaObject.getDefaultValue();
        return (expression == null) ? "" : expression.getBody();
    }


    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getType()
     */
    protected Object handleGetType()
    {
        return metaObject.getType();
    }
    
    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#sReturn()
     */
    public boolean handleIsReturn()
    {
        final ParameterDirectionKind kind = metaObject.getKind();
        return kind != null ? kind.equals(ParameterDirectionKindEnum.PDK_RETURN) : false;
    }

    /**
     * @see org.andromda.metafacades.uml14.ParameterFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return !this.hasStereotype(UMLProfile.STEREOTYPE_NULLABLE);
    }

    // ------------------------------------------------------------

}
