package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * Used for writing resources for the framework. Also keeps histories of
 * previous resources generated so that we can avoid regenerating if the
 * generated resources are current.
 * 
 * @author Chad Brandon
 */
public class ResourceWriter
{
    /**
     * The shared instance
     */
    private static final ResourceWriter instance = new ResourceWriter();

    /**
     * Gets the shared ResourceWriter instance. Normally you'll want to retrieve
     * the instance through this method.
     * 
     * @return the shared instance.
     */
    public static final ResourceWriter instance()
    {
        return instance;
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param file the file to which to write.
     * @param namespace the current namespace for which this resource is being
     *        written.
     * @throws IOException
     */
    public void writeStringToFile(final String string, final File file, final String namespace) throws IOException
    {
        final String methodName = "ResourceWriter.writeStringToFile";
        ExceptionUtils.checkNull(methodName, "file", file);
        this.writeStringToFile(string, file.toString(), namespace, true);
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation the location of the file which to write.
     */
    public void writeStringToFile(final String string, final String fileLocation) throws IOException
    {
        this.writeStringToFile(string, fileLocation, true);
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation the location of the file which to write.
     * @param recordHistory whether or not the history of the file should be
     *        recorded.
     */
    private void writeStringToFile(final String string, final String fileLocation, final boolean recordHistory) throws IOException
    {
        this.writeStringToFile(string, fileLocation, null, recordHistory);
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation the location of the file which to write.
     * @param namespace the current namespace for which this resource is being
     *        written.
     * @throws IOException
     */
    public void writeStringToFile(final String string, final String fileLocation, final String namespace) throws IOException
    {
        this.writeStringToFile(string, fileLocation, namespace, true);
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation the location of the file which to write.
     * @param namespace the current namespace for which this resource is being
     *        written.
     * @param recordHistory whether or not the history of this file should be
     *        recorded.
     * @throws IOException
     */
    private final void writeStringToFile(String string, final String fileLocation, final String namespace, final boolean recordHistory)
            throws IOException
    {
        final String methodName = "ResourceWriter.writeStringToFile";
        if (string == null)
        {
            string = "";
        }
        ExceptionUtils.checkEmpty(methodName, "fileLocation", fileLocation);
        final File file = new File(fileLocation);
        final File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        string = Merger.instance().getMergedString(string, namespace);
        FileOutputStream stream = new FileOutputStream(file);
        byte[] output;
        if (StringUtils.isNotBlank(this.encoding))
        {
            output = string.getBytes(this.encoding);
        }
        else
        {
            output = string.getBytes();
        }
        stream.write(output);
        stream.flush();
        stream.close();
        stream = null;
        if (recordHistory)
        {
            this.recordHistory(file);
        }
    }

    /**
     * Writes the URL contents to a file specified by the fileLocation argument.
     *
     * @param url the URL to read
     * @param fileLocation the location which to write.
     */
    public void writeUrlToFile(final URL url, final String fileLocation) throws IOException
    {
        final String methodName = "ResourceWriter.writeUrlToFile";
        ExceptionUtils.checkNull(methodName, "url", url);
        ExceptionUtils.checkEmpty(methodName, "fileLocation", fileLocation);
        final File file = new File(fileLocation);
        final File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        FileOutputStream stream = new FileOutputStream(file);
        if (StringUtils.isNotBlank(this.encoding))
        {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(url.openStream(), this.encoding));
            for (int ctr = inputReader.read(); ctr != -1; ctr = inputReader.read())
            {
                stream.write(ctr);
            }
            inputReader.close();
            inputReader = null;
        }
        else
        {
            InputStream inputStream = url.openStream();
            for (int ctr = inputStream.read(); ctr != -1; ctr = inputStream.read())
            {
                stream.write(ctr);
            }
            inputStream.close();
            inputStream = null;
        }
        stream.flush();
        stream.close();
        stream = null;
        this.recordHistory(file);
    }
    
    /**
     * Stores the encoding to be used for output.
     */
    private String encoding = null;
    
    /**
     * Sets the encoding to which all output written from this class will be
     * written.
     * 
     * @param encoding the encoding type (UTF-8, ISO-8859-1, etc).
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    private StringBuffer history = new StringBuffer();

    /**
     * Resets the a history file, to write the history {@link #writeHistory()} must be called.
     */
    public void resetHistory(final URL modelUrl)
    {
        String modelFile = modelUrl.toString().replace('\\', '/');
        int lastSlash = modelFile.lastIndexOf('/');
        if (lastSlash != -1)
        {
            modelFile = modelFile.substring(lastSlash + 1, modelFile.length());
        }
        this.modelFile = modelFile;
        this.history = new StringBuffer();
    }

    private String modelFile = null;

    /**
     * Stores the count of the resources written over this instance's history.
     */
    private long writtenCount = 0;

    /**
     * Gets the number of currently written resources over the course of this instance's history.
     *
     * @return the number of written resources.
     */
    public long getWrittenCount()
    {
        return this.writtenCount;
    }

    /**
     * The location to which history is written.
     */
    private static final String HISTORY_LOCATION = Constants.TEMPORARY_DIRECTORY + "history/";

    /**
     * Stores the file history.
     */
    private final String getHistoryStorage()
    {
        return HISTORY_LOCATION + this.modelFile;
    }

    /**
     * Writes the output history to disk.
     *
     * @throws IOException
     */
    public void writeHistory() throws IOException
    {
        writeStringToFile(history.toString(), getHistoryStorage(), false);
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation
     * @param overwrite if true, replaces the file (if it exists, otherwise),
     *        adds to the contents of the file.
     */
    private final void recordHistory(File file)
    {
        if (this.history != null)
        {
            this.history.append(file + ",");
        }
        this.writtenCount++;
    }

    /**
     * Checks to see if the history is before the given <code>time</code>.
     * 
     * @param time the time in milliseconds to check against.
     * @return true/false
     */
    public boolean isHistoryBefore(long time)
    {
        boolean before = true;
        try
        {
            final File historyFile = new File(getHistoryStorage());
            if (historyFile.exists() && historyFile.lastModified() >= time)
            {
                final String history = ResourceUtils.getContents(new File(getHistoryStorage()).toURL());
                final String[] files = history.split(",");
                long lastModified = 0;
                for (int ctr = 0; ctr < files.length; ctr++)
                {
                    final String fileString = StringUtils.trimToEmpty(files[ctr]);
                    if (StringUtils.isNotEmpty(fileString))
                    {
                        File file = new File(fileString);
                        // if we find one file that doesn't exist then
                        // before is automatically false
                        if (!file.exists())
                        {
                            lastModified = 0;
                            break;
                        }
                        if (file.lastModified() > lastModified)
                        {
                            lastModified = file.lastModified();
                        }
                    }
                }
                before = time > lastModified;
            }
        }
        catch (IOException ex)
        {
            before = true;
        }
        return before;
    }
}