package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntity.
 *
 * @see org.andromda.metafacades.uml.ManageableEntity
 */
public class ManageableEntityLogicImpl
    extends ManageableEntityLogic
{
    public ManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String getNamespaceSeparator()
    {
        return (String)getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntity#getManageablePackageName()
     */
    protected java.lang.String handleGetManageablePackageName()
    {
        String manageablePackageName = "";

        final String parentPackage = super.getPackageName();
        if (parentPackage != null && parentPackage.length() > 0)
        {
            manageablePackageName = parentPackage;
        }

        final Object suffix = getConfiguredProperty(UMLMetafacadeProperties.MANAGEABLE_PACKAGE_NAME_SUFFIX);
        if (suffix != null && StringUtils.isNotBlank(suffix.toString()))
        {
            manageablePackageName += this.getNamespaceSeparator() + suffix;
        }

        return manageablePackageName;
    }

    protected String handleGetManageablePackagePath()
    {
        return StringUtils.replace(
            this.getManageablePackageName(),
            this.getNamespaceSeparator(),
            "/");
    }

    protected java.util.List handleGetManageableAssociationEnds()
    {
        final List manageableAssociationEnds = new ArrayList();

        final Collection associationEnds = getAssociationEnds();
        for (final Iterator associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)associationEndIterator.next();
            final AssociationEndFacade otherEnd = associationEnd.getOtherEnd();

            if (otherEnd.isNavigable())
            {
                if (associationEnd.isMany() || (associationEnd.isOne2One() && otherEnd.isChild()))
                {
                    final Object otherEndType = otherEnd.getType();
                    if (otherEndType instanceof Entity)
                    {
                        manageableAssociationEnds.add(otherEnd);
                    }
                }
            }
        }

        return manageableAssociationEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntity#isCreate()
     */
    protected boolean handleIsCreate()
    {
        return true;
    }

    protected String handleGetManageableServiceName()
    {
        return getName() + "ManageableService";
    }

    protected String handleGetManageableServiceFullPath()
    {
        return '/' + StringUtils.replace(
            getFullyQualifiedManageableServiceName(),
            getNamespaceSeparator(),
            "/");
    }

    protected String handleGetFullyQualifiedManageableServiceName()
    {
        return this.getManageablePackageName() + this.getNamespaceSeparator() + getManageableServiceName();
    }

    protected String handleGetManageableServiceAccessorCall()
    {
        final String property = UMLMetafacadeProperties.MANAGEABLE_SERVICE_ACCESSOR_PATTERN;
        final String accessorImplementation =
            this.isConfiguredProperty(property) ? ObjectUtils.toString(this.getConfiguredProperty(property)) : "";
        return accessorImplementation.replaceAll(
            "\\{0\\}",
            getManageablePackageName()).replaceAll(
            "\\{1\\}",
            getManageableServiceName());
    }

    protected boolean handleIsRead()
    {
        return true;
    }

    protected boolean handleIsUpdate()
    {
        return true && !getIdentifiers().isEmpty(); // @todo
    }

    protected boolean handleIsDelete()
    {
        return true && !getIdentifiers().isEmpty(); // @todo
    }

    protected List handleGetManageableMembers()
    {
        final List criteria = new ArrayList();
        criteria.addAll(getAttributes());
        criteria.addAll(getManageableAssociationEnds());
        return criteria;
    }

    protected String handleListManageableMembers(boolean withTypes)
    {
        final StringBuffer buffer = new StringBuffer();

        final Collection attributes = getAttributes();
        for (final Iterator attributeIterator = attributes.iterator(); attributeIterator.hasNext();)
        {
            if (buffer.length() > 0)
            {
                buffer.append(", ");
            }

            final ManageableEntityAttribute attribute = (ManageableEntityAttribute)attributeIterator.next();
            final ClassifierFacade type = attribute.getType();
            if (type != null)
            {
                if (withTypes)
                {
                    buffer.append(type.getFullyQualifiedName());
                    buffer.append(' ');
                }
                buffer.append(attribute.getName());
            }
        }

        final Collection associationEnds = getManageableAssociationEnds();
        for (final Iterator associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final ManageableEntityAssociationEnd associationEnd =
                (ManageableEntityAssociationEnd)associationEndIterator.next();
            final Entity entity = (Entity)associationEnd.getType();

            final Iterator identifierIterator = entity.getIdentifiers().iterator();
            if (identifierIterator.hasNext())
            {
                final AttributeFacade identifier = (AttributeFacade)identifierIterator.next();
                if (identifier != null)
                {
                    if (buffer.length() > 0)
                    {
                        buffer.append(", ");
                    }

                    final ClassifierFacade type = identifier.getType();
                    if (type != null)
                    {
                        if (withTypes)
                        {
                            buffer.append(type.getFullyQualifiedName());
                            if (associationEnd.isMany())
                            {
                                buffer.append("[]");
                            }
                            buffer.append(' ');
                        }
                        buffer.append(associationEnd.getName());
                    }
                }
            }
        }

        return buffer.toString();
    }

    protected boolean handleIsManageable()
    {
        return true;
    }

    protected java.util.List handleGetReferencingManageables()
    {
        final Set referencingManageables = new HashSet();
        final Collection associationEnds = getAssociationEnds();
        for (final Iterator associationEndIterator = associationEnds.iterator(); associationEndIterator.hasNext();)
        {
            final AssociationEndFacade associationEnd = (AssociationEndFacade)associationEndIterator.next();

            if (associationEnd.isNavigable())
            {
                if (associationEnd.isMany() || (associationEnd.isOne2One() && associationEnd.isChild()))
                {
                    final Object otherEndType = associationEnd.getOtherEnd().getType();
                    if (otherEndType instanceof Entity)
                    {
                        referencingManageables.add(otherEndType);
                    }
                }
            }
        }
        return new ArrayList(referencingManageables);
    }

    protected Object handleGetDisplayAttribute()
    {
        AttributeFacade displayAttribute = null;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_DISPLAY_NAME);
        if (taggedValueObject != null)
        {
            displayAttribute = findAttribute(StringUtilsHelper.trimToEmpty(taggedValueObject.toString()));
        }

        final Collection attributes = getAttributes();
        for (final Iterator attributeIterator = attributes.iterator();
            attributeIterator.hasNext() && displayAttribute == null;)
        {
            final EntityAttribute attribute = (EntityAttribute)attributeIterator.next();
            if (attribute.isUnique())
            {
                displayAttribute = attribute;
            }
        }

        if (displayAttribute == null)
        {
            if (!getIdentifiers().isEmpty())
            {
                displayAttribute = (EntityAttribute)getIdentifiers().iterator().next();
            }
            else if (!attributes.isEmpty())
            {
                displayAttribute = (EntityAttribute)attributes.iterator().next();
            }
        }

        return displayAttribute;
    }

    protected java.util.List handleGetUsers()
    {
        final Set users = new HashSet();

        final Collection dependencies = getTargetDependencies();
        for (final Iterator dependencyIterator = dependencies.iterator(); dependencyIterator.hasNext();)
        {
            final DependencyFacade dependency = (DependencyFacade)dependencyIterator.next();
            final Object dependencyObject = dependency.getSourceElement();

            if (!users.contains(dependencyObject) && dependencyObject instanceof ActorFacade)
            {
                collectActors((ActorFacade)dependencyObject, users);
            }
        }

        return new ArrayList(users);
    }

    private void collectActors(
        ActorFacade actor,
        Collection actors)
    {
        if (!actors.contains(actor))
        {
            actors.add(actor);

            final Collection childActors = actor.getGeneralizedByActors();
            for (final Iterator iterator = childActors.iterator(); iterator.hasNext();)
            {
                final ActorFacade childActor = (ActorFacade)iterator.next();
                collectActors(childActor, actors);
            }
        }
    }

    protected int handleGetMaximumListSize()
    {
        int maximumListSize = -1;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_MAXIMUM_LIST_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                maximumListSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                maximumListSize = internalDefaultMaximumListSize();
            }
        }
        else
        {
            maximumListSize = internalDefaultMaximumListSize();
        }

        return maximumListSize;
    }

    private int internalDefaultMaximumListSize()
    {
        int maximumListSize = -1;

        try
        {
            maximumListSize =
                Integer.parseInt((String)getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_MAX_LIST_SIZE));
        }
        catch (NumberFormatException e1)
        {
            maximumListSize = -1;
        }

        return maximumListSize;
    }

    protected int handleGetPageSize()
    {
        int pageSize = 20;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_PAGE_SIZE);
        if (taggedValueObject != null)
        {
            try
            {
                pageSize = Integer.parseInt(taggedValueObject.toString());
            }
            catch (NumberFormatException e)
            {
                pageSize = internalDefaultPageSize();
            }
        }
        else
        {
            pageSize = internalDefaultPageSize();
        }

        return pageSize;
    }

    private int internalDefaultPageSize()
    {
        int pageSize = 20;

        try
        {
            pageSize =
                Integer.parseInt((String)getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_PAGE_SIZE));
        }
        catch (NumberFormatException e1)
        {
            pageSize = 20;
        }

        return pageSize;
    }

    protected boolean handleIsResolveable()
    {
        boolean resolveable = true;

        final Object taggedValueObject = findTaggedValue(UMLProfile.TAGGEDVALUE_MANAGEABLE_RESOLVEABLE);
        if (taggedValueObject != null)
        {
            try
            {
                resolveable = Boolean.valueOf(taggedValueObject.toString()).booleanValue();
            }
            catch (NumberFormatException e)
            {
                resolveable = internalDefaultResolveable();
            }
        }
        else
        {
            resolveable = internalDefaultResolveable();
        }

        return resolveable;
    }

    private boolean internalDefaultResolveable()
    {
        boolean resolveable = true;

        try
        {
            resolveable =
                Boolean.valueOf((String)getConfiguredProperty(UMLMetafacadeProperties.PROPERTY_DEFAULT_RESOLVEABLE))
                       .booleanValue();
        }
        catch (NumberFormatException ex)
        {
            resolveable = true;
        }

        return resolveable;
    }

    protected java.util.List handleGetAllManageables()
    {
        final Set allManageableEntities = new TreeSet(new ManageableComparator());

        final Collection allClasses = getModel().getAllClasses();
        for (final Iterator classIterator = allClasses.iterator(); classIterator.hasNext();)
        {
            final Object classObject = classIterator.next();
            if (classObject instanceof ManageableEntity)
            {
                allManageableEntities.add(classObject);
            }
        }

        return new ArrayList(allManageableEntities);
    }

    private final class ManageableComparator
        implements Comparator
    {
        public int compare(
            Object left,
            Object right)
        {
            final ModelElementFacade leftEntity = (ModelElementFacade)left;
            final ModelElementFacade rightEntity = (ModelElementFacade)right;

            return leftEntity.getName().compareTo(rightEntity.getName());
        }
    }
}