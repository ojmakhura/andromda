package org.andromda.core.cartridge.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Represents a single template &lt;&lt;modelElement/&gt;gt; nested within the
 * &lt;&lt;modelElements/&gt;gt; element. It stores the actual metafacade
 * instances which match the model element criteria (i.e. stereotype, type, etc)
 * defined by this instance.
 * 
 * @see ModelElements
 * @author Chad Brandon
 */
public class ModelElement
{
    private String stereotype;
    private String variable;
    private Collection types = null;
    private Collection metafacades = null;

    public ModelElement()
    {
        this.types = new ArrayList();
        this.metafacades = new ArrayList();
    }

    /**
     * Gets the stereotype of this modelElement.
     * 
     * @return Returns the stereotype.
     */
    public String getStereotype()
    {
        return stereotype;
    }

    /**
     * Returns <code>true</code> or <code>false</code> depending on whether
     * or not this model element has a stereotype defined.
     * 
     * @return true/false
     */
    public boolean hasStereotype()
    {
        return StringUtils.isNotBlank(this.stereotype);
    }

    /**
     * Returns <code>true</code> or <code>false</code> depending on whether
     * or not this model element has any type elements defined.
     * 
     * @return true/false
     */
    public boolean hasTypes()
    {
        return !this.types.isEmpty();
    }

    /**
     * Sets the stereotype of the ModelElement.
     * 
     * @param stereotype The stereotype to set.
     */
    public void setStereotype(String stereotype)
    {
        final String methodName = "ModelElement.setStereotype";
        this.stereotype = StringUtils.trimToEmpty(stereotype);
        ExceptionUtils.checkEmpty(methodName, "stereotype", this.stereotype);
    }

    /**
     * Adds the <code>type</code> to the collection of types belonging to this
     * model element.
     * 
     * @param type the ModelElementType instance.
     */
    public void addType(ModelElementType type)
    {
        final String methodName = "ModelElement.addType";
        ExceptionUtils.checkNull(methodName, "type", type);
        this.types.add(type);
    }

    /**
     * Gets the variable stereotype of this modelElement (this is what is made
     * available to a template during processing).
     * 
     * @return Returns the variable.
     */
    public String getVariable()
    {
        return variable;
    }

    /**
     * Sets the variable name.
     * 
     * @param variable The variable to set.
     */
    public void setVariable(String variable)
    {
        this.variable = StringUtils.trimToEmpty(variable);
    }

    /**
     * Sets the current metafacades that belong to this ModelElement instance.
     * 
     * @param metafacades the collection of metafacdes
     */
    public void setMetafacades(Collection metafacades)
    {
        final String methodName = "ModelElement.setMetafacades";
        ExceptionUtils.checkNull(methodName, "metafacades", metafacades);
        this.metafacades = metafacades;
        this.applyTypeFiltering();
    }

    /**
     * Gets the metafacades that belong to this ModelElement instance. These are
     * the actual elements from the model.
     * 
     * @return the collection of model elements.
     */
    public Collection getMetafacades()
    {
        return this.metafacades;
    }

    /**
     * Applies any filtering by any types specified within this model element.
     * 
     * @param metafacades the metafacades to filter
     */
    private void applyTypeFiltering()
    {
        if (this.hasTypes())
        {
            CollectionUtils.filter(this.metafacades, new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return accept(object);
                }
            });
        }
    }

    /**
     * Checks the <code>object</code> to see whether or not its acceptable. It
     * matches on the types and each type's properties.
     * 
     * @param metafacade the metafacade to check
     * @return true/false
     */
    private boolean accept(Object metafacade)
    {
        Iterator typeIt = types.iterator();
        boolean accept = true;
        while (typeIt.hasNext() && accept)
        {
            ModelElementType type = (ModelElementType)typeIt.next();
            if (StringUtils.isNotBlank(type.getName()))
            {
                try
                {
                    accept = ClassUtils.loadClass(type.getName())
                        .isAssignableFrom(metafacade.getClass());
                    // if the type matches the name, continue
                    if (accept)
                    {
                        Iterator properties = type.getProperties().iterator();
                        while (properties.hasNext())
                        {
                            ModelElementType.Property property = (ModelElementType.Property)properties
                                .next();
                            if (PropertyUtils.isReadable(metafacade, property
                                .getName()))
                            {
                                Object value = PropertyUtils.getProperty(
                                    metafacade,
                                    property.getName());
                                accept = value != null;
                                // if accept is still true, and the property
                                // has a value defined
                                if (accept && property.hasValue())
                                {
                                    accept = String.valueOf(value).equals(
                                        property.getValue());
                                }
                            }
                        }
                    }
                }
                catch (Throwable th)
                {
                    accept = false;
                }
            }
        }
        return accept;
    }
}