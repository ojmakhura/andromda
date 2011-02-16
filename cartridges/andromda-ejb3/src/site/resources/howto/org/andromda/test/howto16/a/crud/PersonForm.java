// license-header java merge-point
package org.andromda.test.howto16.a.crud;

public final class PersonForm
    extends org.apache.struts.validator.ValidatorForm
    implements java.io.Serializable
{
    private static final long serialVersionUID = -3318981191704948354L;

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

    private java.lang.String name;

    public java.lang.String getName()
    {
        return this.name;
    }

    public void setName(java.lang.String name)
    {
        this.name = name;
    }

    private java.util.Date birthDate;

    public java.util.Date getBirthDate()
    {
        return this.birthDate;
    }

    public void setBirthDate(java.util.Date birthDate)
    {
        this.birthDate = birthDate;
    }

    private static final java.text.DateFormat birthDateFormatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
    static { birthDateFormatter.setLenient(true); }

    public java.lang.String getBirthDateAsString()
    {
        return (birthDate == null) ? null : birthDateFormatter.format(birthDate);
    }

    public void setBirthDateAsString(java.lang.String birthDate)
    {
        try
        {
            this.birthDate = (org.apache.commons.lang.StringUtils.isBlank(birthDate)) ? null : birthDateFormatter.parse(birthDate);
        }
        catch (java.text.ParseException pe)
        {
            throw new java.lang.RuntimeException(pe);
        }
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

    private java.lang.Long[] cars;

    public java.lang.Long[] getCars()
    {
        return this.cars;
    }

    public void setCars(java.lang.Long[] cars)
    {
        this.cars = cars;
    }

    private java.util.List carsBackingList;

    public java.util.List getCarsBackingList()
    {
        return this.carsBackingList;
    }

    public void setCarsBackingList(java.util.List carsBackingList)
    {
        this.carsBackingList = carsBackingList;
    }

    /**
     * @see org.apache.struts.validator.ValidatorForm#reset(org.apache.struts.action.ActionMapping,javax.servlet.http.HttpServletRequest)
     */
    public void reset(org.apache.struts.action.ActionMapping mapping, javax.servlet.http.HttpServletRequest request)
    {
        name = null;
        birthDate = null;
        id = null;
        cars = null;
        carsBackingList = null;
    }
}