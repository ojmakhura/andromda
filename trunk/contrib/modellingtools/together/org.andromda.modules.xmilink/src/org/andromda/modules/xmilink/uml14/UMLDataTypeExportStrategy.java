/*
 * DataTypeExportStrategy.java
 * 
 * Copyright 2004, 2005 The AndroMDA Team.
 * All Rights Reserved.
 * 

 * Lufthansa Systems Business Solutions GmbH.
 * Use is subject to license terms.
 * 
 */
package org.andromda.modules.xmilink.uml14;

import org.andromda.modules.xmilink.ExportStrategyFactory;

import com.togethersoft.openapi.model.elements.Entity;


/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 01.11.2004
 */
public class UMLDataTypeExportStrategy
        extends UMLEntityExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("DataType",
                UMLDataTypeExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName(Entity entity)
    {
        return "UML:DataType";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#doExportChildNodes()
     */
    protected boolean doExportChildNodes()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#doExportDependencies()
     */
    protected boolean doExportDependencies()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#doExportProperty(java.lang.String,
     *      java.lang.String)
     */
    protected boolean doExportProperty(String name, String value)
    {
        if (name.equalsIgnoreCase("$name"))
        {
            return true;
        }
        else
        {
            return super.doExportProperty(name, value);
        }
    }

}
