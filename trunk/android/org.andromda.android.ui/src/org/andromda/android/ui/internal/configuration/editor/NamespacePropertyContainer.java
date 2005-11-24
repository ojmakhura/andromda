package org.andromda.android.ui.internal.configuration.editor;

import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;

/**
 *
 * @author Peter Friese
 * @since 22.11.2005
 */
class NamespacePropertyContainer
{

    private final PropertyGroup propertyGroup;

    private final Namespace namespace;

    /**
     * @param propertyGroup
     * @param namespace
     */
    public NamespacePropertyContainer(PropertyGroup propertyGroup,
        Namespace namespace)
    {
        this.propertyGroup = propertyGroup;
        this.namespace = namespace;
    }

    /**
     * @return Returns the propertyGroup.
     */
    public PropertyGroup getPropertyGroup()
    {
        return propertyGroup;
    }

    /**
     * @return Returns the namespace.
     */
    public Namespace getNamespace()
    {
        return namespace;
    }

}
