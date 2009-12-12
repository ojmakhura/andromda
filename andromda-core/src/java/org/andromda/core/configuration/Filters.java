package org.andromda.core.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.PatternSyntaxException;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeFactory;
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
    private final Collection<Filter> filters = new ArrayList<Filter>();

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
        return this.filters.toArray(new Filter[0]);
    }

    private final MetafacadeFactory factory = MetafacadeFactory.getInstance();

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
        for (Filter filter : this.filters)
        {
            if (this.match(value, filter.getValue()))
            {
                shouldApply = filter.isApply() && (filter.getNamespaceList().isEmpty() ||
                    filter.getNamespaceList().contains(StringUtils.trim(factory.getNamespace())));
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
     * The double star constant.
     */
    private static final String DOUBLE_STAR = "**";

    /**
     * Provides matching of simple wildcards. (i.e. '*.java' etc.)
     *
     * @param value the value to match against.
     * @param pattern the pattern to check if the path matches.
     * @return true if the <code>value</code> matches the given <code>pattern</code>, false otherwise.
     */
    public boolean match(
        String value,
        String pattern)
    {
        value = StringUtils.trimToEmpty(value);
        boolean matches = false;
        if (value != null)
        {
            final String scopeOperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
            pattern = StringUtils.replace(
                    pattern,
                    ".",
                    "\\.");
            boolean matchAll = pattern.indexOf(DOUBLE_STAR) != -1;
            pattern = pattern.replaceAll(
                    "\\*{1}",
                    "\\.\\*");
            if (matchAll)
            {
                pattern = StringUtils.replace(
                        pattern,
                        DOUBLE_STAR,
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
        }
        return matches;
    }
}