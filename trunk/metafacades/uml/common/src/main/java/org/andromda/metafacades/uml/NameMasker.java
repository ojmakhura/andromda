package org.andromda.metafacades.uml;

import java.text.Normalizer;

import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Provides the ability to <code>mask</code> names in a standard manner.
 *
 * @author Chad Brandon
 */
public class NameMasker
{
    /**
     * The <code>uppercase</code> mask.
     */
    public static final String UPPERCASE = "uppercase";

    /**
     * The <code>underscore</code> mask.
     */
    public static final String UNDERSCORE = "underscore";

    /**
     * The <code>upperunderscore</code> mask.
     */
    public static final String UPPERUNDERSCORE = "upperunderscore";

    /**
     * The <code>lowercase</code> mask.
     */
    public static final String LOWERCASE = "lowercase";

    /**
     * The <code>lowerunderscore</code> mask.
     */
    public static final String LOWERUNDERSCORE = "lowerunderscore";

    /**
     * The <code>uppercamelcase</code> mask.
     */
    public static final String UPPERCAMELCASE = "uppercamelcase";

    /**
     * The <code>lowercamelcase</code> mask.
     */
    public static final String LOWERCAMELCASE = "lowercamelcase";

    /**
     * The <code>nospace</code> mask.
     */
    public static final String NOSPACE = "nospace";

    /**
     * The <code>noaccent</code> mask.
     */
    public static final String NOACCENT = "noaccent";

    /**
     * The <code>none</code> mask.
     */
    public static final String NONE = "none";

    /**
     * Returns the name with the appropriate <code>mask</code> applied. The mask, must match one of the valid mask
     * properties or will be ignored.
     *
     * @param name the name to be masked
     * @param mask the mask to apply
     * @return the masked name.
     */
    public static String mask(String name, String mask)
    {
        mask = StringUtils.trimToEmpty(mask);
        name = StringUtils.trimToEmpty(name);
        if (!mask.equalsIgnoreCase(NONE))
        {
            if (mask.equalsIgnoreCase(UPPERCASE))
            {
                name = name.toUpperCase();
            }
            else if (mask.equalsIgnoreCase(UNDERSCORE))
            {
                name = StringUtilsHelper.separate(name, "_");
            }
            else if (mask.equalsIgnoreCase(UPPERUNDERSCORE))
            {
                name = StringUtilsHelper.separate(name, "_").toUpperCase();
            }
            else if (mask.equalsIgnoreCase(LOWERCASE))
            {
                name = name.toLowerCase();
            }
            else if (mask.equalsIgnoreCase(LOWERUNDERSCORE))
            {
                name = StringUtilsHelper.separate(name, "_").toLowerCase();
            }
            else if (mask.equalsIgnoreCase(LOWERCAMELCASE))
            {
                name = StringUtilsHelper.lowerCamelCaseName(name);
            }
            else if (mask.equalsIgnoreCase(UPPERCAMELCASE))
            {
                name = StringUtilsHelper.upperCamelCaseName(name);
            }
            else if (mask.equalsIgnoreCase(NOSPACE))
            {
                name = StringUtils.deleteWhitespace(name);
            }
            else if (mask.equalsIgnoreCase(NOACCENT))
            {
                name = Normalizer.normalize(name, java.text.Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","");
            }
        }
        return name;
    }
}
