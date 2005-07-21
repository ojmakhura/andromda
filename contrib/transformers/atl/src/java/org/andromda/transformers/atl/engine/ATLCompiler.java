package org.andromda.transformers.atl.engine;

import java.io.File;
import java.io.InputStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.transformers.atl.ATLTransformerUtils;
import org.atl.engine.repositories.emf4atl.ASMEMFModelElement;
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
        ExceptionUtils.checkNull(methodName, "atlSource", atlSource);
        ExceptionUtils.checkNull(methodName, "outputLocation", outputLocation);
        ResourceUtils.makeDirectories(outputLocation.toString());
        EObject[] compilationResult = null;

        // Parsing + Semantic Analysis
        final ASMModel[] parsed = ATLParser.instance().parseToModelWithProblems(atlSource);
        final ASMModel atlModel = parsed[0];
        final ASMModel problems = parsed[1];

        // Generating code
        ATLModelHandler handler = ATLModelHandler.getInstance(ATLModelHandler.AMH_EMF);
        final Map models = new HashMap();
        models.put(
            "MOF",
            handler.getMOF());
        models.put(
            "ATL",
            handler.getATL());
        models.put("IN", atlModel);

        final Map parameters = new HashMap();
        parameters.put("debug", "false");
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