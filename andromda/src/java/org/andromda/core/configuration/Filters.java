package org.andromda.core.configuration;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.PatternSyntaxException;

import org.andromda.core.metafacade.MetafacadeConstants;
import org.apache.commons.lang.StringUtils;


/**
 * Stores information about all {@link Filter} instances that should or should not be applied
 * to a model.
 *
 * @author Chad Brandon
 * @see org.andromda.core.configuration.Filter
 */
public class Filters
    implements Serializable
{
    /**
     * The flag indicating whether or not all filter
     * should be applied.
     */
    private boolean applyAll = true;

    /**
     * Sets whether or not AndroMDA should apply all filters on a model. If this is set to true, then filter values should be
     * specified if you want to keep certain filters from being being applied. If this is set to false, then you would want
     * to define filter elements to specify which filters <strong>SHOULD BE</strong> applied.
     *
     * @param applyAll whether or not we should apply all filters true/false
     */
    public void setApplyAll(final boolean applyAll)
    {
        this.applyAll = applyAll;
    }

    /**
     * Stores the filters as they're added.
     */
    private final Collection filters = new ArrayList();

    /**
     * Adds the filter to the underlying filters store.
     *
     * @param filter the Filter instance.
     */
    public void addFilter(final Filter filter)
    {
        this.filters.add(filter);
    }

    /**
     * Adds all {@link Filter} instances in the given <code>filters</code>
     * to this instance.
     *
     * @param filters the Filters instance to add.
     */
    public void addFilters(final Filters filters)
    {
        this.filters.addAll(filters.filters);
    }

    /**
     * Gets the actual filters that belong to this filters instance.
     *
     * @return the filters to retrieve.
     */
    public Filter[] getFilters()
    {
        return (Filter[])this.filters.toArray(new Filter[0]);
    }

    /**
     * Determines whether or not the <code>value</code> should be applied. If
     * <code>applyAll</code> is true, then this method will return false only if the Filter
     * corresponding to the <code>value</code> has apply set to false.
     *
     * @param value the name of the model filter to check.
     * @return true/false
     */
    public boolean isApply(final String value)
    {
        boolean shouldApply = this.applyAll;
        for (final Iterator iterator = this.filters.iterator(); iterator.hasNext();)
        {
            final Filter filter = (Filter)iterator.next();
            if (match(value,
                    filter.getValue()))
            {
                shouldApply = filter.isApply();
                break;
            }
        }
        return shouldApply;
    }

    /**
     * Indicates whether or not this model filters instance
     * has any filtering defined.
     *
     * @return true/false
     */
    public boolean isEmpty()
    {
        return this.filters.isEmpty();
    }

    /**
     * Provides matching of simple wildcards. (i.e. '*.java' etc.)
     *
     * @param value the value to match against.
     * @param pattern the pattern to check if the path matches.
     * @return true if the <code>value</code> matches the given <code>pattern</code>, false otherwise.
     */
    public static boolean match(
        String value,
        String pattern)
    {
        value = StringUtils.trimToEmpty(value);
        boolean matches = false;
        final String scopeOperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
        final String doubleStar = "**";
        pattern = StringUtils.replace(
                pattern,
                ".",
                "\\.");
        boolean matchAll = pattern.indexOf(doubleStar) != -1;
        pattern = pattern.replaceAll(
                "\\*{1}",
                "\\.\\*");
        if (matchAll)
        {
            pattern = StringUtils.replace(
                    pattern,
                    doubleStar,
                    ".*");
        }
        try
        {
            matches = value.matches(pattern);
        }
        catch (final PatternSyntaxException exception)
        {
            matches = false;
        }
        if (!matchAll)
        {
            matches =
                matches &&
                StringUtils.countMatches(
                    pattern,
                    scopeOperator) == StringUtils.countMatches(
                    value,
                    scopeOperator);
        }
        return matches;
    }
}