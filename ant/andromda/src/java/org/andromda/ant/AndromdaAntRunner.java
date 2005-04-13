package org.andromda.ant;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @todo: document this class
 */
public class AndromdaAntRunner
{
    private final static String J2EE_RESOURCES_ZIP = "j2ee-app.zip";
    private final static String TEMPLATE_SUFFIX = ".vsl";
    private final static String TEMP_PATH = System.getProperty("user.dir");
    private final static String TEMP_DIR = "~andromda.ant.tmp";

    private VelocityContext templateContext = null;
    private File parentDirectory = null;

    /**
     * @todo: document this constructor
     */
    public AndromdaAntRunner() throws Exception
    {
        Properties properties = new Properties();
        properties.put("resource.loader", "class");
        properties.put("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        Velocity.init(properties);
        templateContext = new VelocityContext(prompt());

        parentDirectory = new File(String.valueOf(templateContext.get("applicationName")));
    }

    public static void main(String[] args)
    {
        try
        {
            AndromdaAntRunner runner = new AndromdaAntRunner();
            runner.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @todo: document this method
     */
    private Map prompt()
    {
        final Map propertiesMap = new HashMap();

        String inputValue = null;
        while (null == (inputValue = promptForInput("developer names"))) ;
        propertiesMap.put("author", inputValue.replaceAll("[\\s]*", ""));

        inputValue = null;
        while (null == (inputValue = promptForInput("application id"))) ;
        propertiesMap.put("applicationId", inputValue.replaceAll("[\\s]*", ""));

        inputValue = null;
        while (null == (inputValue = promptForInput("application name"))) ;
        propertiesMap.put("applicationName", inputValue.replaceAll("[\\s]*", ""));

        inputValue = null;
        while (null == (inputValue = promptForInput("application version"))) ;
        propertiesMap.put("applicationVersion", inputValue.replaceAll("[\\s]*", ""));

        inputValue = null;
        while (null == (inputValue = promptForInput("persistence type [ejb,hibernate]")) ||
                (!"hibernate".equals(inputValue) && !"ejb".equals(inputValue)))
            ;
        propertiesMap.put("persistenceType", inputValue);

/*        inputValue = null;
        while (null == (inputValue = promptForInput("webservices [y,n]"))
                || (!"y".equals(inputValue) && !"n".equals(inputValue)))
            ;
        propertiesMap.put("webServices", inputValue);
*/
        propertiesMap.put("webServices", "n");

        return Collections.unmodifiableMap(propertiesMap);
    }

    /**
     * @todo: document this method
     */
    private String promptForInput(String property)
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter the " + property + ": ");
        String inputString = null;
        try
        {
            inputString = in.readLine();
        }
        catch (IOException e)
        {
            inputString = null;
        }

        return (inputString == null || inputString.trim().length() == 0) ? null : inputString;
    }

    /**
     * @todo: document this method
     */
    private void run() throws Exception
    {
        // unzip j2ee template-bundle & get a collection of template files
        File[] templates = unzipTemplateBundles();

        // loop over templates
        for (int i = 0; i < templates.length; i++)
        {
            File template = templates[i];
            processVelocity(template);
        }

        removeTempFiles();

        showReadMe();
    }

    private File[] unzipTemplateBundles() throws Exception
    {
        URL url = Thread.currentThread().getContextClassLoader().getResource(J2EE_RESOURCES_ZIP);
        File file = File.createTempFile("j2ee", null);
        file.deleteOnExit();
        FileUtils.copyURLToFile(url, file);

        File targetDir = new File(TEMP_PATH, TEMP_DIR);
        targetDir.mkdirs();

        unzip(new ZipFile(file), targetDir);

        IOFileFilter fileFilter = TrueFileFilter.INSTANCE;
        IOFileFilter dirFilter = TrueFileFilter.INSTANCE;

        final Collection templateFiles = FileUtils.listFiles(targetDir, fileFilter, dirFilter);
        return (File[])templateFiles.toArray(new File[templateFiles.size()]);
    }

    private void processVelocity(File templateFile) throws Exception
    {
        System.out.println("Processing template: " + templateFile.getAbsolutePath());

        String resourceName = templateFile.getAbsolutePath();
        resourceName = resourceName.substring(resourceName.indexOf(TEMP_DIR));

        String targetFileName = null;

        if (resourceName.endsWith(TEMPLATE_SUFFIX))
        {
            targetFileName =
                    resourceName.substring(TEMP_DIR.length() + 1, resourceName.length() - TEMPLATE_SUFFIX.length());

            File target = new File(parentDirectory, targetFileName);
            target.getParentFile().mkdirs();

            final Template template = Velocity.getTemplate(resourceName);

            BufferedWriter writer = new BufferedWriter(new FileWriter(target));
            template.merge(templateContext, writer);
            writer.close();
        }
        else
        {
            targetFileName = resourceName.substring(TEMP_DIR.length() + 1, resourceName.length());

            File target = new File(parentDirectory, targetFileName);
            target.getParentFile().mkdirs();

            InputStream instream = new FileInputStream(templateFile);
            Writer writer = new BufferedWriter(new FileWriter(target));
            CopyUtils.copy(instream, writer);
            instream.close();
            writer.close();
        }

    }

    private void removeTempFiles() throws Exception
    {
        FileUtils.deleteDirectory(new File(TEMP_PATH, TEMP_DIR));
    }

    private void unzip(ZipFile zipFile, File targetDir)
    {
        final int BUFFER = 2048;
        try
        {
            BufferedOutputStream dest = null;
            BufferedInputStream is = null;
            ZipEntry entry;
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements())
            {
                entry = (ZipEntry)e.nextElement();

                if (entry.isDirectory() == false)
                {
                    is = new BufferedInputStream(zipFile.getInputStream(entry));
                    int count;
                    byte data[] = new byte[BUFFER];
                    File outFile = new File(targetDir, entry.getName());
                    outFile.getParentFile().mkdirs();
                    outFile.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(outFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = is.read(data, 0, BUFFER)) != -1)
                    {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showReadMe() throws Exception
    {
        File readmeFile = new File(parentDirectory, "readme.txt");
        System.out.println("\n-- Information on how to build can be found here: " + readmeFile.getAbsolutePath());
    }
}

