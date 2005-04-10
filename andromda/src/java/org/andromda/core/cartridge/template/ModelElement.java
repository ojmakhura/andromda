package org.andromda.core.cartridge.template;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Profile;
import org.andromda.core.common.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a single template &lt;modelElement/&gt; nested within the &lt;modelElements/&gt; element. It stores the
 * actual metafacade instances which match the model element criteria (i.e. stereotype, type, etc) defined by this
 * instance.
 *
 * @author Chad Brandon
 * @see ModelElements
 */
public class ModelElement
{
    private String stereotype;
    private String variable;
    private final Collection types = new ArrayList();
    private Collection metafacades = new ArrayList();

    /**
     * Gets the stereotype of this modelElement.
     *
     * @return Returns the stereotype.
     */
    public String getStereotype()
    {
        return this.stereotype;
    }

    /**
     * Returns <code>true</code> or <code>false</code> depending on whether or not this model element has a stereotype
     * defined.
     *
     * @return true/false
     */
    public boolean hasStereotype()
    {
        return StringUtils.isNotBlank(this.getStereotype());
    }

    /**
     * Returns <code>true</code> or <code>false</code> depending on whether or not this model element has any type
     * elements defined.
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
        this.stereotype = Profile.instance().get(StringUtils.trimToEmpty(stereotype));
        ExceptionUtils.checkEmpty(methodName, "stereotype", this.stereotype);
    }

    /**
     * Adds the <code>type</code> to the collection of types belonging to this model element.
     *
     * @param type the {@link Type}instance.
     */
    public void addType(Type type)
    {
        final String methodName = "ModelElement.addType";
        ExceptionUtils.checkNull(methodName, "type", type);
        this.types.add(type);
    }

    /**
     * Gets the variable stereotype of this modelElement (this is what is made available to a template during
     * processing).
     *
     * @return Returns the variable.
     */
    public String getVariable()
    {
        return this.variable;
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
     * Gets the metafacades that belong to this ModelElement instance. These are the actual elements from the model.
     *
     * @return the collection of metafacades.
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
     * Checks the <code>object</code> to see whether or not its acceptable. It matches on the types and each type's
     * properties. <strong>NOTE:</strong> protected visibility to improve performance from within {@link
     * #applyTypeFiltering()}
     *
     * @param metafacade the metafacade to check
     * @return true/false
     */
    protected boolean accept(Object metafacade)
    {
        Iterator typeIt = types.iterator();
        boolean accept = true;
        while (typeIt.hasNext() && accept)
        {
            Type type = (Type) typeIt.next();
            if (StringUtils.isNotBlank(type.getName()))
            {
                try
                {
                    accept = ClassUtils.loadClass(type.getName()).isAssignableFrom(metafacade.getClass());
                    // if the type matches the name, continue
                    if (accept)
                    {
                        Iterator properties = type.getProperties().iterator();
                        while (properties.hasNext())
                        {
                            final Type.Property property = (Type.Property) properties.next();
                            accept = PropertyUtils.containsValidProperty(metafacade, property.getName(),
                                    property.getValue());
                            if (!accept)
                            {
                                // break out of the loop on the first invalid
                                // property since all propertie should be valid.
                                break;
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