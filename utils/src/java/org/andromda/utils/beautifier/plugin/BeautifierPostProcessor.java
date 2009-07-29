package org.andromda.utils.beautifier.plugin;

import org.andromda.core.common.PostProcessor;
import org.andromda.utils.beautifier.core.JavaImportBeautifierImpl;

import java.io.File;

/**
 * BeautifierPostProcessor is PostProcessor to beautify imports in java files.
 * @author Plushnikov Michail
 */
public class BeautifierPostProcessor implements PostProcessor {
    /**
     * Standard Constructor
     */
    public BeautifierPostProcessor() {

    }

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
     * @param pSource the Source
     * @return postprocessed source
     */
    public String postProcess(String pSource) {
        JavaImportBeautifierImpl beautifier = new JavaImportBeautifierImpl();
        beautifier.setFormat(false);
        beautifier.setOrganizeImports(true);

        return beautifier.beautify(pSource);
    }
}
