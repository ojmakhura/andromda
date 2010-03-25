package org.andromda.core.common;

import de.plushnikov.doctorjim.ImportProcessor;
import de.plushnikov.doctorjim.javaparser.ParseException;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Implementation of PostProcessor interface to organize imports in java files
 * @author Plushnikov Michail
 */
public class ImportBeautifierPostProcessorImpl implements PostProcessor
{
    /** The logger instance. */
    private static final Logger LOGGER = Logger.getLogger(ImportBeautifierPostProcessorImpl.class);

    /**
     * Determines if this file should be postprocessed in current postprocessor
     * @param pFile file for postprocessing
     * @return true if postprocessing should be done, false sonst
     */
    public boolean acceptFile(File pFile)
    {
        return null!=pFile && pFile.getName().endsWith(".java");
    }

    /**
     * Postprocess the source
     * @param pSource the Source for postprocessing
     * @param pPreviousData the Source of existing file, may be null or empty
     * @return postprocessed source
     * @throws Exception on errors occurred
     */
    public String postProcess(String pSource, String pPreviousData) throws Exception
    {
        try
        {
            return new ImportProcessor().organizeImports(pSource);
        }
        catch (ParseException ex)
        {
            LOGGER.debug("Error PostProcessing ", ex);
            throw ex;
        }
    }
}
