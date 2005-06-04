package org.andromda.core.cartridge.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Defines the &lt;modelElements/&gt; element within a &lt;template/&gt; within an XML cartridge descriptor. This allows
 * the grouping of model elements by criteria defined within the nested {@link ModelElement}instances.
 *
 * @author Chad Brandon
 * @see Template
 * @see ModelElement
 */
public class ModelElements
{
    private String variable;
    private final Collection modelElements = new ArrayList();

    /**
     * The variable name to make the model element available to the template engine. For example if you have the
     * modelElement &lt;&lt;entity&gt;&gt; defined within your &lt;&lt;modelElements&gt;&gt; element you may want to
     * define this value as <code>entity</code>. If on the other hand the outputToSingleFile flag is set to true you'd
     * probably want to make it available as <code>entities</code>.
     *
     * @return Returns the variable.
     */
    public String getVariable()
    {
        return variable;
    }

    /**
     * @param variable The variable to set.
     */
    public void setVariable(String variable)
    {
        final String methodName = "ModelElements.setVariable";
        variable = StringUtils.trimToEmpty(variable);
        ExceptionUtils.checkEmpty(methodName, "variable", variable);
        this.variable = variable;
    }

    /**
     * Adds a modelElement to the collection of <code>modelElements</code>.
     *
     * @param modelElement the new ModelElement to add.
     */
    public void addModelElement(final ModelElement modelElement)
    {
        final String methodName = "ModelElements.addModelElement";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        modelElements.add(modelElement);
    }

    /**
     * Gets all metafacade instances from each ModelElement belonging to this ModelElements instance.
     *
     * @return Collection of all model elements.
     */
    public Set getAllMetafacades()
    {
        final Set allMetafacades = new HashSet();
        CollectionUtils.forAllDo(
            this.modelElements,
            new Closure()
            {
                public void execute(Object object)
                {
                    allMetafacades.addAll(((ModelElement)object).getMetafacades());
                }
            });
        return allMetafacades;
    }

    /**
     * Returns all model elements belonging to this model elements instance.
     *
     * @return Collection of all {@link ModelElement}instances.
     */
    public Collection getModelElements()
    {
        return this.modelElements;
    }

    /**
     * Returns true if this instance has no <code>modelElements</code> stored within it.
     *
     * @return true/false
     */
    public boolean isEmpty()
    {
        return this.modelElements.isEmpty();
    }
}