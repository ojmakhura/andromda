package org.andromda.transformers.atl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.Constants;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.transformers.atl.engine.ATLCompiler;
import org.andromda.transformers.atl.engine.ATLRunner;
import org.andromda.transformers.atl.engine.ATLModelHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.atl.engine.vm.nativelib.ASMModel;


/**
 * Facilitates MOF transformations using ATL (ATLAS Model Language).
 * model transformer
 *
 * @author Chad Brandon
 */
public class ATLTransformer
{   
    /**
     * Performs the transformation.
     *
     * @param transformationPath the path to the actual ATL transformation source file.
     * @param libraries any ATL libraries used within the ATL transformationPath source file.
     * @param metamodels the metamodels used during the transformation process.
     * @param sourceModels the source models that will be transformed.
     * @param targetModels the models that are the target or result of the transformation.
     */
    public void transform(
        final String transformationPath,
        final Library[] libraries,
        final Model[] metamodels,
        final Model[] sourceModels,
        final Model[] targetModels)
    {
        final String methodName = "ATLTransformer.transform";
        ExceptionUtils.checkEmpty(methodName, "transformationPath", transformationPath);
        if (metamodels == null || metamodels.length == 0)
        {
            throw new TransformerException(
                methodName + " - 'metamodels' can not be null or empty, you must have at least one defined");
        }
        if (sourceModels == null || sourceModels.length == 0)
        {
            throw new TransformerException(
                methodName + " - 'sourceModels' can not be null or empty, you must have at least one defined");
        }
        if (targetModels == null || targetModels.length == 0)
        {
            throw new TransformerException(
                methodName + " - 'targetModels' can not be null or empty, you must have at least one defined");
        }
        
        // - set the context class loader
        Thread.currentThread().setContextClassLoader(ATLTransformer.class.getClassLoader());
        
        try
        {
            // - create the asm URL from the ASM file
            final URL asmUrl = this.getASMFile(transformationPath).toURL();

            // - load the external libraries used by the transformation
            final Map libraryMap = new HashMap();
            if (libraries != null && libraries.length > 0)
            {
                for (int ctr = 0; ctr < libraries.length; ctr++)
                {
                    final Library library = libraries[ctr];
                    final String name = library.getName();
                    final URL libraryUrl = this.getASMFile(library.getPath()).toURL();
                    libraryMap.put(name, libraryUrl);
                }
            }

            // - stores the loaded models
            final Map models = this.loadSourceModels(metamodels, sourceModels);
            final Map loadedTargetModels = this.loadTargetModels(metamodels, targetModels, models);
            models.putAll(loadedTargetModels);

            final ATLRunner runner = ATLRunner.instance();
            runner.run(
                asmUrl,
                libraryMap,
                models,
                new HashMap());

            // - now we write any resulting target models
            final int targetModelNumber = targetModels.length;
            for (int ctr = 0; ctr < targetModelNumber; ctr++)
            {
                final Model model = targetModels[ctr];
                final String modelName = model.getName();
                
                final ASMModel targetModel = (ASMModel)loadedTargetModels.get(modelName);
                final ATLModelHandler handler = ATLModelHandler.getInstance(model.getRepository());
                final String targetModelOutputPath = new URL(model.getPath()).getFile();
                AndroMDALogger.info("Output model: '" + targetModelOutputPath + "'");
                handler.writeModel(targetModel, targetModelOutputPath);
            }
        }
        catch (final Throwable throwable)
        {
            throwable.printStackTrace();
            logger.error(throwable);
            throw new TransformerException(throwable);
        }
    }

    /**
     * Loads all "source" models as input streams keyed by the name of the
     * model into <code>models</code> a Map.
     *
     * @param models the Map in which to load the models.
     * @param metamodels the metamodels.
     * @param sourceModels the models that are the source for this transformation.
     * @return the loaded models as a Map keyed by name.
     * @throws Exception if any error occurs during the loading process.
     */
    private Map loadSourceModels(
        final Model[] metamodels,
        final Model[] sourceModels)
        throws Exception
    {
        final Map models = new HashMap();
        final int inputModelNumber = sourceModels.length;
        for (int ctr = 0; ctr < inputModelNumber; ctr++)
        {
            final Model model = sourceModels[ctr];
            if (model != null)
            {
                final String metamodelName = model.getMetamodel();
                final Model metamodel = this.getModelByName(metamodels, metamodelName);
                final String modelName = model.getName();
                final ATLModelHandler handler = ATLModelHandler.getInstance(model.getRepository());
                final ASMModel mofMetamodel = handler.getMOF();
                mofMetamodel.setIsTarget(false);

                // - check to see if we've already loaded the meta model once before
                ASMModel inputMetaModel = (ASMModel)models.get(metamodelName);
                if (inputMetaModel == null)
                {
                    InputStream metamodelStream = this.getInputStream(metamodel.getPath());
                    inputMetaModel = handler.loadModel(metamodelName, mofMetamodel, metamodelStream);
                    metamodelStream.close();
                    metamodelStream = null;
                    models.put(metamodelName, inputMetaModel);
                }
                inputMetaModel.setIsTarget(false);
                InputStream modelStream = this.getInputStream(model.getPath());
                final ASMModel inputModel = handler.loadModel(modelName, inputMetaModel, modelStream);
                modelStream.close();
                modelStream = null;
                inputModel.setIsTarget(false);
                models.put(modelName, inputModel);
            }
        }
        return models;
    }

    /**
     * Loads all "target" models as input streams keyed by the name of the
     * model into the given <code>models</code> Map.
     *
     * @param metamodels the metamodels.
     * @param targetModels the model(s) that are the target of the transformation
     * @param loadedSourceModels the source models that have already been loaded 
     *        during the call to {@link #loadSourceModels(Model[], Model[])}.  This is
     *        just passed in so that we don't need to reload the same models again (like metamodels
     *        that might be the same) if they've already been loaded during the execution of
     *        {@link #loadSourceModels(Model[], Model[])}.
     * @return the loaded models as a Map keyed by name.
     * @throws Exception if any error occurs during the loading process.
     */
    private Map loadTargetModels(
        final Model[] metamodels,
        final Model[] targetModels,
        final Map loadedSourceModels)
        throws Exception
    {
        final Map models = new HashMap();
        if (targetModels != null && targetModels.length > 0)
        {
            final int targetModelNumber = targetModels.length;
            for (int ctr = 0; ctr < targetModelNumber; ctr++)
            {
                final Model model = targetModels[ctr];
                final String modelName = model.getName();
                final String metamodelName = model.getMetamodel();
                final Model metamodel = this.getModelByName(metamodels, metamodelName);
                final ATLModelHandler handler = ATLModelHandler.getInstance(model.getRepository());
                final ASMModel mofMetamodel = handler.getMOF();
                mofMetamodel.setIsTarget(false);
                ASMModel outputMetamodel = (ASMModel)loadedSourceModels.get(metamodelName);
                if (outputMetamodel == null)
                {
                    outputMetamodel = (ASMModel)models.get(metamodelName);
                }
                if (outputMetamodel == null)
                {
                    InputStream metamodelStream = this.getInputStream(metamodel.getPath());
                    outputMetamodel = handler.loadModel(metamodelName, mofMetamodel, metamodelStream);
                    metamodelStream.close();
                    metamodelStream = null;
                    models.put(metamodelName, outputMetamodel);
                }
                outputMetamodel.setIsTarget(false);
                final ASMModel outputModel = handler.newModel(modelName, outputMetamodel);
                outputModel.setIsTarget(true);
                models.put(modelName, outputModel);
            }
        }
        return models;
    }

    /**
     * Attempts to find the model by the given name, returns the model
     * if found, otherwise returns null.
     *
     * @param models the models to search.
     * @param name the name of the model.
     * @return the model or null if one doesn't exist with the given name.
     */
    private Model getModelByName(
        final Model[] models,
        final String name)
    {
        final Model model =
            (Model)CollectionUtils.find(
                Arrays.asList(models),
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        return object != null && StringUtils.trimToEmpty(((Model)object).getName()).equals(name);
                    }
                });
        if (model == null)
        {
            throw new TransformerException("No model found with name '" + name + "'");
        }
        return model;
    }

    private InputStream getInputStream(final String path)
        throws Exception
    {
        URL url = new URL(path);
        if (url == null)
        {
            throw new TransformerException("Could not load model from '" + path + "'");
        }
        return url.openStream();
    }

    /**
     * The ASM file suffix (ASM files are the files that are the result
     * of the compilation of an ATL file).
     */
    private static final String ASM_SUFFIX = "\\.asm";

    /**
     * The ATL source file suffix.
     */
    private static final String ATL_SUFFIX = "\\.atl";

    /**
     * The ATL compilation directory (the directory to which the resulting ASM files are written).
     */
    private static final String COMPILATION_DIRECTORY = Constants.TEMPORARY_DIRECTORY + "atl-compile";

    /**
     * This method retrieves the compiled ASM File corresponding to the 
     * ATL File. If the ATL file has yet to be compiled (or the ATL
     * source has changed since last compile), compilation 
     * will occur first (which means the ASM file will be written).
     *
     * @param atlSourceUri the URI to the ATL file
     * @return ASM File corresponding to the ATL File
     */
    private File getASMFile(final String atlSourceUri)
        throws Exception
    {
        final URL atlUrl = new URL(StringUtils.trimToEmpty(atlSourceUri).replace('\\', '/'));
        if (atlUrl == null)
        {
            throw new TransformerException("Could not retrieve ATL source from '" + atlSourceUri + "'");
        }
        final String atlUrlAsString = atlUrl.toString();
        final String name = atlUrlAsString.substring(
                atlUrlAsString.lastIndexOf('/'),
                atlUrlAsString.length());
        final File asmFile = new File(COMPILATION_DIRECTORY + '/' + name.replaceAll(ATL_SUFFIX, ASM_SUFFIX));
        long atlSourceModifiedTime = ResourceUtils.getLastModifiedTime(atlUrl);
        long compiledAsmModifiedTime = ResourceUtils.getLastModifiedTime(asmFile.toURL());;
        if (atlSourceModifiedTime > compiledAsmModifiedTime)
        {
            final InputStream stream = atlUrl.openStream();
            ATLCompiler.instance().compile(stream, asmFile);
            stream.close();
        }
        return asmFile;
    }
    
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ATLTransformer.class);
}