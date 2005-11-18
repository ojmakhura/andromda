package org.andromda.metafacades.emf.uml2;

import java.util.Collection;


public interface TagDefinition
    extends org.eclipse.uml2.NamedElement
{
    public String getName();

    public String getValue();

    public Collection getValues();

    public String toString();
}