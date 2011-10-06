// license-header java merge-point
package org.andromda.test.howto16.a.crud;

import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public final class CarForm
    extends ValidatorForm
    implements Serializable
{
    private static final long serialVersionUID = -6129991580863661323L;

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

    private String serial;

    public String getSerial()
    {
        return this.serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
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

    private String type;

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
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

    private Long owner;

    public Long getOwner()
    {
        return this.owner;
    }

    public void setOwner(Long owner)
    {
        this.owner = owner;
    }

    private List ownerBackingList;

    public List getOwnerBackingList()
    {
        return this.ownerBackingList;
    }

    public void setOwnerBackingList(List ownerBackingList)
    {
        this.ownerBackingList = ownerBackingList;
    }

    /**
     * @see ValidatorForm#reset(ActionMapping,HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        serial = null;
        name = null;
        type = null;
        id = null;
        owner = null;
        ownerBackingList = null;
    }
}
