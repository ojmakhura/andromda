/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2002 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.andromda.uml2mof;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Iterator;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.XmiReader;
import javax.jmi.xmi.XmiWriter;

import org.andromda.repositories.mdr.MDRXmiReferenceResolver;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.lib.jmi.uml2mof.Transformer;
import org.omg.uml.UmlPackage;
import org.openide.ErrorManager;
import org.openide.util.Lookup;


/**
 * The Main class that runs UML 2 MOF.
 *
 * @author  Martin Matula
 * @author Chad Brandon
 */
public class Uml2Mof
{
    // name of a MOF extent that will serve as a target extent for the UML2MOF transformation
    private static final String MOF_INSTANCE = "MOFInstance";

    // name of a UML extent (instance of UML metamodel) that the UML models will be loaded into
    private static final String UML_INSTANCE = "UMLInstance";

    // name of a MOF extent that will contain definition of UML metamodel
    private static final String UML_META_MODEL = "UML";

    // repository
    private static MDRepository repository;

    // UML extent
    private static UmlPackage uml;

    // MOF extent
    private static ModelPackage mof;

    // XMI reader
    private static XmiReader reader;

    public static void main(String[] args)
    {
        try
        {
            // get the default repository from the MDR manager
            repository = MDRManager.getDefault().getDefaultRepository();

            // get's the URL of the file passed as the first commandline parameter
            // (file containing the UML model to be transformed to MOF)
            final String uri = args[0];

            // opens an output stream for the file name passed as the second commandline parameter
            // (name of file to be used to save the resulting MOF metamodel)
            final FileOutputStream out = new FileOutputStream(args[1]);
            
            final String arg3 = args.length > 2 ? args[2] : "";
            final String[] moduleSearchPath = arg3.trim().length() == 0 ? new String[0] : arg3.split(",");

            reader =
                XMIReaderFactory.getDefault().createXMIReader(
                    new MDRXmiReferenceResolver(
                        new RefPackage[] {uml},
                        moduleSearchPath));
            // look up an implementation of XmiReader interface
           // reader = (XmiReader)Lookup.getDefault().lookup(XmiReader.class);

            // look up an implementation of XmiWriter interface
            XmiWriter writer = (XmiWriter)Lookup.getDefault().lookup(XmiWriter.class);

            // initialize the repository (make sure the UML metamodel is loaded and both
            // UML and MOF metamodels are instantiated)
            init();

            // start a write transaction
            repository.beginTrans(true);
            try
            {
                // read the UML model into the UML extent
                reader.read(
                    uri,
                    uml);

                // transform the UML model in UML extent into a MOF metamodel (which will reside in MOF extent)
                Transformer.execute(
                    uml,
                    mof);

                // write the content of the MOF extent to the XMI (i.e. write the resulting MOF metamodel)
                writer.write(
                    out,
                    mof,
                    null);
            }
            finally
            {
                // rollback the write transaction
                // (this is to make sure the transformed models are not kept in the storage - they
                // will probably not be needed anymore - another alternative to this would be to
                // remove both UML model and the resulting MOF metamodel and do commit, but in this
                // case doing rollback is simpler)
                repository.endTrans(true);

                // shutdown the repository to make sure all caches are flushed to disk
                MDRManager.getDefault().shutdownAll();
                out.close();
            }
        }
        catch (final Exception exception)
        {
            ErrorManager.getDefault().notify(
                ErrorManager.ERROR,
                exception);
        }
    }

    /** 
     * Makes sure UML and MOF extents are created. 
     */
    private static void init()
        throws Exception
    {
        // try to retrieve MOF and UML extents
        mof = (ModelPackage)repository.getExtent(MOF_INSTANCE);
        uml = (UmlPackage)repository.getExtent(UML_INSTANCE);

        // check whether both extents exist (they do not exist if this is the first time
        // the UML2MOF tool is run or the storage files created by previous runs
        // were deleted)
        if (mof == null)
        {
            // MOF extent does not exist -> create it
            mof = (ModelPackage)repository.createExtent(MOF_INSTANCE);
        }
        if (uml == null)
        {
            // UML extent does not exist -> create it (note that in case one want's to instantiate
            // a metamodel other than MOF, they need to provide the second parameter of the createExtent
            // method which indicates the metamodel package that should be instantiated)
            uml = (UmlPackage)repository.createExtent(
                    UML_INSTANCE,
                    getUmlPackage());
        }
    }

    /** 
     * Finds "UML" package -> this is the topmost package of UML metamodel - that's the
     * package that needs to be instantiated in order to create a UML extent
     */
    private static MofPackage getUmlPackage()
        throws Exception
    {
        // get the MOF extent containing definition of UML metamodel
        ModelPackage umlMetamodel = (ModelPackage)repository.getExtent(UML_META_MODEL);
        if (umlMetamodel == null)
        {
            // it is not present -> create it
            umlMetamodel = (ModelPackage)repository.createExtent(UML_META_MODEL);
        }

        // find package named "UML" in this extent
        MofPackage result = getUmlPackage(umlMetamodel);
        if (result == null)
        {
            // it cannot be found -> UML metamodel is not loaded -> load it from XMI
            final String umlMetamodelPath = "/M2_DiagramInterchangeModel.xml";
            final URL metamodelUrl = UmlPackage.class.getResource(umlMetamodelPath);
            if (metamodelUrl == null)
            {
                throw new RuntimeException("The UML metamodel could not be loaded from '" + umlMetamodelPath + "'");
            }
            reader.read(
                metamodelUrl.toString(),
                umlMetamodel);

            // try to find the "UML" package again
            result = getUmlPackage(umlMetamodel);
        }
        return result;
    }

    /** Finds "UML" package in a given extent
     * @param umlMetamodel MOF extent that should be searched for "UML" package.
     */
    private static MofPackage getUmlPackage(final ModelPackage umlMetamodel)
    {
        // iterate through all instances of package
        for (final Iterator iterator = umlMetamodel.getMofPackage().refAllOfClass().iterator(); iterator.hasNext();)
        {
            MofPackage pkg = (MofPackage)iterator.next();

            // is the package topmost and is it named "UML"?
            if (pkg.getContainer() == null && "UML".equals(pkg.getName()))
            {
                // yes -> return it
                return pkg;
            }
        }

        // a topmost package named "UML" could not be found
        return null;
    }
}