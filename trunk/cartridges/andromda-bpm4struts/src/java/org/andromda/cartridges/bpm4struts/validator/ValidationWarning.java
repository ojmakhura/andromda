package org.andromda.cartridges.bpm4struts.validator;

import org.omg.uml.foundation.core.ModelElement;

public class ValidationWarning extends ValidationMessage
{
    public ValidationWarning(ModelElement modelElement, String warningMessage)
    {
        super(modelElement, warningMessage);
    }

    public String getMessage()
    {
        return "[Warning] " + super.getMessage();
    }
}
