package org.andromda.schema2xmi;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.XmlObjectFactory;
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
     * The command line argument to specify the XMI version that will be
     * producted.
     */
    private static final String XMI_VERSION = "x";

    /**
     * The command line argument to specify the input model file.
     */
    private static final String INPUT_MODEL = "i";

    /**
     * The command line argument to specify the JDBC driver class
     */
    private static final String DRIVER = "d";

    /**
     * The command line argument to specify the schema user.
     */
    private static final String USER = "u";

    /**
     * The command line argument to specify the schema user password.
     */
    private static final String PASSWORD = "p";

    /**
     * The command line argument to specify the connection URL.
     */
    private static final String CONNECTION_URL = "c";

    /**
     * The command line argument to specify the transformed output file.
     */
    private static final String OUTPUT_MODEL = "o";

    /**
     * The command line argument specifying the URI to the type mappings file.
     */
    private static final String MAPPINGS = "m";

    /**
     * The command line argument specifying the package to which the model
     * element will be generated.
     */
    private static final String PACKAGE = "P";

    /**
     * The command line argument specifying the name of the schema where the
     * table resides.
     */
    private static final String SCHEMA = "s";

    /**
     * The command line argument specifying the tables names to match on
     */
    private static final String TABLE_PATTERN = "t";

    /**
     * The command line argument specifying the attribute names pattern to match on.
     */
    private static final String COLUMN_PATTERN = "a";

    /**
     * The command line argument specifying the class stereotype name.
     */
    private static final String CLASS_STEREOTYPES = "C";

    /**
     * The command line argument specifying the identifier stereotype name.
     */
    private static final String IDENTIFIER_STEREOTYPES = "I";

    /**
     * The command line argument specifiying the name of the tagged value to use
     * for tagged column names.
     */
    private static final String TABLE_TAGGEDVALUE = "V";

    /**
     * The command line argument specifiying the name of the tagged value to use
     * for tagged column names.
     */
    private static final String COLUMN_TAGGEDVALUE = "v";

    /**
     * The command line argument specifiying additional tagged values to add to
     * each column attribute.
     */
    private static final String ATTRIBUTE_TAGGEDVALUES = "A";
    
    /**
     * Configure the CLI options.
     */
    static
    {
        try
        {
            AndroMDALogger.initialize();

            // turn off validation because of the incorrect parsers
            // in the JDK
            XmlObjectFactory.setDefaultValidating(false);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }

        options = new Options();

        Option option = new Option(HELP, false, "Display help information");
        option.setLongOpt("help");
        options.addOption(option);

        option = new Option(XMI_VERSION, true, "Specifies the XMI version that will be produced");
        option.setLongOpt("xmi");
        options.addOption(option);

        option = new Option(INPUT_MODEL, true, "Input model file (to which model elements will be added)");
        option.setLongOpt("input");
        options.addOption(option);

        option = new Option(DRIVER, true, "JDBC driver class");
        option.setLongOpt("driver");
        options.addOption(option);

        option = new Option(CONNECTION_URL, true, "JDBC connection URL");
        option.setLongOpt("connectionUrl");
        options.addOption(option);

        option = new Option(USER, true, "Schema user name");
        option.setLongOpt("user");
        options.addOption(option);

        option = new Option(PASSWORD, true, "Schema user password");
        option.setLongOpt("password");
        options.addOption(option);

        option = new Option(MAPPINGS, true, "The type mappings URI (i.e. file:${basedir}/DataypeMappings.xml)");
        option.setLongOpt("mappings");
        options.addOption(option);

        option = new Option(SCHEMA, true, "The name of the schema where the tables can be found");
        option.setLongOpt("schema");
        options.addOption(option);

        option = new Option(TABLE_PATTERN, true, "The table name pattern of tables to process (regular expression)");
        option.setLongOpt("tablePattern");
        options.addOption(option);

        option = new Option(COLUMN_PATTERN, true, "The column name pattern of columns to process (regular expression)");
        option.setLongOpt("columnPattern");
        options.addOption(option);

        option = new Option(PACKAGE, true, "The package to output classifiers");
        option.setLongOpt("package");
        options.addOption(option);

        option =
            new Option(CLASS_STEREOTYPES, true, "Comma separated list of stereotype names to add to the created class");
        option.setLongOpt("classStereotypes");
        options.addOption(option);

        option =
            new Option(
                IDENTIFIER_STEREOTYPES, true, "Comma separated list of stereotype names to add to any class identifiers");
        option.setLongOpt("identifierStereotypes");
        options.addOption(option);

        option = new Option(TABLE_TAGGEDVALUE, true, "The tagged value to use for storing the table name");
        option.setLongOpt("tableTaggedValue");
        options.addOption(option);

        option = new Option(COLUMN_TAGGEDVALUE, true, "The tagged value to use for storing the column name");
        option.setLongOpt("columnTaggedValue");
        options.addOption(option);

        option =
            new Option(OUTPUT_MODEL, true, "Output location to which the result of the transformation will be written");
        option.setLongOpt("output");
        options.addOption(option);
        
        option = new Option(ATTRIBUTE_TAGGEDVALUES, true, "The tagged value(s) added to each attribute generated. Comma separated list (i.e. @tag1=val1,@tag2=val2)");
        option.setLongOpt("attributeTaggedValues");
        options.addOption(option);
        
    }

    /**
     * Display usage information based upon current command-line option
     * configuration.
     */
    public static void displayHelp()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(Schema2XMI.class.getName() + " [options] ...]]", "\nOptions:", options, "\n");
    }

    /**
     * Parse a string-array of command-line arguments.
     * <p>
     * This will parse the arguments against the configured schema2xmi
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
    public CommandLine parseCommands(String[] args)
        throws ParseException
    {
        CommandLineParser parser = new PosixParser();
        return parser.parse(options, args);
    }

    public static void main(String[] args)
    {
        Schema2XMI schema2Xmi = new Schema2XMI();
        try
        {
            CommandLine commandLine = schema2Xmi.parseCommands(args);
            if (
                commandLine.hasOption(HELP) ||
                !(
                    commandLine.hasOption(OUTPUT_MODEL) && commandLine.hasOption(DRIVER) &&
                    commandLine.hasOption(CONNECTION_URL) && commandLine.hasOption(USER) &&
                    commandLine.hasOption(PASSWORD)
                ))
            {
                Schema2XMI.displayHelp();
            }
            else
            {
                String inputModel = commandLine.getOptionValue(INPUT_MODEL);
                SchemaTransformer transformer =
                    new SchemaTransformer(
                        commandLine.getOptionValue(DRIVER),
                        commandLine.getOptionValue(CONNECTION_URL),
                        commandLine.getOptionValue(USER),
                        commandLine.getOptionValue(PASSWORD));

                // set the extra options
                transformer.setXmiVersion(commandLine.getOptionValue(XMI_VERSION));
                transformer.setTypeMappings(commandLine.getOptionValue(MAPPINGS));
                transformer.setPackageName(commandLine.getOptionValue(PACKAGE));
                transformer.setSchema(commandLine.getOptionValue(SCHEMA));
                transformer.setTableNamePattern(commandLine.getOptionValue(TABLE_PATTERN));
                transformer.setColumnNamePattern(commandLine.getOptionValue(COLUMN_PATTERN));
                transformer.setClassStereotypes(commandLine.getOptionValue(CLASS_STEREOTYPES));
                transformer.setIdentifierStereotypes(commandLine.getOptionValue(IDENTIFIER_STEREOTYPES));
                transformer.setTableTaggedValue(commandLine.getOptionValue(TABLE_TAGGEDVALUE));
                transformer.setColumnTaggedValue(commandLine.getOptionValue(COLUMN_TAGGEDVALUE));
                transformer.setAttributeTaggedValues(commandLine.getOptionValue(ATTRIBUTE_TAGGEDVALUES));

                String outputLocation = commandLine.getOptionValue(OUTPUT_MODEL);
                transformer.transform(inputModel, outputLocation);
            }
        }
        catch (Throwable throwable)
        {
            throwable = getRootCause(throwable);
            throwable.printStackTrace();
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