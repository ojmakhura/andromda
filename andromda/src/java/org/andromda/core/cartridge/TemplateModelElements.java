package org.andromda.core.cartridge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Defines the &lt;&lt;modelElements/&gt;&gt;
 * element within a &lt;&lt;template/&gt;&gt; 
 * within an XML cartridge descriptor.
 * 
 * @see org.andromda.core.cartridge.TemplateConfiguration
 * @see org.andromda.core.cartridge.TemplateModelElement
 * 
 * @author Chad Brandon
 */
public class TemplateModelElements {
    
    private String variable;
    private Map modelElements = null;
     
    public TemplateModelElements() {
        this.modelElements = new HashMap();   
    }
    
    /**
     * The variable name to make the model element 
     * available to the template engine. For example if you
     * have the modelElement &lt;&lt;entity&gt;&gt;
     * defined within your &lt;&lt;modelElements&gt;&gt; element
     * you may want to define this value as <code>entity</code>.
     * If on the other hand the outputToSingleFile
     * flag is set to true you'd probably want to
     * make it available as <code>entities</code>. 
     * 
     * @return Returns the variable.
     */
    public String getVariable() {
        return variable;
    }
    
    /**
     * @param variable The variable to set.
     */
    public void setVariable(String variable) {
        final String methodName = "TemplateModelElements.setVariable";
        variable = StringUtils.trimToEmpty(variable);
        ExceptionUtils.checkEmpty(methodName, "variable", variable);
        this.variable = variable;
    }
    
    /**
     * Adds a modelElement to the Map of <code>modelElements</code>
     * and keys it by <code>stereotype</code>. The <code>stereotype</code>
     * of the modelElement must be defined.
     * @param modelElement the new TemplateModelElement to add.
     */
    public void addModelElement(TemplateModelElement modelElement) {
        final String methodName = "TemplateModelElements.addModelElement";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        String stereotype = StringUtils.trimToEmpty(modelElement.getStereotype());
        ExceptionUtils.checkEmpty(methodName, "modelElement.stereotype", stereotype);
        modelElements.put(stereotype, modelElement);
    }
    
    /**
     * Retrieves the TemplateModelElement
     * having the given <code>stereotypeName</code>.  
     * 
     * @param stereotypeName the name of the modelElement's stereotype
     */
    public TemplateModelElement getModelElement(String stereotypeName) {
        final String methodName = "TemplateModelElements.getModelElement";
        stereotypeName = StringUtils.trimToEmpty(stereotypeName);
        ExceptionUtils.checkEmpty(methodName, "stereotypeName", stereotypeName);       
        return (TemplateModelElement)this.modelElements.get(stereotypeName);
    }
    
    /**
     * Retrieves the model elements bound to the stereotype
     * having the given <code>stereotypeName</code>.  
     * 
     * @param stereotypeName the name of the stereotype 
     */
    public Collection getModelElements(String stereotypeName) {    
        Collection modelElements = null;
        TemplateModelElement stereotype = this.getModelElement(stereotypeName);
        if (stereotype != null) {
            modelElements = stereotype.getModelElements();
        }
        return modelElements;
    }
    
    /**
     * Gets all model elements for each TemplateModelElement
     * belonging to this TemplateModelElements instance.
     * @return Collection of all model elements.
     */
    public Collection getAllModelElements() {
    	Collection allModelElements = new ArrayList();
        Iterator stereotypeNames = this.stereotypeNames();
        while (stereotypeNames.hasNext()) {
        	String name = (String)stereotypeNames.next();
            allModelElements.addAll(getModelElements(name));
        }
        return allModelElements;
    }
    
    /**
     * Retrieves the variable for the given 
     * <code>stereotypeName</code>.  First it checks
     * to see if a variable has been defined for the stereotype
     * wit the <code>stereotypeName</code>, if it can't find
     * that, it uses the default <code>variable</code> defined
     * for this TemplateModelElements object.
     * 
     * @param stereotypeName the name of the stereotype of which
     *        to retrieve the variable
     */
    protected String getVariable(String stereotypeName) {
        stereotypeName = StringUtils.trimToEmpty(stereotypeName);
        String variable = (String)modelElements.get(stereotypeName);
        if (StringUtils.isEmpty(variable)) {
        	variable = this.variable;
        }
        return variable;
    }
    
    /**
     * Returns an Iterator containing all stereotype
     * names for the modelElement stereotypes of this
     * instance.
     * 
     * @return Iterator
     */
    protected Iterator stereotypeNames() {
    	return this.modelElements.keySet().iterator();
    }
        
    /**
     * Returns true if this instance
     * has no <code>modelElements</code>
     * stored within it.
     * 
     * @return true/false
     */
    protected boolean isEmpty() {
    	return this.modelElements.isEmpty();
    }
    
}
