package org.andromda.core.common;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Enumeration;
import java.util.Collections;

public class CollectionUtilsHelper extends CollectionUtils
{
    public static List listEnumeration(Enumeration enum)
    {
        return Collections.list(enum);
    }
}
