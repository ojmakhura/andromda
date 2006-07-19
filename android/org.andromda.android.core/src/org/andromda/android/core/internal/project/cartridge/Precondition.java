package org.andromda.android.core.internal.project.cartridge;

import java.util.Map;

import org.andromda.android.core.project.cartridge.IPrecondition;
import org.apache.commons.lang.ObjectUtils;

/**
 * A precondition in a project cartridge descriptor file.
 *
 * @author Peter Friese
 * @since 16.07.2006
 */
public class Precondition
        implements IPrecondition
{

    /** The <code>id</code> of this precondition. */
    private String id;

    /** The value for equality for this precondition. */
    private String equal;

    /** The value for non-equality for this precondition. */
    private String notEqual;

    /**
     * Creates a new Precondition.
     *
     * @param conditionId The id for this precondition.
     * @param conditionEqual The value for equality.
     * @param conditionNotEqual The value for non-equality.
     */
    public Precondition(final String conditionId,
        final String conditionEqual,
        final String conditionNotEqual)
    {
        id = conditionId;
        equal = conditionEqual;
        notEqual = conditionNotEqual;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(final String id)
    {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    public String getId()
    {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public void setEqual(final String equal)
    {
        this.equal = equal;
    }

    /**
     * {@inheritDoc}
     */
    public String getEqual()
    {
        return equal;
    }

    /**
     * {@inheritDoc}
     */
    public void setNotEqual(final String notEqual)
    {
        this.notEqual = notEqual;

    }

    /**
     * {@inheritDoc}
     */
    public String getNotEqual()
    {
        return notEqual;
    }

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(final Map projectProperties)
    {
        boolean valid = false;
        final String equalString = this.getEqual();
        final String notEqualString = this.getNotEqual();
        final boolean equalConditionPresent = equalString != null;
        final boolean notEqualConditionPresent = notEqualString != null;
        Object value = ObjectUtils.toString(projectProperties.get(this.getId()));
        if (equalConditionPresent || notEqualConditionPresent)
        {
            if (equalConditionPresent)
            {
                valid = equalString.equals(value);
            }
            else if (notEqualConditionPresent)
            {
                valid = !notEqualString.equals(value);
            }
        }

        return valid;
    }

}
