package org.andromda.translation.testsuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.translation.BaseTranslator;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.node.AOperationContextDeclaration;
import org.andromda.core.translation.syntax.OperationDeclaration;
import org.andromda.core.translation.syntax.VariableDeclaration;
import org.andromda.core.translation.syntax.impl.ConcreteSyntaxUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Finds the context element defined in the OCL expression.
 * 
 * @author Chad Brandon
 */
public class ContextElementFinder
    extends BaseTranslator
{

    private ModelAccessFacade model;

    /**
     * The Constructor which takes <code>model</code>, which is an instance
     * of ModelAccessFacade that will allow us to search the model for the
     * context element.
     * 
     * @param model the ModelAccessFacade to search.
     */
    public ContextElementFinder(
        ModelAccessFacade model)
    {
        ExceptionUtils.checkNull("ContextElementFinder", "model", model);
        this.model = model;
    }

    /**
     * Hide the default constructor
     */
    private ContextElementFinder()
    {}

    /**
     * The operation that is set if the context of the constraint happens to be
     * an operation.
     */
    private OperationDeclaration operation = null;

    /**
     * The found context type.
     */
    private Object contextElement = null;

    /**
     * @see org.andromda.core.translation.analysis.DepthFirstAdapter#inAOperationContextDeclaration(org.andromda.core.translation.node.AOperationContextDeclaration)
     */
    public void inAOperationContextDeclaration(
        AOperationContextDeclaration declaration)
    {
        super.inAOperationContextDeclaration(declaration);
        if (declaration != null)
        {
            operation = ConcreteSyntaxUtils.getOperationDeclaration(declaration
                .getOperation());
        }
    }

    /**
     * We use the postProcess method to retrieve the contextType from the
     * expression and then find the actual model element using metafacades.
     * 
     * @see org.andromda.core.translation.BaseTranslator#postProcess()
     */
    public void postProcess()
    {
        super.postProcess();
        Expression expression = this.getExpression();
        if (expression != null)
        {
            String contextElementName = expression.getContextElement();
            this.contextElement = this.findModelElement(contextElementName
                .replaceAll("::", "\\."));
            if (this.contextElement != null)
            {
                logger.info("found context element --> '" + contextElementName
                    + "'");
            }
            else
            {
                logger.info("Could not find model element --> '"
                    + contextElementName + "'");
            }

            if (this.contextElement != null
                && this.operation != null
                && ClassifierFacade.class.isAssignableFrom(contextElement
                    .getClass()))
            {
                ClassifierFacade type = (ClassifierFacade)this.contextElement;
                Collection operations = type.getOperations();
                this.contextElement = CollectionUtils.find(
                    operations,
                    new OperationFinder());
                if (this.contextElement == null)
                {
                    throw new ContextElementFinderException(
                        "No operation matching '" + operation
                            + "' could be found on element --> '"
                            + contextElementName + "', please check your model");
                }

                // if we only have one operation then we just set that
                // as the context element, otherwise we'll need to figure
                // out which operation is the context operation by checking
                // the arguments.
                if (operations.size() == 1)
                {
                    this.contextElement = operations.iterator().next();
                }
                else
                {
                    // now find the correct operation since there are
                    // more than one with the same name
                }
            }
        }
    }

    private final class OperationFinder
        implements Predicate
    {
        public boolean evaluate(Object object)
        {

            OperationFacade facadeOperation = (OperationFacade)object;
            boolean valid = StringUtils.trimToEmpty(facadeOperation.getName())
                .equals(StringUtils.trimToEmpty(operation.getName()));
            // if we've found an operation with a matching name
            // check the parameters
            if (valid)
            {
                valid = argumentsMatch(operation, facadeOperation);
            }
            return valid;
        }
    }

    /**
     * Returns true if the arguments contained within <code>oclOperation</code>
     * and <code>facadeOperation</code> match, false otherwise.
     * 
     * @param oclOperation an OCL Operation
     * @param facadeOperation a metafacade Operation
     * @return boolean whether the arguments match.
     */
    private boolean argumentsMatch(
        OperationDeclaration oclOperation,
        OperationFacade facadeOperation)
    {
        boolean argumentsMatch = this.argumentCountsMatch(
            oclOperation,
            facadeOperation);
        if (argumentsMatch)
        {
            argumentsMatch = this.argumentNamesMatch(
                oclOperation,
                facadeOperation);
        }
        return argumentsMatch;
    }

    /**
     * Returns true if the number of arguments contained within
     * <code>oclOperation</code> and <code>facadeOperation</code> match,
     * false otherwise.
     * 
     * @param oclOperation an OCL Operation
     * @param facadeOperation a metafacade Operation
     * @return boolean whether the count of the arguments match.
     */
    private boolean argumentCountsMatch(
        OperationDeclaration oclOperation,
        OperationFacade facadeOperation)
    {
        final String methodName = "ContextElementFinder.argumentCountMatch";
        ExceptionUtils.checkNull(methodName, "oclOperation", oclOperation);
        ExceptionUtils
            .checkNull(methodName, "facadeOperation", facadeOperation);
        VariableDeclaration[] expressionOpArgs = oclOperation.getArguments();
        Collection facadeOpArgs = facadeOperation.getArguments();
        boolean countsMatch = (expressionOpArgs == null || expressionOpArgs.length == 0)
            && (facadeOpArgs == null || facadeOpArgs.isEmpty());
        if (!countsMatch)
        {
            countsMatch = expressionOpArgs != null && facadeOpArgs != null
                && expressionOpArgs.length == facadeOpArgs.size();
        }
        return countsMatch;
    }

    /**
     * Returns true if the argument names contained within
     * <code>oclOperation</code> and <code>facadeOperation</code> match,
     * false otherwise.
     * 
     * @param oclOperation an OCL Operation
     * @param facadeOperation a metafacade Operation
     * @return boolean whether the arg names match or not.
     */
    private boolean argumentNamesMatch(
        OperationDeclaration oclOperation,
        OperationFacade facadeOperation)
    {

        final String methodName = "ContextElementFinder.argumentNamesMatch";
        ExceptionUtils.checkNull(methodName, "oclOperation", oclOperation);
        ExceptionUtils
            .checkNull(methodName, "facadeOperation", facadeOperation);

        Collection facadeOpArguments = facadeOperation.getArguments();
        VariableDeclaration[] expressionOpArgs = oclOperation.getArguments();
        Collection expressionArgNames = new ArrayList();
        if (expressionOpArgs != null)
        {
            for (int ctr = 0; ctr < expressionOpArgs.length; ctr++)
            {
                expressionArgNames.add(expressionOpArgs[ctr].getName());
            }
        }
        Collection facadeArgNames = new ArrayList();
        if (facadeOpArguments != null)
        {
            Iterator facadeOpArgumentIt = facadeOpArguments.iterator();
            while (facadeOpArgumentIt.hasNext())
            {
                ParameterFacade facadeArg = (ParameterFacade)facadeOpArgumentIt
                    .next();
                facadeArgNames.add(facadeArg.getName());
            }
        }
        return CollectionUtils.isEqualCollection(
            expressionArgNames,
            facadeArgNames);
    }

    /**
     * Finds the model element with the given <code>modelElementName</code>.
     * Will find either the non qualified name or qualified name. If more than
     * one model element is found with the non qualified name an exception will
     * be thrown.
     * 
     * @param modelElementName
     * @return Object the found model element
     */
    private Object findModelElement(final String modelElementName)
    {
        Object modelElement = null;
        Collection modelElements = this.model.getModelElements();
        CollectionUtils.filter(modelElements, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                boolean valid = false;
                if (ModelElementFacade.class.isAssignableFrom(object.getClass()))
                {
                    ModelElementFacade modelElement = (ModelElementFacade)object;
                    String elementName = StringUtils.trimToEmpty(modelElement
                        .getName());
                    String name = StringUtils.trimToEmpty(modelElementName);
                    valid = elementName.equals(name);
                    if (!valid)
                    {
                        elementName = StringUtils.trimToEmpty(modelElement
                            .getFullyQualifiedName());
                        valid = elementName.equals(name);
                    }
                }
                return valid;
            }
        });
        if (modelElements.size() > 1)
        {
            throw new ContextElementFinderException(
                "More than one element named '" + modelElementName
                    + "' was found within your model,"
                    + " please give the fully qualified name");
        }
        else if (modelElements.size() == 1)
        {
            modelElement = modelElements.iterator().next();
        }
        return modelElement;
    }

    /**
     * Returns the context element found in the model from the expression.
     * 
     * @return the context type as a ModelElementFacade.
     */
    public Object getContextElement()
    {
        return this.contextElement;
    }

}