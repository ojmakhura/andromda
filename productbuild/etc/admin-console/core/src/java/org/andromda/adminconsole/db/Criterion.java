package org.andromda.adminconsole.db;

import java.io.Serializable;

/**
 * Represents a criterion for an sql query. Criterions are used in the where clause
 * of a typically sql query such as:
 * <code>select * from PERSON where NAME = 'wouter'</code>
 * <p/>
 * The part <code>NAME = 'wouter'</code> is a criterion.
 *
 * @author Wouter Zoons
 */
public interface Criterion extends Serializable
{
    /**
     * @return this criterion translated into something suitable as an sql segment
     */
    public String toSqlString();
}
