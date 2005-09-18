package org.andromda.transformers.atl.engine;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.transformers.atl.TransformerException;
import org.apache.log4j.Logger;
import org.atl.engine.injectors.ebnf.EBNFInjector2;
import org.atl.engine.repositories.emf4atl.ASMEMFModel;
import org.atl.engine.repositories.emf4atl.ASMEMFModelElement;
import org.atl.engine.repositories.mdr4atl.ASMMDRModel;
import org.atl.engine.vm.nativelib.ASMModel;
import org.eclipse.emf.ecore.EObject;


public class ATLParser
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ATLParser.class);
    
    /**
     * The shared instance.
     */
    private static ATLParser instance = null;
    private ATLModelHandler modelHandler;
    private ASMModel asmModel;
    
    private static final String PROBLEM = "Problem";
    private static final String UNIT = "Unit";

    private ATLParser()
    {
        this.modelHandler = ATLModelHandler.getInstance(ATLModelHandler.AMH_EMF);
        asmModel = modelHandler.getBuiltInMetaModel(PROBLEM);
    }

    /**
     * Gets the shared instance of this ATLParser
     * @return
     */
    public static ATLParser instance()
    {
        if (instance == null)
        {
            instance = new ATLParser();
        }
        return instance;
    }

    public ASMModel parseToModel(final InputStream inputStream)
    {
        return parseToModelWithProblems(inputStream)[0];
    }
    
    /**
     * The name of the temporary model.
     */
    private static final String TEMPORARY_MODEL = "TEMP";
    
    /**
     * The name of the ATL model.
     */
    private static final String ATL_MODEL = "ATL";
    
    /**
     * The problem model.
     */
    private static final String PROBLEM_MODEL = "PROBLEM";

    public ASMModel[] parseToModelWithProblems(final InputStream inputStream)
    {
        ASMModel[] result = new ASMModel[2];
        ASMModel atlMetamodel = modelHandler.getATL();
        try
        {
            if (atlMetamodel instanceof ASMEMFModel)
            {
                result[0] = ASMEMFModel.newASMEMFModel(TEMPORARY_MODEL, (ASMEMFModel)atlMetamodel, null);
            }
            else
            {
                result[0] = ASMMDRModel.newASMMDRModel(TEMPORARY_MODEL, (ASMMDRModel)atlMetamodel, null);
            }
            result[1] = modelHandler.newModel(PROBLEM_MODEL, asmModel);

            final EBNFInjector2 injector = new EBNFInjector2();
            injector.performImportation(atlMetamodel, result[0], inputStream, ATL_MODEL, result[1]);

            /*// - Semantic Analysis
            final Map models = new HashMap();
            final String MOF_MODEL = "MOF";
            models.put(
                MOF_MODEL,
                modelHandler.getMOF());
            models.put(
                ATL_MODEL,
                modelHandler.getATL());
            models.put("IN", result[0]);

            final Map parameters = new HashMap();
            parameters.put("debug", "false");

            ATLRunner.instance().run(
                ATLTransformerUtils.getResource("ATLSemanticAnalyzer.asm"),
                models,
                parameters);
            */
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }

        return result;
    }

    public EObject parse(InputStream in)
    {
        return parseWithProblems(in)[0];
    }

    /**
     *
     * @param in InputStream to parse ATL code from.
     * @return An array of EObject, the first one being an ATL!Unit and
     *             the following ones Problem!Problem.
     */
    public EObject[] parseWithProblems(final InputStream inputStream)
    {
        EObject resultUnit = null;
        final ASMModel[] parsed = this.parseToModelWithProblems(inputStream);
        final ASMModel atlmodel = parsed[0];
        final ASMModel problems = parsed[1];
        Collection probs = null;
        if (atlmodel instanceof ASMEMFModel)
        {
            Collection modules = atlmodel.getElementsByType(UNIT);
            if (!modules.isEmpty())
            {
                resultUnit = ((ASMEMFModelElement)modules.iterator().next()).getObject();
            }
            probs = problems.getElementsByType(PROBLEM);
        }
        else
        {
            final Object object = atlmodel.getElementsByType(UNIT);
            logger.error(object);
        }

        EObject[] result = null;
        if (probs != null)
        {
            result = new EObject[1 + probs.size()];
            int ctr = 1;
            for (Iterator iterator = probs.iterator(); iterator.hasNext();)
            {
                result[ctr++] = ((ASMEMFModelElement)iterator.next()).getObject();
            }
        }
        else
        {
            result = new EObject[1];
        }
        result[0] = resultUnit;
        return result;
    }
}