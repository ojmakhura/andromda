package org.andromda.mdr.xmi.uml20.load.core;

/**
 * Info about referenced element. In case id changed this info is used to find
 * element.
 * 
 * @author Donatas Simkunas
 */
public class HREFInfo
{
    private String mPath;
    private String mType;
    private String mSupplierPath;
    private String mClientPath;
    private String operationParams;
    private String mResource;

    public String getClientPath()
    {
        return mClientPath;
    }

    public String getPath()
    {
        return mPath;
    }

    public String getSupplierPath()
    {
        return mSupplierPath;
    }

    public String getType()
    {
        return mType;
    }

    public void setClientPath(String string)
    {
        mClientPath = string;
    }

    public void setPath(String string)
    {
        mPath = string;
    }

    public void setSupplierPath(String string)
    {
        mSupplierPath = string;
    }

    public void setType(String string)
    {
        mType = string;
    }

    public String getOperationParams()
    {
        return operationParams;
    }

    public void setOperationParams(String list)
    {
        operationParams = list;
    }

    public String getResource()
    {
        return mResource;
    }

    public void setResource(String resource)
    {
        mResource = resource;
    }
}