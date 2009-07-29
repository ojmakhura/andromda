package org.andromda.core.common;

import java.io.File;

/**
 * Interface for all PostProcessor
 * @author Plushnikov Michail
 */
public interface PostProcessor {

    boolean acceptFile(File pFile); 

    String postProcess(String pSource);
}
