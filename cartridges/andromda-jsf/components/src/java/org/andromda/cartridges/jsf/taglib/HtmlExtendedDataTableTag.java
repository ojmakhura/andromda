package org.andromda.cartridges.jsf.taglib;

import javax.faces.component.UIComponent;

import org.andromda.cartridges.jsf.component.html.HtmlExtendedDataTable;
import org.apache.myfaces.taglib.html.ext.HtmlDataTableTag;

/**
 * Extends dataTable to provide the ability to submit tables of data and render 
 * the data back into the table using a backing value.
 * 
 * @author Chad Brandon
 */
public class HtmlExtendedDataTableTag
    extends HtmlDataTableTag
{

    /**
     * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
     */
    @Override
    protected void setProperties(final UIComponent component)
    {
        super.setProperties(component);
        this.setStringProperty(component, HtmlExtendedDataTable.IDENTIFIER_COLUMNS, this.identifierColumns);
        this.setStringProperty(component, HtmlExtendedDataTable.BACKING_VALUE, this.backingValue);
    }
    
    /**
     * A comma separated list of the columns that uniquely identify the row for this data table.
     */
    private String identifierColumns;

    /**
     * @return Returns the identifierColumns.
     */
    public String getIdentifierColumns()
    {
        return this.identifierColumns;
    }

    /**
     * @param identifierColumnsIn The identifierColumns to set.
     */
    public void setIdentifierColumns(String identifierColumnsIn)
    {
        this.identifierColumns = identifierColumnsIn;
    }
    
    /**
     * The backing value for this table.
     */
    private String backingValue;

    /**
     * @return Returns the backingValue.
     */
    public String getBackingValue()
    {
        return this.backingValue;
    }

    /**
     * @param backingValueIn The backingValue to set.
     */
    public void setBackingValue(String backingValueIn)
    {
        this.backingValue = backingValueIn;
    }
    
    /**
     * The component type for this tag.
     */
    private static final String COMPONENT_TYPE = HtmlExtendedDataTable.class.getName();
    
    /**
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    @Override
    public String getComponentType()
    {
        return COMPONENT_TYPE;
    }
}
