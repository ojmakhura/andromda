package org.andromda.core.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static ResourceWriter instance = null;

    /**
     * Gets the shared ResourceWriter instance. Normally you'll want to retrieve
     * the instance through this method.
     * 
     * @return the shared instance.
     */
    public static ResourceWriter instance()
    {
        if (instance == null)
        {
            instance = new ResourceWriter();
        }
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
    public void writeStringToFile(String string, File file, String namespace)
        throws IOException
    {
        final String methodName = "ResourceWriter.writeStringToFile";
        ExceptionUtils.checkNull(methodName, "file", file);
        writeStringToFile(string, file.toString(), namespace);
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation the location of the file which to write.
     */
    public void writeStringToFile(String string, String fileLocation)
        throws IOException
    {
        this.writeStringToFile(string, fileLocation, null);
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
    public void writeStringToFile(
        String string,
        String fileLocation,
        String namespace) throws IOException
    {
        final String methodName = "ResourceWriter.writeStringToFile";
        if (string == null)
        {
            string = "";
        }
        ExceptionUtils.checkEmpty(methodName, "fileLocation", fileLocation);
        File file = new File(fileLocation);
        File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        string = Merger.instance().getMergedString(string, namespace);
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(string.getBytes());
        stream.flush();
        stream.close();
        stream = null;
        recordHistory(file);
    }

    /**
     * Writes the URL contents to a file specified by the fileLocation argument.
     * 
     * @param url the URL to read
     * @param fileLocation the location which to write.
     */
    public void writeUrlToFile(URL url, String fileLocation) throws IOException
    {
        final String methodName = "ResourceWriter.writeUrlToFile";
        ExceptionUtils.checkNull(methodName, "url", url);
        ExceptionUtils.checkEmpty(methodName, "fileLocation", fileLocation);

        InputStream inputStream = url.openStream();

        File file = new File(fileLocation);
        File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        // make any directories that don't exist
        FileOutputStream stream = new FileOutputStream(file);
        for (int ctr = inputStream.read(); ctr != -1; ctr = inputStream.read())
        {
            stream.write(ctr);
        }
        stream.flush();
        stream.close();
        stream = null;
        recordHistory(file);
    }

    private StringBuffer history = new StringBuffer();

    /**
     * Resets the a history file, to write the history {@link #writeHistory()}
     * must be called.
     */
    public void resetHistory(URL modelUrl)
    {
        String modelFile = modelUrl.toString().replaceAll("\\\\", "/");
        int lastSlash = modelFile.lastIndexOf('/');
        if (lastSlash != -1)
        {
            modelFile = modelFile.substring(lastSlash + 1, modelFile.length());
        }
        this.setModelFile(modelFile);
        history = new StringBuffer();
    }

    private String modelFile = null;

    private void setModelFile(String modelFile)
    {
        this.modelFile = modelFile;
    }

    /**
     * The location to which history is written.
     */
    private final String HISTORY_LOCATION = ".andromda/history/";

    /**
     * Stores the file history.
     */
    private String getHistoryStorage()
    {
        String tmpDir = System.getProperty("java.io.tmpdir").replace('\\', '/');
        StringBuffer historyStorage = new StringBuffer(tmpDir);
        if (!historyStorage.toString().endsWith("/"))
        {
            historyStorage.append("/");
        }
        historyStorage.append(HISTORY_LOCATION);
        historyStorage.append(this.modelFile);
        return historyStorage.toString();
    }

    /**
     * Writes the output history to disk.
     * 
     * @throws IOException
     */
    public void writeHistory() throws IOException
    {
        writeStringToFile(history.toString(), getHistoryStorage());
    }

    /**
     * Writes the string to the file specified by the fileLocation argument.
     * 
     * @param string the string to write to the file
     * @param fileLocation
     * @param overwrite if true, replaces the file (if it exists, otherwise),
     *        adds to the contents of the file.
     */
    private void recordHistory(File file)
    {
        if (history != null)
        {
            history.append(file + ",");
        }
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
            File historyFile = new File(getHistoryStorage());
            if (historyFile.exists() && historyFile.lastModified() >= time)
            {
                String history = ResourceUtils.getContents(new File(
                    getHistoryStorage()).toURL());
                String[] files = history.split(",");
                long lastModified = 0;
                for (int ctr = 0; ctr < files.length; ctr++)
                {
                    String fileString = StringUtils.trimToEmpty(files[ctr]);
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