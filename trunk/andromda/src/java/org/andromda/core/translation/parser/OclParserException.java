package org.andromda.core.translation.parser;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * Retrieves information from the OCL parser exceptions in a more user friendly
 * format.
 */
public class OclParserException
    extends RuntimeException
{

    private StringBuffer detailMessage;
    private int errorLine;
    private int errorCol;

    /**
     * Constructs an instance of OclParserException.
     * 
     * @param message
     */
    public OclParserException(
        String message)
    {
        super();
        if (StringUtils.isNotEmpty(message))
        {
            extractErrorPosition(message);
        }
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage()
    {
        int position = 0;
        if (errorLine != -1)
        {
            String msg = "line: " + errorLine + " ";
            detailMessage.insert(
                0,
                msg);
            position = msg.length();
        }
        if (errorCol != -1)
        {
            String msg = "column: " + errorCol + " ";
            detailMessage.insert(
                position,
                msg);
            position = position + msg.length();
        }
        detailMessage.insert(
            position,
            "--> ");
        return detailMessage.toString();
    }

    /**
     * The line of the error.
     * 
     * @return int
     */
    public int getErrorLine()
    {
        return errorLine;
    }

    /**
     * The column of the error.
     * 
     * @return int
     */
    public int getErrorCol()
    {
        return errorCol;
    }

    /**
     * Extract error position from detail message, if possible. Assumes SableCC
     * detail message format: "[" <line>"," <col>"]" <error message>
     * 
     * <p>
     * Error line and column are stored in {@link #errorLine}and
     * {@link #errorCol}so that they can be retrieved using
     * {@link #getErrorLine}and {@link #getErrorCol}. The detail message
     * without the position information is stored in {@link #detailMessage}
     * </p>
     * 
     * @param sDetailMessage
     */
    private void extractErrorPosition(
        String sDetailMessage)
    {
        detailMessage = new StringBuffer();
        if (sDetailMessage.charAt(0) == '[')
        {
            // Positional data seems to be available
            StringTokenizer st = new StringTokenizer(
                sDetailMessage.substring(1),
                ",]");

            try
            {
                errorLine = Integer.parseInt(st.nextToken());
                errorCol = Integer.parseInt(st.nextToken());

                detailMessage.append(st.nextToken(
                    "").substring(
                    2)); // skip "] "
            }
            catch (NumberFormatException nfe)
            {
                nfe.printStackTrace();
                // No positional data available
                detailMessage.append(sDetailMessage);
                errorLine = -1;
                errorCol = -1;
            }
        }
        else
        {
            // No positional data available
            detailMessage = detailMessage.append(sDetailMessage);
            errorLine = -1;
            errorCol = -1;
        }
    }
}