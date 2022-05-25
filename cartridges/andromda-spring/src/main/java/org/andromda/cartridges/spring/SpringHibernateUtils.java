package org.andromda.cartridges.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic;
import org.andromda.cartridges.spring.metafacades.SpringGlobals;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang3.StringUtils;

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
    private String hibernateVersion = SpringGlobals.HIBERNATE_VERSION_5;

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
        return this.isVersion3() || this.isVersion4() || this.isVersion5() ? "org.hibernate" : "net.sf.hibernate";
    }

    /**
     * Gets the appropriate hibernate criterion package name for the given <code>version</code>.
     *
     * @return the Hibernate criterion package name.
     */
    public String getCriterionPackage()
    {
        return this.getBasePackage() + (this.isVersion3() || this.isVersion4() || this.isVersion5() ? ".criterion" : ".expression");
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
        return getCriterionPackage() + (this.isVersion3() || this.isVersion4() || this.isVersion5() ? ".Restrictions" : ".Expression");
    }

    /**
     * Gets the appropriate Spring Hibernate package based on the given
     * <code>version</code>.
     *
     * @return the spring hibernate package.
     */
    public String getSpringHibernatePackage()
    {
    	if(this.isVersion3()) {
    		return "org.springframework.orm.hibernate3";
    	} else if(this.isVersion4()) {
    		return "org.springframework.orm.hibernate4";
    	} else if(this.isVersion5()) {
    		return "org.springframework.orm.hibernate5";
    	} else {
    		return "org.springframework.orm.hibernate";
    	}
    }

    /**
     * Retrieves the appropriate package for Hibernate user types given
     * the version defined within this class.
     *
     * @return the hibernate user type package.
     */
    public String getEagerFetchMode()
    {
        return this.isVersion3() || this.isVersion4() || this.isVersion5() ? "JOIN" : "EAGER";
    }

    /**
     * Retrieves the fully qualified name of the class that retrieves the Hibernate
     * disjunction instance.
     * @return the fully qualified class name.
     */
    public String getDisjunctionClassName()
    {
        return this.getCriterionPackage() + (this.isVersion3() || this.isVersion4() || this.isVersion5() ? ".Restrictions" : ".Expression");
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
     * Indicates whether or not version 5 is the one that is currently being used.
     *
     * @return true/false
     */
    public boolean isVersion5()
    {
        return isVersion5(hibernateVersion);
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
     * @param hibernateVersionPropertyValue
     * @return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_5)
     */
    public static boolean isVersion5(String hibernateVersionPropertyValue)
    {
        return hibernateVersionPropertyValue.startsWith(SpringGlobals.HIBERNATE_VERSION_5);
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

    public String getAttributeName(String name) {

        String[] splits = name.split("\\.");
        String attributeName = splits[0];

        for (int i = 1; i < splits.length; i++) {
            attributeName = attributeName + StringUtils.capitalize(splits[i]);
        }

        return attributeName;
    }

    public String getFirstJoinName(String name) {

        if(name == null || name.length() == 0) {
            return null;
        }

        String[] splits = name.split("\\.");
        return splits[0];
    }

    public String getSecondJoinName(String name) {

        if(name == null || name.length() == 0) {
            return null;
        }
        
        String[] splits = name.split("\\.");

        if(splits.length > 2) {
            return null;
        }

        return splits[0];
    }

    public boolean isJoin(String attributeName) {
        return attributeName.contains(".");
    }

    public int joinLength(String attributeName) {
        return attributeName.split("\\.").length;
    }
    
    public Collection<String> getJoins(String attributeName) {

        String[] splits = attributeName.split("\\.");
        Collection<String> joins = new ArrayList<>();
        joins.add("javax.persistence.criteria.Join " + splits[0] + "Join = root.join(\"" + splits[0] + "\")");

        for (int i = 1; i < splits.length-1; i++) {
            String join = "javax.persistence.criteria.Join " + splits[i] + "Join = ";
            join = join + splits[i-1] + "Join.join(\"" + splits[i] + "\")";
            joins.add(join);
        }

        return joins;
    }

    public String getCriteriaAttributeMethodName(SpringCriteriaAttributeLogic criteriaAttribute) {

        StringBuilder builder = new StringBuilder();
        builder.append("findBy");
        builder.append(StringUtils.capitalize(getAttributeName(criteriaAttribute.getAttributeName())));

        if(criteriaAttribute.isComparatorPresent() && criteriaAttribute.isMatchModePresent()) {

            if(criteriaAttribute.getComparator().equals("insensitive_like")
                || criteriaAttribute.getComparator().equals("like")) {

                if(criteriaAttribute.getMatchMode().equals("end")) {
                    builder.append("EndingWithIgnoreCase");
                } else if(criteriaAttribute.getMatchMode().equals("start")) {

                    builder.append("StartingWithIgnoreCase");
                } else if(criteriaAttribute.getMatchMode().equals("anywhere")) {
                    builder.append("ContainingIgnoreCase");
                }
            } else {
                builder.append(getMatchMode(criteriaAttribute));
            }

        } else if(criteriaAttribute.isComparatorPresent()) {
            builder.append(getMatchMode(criteriaAttribute));
        } else if(criteriaAttribute.isMatchModePresent()) {
            builder.append(getMatchMode(criteriaAttribute));
        }

        return builder.toString();
    }

    private String getMatchMode(SpringCriteriaAttributeLogic criteriaAttribute) {
        if(criteriaAttribute.getMatchMode().equals("greater")) {

            return "Greater";

        } else if(criteriaAttribute.getMatchMode().equals("greater_equal")) {
            return "GreaterThanEqual";

        } else if(criteriaAttribute.getMatchMode().equals("less")) {
            return "Less";

        } else if(criteriaAttribute.getMatchMode().equals("less_equal")) {
            return "LessThanEqual";
        } else if(criteriaAttribute.getMatchMode().equals("not_equal")) {
            return "NotEqual";
        } else if(criteriaAttribute.getMatchMode().equals("in") || criteriaAttribute.isMany() || criteriaAttribute.getType().isCollectionType()) {
            return "In";
        }

        return "";
    }
}
