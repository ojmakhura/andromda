package org.andromda.cartridges.bpm4struts.validator;

import org.andromda.cartridges.bpm4struts.StrutsScriptHelper;
import org.andromda.core.uml14.UMLDynamicHelper;
import org.omg.uml.behavioralelements.usecases.UseCase;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class StrutsWorkflowValidator
{
    private StrutsModelValidator modelValidator = null;
    private final Collection validationMessages = new LinkedList();

    StrutsWorkflowValidator(StrutsModelValidator modelValidator)
    {
        this.modelValidator = modelValidator;
    }

    public Collection validate()
    {
        validationMessages.clear();

        final StrutsScriptHelper helper = modelValidator.getHelper();
        final UMLDynamicHelper dynamicHelper = helper.getDynamicHelper();
        final Collection allUseCases = dynamicHelper.getAllUseCases();
        Collection workflows = helper.filterWithStereotypeName(allUseCases, StrutsScriptHelper.ASPECT_FRONT_END_WORKFLOW);

        Iterator workflowIterator = workflows.iterator();
        while (workflowIterator.hasNext())
        {
            UseCase useCase = (UseCase) workflowIterator.next();
            // perform the validation here

            StrutsWorkflowStateValidator workflowStateValidator = new StrutsWorkflowStateValidator(useCase, modelValidator);
            validationMessages.addAll( workflowStateValidator.validate() );

            StrutsWorkflowPseudostateValidator workflowPseudostateValidator = new StrutsWorkflowPseudostateValidator(useCase, modelValidator);
            validationMessages.addAll( workflowPseudostateValidator.validate() );
        }

        return Collections.unmodifiableCollection(validationMessages);
    }
}
