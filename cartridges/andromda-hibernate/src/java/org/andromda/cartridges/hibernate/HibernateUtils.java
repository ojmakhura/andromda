package org.andromda.cartridges.hibernate;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.andromda.cartridges.hibernate.metafacades.HibernateGlobals;
import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;


/**
 * Contains utilities used within the Hibernate cartridge.
 *
 * @author Chad Brandon
 * @author Joel Kozikowski
 * @author Wouter Zoons
 */
public class HibernateUtils
{
    /**
     * Retrieves all roles from the given <code>services</code> collection.
     *
     * @param services the collection services.
     * @return all roles from the collection.
     */
    public Collection getAllRoles(Collection services)
    {
        final Collection allRoles = new LinkedHashSet();
        CollectionUtils.forAllDo(
            services,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object instanceof Service)
                    {
                        allRoles.addAll(((Service)object).getAllRoles());
                    }
                }
            });
        return allRoles;
    }

    /**
     * Stores the version of Hibernate we're generating for.
     */
    private String hibernateVersion;

    /**
     * Sets the version of Hibernate we're generating for.
     *
     * @param hibernateVersion The version to set.
     */
    public void setHibernateVersion(final String hibernateVersion)
    {
        this.hibernateVersion = hibernateVersion;
    }

    /**
     * Retrieves the appropriate Hibernate package for the given version.
     *
     * @return the Hibernate package name.
     */
    public String getHibernatePackage()
    {
        return this.isVersion3() ? "org.hibernate" : "net.sf.hibernate";
    }

    /**
     * Retrieves the appropriate package for Hibernate user types given
     * the version defined within this class.
     *
     * @return the hibernate user type package.
     */
    public String getHibernateUserTypePackage()
    {
        return isVersion3() ? this.getHibernatePackage() + ".usertype" : this.getHibernatePackage();
    }

    /**
     * Indicates whether or not Hibernate 3 is enabled.
     *
     * @return true/false
     */
    public boolean isVersion3()
    {
        return isVersion3(this.hibernateVersion);
    }

    /**
     * Indicates whether or not the given property value is version 3 or not.
     *
     * @param hibernateVersionPropertyValue the value of the property
     * @return true/false
     */
    public static boolean isVersion3(String hibernateVersionPropertyValue)
    {
        boolean version3 = false;
        if (hibernateVersionPropertyValue != null)
        {
            version3 = hibernateVersionPropertyValue.equals(HibernateGlobals.HIBERNATE_VERSION_3);
        }
        return version3;
    }

    /**
     * Denotes whether or not to make use of Hibernate 3 XML persistence support.
     */
    private String hibernateXmlPersistence;

    /**
     * @param hibernateXmlPersistence <code>true</code> when you to make use of Hibernate 3 XML persistence support,
     *      <code>false</code> otherwise
     */
    public void setHibernateXMLPersistence(final String hibernateXmlPersistence)
    {
        this.hibernateXmlPersistence = hibernateXmlPersistence;
    }

    public boolean isXmlPersistenceActive()
    {
        return isXmlPersistenceActive(
            this.hibernateVersion,
            this.hibernateXmlPersistence);
    }

    public static boolean isXmlPersistenceActive(
        String hibernateVersionPropertyValue,
        String hibernateXMLPersistencePropertyValue)
    {
        return isVersion3(hibernateVersionPropertyValue) &&
            "true".equalsIgnoreCase(hibernateXMLPersistencePropertyValue);
    }

    private String hibernateMappingStrategy;

    public void setHibernateMappingStrategy(String hibernateMappingStrategy)
    {
        this.hibernateMappingStrategy = hibernateMappingStrategy;
    }

    public boolean isMapSubclassesInSeparateFile()
    {
        return mapSubclassesInSeparateFile(this.hibernateMappingStrategy);
    }

    public static boolean mapSubclassesInSeparateFile(
        String hibernateMappingStrategy)
    {
        // subclass or hierarchy
        return HibernateGlobals.HIBERNATE_MAPPING_STRATEGY_SUBCLASS.equalsIgnoreCase(hibernateMappingStrategy);
    }
}
