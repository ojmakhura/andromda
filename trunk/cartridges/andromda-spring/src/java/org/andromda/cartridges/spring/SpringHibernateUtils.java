package org.andromda.cartridges.spring;


/**
 * Contains utilities used within the Spring cartridge
 * when dealing with Hibernate.
 *
 * @author Chad Brandon
 */
public class SpringHibernateUtils
{
    /**
     * The version of Hibernate we're generating for.
     */
    private String hibernateVersion = "3";

    /**
     * Sets the version of Hibernate we're generating for.
     *
     * @param hibernateVersion the Hibernate version.
     */
    public void setHibernateVersion(final String hibernateVersion)
    {
        this.hibernateVersion = hibernateVersion;
    }

    /**
     * The Hibernate 2 version number (for determining the
     * correct package).
     */
    private static final String VERSION_2 = "2";

    /**
     * Gets the appropriate hibernate package name for the given
     * <code>version</code>.
     *
     * @return the base package name.
     */
    public String getBasePackage()
    {
        String packageName = "org.hibernate";
        if (VERSION_2.equals(hibernateVersion))
        {
            packageName = "net.sf.hibernate";
        }
        return packageName;
    }

    /**
     * Gets the appropriate hibernate criterion package name for the given <code>version</code>.
     *
     * @return the Hibernate criterion package name.
     */
    public String getCriterionPackage()
    {
        StringBuffer packageName = new StringBuffer();
        if (VERSION_2.equals(hibernateVersion))
        {
            packageName.append(".expression");
        }
        else
        {
            packageName.append(".criterion");
        }
        packageName.insert(
            0,
            this.getBasePackage());
        return packageName.toString();
    }

    /**
     * Gets the appropriate Spring Hibernate package based on the given
     * <code>version</code>.
     * @return
     */
    public String getSpringHibernatePackage()
    {
        String packageName = "org.springframework.orm.hibernate3";
        if (VERSION_2.equals(hibernateVersion))
        {
            packageName = "org.springframework.orm.hibernate";
        }
        return packageName;
    }
}