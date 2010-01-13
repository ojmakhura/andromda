package org.andromda.core.common;

import de.plushnikov.doctorjim.ImportProcessor;
import de.plushnikov.doctorjim.javaparser.ParseException;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * @author: Overheat
 * Date: 13.01.2010
 * Time: 21:14:36
 */
public class ImportBeautifierPostProcessorImpl implements PostProcessor
{
    /** The logger instance. */
    private static final Logger LOGGER = Logger.getLogger(ImportBeautifierPostProcessorImpl.class);

    /**
     * @param pFile
     * @return file
     */
    public boolean acceptFile(File pFile)
    {
        return pFile.getName().endsWith(".java");
    }

    /**
     * @param pSource
     * @return postProcess String
     */
    public String postProcess(String pSource)
    {
        try
        {
            return new ImportProcessor().organizeImports(pSource);
        }
        catch (ParseException ex)
        {
            LOGGER.debug("Error PostProcessing ", ex);
            return null;
        }
    }
}
