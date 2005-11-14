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
                    if (object != null && Service.class.isAssignableFrom(object.getClass()))
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
        String packageName = "org.hibernate";
        if (!HibernateGlobals.HIBERNATE_VERSION_3.equals(this.hibernateVersion))
        {
            packageName = "net.sf.hibernate";
        }
        return packageName;
    }

    /**
     * Retrieves the appropriate package for Hibernate user types given
     * the version defined within this class.
     *
     * @return the hibernate user type package.
     */
    public String getHibernateUserTypePackage()
    {
        StringBuffer packageName = new StringBuffer();
        if (HibernateGlobals.HIBERNATE_VERSION_3.equals(this.hibernateVersion))
        {
            packageName.append(".usertype");
        }
        packageName.insert(
            0,
            this.getHibernatePackage());
        return packageName.toString();
    }
    
    /**
     * Indicates whether or not Hibernate 3 is enabled.
     * 
     * @return true/false
     */
    public boolean isVersion3()
    {
        return HibernateGlobals.HIBERNATE_VERSION_3.equals(this.hibernateVersion);
    }
}