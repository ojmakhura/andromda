package org.andromda.utils.beautifier.core;

/**
 * Copyright 2008 hybrid labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.storage.Loggers;

/**
 * Abstract implementation of the Beautifier interface focussing on Java beautification.
 *
 * @author Karsten Klein, hybrid labs; Plushnikov Michail
 */
public abstract class JavaBeautifier implements Beautifier, ImportBeautifierJalopyConstants {

    private static final Pattern PATTERN_NEWLINE = Pattern.compile("\\n");

    private static final Logger LOG = Logger.getLogger(JavaBeautifier.class.toString());

    private String conventionFilePath;

    private boolean conventionFileInitialized = false;

    /**
     * @return conventionFilePath
     */
    public String getConventionFilePath() {
        return conventionFilePath;
    }

    /**
     * @param conventionFilePath
     */
    public void setConventionFilePath(String conventionFilePath) {
        this.conventionFilePath = conventionFilePath;
        conventionFileInitialized = false;
    }

    protected Jalopy createJavaNode(String pSource, File pTempFile) {
        initializeConventionFileUrl();

        Jalopy jalopy = initializeJalopy();
        jalopy.setInput(pSource, pTempFile.getAbsolutePath());

        return jalopy;
    }

    private URL testUrl(URL url) {
        if (url != null) {
            InputStream inputStream = null;
            try {
                inputStream = url.openStream();
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
            return url;
        }
        return null;
    }

    private void initializeConventionFileUrl() {
        if (conventionFileInitialized) {
            return;
        }
        conventionFileInitialized = true;

        URL url = null;
        if (conventionFilePath != null) {
            url = testUrl(getClass().getResource(conventionFilePath));

            if (url == null) {
                try {
                    url = testUrl(new URL("file:" + conventionFilePath));
                } catch (MalformedURLException e) {
                    LOG.error("Cannot read convention file from 'file:" + conventionFilePath + "'.", e);
                }
            }
        }

        if (url == null) {
            url = testUrl(getClass().getResource("default-convention.xml"));
        }

        if (url != null) {
            try {
                Jalopy.setConvention(url);
            } catch (IOException e) {
                LOG.error("Cannot read convention file from '" + url + "'.", e);
            }
        }
    }

    protected int findPositionInCharacterSequence(String sequence, int line, int column) {
        Pattern newlinePattern = PATTERN_NEWLINE;
        Matcher newLineMatcher = newlinePattern.matcher(sequence);
        int pos = 0;
        line--;
        while (line > 0) {
            newLineMatcher.find();
            pos = newLineMatcher.end();
            line--;
        }
        pos += column;

        if (pos >= 0) {
            while (pos < sequence.length() && (sequence.charAt(pos) == '\r' || sequence.charAt(pos) == '\n')) {
                pos++;
            }
        }

        return pos;
    }

    protected String format(String pSource, File file) {
        Jalopy jalopy = initializeJalopy();
        StringBuffer sb = new StringBuffer();
        try {
            jalopy.setInput(pSource, file.getAbsolutePath());

            jalopy.setOutput(sb);
            jalopy.format();
        } finally {
            cleanupJalopy();
        }

        return sb.toString();
    }

    private Jalopy initializeJalopy() {
        Jalopy jalopy = new Jalopy();
        jalopy.setInspect(false);
        jalopy.setBackup(false);
        jalopy.setForce(false);
        // NOTE: the convention file is static (done during first initialize() invocation)
        return jalopy;
    }

    @SuppressWarnings("unchecked")
    private void cleanupJalopy() {
        List<Appender> toBeDeleted = new ArrayList<Appender>();

        Logger logger = Loggers.ALL;
        
        for (Enumeration<Object> it = logger.getAllAppenders(); it.hasMoreElements(); ) {
            Object obj = it.nextElement();
            String name = obj.getClass().getName();
            if (name.equals("de.hunsicker.jalopy.Jalopy$SpyAppender"))
                toBeDeleted.add((Appender)obj);
        }

        for (Appender appender : toBeDeleted)
            logger.removeAppender(appender);
    }
}
