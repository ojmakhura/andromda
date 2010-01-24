package org.andromda.core.common;

import java.io.File;

/**
 * Interface for all PostProcessor
 * @author Plushnikov Michail
 */
public interface PostProcessor
{
    /**
     * @param pFile
     * @return file
     */
    public boolean acceptFile(File pFile);

    /**
     * Postprocess the source
     * @param pSource the Source for postprocessing
     * @return postprocessed source
     * @throws Exception on errors occurred
     */
    public String postProcess(String pSource) throws Exception;
}
