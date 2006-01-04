package org.andromda.mdr.repositoryutils.browser;

import javax.jmi.model.StructuralFeature;


class Value
{
    final private StructuralFeature mFeature;
    final private Object mValue;

    public Value(
        StructuralFeature feature,
        Object value)
    {
        mFeature = feature;
        mValue = value;
    }

    /**
     * @return Returns the value.
     */
    Object getValue()
    {
        return mValue;
    }

    StructuralFeature getFeature()
    {
        return mFeature;
    }
}