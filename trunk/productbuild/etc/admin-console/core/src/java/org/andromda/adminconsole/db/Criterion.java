package org.andromda.adminconsole.db;

import java.io.Serializable;

public interface Criterion extends Serializable
{
    public String toSqlString();
}
