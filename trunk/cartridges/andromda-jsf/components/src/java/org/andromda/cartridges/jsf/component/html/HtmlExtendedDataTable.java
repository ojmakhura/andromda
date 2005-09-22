package org.andromda.cartridges.jsf.component.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.component.html.ext.HtmlDataTable;


/**
 * Extends the datatype and provides the ability to have a backing value: this is
 * useful when submitting tables of information.
 *
 * @author Chad Brandon
 */
public class HtmlExtendedDataTable
    extends HtmlDataTable
{
    /**
     * Overriden to provid population of the backingValue's items with the value's items (and
     * updating the model afterwards).
     *
     * @see javax.faces.component.UIData#getValue()
     */
    public Object getValue()
    {
        Object value = super.getValue();
        Object backingValue = this.getBackingValue();
        if (backingValue != null)
        {
            final Class valueType = this.getValueType();
            final Class backingValueType = this.getBackingValueType();
            if (valueType != backingValueType)
            {
                throw new IllegalArgumentException("The 'value' and 'backingValue' must be of the same type");
            }
            final String identifierColumnsString = this.getIdentifierColumns();
            final String[] identifierColumns =
                StringUtils.isBlank(identifierColumnsString) ? new String[0]
                                                             : StringUtils.trimToEmpty(identifierColumnsString).split(
                    "\\s*,\\s*");

            // - handle collections
            if (backingValue instanceof Collection)
            {
                if (!(backingValue instanceof List))
                {
                    backingValue = new ArrayList((Collection)backingValue);
                }
                if (!(value instanceof List))
                {
                    value = new ArrayList((Collection)value);
                }
                final List backingValues = (List)backingValue;
                final List values = value != null ? (List)value : Collections.EMPTY_LIST;
                for (final ListIterator iterator = backingValues.listIterator(); iterator.hasNext();)
                {
                    final Object backingValueItem = iterator.next();
                    Object backingValueItemIdentifier = null;
                    if (identifierColumns != null && identifierColumns.length > 0)
                    {
                        backingValueItemIdentifier = this.getProperty(
                                backingValueItem,
                                identifierColumns[0]);
                    }
                    for (final Iterator valueIterator = values.iterator(); valueIterator.hasNext();)
                    {
                        final Object valueItem = valueIterator.next();
                        if (valueItem.equals(backingValueItem))
                        {
                            iterator.set(valueItem);
                            break;
                        }
                        if (backingValueItemIdentifier != null)
                        {
                            Object valueItemIdentifier = this.getProperty(
                                    valueItem,
                                    identifierColumns[0]);
                            if (backingValueItemIdentifier.equals(valueItemIdentifier))
                            {
                                iterator.set(valueItem);
                                break;
                            }
                        }
                    }
                }
                value = backingValues;
            }

            // - handle arrays
            else if (backingValue.getClass().isArray())
            {
                final Object[] backingValues = (Object[])backingValue;
                final Object[] values = value != null ? (Object[])value : new Object[0];
                for (int backingValueCtr = 0; backingValueCtr < backingValues.length; backingValueCtr++)
                {
                    final Object backingValueItem = backingValues[backingValueCtr];
                    Object backingValueItemIdentifier = null;
                    if (identifierColumns != null && identifierColumns.length > 0)
                    {
                        backingValueItemIdentifier = this.getProperty(
                                backingValueItem,
                                identifierColumns[0]);
                    }
                    for (int valueCtr = 0; valueCtr < values.length; valueCtr++)
                    {
                        final Object valueItem = values[valueCtr];
                        if (valueItem.equals(backingValueItem))
                        {
                            backingValues[backingValueCtr] = valueItem;
                            break;
                        }
                        if (backingValueItemIdentifier != null)
                        {
                            Object valueItemIdentifier = this.getProperty(
                                    valueItem,
                                    identifierColumns[0]);
                            if (backingValueItemIdentifier.equals(valueItemIdentifier))
                            {
                                backingValues[backingValueCtr] = valueItem;
                                break;
                            }
                        }
                    }
                }
                value = backingValues;
            }
            else
            {
                throw new IllegalArgumentException("The backing value must be a collection or array");
            }
        }
        this.updateModelValue(value);
        return value;
    }

    /**
     * Updates the model (i.e. underlying managed bean's value).
     * 
     * @param value the value from which to update the model.
     */
    public void updateModelValue(final Object value)
    {
        final ValueBinding binding = getValueBinding(VALUE);
        if (binding != null)
        {
            binding.setValue(
                this.getFacesContext(),
                value);
        }
    }

    /**
     * The value attribute.
     */
    private static final String VALUE = "value";

    /**
     * Gets the type of the backing value attribute.
     * @return the backing value's type or null if the backing value
     *         isn't defined.
     */
    private Class getBackingValueType()
    {
        return this.getBindingType(BACKING_VALUE);
    }

    /**
     * Gets the value's type attribute or null if value was not defined.
     *
     * @return the value's type or null if undefined.
     */
    private Class getValueType()
    {
        return this.getBindingType(VALUE);
    }

    /**
     * Gets the binding type given the attribute name.
     *
     * @param name the name of the component's attribute.
     * @return the binding type or null if the binding wasn't found.
     */
    private Class getBindingType(final String name)
    {
        Class type = null;
        ValueBinding binding = getValueBinding(name);
        if (binding != null)
        {
            final FacesContext context = this.getFacesContext();
            type = binding.getType(context);
        }
        return type;
    }

    private Object getProperty(
        final Object bean,
        final String property)
    {
        try
        {
            return PropertyUtils.getProperty(
                bean,
                property);
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }

    private Object backingValue = null;

    /**
     * The attribute that stores the backing value.
     */
    public static final String BACKING_VALUE = "backingValue";

    /**
     * Retrieves the backing value of this extended data table (the backing value contains
     * the values which the result of the value attribute are compared against).
     * @return
     */
    protected Object getBackingValue()
    {
        if (this.backingValue == null)
        {
            final ValueBinding binding = this.getValueBinding(BACKING_VALUE);
            this.backingValue = binding == null ? null : binding.getValue(getFacesContext());
        }
        return this.backingValue;
    }

    /**
     * Stores the identifier columns attribute.
     */
    private String identifierColumns;

    /**
     * The attrribute that stores the identifier columns.
     */
    public static final String IDENTIFIER_COLUMNS = "identifierColumns";

    /**
     * Retrieves the identifier columns component attribute.
     *
     * @return the identifier columns component attribute.
     */
    protected String getIdentifierColumns()
    {
        if (this.identifierColumns == null)
        {
            this.identifierColumns = (String)this.getAttributes().get(IDENTIFIER_COLUMNS);
            if (this.identifierColumns == null)
            {
                final ValueBinding binding = this.getValueBinding(IDENTIFIER_COLUMNS);
                this.identifierColumns =
                    binding == null ? null : ObjectUtils.toString(binding.getValue(getFacesContext()));
            }
        }
        return this.identifierColumns;
    }
}