/*
 * Created on 1 juin 2004
 *
 */
package org.andromda.transformers.atl.engine;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import org.atl.engine.vm.nativelib.ASMModel;

/**
 * Provides the handling of models by ATL.
 * Currently supports EMF and MDR.
 * 
 * @author Frédéric Jouault
 * @author Chad Brandon
 */
public abstract class ATLModelHandler
{
    public final static String AMH_MDR = "MDR";
    public final static String AMH_EMF = "EMF";
    private static Map modelHandlerInstances = new HashMap();

    /**
     * Gets the instance of the model handler dependant upon the respository
     * name given.
     * @param repository the name of the respository for which to retrieve the model hander.
     * @return the ATLModelHandler instance or null if one could not be found.
     */
    public static ATLModelHandler getInstance(final String repository)
    {
        ATLModelHandler handler = (ATLModelHandler)modelHandlerInstances.get(repository);
        if (handler == null)
        {
            if (AMH_MDR.equals(repository))
            {
                handler = new ATLMDRModelHandler();
                modelHandlerInstances.put(AMH_MDR, handler);
            }
            else if (AMH_EMF.equals(repository))
            {
                handler = new ATLEMFModelHandler();
                modelHandlerInstances.put(AMH_EMF, handler);
            }
        }
        return handler;
    }

    /**
     * Writes the <code>model</code> to the given
     * <code>uri</code>.
     * 
     * @param model the model to write
     * @param uri the URI to which the model will be written.
     */
    public abstract void writeModel(
        final ASMModel model,
        final String uri);

    /**
     * Gets the ATL metamodel.
     * 
     * @return the ATL metamodel
     */
    public abstract ASMModel getATL();

    /**
     * Gets the MOF metamodel.
     * 
     * @return the MOF metamodel.
     */
    public abstract ASMModel getMOF();

    /**
     * Loads an existing model.
     * 
     * @param name the name of the model.
     * @param metamodel the metamodel of the model.
     * @param inputStream the input stream containing the actual model being loaded.
     * @return the loaded model.
     */
    public abstract ASMModel loadModel(
        final String name,
        final ASMModel metamodel,
        final InputStream inputStream);

    /**
     * Creates a new model.
     * 
     * @param name the name of the model.
     * @param metamodel the metamodel of the model to create.
     * @return the new model instance.
     */
    public abstract ASMModel newModel(
        final String name,
        final ASMModel metamodel);

    public abstract ASMModel getBuiltInMetaModel(final String name);
}