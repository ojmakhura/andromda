package org.hybridlabs.beautifier.plugin;

import org.andromda.core.common.PostProcessor;
import org.hybridlabs.beautifier.core.JavaImportBeautifierImpl;

import java.io.File;

/**
 * BeautifierPostProcessor is PostProcessor to beautify imports in java files.
 * @author Plushnikov Michail
 */
public class BeautifierPostProcessor implements PostProcessor {
    /**
     * Accept Files or not
     * @param pFile the File to postprocess
     * @return true if accept
     */
    public boolean acceptFile(File pFile) {
        return pFile.getName().endsWith(".java");
    }

    /**
     * Postprocess the source
     * @param pSource the Source for postprocessing
     * @return postprocessed source
     * @throws Exception on errors occurred
     */
    public String postProcess(String pSource) throws Exception {
        JavaImportBeautifierImpl beautifier = new JavaImportBeautifierImpl();
        beautifier.setFormat(false);
        beautifier.setOrganizeImports(true);

        return beautifier.beautify(pSource);
    }
}
