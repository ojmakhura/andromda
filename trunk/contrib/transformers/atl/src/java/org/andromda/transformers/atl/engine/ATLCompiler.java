package org.andromda.transformers.atl.engine;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.transformers.atl.ATLTransformerUtils;
import org.atl.engine.repositories.emf4atl.ASMEMFModelElement;
import org.atl.engine.vm.nativelib.ASMEnumLiteral;
import org.atl.engine.vm.nativelib.ASMModel;
import org.eclipse.emf.ecore.EObject;


/**
 * Provides the compilation of the ATL source files.  Compiles
 * them into the compiled <em>ASM</em> files.
 *
 * @author Frédéric Jouault
 * @author Chad Brandon
 */
public class ATLCompiler
{
    /**
     * The shared instance
     */
    private static ATLCompiler instance = null;

    public static ATLCompiler instance()
    {
        if (instance == null)
        {
            instance = new ATLCompiler();
        }
        return instance;
    }

    private ASMModel problem;

    private Object[] getProblems(
        ASMModel problems,
        EObject[] prev)
    {
        Object[] ret = new Object[2];
        EObject[] pbsa = null;
        Collection pbs = problems.getElementsByType("Problem");

        int nbErrors = 0;
        if (pbs != null)
        {
            pbsa = new EObject[pbs.size() + prev.length];
            System.arraycopy(
                prev,
                0,
                pbsa,
                0,
                prev.length);
            int k = prev.length;
            for (Iterator i = pbs.iterator(); i.hasNext();)
            {
                ASMEMFModelElement ame = ((ASMEMFModelElement)i.next());
                pbsa[k++] = ame.getObject();
                if ("error".equals(((ASMEnumLiteral)ame.get(
                            null,
                            "severity")).getName()))
                {
                    nbErrors++;
                }
            }
        }

        ret[0] = new Integer(nbErrors);
        ret[1] = pbsa;

        return ret;
    }

    /**
     * Compiles the <em>atlSource</em> and outputs the ASM file to the given
     * <code>out</code> location.
     *
     * @param atlSource The InputStream to get atl source from.
     * @param out The File to which the ATL compiled program will be saved.
     * @return an array of EObject instances containing any problems (or null if no
     *         problems exist).
     */
    public EObject[] compile(
        final InputStream atlSource,
        final File outputLocation)
    {
        final String methodName = "AtlCompiler.compile";
        ExceptionUtils.checkNull(
            methodName,
            "atlSource",
            atlSource);
        ExceptionUtils.checkNull(
            methodName,
            "outputLocation",
            outputLocation);
        ResourceUtils.makeDirectories(outputLocation.toString());
        EObject[] compilationResult = null;

        // - parsing + Semantic Analysis
        final ASMModel[] parsed = ATLParser.instance().parseToModelWithProblems(atlSource);
        final ASMModel atlModel = parsed[0];
        final ASMModel problems = parsed[1];

        Object[] problemsResult = this.getProblems(
                problems,
                new EObject[0]);
        int numberOfErrors = ((Integer)problemsResult[0]).intValue();
        compilationResult = (EObject[])problemsResult[1];

        final ATLModelHandler handler = ATLModelHandler.getInstance(ATLModelHandler.AMH_EMF);
        if (numberOfErrors == 0)
        {
            final Map models = new HashMap();
            models.put(
                "MOF",
                handler.getMOF());
            models.put(
                "ATL",
                atlModel.getMetamodel());
            models.put(
                "IN",
                atlModel);
            models.put(
                "Problem",
                problem);
            models.put(
                "OUT",
                problems);

            final Map parameters = new HashMap();

            final Map libraries = Collections.EMPTY_MAP;

            ATLRunner.instance().run(
                ATLTransformerUtils.getResource("ATL-WFR.asm"),
                libraries,
                models,
                parameters);

            problemsResult = getProblems(
                    problems,
                    compilationResult);
            numberOfErrors = ((Integer)problemsResult[0]).intValue();
            compilationResult = (EObject[])problemsResult[1];
        }

        // - generating code
        final Map models = new HashMap();
        models.put(
            "MOF",
            handler.getMOF());
        models.put(
            "ATL",
            handler.getATL());
        models.put(
            "IN",
            atlModel);

        final Map parameters = new HashMap();
        parameters.put(
            "debug",
            "false");
        parameters.put(
            "WriteTo",
            outputLocation.toString());

        final Map libraries = new HashMap();
        libraries.put(
            "typeencoding",
            ATLTransformerUtils.getResource("typeencoding.asm"));
        libraries.put(
            "strings",
            ATLTransformerUtils.getResource("strings.asm"));

        ATLRunner.instance().run(
            ATLTransformerUtils.getResource("ATLToASMCompiler.asm"),
            libraries,
            models,
            parameters);
        final Collection compilationProblems = problems.getElementsByType("Problem");
        if (compilationProblems != null)
        {
            compilationResult = new EObject[compilationProblems.size()];
            int ctr = 0;
            for (final Iterator iterator = compilationProblems.iterator(); iterator.hasNext();)
            {
                compilationResult[ctr++] = ((ASMEMFModelElement)iterator.next()).getObject();
            }
        }
        return compilationResult;
    }
}