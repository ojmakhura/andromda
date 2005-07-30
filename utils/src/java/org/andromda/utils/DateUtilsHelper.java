package org.andromda.utils;

import org.apache.commons.lang.time.DateUtils;

/**
 * Provides additional methods supporting various date-related features
 */
public class DateUtilsHelper
    extends DateUtils
{
    // order is important !
    private static final FormatPattern[] JAVA2PERL_FORMAT_PATTERNS = {new FormatPattern("y{4,}", "%Y"),
        new FormatPattern("y{1,3}", "%y"),
        new FormatPattern("M{4,}", "%B"),
        new FormatPattern("M{3}", "%b"),
        new FormatPattern("M{1,2}", "%m"),
        new FormatPattern("d{4,}", "%A"),
        new FormatPattern("d{3,}", "%a"),
        new FormatPattern("d{2}", "%d"),
        new FormatPattern("d{1}", "%e"),
        new FormatPattern("E{4,}", "%A"),
        new FormatPattern("E{1,3}", "%a"),
        new FormatPattern("H{2,}", "%H"),
        new FormatPattern("H{1}", "%k"),
        new FormatPattern("h{2,}", "%I"),
        new FormatPattern("h{1}", "%l"),
        new FormatPattern("D{3}", "%j"),
        new FormatPattern("s{2,}", "%S"),
        new FormatPattern("s{1}", "%s"),
        new FormatPattern("m{2,}", "%M")};

    /**
     * Converts a Java SimpleDateFormat into an equivalent String suited for dates used on Perl/PHP platforms.
     */
    public static String formatJavaToPerl(String javaFormat)
    {
        String perlFormat = null;

        if (javaFormat != null)
        {
            perlFormat = javaFormat;

            // this implementation is quite rough, and not at all performant, but it works for now
            // @todo (wouter): re-implement
            for (int i = 0; i < JAVA2PERL_FORMAT_PATTERNS.length; i++)
            {
                final FormatPattern formatPattern = JAVA2PERL_FORMAT_PATTERNS[i];
                perlFormat = perlFormat.replaceAll(formatPattern.getPattern(), "%%%" + i + "%%%");
            }

            for (int i = 0; i < JAVA2PERL_FORMAT_PATTERNS.length; i++)
            {
                final FormatPattern formatPattern = JAVA2PERL_FORMAT_PATTERNS[i];
                perlFormat = perlFormat.replaceAll("%%%" + i + "%%%", formatPattern.getReplacement());
            }
        }

        return perlFormat;
    }

    private static final String[] PERL_TIME_FORMATS = new String[]{"%H", "%I", "%k", "%l", "%p", "%P", "%s", "%S"};

    /**
     * Checks whether a perl formatted date contains information about displaying time.
     */
    public static boolean containsTimeFormat(String perlFormat)
    {
        boolean containsTimeFormat = false;

        for (int i = 0; i < PERL_TIME_FORMATS.length && !containsTimeFormat; i++)
        {
            String timeFormatPattern = PERL_TIME_FORMATS[i];
            containsTimeFormat = perlFormat.indexOf(timeFormatPattern) > -1;
        }

        return containsTimeFormat;
    }

    private static final class FormatPattern
    {
        private String pattern = null;
        private String replacement = null;

        public FormatPattern(
            final String formatPattern,
            final String replacement)
        {
            this.pattern = formatPattern;
            this.replacement = replacement;
        }

        public String getPattern()
        {
            return pattern;
        }

        public String getReplacement()
        {
            return replacement;
        }
    }
}
