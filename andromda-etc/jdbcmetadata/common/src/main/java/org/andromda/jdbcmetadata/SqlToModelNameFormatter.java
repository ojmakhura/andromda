package org.andromda.jdbcmetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Provides formatting functions, when converting SQL names to model names.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class SqlToModelNameFormatter
{
    private static final Logger logger = Logger.getLogger(SqlToModelNameFormatter.class);
    // Directory location for Excel files
    private static String inputDirectory;
    // Excel file contains physical -> logical DB name columns
    private static String wordFile = "Logical and Physical Modeling Naming Standards.xlsx";
    private static String wordSheet = "Logical and Physical Model";
    // File contains suffix (last word part) name columns
    private static String suffixFile;
    private static String suffixSheet;
    // File Used by AppDevs to override the values given in the DBArchitect worksheet for Java/UML names
    private static String overrideFile = "Overrides.xlsx";
    private static String overrideSheet = "Overrides";
    // Contains physical -> logical DB name columns
    private static Map<String, String> wordMap = new HashMap<String, String>();
    // Contains suffix (last word part) name columns
    private static Map<String, String> suffixMap = new HashMap<String, String>();
    // Used by AppDevs to override the values given in the DBArchitect worksheet for Java/UML names
    private static Map<String, String> overrideMap = new HashMap<String, String>();

    /**
     * Converts a table name to an class name.
     *
     * @param name the name of the table.
     * @return the new class name.
     */
    public static String toClassName(String name)
    {
        return toCamelCase(name);
    }

    /**
     * Converts a column name to an attribute name.
     *
     * @param name the name of the column
     * @return the new attribute name.
     */
    public static String toAttributeName(String name)
    {
        return StringUtils.uncapitalize(toClassName(name));
    }

    /**
     * Turns a table name into a model element class name.
     *
     * @param name the table name.
     * @return the new class name.
     */
    public static String toCamelCase(String name)
    {
        if (StringUtils.isBlank(name)) return "";
        name = replaceMappedWords(name);
        StringBuilder buffer = new StringBuilder();
        // Still can't get it to split at token '-' even when escaped. Stupid regex.
        String[] tokens = name.split("_|\\s+");
        if (tokens != null && tokens.length > 0)
        {
            for (int ctr = 0; ctr < tokens.length; ctr++)
            {
                String token = replaceMappedTokens(tokens[ctr]);
                if (token.equals(tokens[ctr]))
                {
                    buffer.append(StringUtils.capitalize(tokens[ctr].toLowerCase()));
                }
                else
                {
                    buffer.append(StringUtils.capitalize(token));
                }
            }
        }
        else
        {
            buffer.append(StringUtils.capitalize(name.toLowerCase()));
        }
        // Remove invalid metacharacters from String result.
        return StringUtils.replaceChars(buffer.toString(), "([{\\^-=$!|]})?*+.", "");
    }

    /**
     * @param name String in which to replace the token value
     * @return name with replaced tokens
     */
    public static String replaceMappedWords(String name)
    {
        // Assumes wordMap has already been populated
        for (String key : wordMap.keySet())
        {
            String value = wordMap.get(key);
            if (key.indexOf('_') > 0 && name.contains(key))
            {
                name = name.replace(key, value);
                logger.debug("Replaced: " + name +
                    " words " + key + " value " + value);
            }
        }
        return name;
    }

    /**
     * @param name String in which to replace the token value
     * @return name with replaced tokens
     */
    public static String replaceMappedTokens(String name)
    {
        // Assumes wordMap has already been populated
        for (String key : wordMap.keySet())
        {
            String value = wordMap.get(key);
            if (name.equals(key))
            {
                name = name.replace(key, value);
                logger.debug("Replaced: " + name +
                    " token " + key + " value " + value);
            }
        }
        return name;
    }

    /**
     * Loads the wordMap from Logical and Physical Modeling Naming Standards.xlsx
     */
    public static void loadWordMap()
    {
        SqlToModelNameFormatter.loadMap(SqlToModelNameFormatter.wordMap,
            SqlToModelNameFormatter.inputDirectory + "\\" + SqlToModelNameFormatter.wordFile,
            SqlToModelNameFormatter.wordSheet, 1, 0);
        SqlToModelNameFormatter.loadMap(SqlToModelNameFormatter.overrideMap,
            SqlToModelNameFormatter.inputDirectory + "\\" + SqlToModelNameFormatter.overrideFile,
            SqlToModelNameFormatter.overrideSheet, 1, 0);
        /*SqlToModelNameFormatter.loadMap(SqlToModelNameFormatter.wordMap,
            "${basedir}\\Logical and Physical Modeling Naming Standards.xlsx",
            "Logical and Physical Model", 1, 0);
        SqlToModelNameFormatter.loadMap(SqlToModelNameFormatter.overrideMap,
            "${basedir}\\Overrides.xlsx",
            "Overrides", 1, 0);*/
        for (String key : SqlToModelNameFormatter.overrideMap.keySet())
        {
            SqlToModelNameFormatter.wordMap.put(key, SqlToModelNameFormatter.overrideMap.get(key));
        }
    }

    /**
     * Reads an excel file. Populates mapping list. Uses the shortest 'to' mapping
     * @param map The Map<String, String> to load
     * @param fileName the name of the Excel File
     * @param sheetName the sheet within the file containing the data
     * @param fromCol The column where the 'from' mapped value is located
     * @param toCol The column where the 'to' mapped value is located
     */
    public static void loadMap(Map<String, String> map,
        String fileName, String sheetName, int fromCol, int toCol)
    {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isBlank(fileName))
        {
            logger.warn("Map file name was blank");
        }
        else if (!new File(fileName).exists())
        {
            logger.warn(fileName + " file does not exist");
        }
        else
        {
            try {
                // TODO Allow FileInputStream from the classpath so xls resources can be bundled with jar
                //FileInputStream fileInputStream = new FileInputStream("${basedir}\\Logical and Physical Modeling Naming Standards.xlsx");
                FileInputStream fileInputStream = new FileInputStream(fileName);
                //Workbook workbook = new Workbook(fileInputStream);
                //Workbook[] wbs = new Workbook[] { new HSSFWorkbook(), new XSSFWorkbook(fileInputStream) };
                Workbook[] wbs = new Workbook[] { new XSSFWorkbook(fileInputStream) };
                for(int i=0; i<wbs.length; i++)
                {
                    Workbook workbook = wbs[i];
                    //Sheet worksheet = workbook.getSheet("Logical and Physical Model");
                    Sheet worksheet = workbook.getSheet(sheetName);
                    if (worksheet == null)
                    {
                        int sheetNumber = 0;
                        try
                        {
                            sheetNumber = Integer.parseInt(sheetName);
                        }
                        catch (NumberFormatException e)
                        {
                            // Ignore, just use sheet 0
                        }
                        // Just use the first sheet if sheet name not found
                        worksheet = workbook.getSheetAt(sheetNumber);
                    }
                    int lastRow = worksheet.getLastRowNum();
                    for (int rowNum = 1; rowNum <= lastRow; rowNum++)
                    {
                        Row row = worksheet.getRow(rowNum);
                        if (row != null)
                        {
                            Cell keyCell = row.getCell(fromCol);
                            // DB values are always uppercase
                            String key = keyCell.getStringCellValue().toUpperCase();
                            Cell valueCell = row.getCell(toCol);
                            String value = toCamelCase(valueCell.getStringCellValue());
                            /*if (value.indexOf(' ') > 0)
                            {
                                // Value is an acronym, use the uppercase abbreviation instead
                                value = key;
                            }*/

                            /*logger.debug("Sheet: " + workbook.getSheetName(0));
                            logger.debug("key: " + key);
                            logger.debug("value: " + value);*/
                            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
                            {
                                String oldValue = map.get(key);
                                // Add the new key/value, or replace the old value if this value is shorter
                                if (oldValue == null || oldValue.length() > value.length())
                                {
                                    map.put(key, value);
                                }
                            }
                            logger.debug(rowNum + " key=" + key + " value: " + value);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                logger.error("File Not Found: " + fileName, e);
            } catch (IOException e) {
                logger.error("File Read Error: " + fileName, e);
            }
        }
        logger.info("Loaded: " + map.size() +
            " lines from file " + fileName + ", Time=" + ((System.currentTimeMillis() - startTime) / 1000.0) + "s");
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        SqlToModelNameFormatter.loadWordMap();
        /*SqlToModelNameFormatter.loadMap(SqlToModelNameFormatter.wordMap,
            "${basedir}\\Logical and Physical Modeling Naming Standards.xlsx",
            "Logical and Physical Model", 1, 0);*/
    }

    /**
     * @return the inputDirectory
     */
    public static String getInputDirectory()
    {
        return SqlToModelNameFormatter.inputDirectory;
    }

    /**
     * @param inputDirectory the inputDirectory to set
     */
    public static void setInputDirectory(String inputDirectory)
    {
        SqlToModelNameFormatter.inputDirectory = inputDirectory;
    }

    /**
     * @return the wordFile
     */
    public static String getWordFile()
    {
        return SqlToModelNameFormatter.wordFile;
    }

    /**
     * @param wordFile the wordFile to set
     */
    public static void setWordFile(String wordFile)
    {
        SqlToModelNameFormatter.wordFile = wordFile;
    }

    /**
     * @return the wordSheet
     */
    public static String getWordSheet()
    {
        return SqlToModelNameFormatter.wordSheet;
    }

    /**
     * @param wordSheet the wordSheet to set
     */
    public static void setWordSheet(String wordSheet)
    {
        SqlToModelNameFormatter.wordSheet = wordSheet;
    }

    /**
     * @return the suffixFile
     */
    public static String getSuffixFile()
    {
        return SqlToModelNameFormatter.suffixFile;
    }

    /**
     * @param suffixFile the suffixFile to set
     */
    public static void setSuffixFile(String suffixFile)
    {
        SqlToModelNameFormatter.suffixFile = suffixFile;
    }

    /**
     * @return the suffixSheet
     */
    public static String getSuffixSheet()
    {
        return SqlToModelNameFormatter.suffixSheet;
    }

    /**
     * @param suffixSheet the suffixSheet to set
     */
    public static void setSuffixSheet(String suffixSheet)
    {
        SqlToModelNameFormatter.suffixSheet = suffixSheet;
    }

    /**
     * @return the overrideFile
     */
    public static String getOverrideFile()
    {
        return SqlToModelNameFormatter.overrideFile;
    }

    /**
     * @param overrideFile the overrideFile to set
     */
    public static void setOverrideFile(String overrideFile)
    {
        SqlToModelNameFormatter.overrideFile = overrideFile;
    }

    /**
     * @return the overrideSheet
     */
    public static String getOverrideSheet()
    {
        return SqlToModelNameFormatter.overrideSheet;
    }

    /**
     * @param overrideSheet the overrideSheet to set
     */
    public static void setOverrideSheet(String overrideSheet)
    {
        SqlToModelNameFormatter.overrideSheet = overrideSheet;
    }
}