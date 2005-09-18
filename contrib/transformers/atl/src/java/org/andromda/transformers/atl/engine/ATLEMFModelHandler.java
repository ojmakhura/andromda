package org.andromda.transformers.atl.engine;

import java.io.InputStream;

import java.net.URL;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.andromda.transformers.atl.ATLTransformerUtils;
import org.andromda.transformers.atl.TransformerException;
import org.atl.engine.repositories.emf4atl.ASMEMFModel;
import org.atl.engine.vm.nativelib.ASMModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;


/**
 * The {@link ATLModelHandler} implementation for EMF models.
 *
 * @author Frédéric Jouault
 * @author Chad Brandon
 */
public class ATLEMFModelHandler
    extends ATLModelHandler
{
    private ASMEMFModel mofMetamodel;
    private ASMEMFModel atlMetamodel;

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#writeModel(org.atl.engine.vm.nativelib.ASMModel, java.lang.String)
     */
    public void writeModel(
        final ASMModel model,
        final String path)
    {
        this.saveModel(
            model,
            URI.createURI(path));
    }

    private void saveModel(
        final ASMModel model,
        URI uri)
    {
        final Resource resource = ((ASMEMFModel)model).getExtent();
        resource.setURI(uri);
        try
        {
            resource.save(Collections.EMPTY_MAP);
        }
        catch (final Throwable exception)
        {
            throw new TransformerException(exception);
        }
    }

    protected ATLEMFModelHandler()
    {
        final URL atlUrl = ATLTransformerUtils.getResource("ATL-0.2.ecore");
        this.mofMetamodel = org.atl.engine.repositories.emf4atl.ASMEMFModel.createMOF(null);
        try
        {
            this.atlMetamodel = ASMEMFModel.loadASMEMFModel("ATL", mofMetamodel, atlUrl, null);
        }
        catch (final Throwable exception)
        {
            throw new TransformerException(exception);
        }
    }

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#getMOF()
     */
    public ASMModel getMOF()
    {
        return this.mofMetamodel;
    }

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#getATL()
     */
    public ASMModel getATL()
    {
        return this.atlMetamodel;
    }

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#loadModel(java.lang.String, org.atl.engine.vm.nativelib.ASMModel, java.io.InputStream, java.lang.String[])
     */
    public ASMModel loadModel(
        final String name,
        final ASMModel metamodel,
        final InputStream inputStream,
        final String[] moduleSearchPaths)
    {
        ASMModel result = null;

        try
        {
            result = ASMEMFModel.loadASMEMFModel(name, (ASMEMFModel)metamodel, inputStream, null);
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }
        return result;
    }

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#newModel(java.lang.String, org.atl.engine.vm.nativelib.ASMModel)
     */
    public ASMModel newModel(
        final String name,
        final ASMModel metamodel)
    {
        ASMModel result = null;

        try
        {
            result = ASMEMFModel.newASMEMFModel(name, (ASMEMFModel)metamodel, null);
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }
        return result;
    }

    private final Map builtInMetamodel = new HashMap();

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#getBuiltInMetaModel(java.lang.String)
     */
    public ASMModel getBuiltInMetaModel(String name)
    {
        ASMModel result = (ASMModel)builtInMetamodel.get(name);
        if (result == null)
        {
            final URL metaModelUrl = ATLTransformerUtils.getResource(name + ".ecore");
            try
            {
                final InputStream stream = metaModelUrl.openStream();
                result = this.loadModel(name, mofMetamodel, stream, null);
                stream.close();
            }
            catch (final Throwable throwable)
            {
                throw new TransformerException(throwable);
            }
            builtInMetamodel.put(name, result);
        }
        return result;
    }
}