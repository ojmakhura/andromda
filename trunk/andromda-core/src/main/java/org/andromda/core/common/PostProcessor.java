package org.andromda.core.common;

import java.io.File;

/**
 * Interface for all PostProcessor
 * @author Plushnikov Michail
 */
public interface PostProcessor
{
    /**
     * Determines if this file should be postprocessed in current postprocessor
     * @param pFile file for postprocessing
     * @return true if postprocessing should be done, false sonst 
     */
    public boolean acceptFile(File pFile);

    /**
     * Postprocess the source
     * @param pSource the Source for postprocessing
     * @param pPreviousData the Source of existing file, may be null or empty
     * @return postprocessed source
     * @throws Exception on errors occurred
     */
    public String postProcess(String pSource, String pPreviousData) throws Exception;
}
