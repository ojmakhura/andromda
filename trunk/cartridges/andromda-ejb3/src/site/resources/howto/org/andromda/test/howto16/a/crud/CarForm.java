// license-header java merge-point
package org.andromda.test.howto16.a.crud;

public final class CarForm
    extends org.apache.struts.validator.ValidatorForm
    implements java.io.Serializable
{
    private java.util.List manageableList = null;

    public java.util.List getManageableList()
    {
        return this.manageableList;
    }

    public void setManageableList(java.util.List manageableList)
    {
        this.manageableList = manageableList;
    }

    private java.lang.Long[] selectedRows = null;

    public java.lang.Long[] getSelectedRows()
    {
        return this.selectedRows;
    }

    public void setSelectedRows(java.lang.Long[] selectedRows)
    {
        this.selectedRows = selectedRows;
    }

    private java.lang.String serial;

    public java.lang.String getSerial()
    {
        return this.serial;
    }

    public void setSerial(java.lang.String serial)
    {
        this.serial = serial;
    }

    private java.lang.String name;

    public java.lang.String getName()
    {
        return this.name;
    }

    public void setName(java.lang.String name)
    {
        this.name = name;
    }

    private java.lang.String type;

    public java.lang.String getType()
    {
        return this.type;
    }

    public void setType(java.lang.String type)
    {
        this.type = type;
    }

    private java.lang.Long id;

    public java.lang.Long getId()
    {
        return this.id;
    }

    public void setId(java.lang.Long id)
    {
        this.id = id;
    }

    private java.lang.Long owner;

    public java.lang.Long getOwner()
    {
        return this.owner;
    }

    public void setOwner(java.lang.Long owner)
    {
        this.owner = owner;
    }

    private java.util.List ownerBackingList;

    public java.util.List getOwnerBackingList()
    {
        return this.ownerBackingList;
    }

    public void setOwnerBackingList(java.util.List ownerBackingList)
    {
        this.ownerBackingList = ownerBackingList;
    }

    /**
     * @see org.apache.struts.validator.ValidatorForm#reset(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest)
     */
    public void reset(org.apache.struts.action.ActionMapping mapping, javax.servlet.http.HttpServletRequest request)
    {
        serial = null;
        name = null;
        type = null;
        id = null;
        owner = null;
        ownerBackingList = null;
    }
}