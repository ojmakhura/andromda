package org.andromda.translation.testsuite;

import java.util.Collection;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.translation.BaseTranslator;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.node.AOperationContextDeclaration;
import org.andromda.core.translation.syntax.Operation;
import org.andromda.core.translation.syntax.impl.ConcreteSyntaxUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Finds the context element defined in the OCL expression.
 * 
 * @author Chad Brandon
 */
public class ContextElementFinder extends BaseTranslator {
    
    private ModelAccessFacade model;
        
    /**
     * The Constructor which takes <code>model</code>,
     * which is an instance of ModelAccessFacade that will
     * allow us to search the model for the context element.
     * 
     * @param model the ModelAccessFacade to search.
     */
    public ContextElementFinder(ModelAccessFacade model) {
        ExceptionUtils.checkNull("ContextElementFinder", "model", model);
        this.model = model;
    }
    
    /**
     * Hide the default constructor
     */
    private ContextElementFinder(){}
    
    /**
     * The operation that is set if the context
     * of the constraint happens to be an operation.
     */
    private Operation operation = null;
    
    /**
     * The found context type.
     */
    private Object contextElement = null;

    /**
     * @see org.andromda.core.translation.analysis.DepthFirstAdapter#inAOperationContextDeclaration(org.andromda.core.translation.node.AOperationContextDeclaration)
     */
    public void inAOperationContextDeclaration(AOperationContextDeclaration declaration) {
        super.inAOperationContextDeclaration(declaration);
        if (declaration != null) {
            operation = ConcreteSyntaxUtils.getOperationDeclaration(declaration.getOperation());
        }
    }    
    
    /**
     * We use the postProcess method to retrieve
     * the contextType from the expression and then find
     * the actual model element using metafacades.
     * 
     * @see org.andromda.core.translation.BaseTranslator#postProcess()
     */
    public void postProcess() {
        super.postProcess();  
        Expression expression = this.getExpression();
        if (expression != null) {
            String contextElementName = expression.getContextElement();
            this.contextElement = this.findModelElement(
                contextElementName.replaceAll("::", "\\."));
            if (this.contextElement != null) {
                logger.info("found context element --> '" 
                    + contextElementName + "'");
            } else {
            	logger.info("Could not find model element --> '" 
                    + contextElementName + "'"); 
            }
            
            if (this.contextElement != null && 
                this.operation != null && 
                ClassifierFacade.class.isAssignableFrom(contextElement.getClass())) {
                ClassifierFacade type = (ClassifierFacade)this.contextElement;
                Collection operations = type.getOperations();
                CollectionUtils.filter(
                    operations,
                    new Predicate() {
                        public boolean evaluate(Object object) {
                            return StringUtils.trimToEmpty(
                                ((OperationFacade)object).getName()).equals(
                                     StringUtils.trimToEmpty(operation.getName()));
                        }
                    });
                if (operations.isEmpty()) {
                    throw new ContextElementFinderException("No operation named '" 
                        + operation.getName() + "' could be found on element --> '" 
                        + contextElementName 
                        + "', please check your model");
                }
                
                // if we only have one operation then we just set that
                // as the context element, otherwise we'll need to figure 
                // out which operation is the context operation by checking
                // the arguments.
                if (operations.size() == 1) {
                   this.contextElement = operations.iterator().next();
                } else {
                    // now find the correct operation since there are
                    // more than one with the same name
                }
            }
        }
    }
    
    /**
     * Finds the model element with the given 
     * <code>modelElementName</code>.  Will find either
     * the non qualified name or qualified name.  If more
     * than one model element is found with the non qualified
     * name an exception will be thrown.
     * 
     * @param modelElementName
     * @return Object the found model element
     */
    private Object findModelElement(final String modelElementName) {
        Object modelElement = null;
        Collection modelElements = this.model.getModelElements();
        CollectionUtils.filter(
            modelElements,
            new Predicate() {
                public boolean evaluate(Object object) {
                    ModelElementFacade modelElement = 
                        (ModelElementFacade)object;
                    String elementName = 
                        StringUtils.trimToEmpty(modelElement.getName());
                    String name = StringUtils.trimToEmpty(modelElementName);
                    boolean valid = elementName.equals(name);
                    if (!valid) {
                        elementName = 
                            StringUtils.trimToEmpty(
                                modelElement.getFullyQualifiedName());                        valid = 
                        elementName.equals(name);
                    }
                    return valid;
                }
            });
        if (modelElements.size() > 1) {
            throw new ContextElementFinderException(
                "More than one element named '" 
                + modelElementName 
                + "' was found within your model,"
                + " please give the fully qualified name");    
        } else  if (modelElements.size() == 1) {
            modelElement = modelElements.iterator().next();
        }
        return modelElement;
    }
    
    /**
     * Returns the context element found in the
     * model from the expression.
     * 
     * @return the context type as a ModelElementFacade.
     */
    public Object getContextElement() {
        return this.contextElement;    
    }
    
}
