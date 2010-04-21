package org.andromda.cartridges.spring;

import org.andromda.cartridges.spring.metafacades.SpringGlobals;

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
        return this.isVersion3() ? "org.hibernate" : "net.sf.hibernate";
    }

    /**
     * Gets the appropriate hibernate criterion package name for the given <code>version</code>.
     *
     * @return the Hibernate criterion package name.
     */
    public String getCriterionPackage()
    {
        return this.getBasePackage() + (this.isVersion3() ? ".criterion" : ".expression");
    }

    /**
     * Gets the appropriate hibernate Restrictions/Expression fully qualified class name for the given <code>version</code>.
     *
     * @return the fully qualified Hibernate Restriction/Expression class name.
     */
    public String getRestrictionClass()
    {
        return getCriterionPackage() + (this.isVersion3() ? ".Restrictions" : ".Expression");
    }

    /**
     * Gets the appropriate Spring Hibernate package based on the given
     * <code>version</code>.
     *
     * @return the spring hibernate package.
     */
    public String getSpringHibernatePackage()
    {
        return this.isVersion3() ? "org.springframework.orm.hibernate3" : "org.springframework.orm.hibernate";
    }

    /**
     * Retrieves the appropriate package for Hibernate user types given
     * the version defined within this class.
     *
     * @return the hibernate user type package.
     */
    public String getEagerFetchMode()
    {
        return this.isVersion3() ? "JOIN" : "EAGER";
    }

    /**
     * Retrieves the fully qualified name of the class that retrieves the Hibernate
     * disjunction instance.
     * @return the fully qualified class name.
     */
    public String getDisjunctionClassName()
    {
        return this.getCriterionPackage() + (this.isVersion3() ? ".Restrictions" : ".Expression");
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
     * @param hibernateVersionPropertyValue
     * @return SpringGlobals.HIBERNATE_VERSION_3.equals(hibernateVersionPropertyValue)
     */
    public static boolean isVersion3(String hibernateVersionPropertyValue)
    {
        return SpringGlobals.HIBERNATE_VERSION_3.equals(hibernateVersionPropertyValue);
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
