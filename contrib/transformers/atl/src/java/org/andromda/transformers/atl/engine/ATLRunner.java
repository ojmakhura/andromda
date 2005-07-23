package org.andromda.transformers.atl.engine;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.transformers.atl.TransformerException;
import org.atl.engine.vm.ASM;
import org.atl.engine.vm.ASMExecEnv;
import org.atl.engine.vm.ASMInterpreter;
import org.atl.engine.vm.ASMXMLReader;
import org.atl.engine.vm.Debugger;
import org.atl.engine.vm.SimpleDebugger;
import org.atl.engine.vm.nativelib.ASMModel;
import org.atl.engine.vm.nativelib.ASMModule;


/**
 * Runs the ATL engine.
 * 
 * @author Chad Brandon
 */
public class ATLRunner
{
    /**
     * The shared ATL launcher instance.
     */
    private static ATLRunner instance = null;

    /**
     * Gets the AtlLauncher shared instance.
     *
     * @return the AtlLauncher shared instance.
     */
    public static ATLRunner instance()
    {
        if (instance == null)
        {
            instance = new ATLRunner();
        }
        return instance;
    }

    /**
     * Runs ATL given the <code>sourceUri</code> (the ATL souce file)
     * the models (containing the target and source models) and any
     * parameters to pass to the ATL engine.
     *
     * @param sourceUri the URI to the ATL source file.
     * @param models any models (this includes all source, target and meta models)
     *        keyed by name.
     * @param parameters any parameters to pass to the ATL engine.
     * @return
     */
    public Object run(
        final URL sourceUri,
        final Map models,
        final Map parameters)
    {
        return run(
            sourceUri,
            new HashMap(),
            models,
            parameters);
    }

    /**
     * Runs ATL given the <code>sourceUri</code> (the ATL souce file),
     * <code>models</code> (containing the target and source models), <code>libraries</code>,
     * and any <code>parameters</code> to pass to the ATL engine.
     *
     * @param sourceUri the URI to the ATL source file.
     * @param libraries any external ATL libraries used by the atl source.
     * @param models any models (this includes all source, target and meta models)
     *        keyed by name.
     * @param parameters any parameters to pass to the ATL engine.
     * @return
     */
    public Object run(
        final URL sourceUri,
        final Map libraries,
        final Map models,
        final Map parameters)
    {
        Object result = null;
        try
        {
            parameters.put("debug", "false");
            final ASM asm = new ASMXMLReader().read(new BufferedInputStream(sourceUri.openStream()));
            ASMModule asmModule = new ASMModule(asm);

            final Debugger debugger =
                new SimpleDebugger(false, new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), true);
            final ASMExecEnv environment = new ASMExecEnv(asmModule, debugger);

            for (final Iterator iterator = models.keySet().iterator(); iterator.hasNext();)
            {
                String modelName = (String)iterator.next();
                environment.addModel(modelName, (ASMModel)models.get(modelName));
            }
            environment.registerOperations(asm);

            for (final Iterator iterator = libraries.keySet().iterator(); iterator.hasNext();)
            {
                final String libraryName = (String)iterator.next();
                final URL url = (URL)libraries.get(libraryName);
                final InputStream stream = new BufferedInputStream(url.openStream());
                final ASM library = new ASMXMLReader().read(stream);
                stream.close();
                environment.registerOperations(library);
            }

            final ASMInterpreter interpreter = new ASMInterpreter(asm, asmModule, environment, parameters);
            result = interpreter.getReturnValue();
        }
        catch (final Throwable throwable)
        {
            throw new TransformerException(throwable);
        }
        return result;
    }
}