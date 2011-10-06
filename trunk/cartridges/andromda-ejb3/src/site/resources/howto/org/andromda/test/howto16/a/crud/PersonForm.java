// license-header java merge-point
package org.andromda.test.howto16.a.crud;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public final class PersonForm
    extends ValidatorForm
    implements Serializable
{
    private static final long serialVersionUID = -3318981191704948354L;

    private List manageableList = null;

    public List getManageableList()
    {
        return this.manageableList;
    }

    public void setManageableList(List manageableList)
    {
        this.manageableList = manageableList;
    }

    private Long[] selectedRows = null;

    public Long[] getSelectedRows()
    {
        return this.selectedRows;
    }

    public void setSelectedRows(Long[] selectedRows)
    {
        this.selectedRows = selectedRows;
    }

    private String name;

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private Date birthDate;

    public Date getBirthDate()
    {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    private static final DateFormat birthDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    static { birthDateFormatter.setLenient(true); }

    public String getBirthDateAsString()
    {
        return (birthDate == null) ? null : birthDateFormatter.format(birthDate);
    }

    public void setBirthDateAsString(String birthDate)
    {
        try
        {
            this.birthDate = (StringUtils.isBlank(birthDate)) ? null : birthDateFormatter.parse(birthDate);
        }
        catch (ParseException pe)
        {
            throw new RuntimeException(pe);
        }
    }

    private Long id;

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    private Long[] cars;

    public Long[] getCars()
    {
        return this.cars;
    }

    public void setCars(Long[] cars)
    {
        this.cars = cars;
    }

    private List carsBackingList;

    public List getCarsBackingList()
    {
        return this.carsBackingList;
    }

    public void setCarsBackingList(List carsBackingList)
    {
        this.carsBackingList = carsBackingList;
    }

    /**
     * @see ValidatorForm#reset(ActionMapping,HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        name = null;
        birthDate = null;
        id = null;
        cars = null;
        carsBackingList = null;
    }
}
