package org.andromda.mdr.xmi.uml20.load.core;

import java.util.HashMap;
import java.util.Map;

import javax.jmi.reflect.RefObject;

import org.netbeans.api.mdr.MDRepository;

public class IDRegistry
{
    private final Map mOld2NewIDS;
    private final MDRepository mRepository;
    private final Map mNew2Old;

    public IDRegistry(
        MDRepository repository)
    {
        mRepository = repository;
        mNew2Old = new HashMap();
        mOld2NewIDS = new HashMap();
    }

    public void registerID(String savedID, String newID)
    {
        mOld2NewIDS.put(savedID, newID);
        mNew2Old.put(newID, savedID);
    }

    public RefObject getElementByID(String id)
    {
        return (RefObject)mRepository.getByMofId(getElementID(id));
    }

    private String getElementID(String savedID)
    {
        String newID = (String)mOld2NewIDS.get(savedID);
        if (newID != null)
        {
            return newID;
        }
        return savedID;
    }

    public String getSavedID(String mofID)
    {
        return (String)mNew2Old.get(mofID);
    }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}