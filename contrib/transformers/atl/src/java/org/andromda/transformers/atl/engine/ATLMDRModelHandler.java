package org.andromda.transformers.atl.engine;

import java.net.URL;

import org.andromda.transformers.atl.ATLTransformerUtils;
import org.andromda.transformers.atl.TransformerException;
import org.atl.engine.repositories.mdr4atl.ASMMDRModel;
import org.atl.engine.vm.nativelib.ASMModel;

/**
 * The {@link ATLModelHandler} implementation for MDR models.
 * 
 * @author Frédéric Jouault
 * @author Chad Brandon
 */
public class ATLMDRModelHandler
    extends ATLModelHandler
{

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#writeModel(org.atl.engine.vm.nativelib.ASMModel, java.lang.String)
     */
    public void writeModel(
        final ASMModel model,
        final String path)
    {
        try
        {
            ((ASMMDRModel)model).save(path);
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }
    }
    
    /**
     * Stores the ATL metamodel.
     */
    private ASMMDRModel atlMetamodel;

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#getATL()
     */
    public ASMModel getATL()
    {
        return this.atlMetamodel;
    }
    
    /**
     * Stores the MOF metamodel.
     */
    private ASMMDRModel mofMetamodel;

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#getMOF()
     */
    public ASMModel getMOF()
    {
        return this.mofMetamodel;
    }

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#loadModel(java.lang.String, org.atl.engine.vm.nativelib.ASMModel, java.lang.String, java.lang.String[])
     */
    public ASMModel loadModel(
        final String name,
        final ASMModel metamodel,
        final String uri,
        final String[] moduleSearchPaths)
    {
        try
        {
            return ASMMDRModel.loadASMMDRModel(name, (ASMMDRModel)metamodel, uri, null, moduleSearchPaths);
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }
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
            result = ASMMDRModel.newASMMDRModel(name, (ASMMDRModel)metamodel, null);
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }
        return result;
    }

    protected ATLMDRModelHandler()
    {
        final URL atlUrl = ATLTransformerUtils.getResource("ATL-0.2.xmi");
        this.mofMetamodel = ASMMDRModel.createMOF(null);
        try
        {
            this.atlMetamodel = ASMMDRModel.loadASMMDRModel("ATL", mofMetamodel, atlUrl, null, null);
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }
    }

    /**
     * @see org.andromda.transformers.atl.engine.ATLModelHandler#getBuiltInMetaModel(java.lang.String)
     */
    public ASMModel getBuiltInMetaModel(String name)
    {
        return null;
    }
}