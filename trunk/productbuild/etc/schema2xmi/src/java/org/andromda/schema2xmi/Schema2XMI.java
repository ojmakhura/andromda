package org.andromda.schema2xmi;

import org.andromda.core.common.AndroMDALogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Converts a database schema to an XMI document.
 * 
 * @author Chad Brandon
 */
public class Schema2XMI
{
    private static Options options;

    /**
     * The command to display help
     */
    private static final String HELP = "h";

    /**
     * The command line argument to specify the input model file.
     */
    private static final String INPUT = "im";
    
    /**
     * The command line argument to specify the JDBC driver class
     */
    private static final String DRIVER = "dc";
    
    /**
     * The command line argument to specify the schema user.
     */
    private static final String USER = "un";
    
    /**
     * The command line argument to specify the schema user password.
     */
    private static final String PASSWORD = "pw";
    
    /**
     * The command line argument to specify the connection URL.
     */
    private static final String CONNECTION_URL = "cu";

    /**
     * The command line argument to specify the transformed output file.
     */
    private static final String OUTPUT_NAME = "on";
    
    /**
     * The command line argument specifying the URI to the 
     * type mappings file.
     */
    private static final String TYPE_MAPPINGS_URI = "tmu";
    
    /**
     * The command line argument specifying the package to which 
     * the model element will be generated.
     */
    private static final String PACKAGE = "pkg";
    
    /**
     * The command line argument specifying the tables names to 
     * match on
     */
    private static final String TABLE_NAME_PATTERN = "tnp";
    
    /**
     * Configure the CLI options.
     */
    static
    {
        try
        {
            AndroMDALogger.configure();
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }

        options = new Options();

        Option option = new Option(HELP, false, "Display help information");
        option.setLongOpt("help");
        options.addOption(option);

        option = new Option(INPUT, false, "Model input file to start with");
        option.setLongOpt("input");
        options.addOption(option);
        
        option = new Option(DRIVER, true, "JDBC driver class to use");
        option.setLongOpt("driver");
        options.addOption(option);        
        
        option = new Option(CONNECTION_URL, true, "JDBC connection URL");
        option.setLongOpt("connectionUrl");
        options.addOption(option);  
        
        option = new Option(USER, true, "JDBC schema user");
        option.setLongOpt("user");
        options.addOption(option);  
        
        option = new Option(PASSWORD, true, "JDBC schema user password");
        option.setLongOpt("password");
        options.addOption(option);  
                
        option = new Option(TYPE_MAPPINGS_URI, true, "The type mappings URI (i.e. file:${basedir}/DataypeMappings.xml)");
        option.setLongOpt("typeMappingsUri");
        options.addOption(option); 
        
        option = new Option(TABLE_NAME_PATTERN, false, "The table name pattern of tables to process");
        option.setLongOpt("tableNamePattern");
        options.addOption(option); 
        
        option = new Option(PACKAGE, false, "The package to output classifiers");
        option.setLongOpt("package");
        options.addOption(option); 

        option = new Option(
            OUTPUT_NAME,
            true,
            "Set output name to which the result of the transformation will be written");
        option.setLongOpt("output");
        options.addOption(option);
    }

    /**
     * Display usage information based upon current command-line option
     * configuration.
     */
    public static void displayHelp()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
            Schema2XMI.class.getName() + " [options] ...]]",
            "\nOptions:",
            options,
            "\n");

    }

    /**
     * Parse a string-array of command-line arguments.
     * <p>
     * This will parse the arguments against the configured odm2flatfile
     * command-line options, and return a <code>CommandLine</code> object from
     * which we can retrieve the command like options.
     * </p>
     * 
     * @see <a href="http://jakarta.apache.org/commons/cli/">CLI </a>
     * @param args The command-line arguments to parse.
     * @return The <code>CommandLine</code> result.
     * @throws ParseException If an error occurs while parsing the command-line
     *         options.
     */
    public CommandLine parseCommands(String[] args) throws ParseException
    {
        CommandLineParser parser = new PosixParser();
        return parser.parse(options, args);
    }

    public static void main(String args[])
    {
        Schema2XMI schema2Xmi = new Schema2XMI();
        try
        {
            CommandLine commandLine = schema2Xmi.parseCommands(args);
            if (commandLine.hasOption(HELP) || !commandLine.hasOption(INPUT)
                || !commandLine.hasOption(OUTPUT_NAME))
            {
                Schema2XMI.displayHelp();
            }
            else if (commandLine.hasOption(INPUT)
                && (commandLine.hasOption(OUTPUT_NAME)))
            {
                String inputModel = commandLine.getOptionValue(INPUT);
                SchemaTransformer transformer = 
                    new SchemaTransformer(
                        commandLine.getOptionValue(DRIVER),
                        commandLine.getOptionValue(CONNECTION_URL),
                        commandLine.getOptionValue(USER),
                        commandLine.getOptionValue(PASSWORD));
                
                // set the extra options
                transformer.setTypeMappings(commandLine.getOptionValue(TYPE_MAPPINGS_URI));
                transformer.setPackageName(commandLine.getOptionValue(PACKAGE));
                transformer.setTableNamePattern(commandLine.getOptionValue(TABLE_NAME_PATTERN));
                
                String outputLocation = commandLine.getOptionValue(OUTPUT_NAME);
                transformer.transform(inputModel, outputLocation);
            }
        }
        catch (Throwable th)
        {
            th = getRootCause(th);
            th.printStackTrace();
        }
    }

    private static Throwable getRootCause(Throwable th)
    {
        Throwable cause = th;
        if (cause.getCause() != null)
        {
            cause = cause.getCause();
            cause = getRootCause(cause);
        }
        return cause;
    }
}
