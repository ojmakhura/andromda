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
    boolean acceptFile(File pFile); 

    /**
     * @param pSource
     * @return postProcess String
     */
    String postProcess(String pSource);
}
