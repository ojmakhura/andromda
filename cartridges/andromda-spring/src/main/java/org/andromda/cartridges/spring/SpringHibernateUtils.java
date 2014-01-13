package org.andromda.cartridges.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.andromda.cartridges.spring.metafacades.SpringGlobals;
import org.andromda.metafacades.uml.Entity;

/**
 * Contains utilities used within the Spring cartridge
 * when dealing with Hibernate.
 *
 * @author Chad Brandon
 * @author Joel Kozikowski
 */
public class SpringHibernateUtils
{
    /**
     * The version of Hibernate we're generating for.
     */
    private String hibernateVersion = SpringGlobals.HIBERNATE_VERSION_3;

    /**
     * Sets the version of Hibernate we're generating for.
     *
     * @param hibernateVersionIn the Hibernate version.
     */
    public void setHibernateVersion(final String hibernateVersionIn)
    {
        this.hibernateVersion = hibernateVersionIn;
    }

    /**
     * Gets the appropriate hibernate package name for the given
     * <code>version</code>.
     *
     * @return the base package name.
     */
    public String getBasePackage()
    {
        return this.isVersion3() || this.isVersion4() ? "org.hibernate" : "net.sf.hibernate";
    }

    /**
     * Gets the appropriate hibernate criterion package name for the given <code>version</code>.
     *
     * @return the Hibernate criterion package name.
     */
    public String getCriterionPackage()
    {
        return this.getBasePackage() + (this.isVersion3() || this.isVersion4() ? ".criterion" : ".expression");
    }

    /**
     * Gets the appropriate hibernate criterion package name for the given <code>version</code>.
     * @param entities 
     * @return the Hibernate criterion package name.
     */
    public String getEntityBasePackage(Collection<Entity> entities)
    {
        String base = "";
        List<String> packages = new ArrayList<String>();
        // Get unique packages containing entities
        for (Entity entity : entities)
        {
            String packageName = entity.getPackageName();
            if (!packages.contains(packageName))
            {
                // TODO: Allow entities in vastly different packages
                /*for (String pkgName : packages)
                {
                    if (packageName.length() < pkgName.length() &&
                        pkgName.contains(packageName))
                    {
                        // replace the longer contained package name in the List
                    }
                }*/
                packages.add(packageName);
            }
        }
        // Get unique top level packages containing entities
        /*for (String packageName : packages)
        {

        }*/
        // TODO Implement this function. Right now it just returns "" always
        return base;
    }

    /**
     * Gets the appropriate hibernate Restrictions/Expression fully qualified class name for the given <code>version</code>.
     *
     * @return the fully qualified Hibernate Restriction/Expression class name.
     */
    public String getRestrictionClass()
    {
        return getCriterionPackage() + (this.isVersion3() || this.isVersion4() ? ".Restrictions" : ".Expression");
    }

    /**
     * Gets the appropriate Spring Hibernate package based on the given
     * <code>version</code>.
     *
     * @return the spring hibernate package.
     */
    public String getSpringHibernatePackage()
    {
        return this.isVersion3() ? "org.springframework.orm.hibernate3" : this.isVersion4() ? "org.springframework.orm.hibernate4" : "org.springframework.orm.hibernate";
    }

    /**
     * Retrieves the appropriate package for Hibernate user types given
     * the version defined within this class.
     *
     * @return the hibernate user type package.
     */
    public String getEagerFetchMode()
    {
        return this.isVersion3() || this.isVersion4() ? "JOIN" : "EAGER";
    }

    /**
     * Retrieves the fully qualified name of the class that retrieves the Hibernate
     * disjunction instance.
     * @return the fully qualified class name.
     */
    public String getDisjunctionClassName()
    {
        return this.getCriterionPackage() + (this.isVersion3() || this.isVersion4() ? ".Restrictions" : ".Expression");
    }

    /**
     * Indicates whether or not version 2 is the one that is currently being used.
     *
     * @return true/false
     */
    public boolean isVersion2()
    {
        return isVersion2(hibernateVersion);
    }

    /**
     * Indicates whether or not version 3 is the one that is currently being used.
     *
     * @return true/false
     */
    public boolean isVersion3()
    {
        return isVersion3(hibernateVersion);
    }

    /**
     * Indicates whether or not version 4 is the one that is currently being used.
     *
     * @return true/false
     */
    public boolean isVersion4()
    {
        return isVersion4(hibernateVersion);
    }

    /**
     * @param hibernateVersionPropertyValue
     * @return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_2)
     */
    public static boolean isVersion2(String hibernateVersionPropertyValue)
    {
        return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_2);
    }

    /**
     * @param hibernateVersionPropertyValue
     * @return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_3)
     */
    public static boolean isVersion3(String hibernateVersionPropertyValue)
    {
        return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_3);
    }

    /**
     * @param hibernateVersionPropertyValue
     * @return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_4)
     */
    public static boolean isVersion4(String hibernateVersionPropertyValue)
    {
        return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_4);
    }

    /**
     * Denotes whether or not to make use of Hibernate 3 XML persistence support.
     */
    private String hibernateXmlPersistence;

    /**
     * @param hibernateXmlPersistenceIn <code>true</code> when you to make use of Hibernate 3 XML persistence support,
     *      <code>false</code> otherwise
     */
    public void setHibernateXMLPersistence(final String hibernateXmlPersistenceIn)
    {
        this.hibernateXmlPersistence = hibernateXmlPersistenceIn;
    }

    /**
     * @return isXmlPersistenceActive(hibernateVersion, hibernateXmlPersistence)
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
     * @return isVersion3(hibernateVersionPropertyValue) && "true".equalsIgnoreCase(hibernateXMLPersistencePropertyValue)
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
     * @param hibernateMappingStrategyIn
     */
    public void setHibernateMappingStrategy(String hibernateMappingStrategyIn)
    {
        this.hibernateMappingStrategy = hibernateMappingStrategyIn;
    }

    /**
     * @return mapSubclassesInSeparateFile(this.hibernateMappingStrategy)
     */
    public boolean isMapSubclassesInSeparateFile()
    {
        return mapSubclassesInSeparateFile(this.hibernateMappingStrategy);
    }

    /**
     * @param hibernateMappingStrategyIn
     * @return SpringGlobals.HIBERNATE_MAPPING_STRATEGY_SUBCLASS.equalsIgnoreCase(hibernateMappingStrategy)
     */
    public static boolean mapSubclassesInSeparateFile(
        String hibernateMappingStrategyIn)
    {
        // subclass or hierarchy
        return SpringGlobals.HIBERNATE_MAPPING_STRATEGY_SUBCLASS.equalsIgnoreCase(hibernateMappingStrategyIn);
    }
}
