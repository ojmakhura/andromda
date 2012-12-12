package org.andromda.cartridges.hibernate;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import org.andromda.cartridges.hibernate.metafacades.HibernateGlobals;
import org.andromda.metafacades.uml.Role;
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
    public Collection<Role> getAllRoles(Collection services)
    {
        final Collection<Role> allRoles = new LinkedHashSet<Role>();
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
        return this.isVersion3() || this.isVersion4() ? "org.hibernate" : "net.sf.hibernate";
    }

    /**
     * Retrieves the appropriate package for Hibernate user types given
     * the version defined within this class.
     *
     * @return the hibernate user type package.
     */
    public String getHibernateUserTypePackage()
    {
        return this.isVersion3() || this.isVersion4() ? this.getHibernatePackage() + ".usertype" : this.getHibernatePackage();
    }

    /**
     * Indicates whether or not Hibernate 2 is enabled.
     *
     * @return true/false
     */
    public boolean isVersion2()
    {
        return isVersion2(this.hibernateVersion);
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
     * Indicates whether or not Hibernate 3 is enabled.
     *
     * @return true/false
     */
    public boolean isVersion4()
    {
        return isVersion4(this.hibernateVersion);
    }

    /**
     * Indicates whether or not the given property value is version 3 or not.
     *
     * @param hibernateVersionPropertyValue the value of the property
     * @return true/false
     */
    public static boolean isVersion2(String hibernateVersionPropertyValue)
    {
        boolean version2 = false;
        if (hibernateVersionPropertyValue != null)
        {
            version2 = hibernateVersionPropertyValue.startsWith(HibernateGlobals.HIBERNATE_VERSION_2);
        }
        return version2;
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
            version3 = hibernateVersionPropertyValue.startsWith(HibernateGlobals.HIBERNATE_VERSION_3);
        }
        return version3;
    }

    /**
     * Indicates whether or not the given property value is version 3 or not.
     *
     * @param hibernateVersionPropertyValue the value of the property
     * @return true/false
     */
    public static boolean isVersion4(String hibernateVersionPropertyValue)
    {
        boolean version4 = false;
        if (hibernateVersionPropertyValue != null)
        {
            version4 = hibernateVersionPropertyValue.startsWith(HibernateGlobals.HIBERNATE_VERSION_4);
        }
        return version4;
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

    /**
     * @return isXmlPersistenceActive
     */
    public boolean isXmlPersistenceActive()
    {
        return isXmlPersistenceActive(
            this.hibernateVersion,
            this.hibernateXmlPersistence);
    }

    /**
     * @param hibernateVersionPropertyValue
     * @param hibernateXMLPersistencePropertyValue
     * @return isXmlPersistenceActive
     */
    public static boolean isXmlPersistenceActive(
        String hibernateVersionPropertyValue,
        String hibernateXMLPersistencePropertyValue)
    {
        return isVersion3(hibernateVersionPropertyValue) &&
            "true".equalsIgnoreCase(hibernateXMLPersistencePropertyValue);
    }

    private String hibernateMappingStrategy;

    /**
     * @param hibernateMappingStrategy
     */
    public void setHibernateMappingStrategy(String hibernateMappingStrategy)
    {
        this.hibernateMappingStrategy = hibernateMappingStrategy;
    }

    /**
     * @return mapSubclassesInSeparateFile(this.hibernateMappingStrategy)
     */
    public boolean isMapSubclassesInSeparateFile()
    {
        return mapSubclassesInSeparateFile(this.hibernateMappingStrategy);
    }

    /**
     * @param hibernateMappingStrategy
     * @return mapSubclassesInSeparateFile
     */
    public static boolean mapSubclassesInSeparateFile(
        String hibernateMappingStrategy)
    {
        // subclass or hierarchy
        return HibernateGlobals.HIBERNATE_MAPPING_STRATEGY_SUBCLASS.equalsIgnoreCase(hibernateMappingStrategy);
    }

    /**
     * Needed for InheritanceType enumeration in hibernate annotations
     * @param hibernateMappingStrategy
     * @return javax.persistence.InheritanceType enum value for template
     */
    public static String getInheritanceTypeEnum(
        String hibernateMappingStrategy)
    {
        String inheritanceType = null;
        if (HibernateGlobals.HIBERNATE_MAPPING_STRATEGY_HIERARCHY.equalsIgnoreCase(hibernateMappingStrategy))
        {
            inheritanceType = "JOINED";
        }
        else if (HibernateGlobals.HIBERNATE_MAPPING_STRATEGY_CONCRETE.equalsIgnoreCase(hibernateMappingStrategy))
        {
            inheritanceType = "TABLE_PER_CLASS";
        }
        else /* if (HibernateGlobals.HIBERNATE_MAPPING_STRATEGY_SUBCLASS.equalsIgnoreCase(hibernateMappingStrategy)) */
        {
            inheritanceType = "SINGLE_TABLE";
        }
        return inheritanceType;
    }

    private static final SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssZ");
    /**
     * Returns the current Date in the specified format. $conversionUtils does not seem to work in vsl.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        /*if (DF == null || !format.equals(DF.toLocalizedPattern()))
        {
            DF = new SimpleDateFormat(format);
        }*/
        return DF.format(new Date());
    }

    /**
     * Returns the current Date in the specified format.
     *
     * @return the current date with the default format .
     */
    public static String getDate()
    {
        return DF.format(new Date());
    }
}
