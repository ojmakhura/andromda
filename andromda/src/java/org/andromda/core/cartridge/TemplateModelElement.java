package org.andromda.core.cartridge;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Represents a single template &lt;&lt;modelElement/&gt;gt;
 * within the &lt;&lt;modelElements/&gt;gt; element.
 * 
 * @see org.andromda.core.cartridge.TemplateModelElements
 * 
 * @author Chad Brandon
 */
public class TemplateModelElement {
    
    private String stereotype;
    private String variable;
    private Collection modelElements = null;
    
    public TemplateModelElement() {
    	this.modelElements = new ArrayList();
    }
    
	/**
     * Gets the stereotype of this modelElement.
     * 
	 * @return Returns the stereotype.
	 */
	public String getStereotype() {
		return stereotype;
	}
    
	/**
     * Sets the stereotype of the TemplateModelElement.
     * 
	 * @param stereotype The stereotype to set.
	 */
	public void setStereotype(String stereotype) {
        final String methodName = "TemplateModelElement.setStereotype";
        stereotype = StringUtils.trimToEmpty(stereotype);
        ExceptionUtils.checkEmpty(methodName, "stereotype", stereotype);
		this.stereotype = stereotype;
	}
    
	/**
     * Gets the variable stereotype of this modelElement (this
     * is what is made available to a template during
     * processing).
     * 
	 * @return Returns the variable.
	 */
	public String getVariable() {
		return variable;
	}
    
	/**
     * Sets the variable name. 
     * 
	 * @param variable The variable to set.
	 */
	public void setVariable(String variable) {
		this.variable = StringUtils.trimToEmpty(variable);
	}
    
    /**
     * Adds the <code>modelElement</code> to the model
     * elements belonging to this TemplateModelElement.
     * 
     * @param modelElement the model element to add, can
     *        not be null;
     */
    protected void addModelElement(Object modelElement) {
        final String methodName = "TemplateModelElement.addModelElement";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        this.modelElements.add(modelElement);
    }
    
    /**
     * Sets the current model elements that
     * belong to this TemplateModelElement instanec.
     * 
     * @param the collection of model elements.
     */
    protected void setModelElements(Collection modelElements) {
        final String methodName = "TemplateModelElement.setModelElements";
        ExceptionUtils.checkNull(methodName, "modelElements", modelElements);
        this.modelElements = modelElements;
    }
    
    /**
     * Gets the current model elements that
     * belong to this TemplateModelElement instanec.
     * 
     * @return the collection of model elements.
     */
    protected Collection getModelElements() {
    	return this.modelElements;
    }
}
