package org.andromda.ant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @todo: document this class
 * @todo: the way templates are read and written can be more generic, without hardcoding their names
 */
public class AndromdaAntRunner
{
    /**
     * @todo: document this constructor
     */
    public AndromdaAntRunner()
    {
    }

    public static void main(String[] args)
    {
        AndromdaAntRunner runner = new AndromdaAntRunner();
        runner.run();
    }

    /**
     * @todo: document this method
     */
    private Map prompt()
    {
        final Map propertiesMap = new HashMap();

        String inputValue = null;
        while (null == (inputValue=promptForInput("application id")));
        propertiesMap.put("applicationId", inputValue.replaceAll("[\\s]*",""));

        inputValue = null;
        while (null == (inputValue=promptForInput("application name")));
        propertiesMap.put("applicationName", inputValue.replaceAll("[\\s]*",""));

        inputValue = null;
        while (null == (inputValue=promptForInput("application version")));
        propertiesMap.put("applicationVersion", inputValue.replaceAll("[\\s]*",""));

        inputValue = null;
        while (null == (inputValue=promptForInput("persistence type [ejb,hibernate]"))
                || (!"hibernate".equals(inputValue) && !"ejb".equals(inputValue)));
        propertiesMap.put("persistenceType", inputValue);

        return Collections.unmodifiableMap(propertiesMap);
    }

    /**
     * @todo: document this method
     */
    private String promptForInput(String property)
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter the "+property+": ");
        String inputString = null;
        try
        {
            inputString = in.readLine();
        } catch (IOException e)
        {
            inputString = null;
        }

        return (inputString == null || inputString.trim().length()==0)
            ? null
            : inputString;
    }
    /**
     * @todo: document this method
     */
    private void run()
    {
        try
        {
            Properties properties = new Properties();
            properties.put("resource.loader", "class");
            properties.put("class.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            Velocity.init(properties);

            VelocityContext context = new VelocityContext(prompt());
            VelocityContext emptyContext = new VelocityContext();

            Map templatesMap = listTemplates(context);

            File parentDirectory = new File((String) context.get("applicationName"));

            Collection templates = templatesMap.entrySet();
            for (Iterator iterator = templates.iterator(); iterator.hasNext();)
            {
                Map.Entry entry = (Map.Entry) iterator.next();
                Template template = Velocity.getTemplate((String) entry.getKey());
                File target = new File(parentDirectory, (String) entry.getValue());

                target.getParentFile().mkdirs();

                BufferedWriter writer = writer = new BufferedWriter(new FileWriter(target));

                if (entry.getKey().toString().endsWith(".vsl"))
                {
                    template.merge(context, writer);
                }
                else
                {
                    // no substitution for these files
                    template.merge(emptyContext, writer);
                }
                writer.flush();
                writer.close();

                System.out.println("Processed: "+target.getAbsolutePath());
            }
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * @todo: document this method
     * @todo: templates should be picked up automatically
     * @todo: target paths should be determined automatically
     */
    private Map listTemplates(VelocityContext context)
    {
        final Map templateMap = new HashMap();

        final String appName = (String)context.get("applicationName");

        templateMap.put("templates/j2ee-app/readme.txt.vsl", "readme.txt");
        templateMap.put("templates/j2ee-app/build.xml.vsl", "build.xml");
        templateMap.put("templates/j2ee-app/build.properties.vsl", "build.properties");

        templateMap.put("templates/j2ee-app/web/build.xml.vsl", "web/build.xml");
        templateMap.put("templates/j2ee-app/web/build.properties.vsl", "web/build.properties");

        templateMap.put("templates/j2ee-app/mda/build.xml.vsl", "mda/build.xml");
        templateMap.put("templates/j2ee-app/mda/build.properties.vsl", "mda/build.properties");
        templateMap.put("templates/j2ee-app/mda/src/uml/model.xmi", "mda/src/uml/"+appName+".xmi");
        templateMap.put("templates/j2ee-app/mda/src/mappings/HypersonicSqlMappings.xml", "mda/src/mappings/HypersonicSqlMappings.xml");
        templateMap.put("templates/j2ee-app/mda/src/mappings/JavaMappings.xml", "mda/src/mappings/JavaMappings.xml");
        templateMap.put("templates/j2ee-app/mda/src/mappings/JdbcMappings.xml", "mda/src/mappings/JdbcMappings.xml");
        templateMap.put("templates/j2ee-app/mda/src/mappings/MySQLMappings.xml", "mda/src/mappings/MySQLMappings.xml");
        templateMap.put("templates/j2ee-app/mda/src/mappings/Oracle9iMappings.xml", "mda/src/mappings/Oracle9iMappings.xml");

        templateMap.put("templates/j2ee-app/app/build.xml.vsl", "app/build.xml");
        templateMap.put("templates/j2ee-app/app/build.properties.vsl", "app/build.properties");
        templateMap.put("templates/j2ee-app/app/src/META-INF/application.xml.vsl", "app/src/META-INF/application.xml");
        templateMap.put("templates/j2ee-app/app/src/META-INF/jboss-app.xml.vsl", "app/src/META-INF/jboss-app.xml");

        templateMap.put("templates/j2ee-app/common/build.xml.vsl", "common/build.xml");
        templateMap.put("templates/j2ee-app/common/build.properties.vsl", "common/build.properties");

        if ("hibernate".equals(context.get("persistenceType")))
        {
            templateMap.put("templates/j2ee-app/hibernate/build.xml.vsl", "hibernate/build.xml");
            templateMap.put("templates/j2ee-app/hibernate/build.properties.vsl", "hibernate/build.properties");

            templateMap.put("templates/j2ee-app/hibernate/db/conf/initializeSchema.cmd", "hibernate/db/conf/initializeSchema.cmd");
            templateMap.put("templates/j2ee-app/hibernate/db/conf/initializeSchema.sh", "hibernate/db/conf/initializeSchema.sh");
            templateMap.put("templates/j2ee-app/hibernate/db/conf/reInitializeSchema.cmd", "hibernate/db/conf/reInitializeSchema.cmd");
            templateMap.put("templates/j2ee-app/hibernate/db/conf/reInitializeSchema.sh", "hibernate/db/conf/reInitializeSchema.sh");
            templateMap.put("templates/j2ee-app/hibernate/db/conf/removeSchema.cmd", "hibernate/db/conf/removeSchema.cmd");
            templateMap.put("templates/j2ee-app/hibernate/db/conf/removeSchema.sh", "hibernate/db/conf/removeSchema.sh");

            templateMap.put("templates/j2ee-app/hibernate/sar/build.xml.vsl", "hibernate/sar/build.xml");
            templateMap.put("templates/j2ee-app/hibernate/sar/build.properties.vsl", "hibernate/sar/build.properties");
            templateMap.put("templates/j2ee-app/hibernate/ejb/build.xml.vsl", "hibernate/ejb/build.xml");
            templateMap.put("templates/j2ee-app/hibernate/ejb/build.properties.vsl", "hibernate/ejb/build.properties");
            templateMap.put("templates/j2ee-app/hibernate/ejb/src/META-INF/MANIFEST.MF.vsl", "hibernate/ejb/src/META-INF/MANIFEST.MF");
        } else
        {
            templateMap.put("templates/j2ee-app/ejb/build.xml.vsl", "ejb/build.xml");
            templateMap.put("templates/j2ee-app/ejb/build.properties.vsl", "ejb/build.properties");
            templateMap.put("templates/j2ee-app/ejb/src/META-INF/MANIFEST.MF.vsl", "ejb/src/META-INF/MANIFEST.MF");
        }

        return templateMap;
    }

/*
    private String getTargetName(String rootPath, File file)
    {
        String filePath = file.getPath();

        if (filePath.endsWith(".vsl"))
            filePath = filePath.substring(0, filePath.length() - 4);

        return (filePath.startsWith(rootPath))
                ? filePath.substring(rootPath.length())
                : filePath;
    }
*/
}

