package org.andromda.cartridges.bpm4struts.validator;

import org.andromda.cartridges.bpm4struts.StrutsScriptHelper;
import org.omg.uml.UmlPackage;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Collections;

public class StrutsModelValidator
{
    private StrutsScriptHelper helper = null;

    private final Collection validationMessages = new LinkedList();

    private StrutsWorkflowValidator workflowValidator = null;
    private StrutsUseCaseValidator useCaseValidator = null;
    private StrutsPresentationClassValidator presentationClassValidator = null;
    private StrutsControllerClassValidator controllerClassValidator = null;

    public StrutsModelValidator(StrutsScriptHelper helper)
    {
        this.helper = helper;

        workflowValidator = new StrutsWorkflowValidator(this);
        useCaseValidator = new StrutsUseCaseValidator(this);
        presentationClassValidator = new StrutsPresentationClassValidator(this);
        controllerClassValidator = new StrutsControllerClassValidator(this);
    }

    public Collection validate()
    {
        validationMessages.clear();

        validationMessages.addAll( workflowValidator.validate() );
        validationMessages.addAll( useCaseValidator.validate() );
        validationMessages.addAll( presentationClassValidator.validate() );
        validationMessages.addAll( controllerClassValidator.validate() );

        return Collections.unmodifiableCollection(validationMessages);
    }

    public StrutsScriptHelper getHelper()
    {
        return helper;
    }
}
