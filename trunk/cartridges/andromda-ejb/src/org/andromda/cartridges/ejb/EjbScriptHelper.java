package org.andromda.cartridges.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.simpleuml.SimpleOOHelper;
import org.andromda.core.simpleuml.UMLClassifier;
import org.andromda.core.uml14.DirectionalAssociationEnd;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.AggregationKind;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

/**
 * Transform class for the EJB cartridge.
 * @author Richard Kunze
 */
public class EjbScriptHelper extends SimpleOOHelper {

    /**
     * Find the primary key type name for <code>obj</code>.
     * If <code>obj</code> is of type {@link Classifier} or {@link UMLClassifier}, 
     * the primary key type is determined as follows:
     * <ol>
     * <li>If <code>obj</code> has a dependency with stereotype
     *  &lt;&lt;PrimaryKey&gt;&gt;, the primary key type is the supplier
     * of this dependency.
     * <li>Otherwise, if <code>obj</code> has an attribute with stereotype
     * &lt;&lt;PrimaryKey&gt;&gt;, the primary key type is the 
     * type of this attribute
     * <li>If there is neither a dependency nor an attribute with the
     * &lt;&lt;PrimaryKey&gt;&gt; stereotype, recurse up the inheritance tree.
     * <li>Finally, if no explict primary key type can be found, return
     * <code>defaultPK</code>  
     * </ol>
     * <p>If <code>obj</code> is any other type, <code>defaultPK</code> 
     * is returned.
     * @param obj the entity to check
     * @param defaultPK the the default PK type name to use if no PK type is found
     * @return the fully qualified name of the primary key type
     */
    public String findPkTypeName(Object obj, String defaultPK) {
        Classifier entity;
        if (obj instanceof UMLClassifier) {
            entity = (Classifier) ((UMLClassifier) obj).getId();
        } else if (obj instanceof Classifier) {
            entity = (Classifier) obj;
        } else {
            return defaultPK;
        }

        Iterator iter = getDependencies(entity).iterator();
        while (iter.hasNext()) {
            Dependency dep = (Dependency) iter.next();
            if ("PrimaryKey".equals(getStereotype(dep))) {
                return getFullyQualifiedName(
                    dep.getSupplier().iterator().next());
            }
        }
        // No PK dependency found - try a PK attribute
        Attribute attr = getPrimaryKeyAttribute(entity);
        if (attr != null) {
            String retval = findFullyQualifiedName(attr.getType());
            if (retval != null) {
                return retval;
            }
        }

        // Still nothing found - recurse up the inheritance tree
        GeneralizableElement parent = getGeneralization(entity);
        return findPkTypeName(parent, defaultPK);
    }
    
    /**
     * Find the names of the primary key fields for <code>obj</code>.
     * If <code>obj</code> is of type {@link Classifier} or {@link UMLClassifier}, 
     * the primary key type is determined as follows:
     * @param obj the entity to check
     * @return a collection of strings
     * @see #findPkFields()
     */
    public Collection findPkFieldNames(Object obj) {
        Collection fields = findPkFields(obj);
        Collection names = new ArrayList(fields.size());
        for (Iterator i = fields.iterator(); i.hasNext();) {
            Attribute attr = (Attribute) i.next();
            names.add(attr.getName());
        }
        return names;
    }
    
    /**
     * Check if the primary key for <code>class</code> has been specified with
     * a dependency to an external class.
     * @param clazz the class to check
     * @return <code>true</code> if the primary key has ben specified by a dependency, 
     * <code>false</code> if it has been specified by an attribute marked as 
     * &lt;&lt;PrimaryKey&gt;&/gt;
     */
    public boolean hasComplexPrimaryKey(Object clazz) {
        return getSimplePkField(clazz) == null;
    }

    /** 
     * If this <code>object</code> does not have a complex primaray key, get the
     * (unqiue) attribute that is used as the primary key.
     * @param object the class to check
     * @return the attribute used as primary key, or <code>null</code> if there is
     * none or the class has a complex primary key.
     */
    public Attribute getSimplePkField(Object object) {
        Collection c = findPkFields(object);
        if (c.size() != 1) {
            return null;
        } else {
            Attribute pkField = (Attribute)c.iterator().next();
            if ("PrimaryKey".equals(getStereotype(pkField))) {
                return pkField;
            } else {
                return null;
            }
        }
    }

    
    /**
     * Find the primary key fields for <code>obj</code>.
     * If <code>obj</code> is of type {@link Classifier} or {@link UMLClassifier}, 
     * the primary key type is determined as follows:
     * <ol>
     * <li>If <code>obj</code> has a dependency with stereotype
     *  &lt;&lt;PrimaryKey&gt;&gt;, the primary key fields are 
     * the public attributes of the supplier of this dependency.
     * <li>Otherwise, if <code>obj</code> has an attribute with stereotype
     * &lt;&lt;PrimaryKey&gt;&gt;, the primary key field is this attribute
     * <li>If there is neither a dependency nor an attribute with the
     * &lt;&lt;PrimaryKey&gt;&gt; stereotype, recurse up the inheritance tree.
     * <li>Finally, if no explict primary key type can be found, return
     * an empty list  
     * </ol>
     * <p>If <code>obj</code> is any other type, an empty list 
     * is returned.
     * @param obj the entity to check
     * @return a collection of {@link Attribute} objects
     */
    public Collection findPkFields(Object obj) {
        Classifier entity;
        if (obj instanceof UMLClassifier) {
            entity = (Classifier) ((UMLClassifier) obj).getId();
        } else if (obj instanceof Classifier) {
            entity = (Classifier) obj;
        } else {
            return Collections.EMPTY_LIST;
        }

        Iterator iter = getDependencies(entity).iterator();
        while (iter.hasNext()) {
            Dependency dep = (Dependency) iter.next();
            if ("PrimaryKey".equals(getStereotype(dep))) {
                Collection allAttrib = 
                  getInstanceAttributes(dep.getSupplier().iterator().next());
                Collection publicAttrib = new ArrayList();
                for (Iterator i = allAttrib.iterator(); i.hasNext();) {
                    Attribute att = (Attribute) i.next();
                    if ("public".equals(getVisibility(att))) {
                      publicAttrib.add(att);
                    }
                }
                return publicAttrib;
            }
        }
        // No PK dependency found - try a PK attribute
        Attribute attr = getPrimaryKeyAttribute(entity);
        if (attr != null) {
            Collection retval = new ArrayList(1);
            retval.add(attr);
            return retval;
        }

        // Still nothing found - recurse up the inheritance tree
        GeneralizableElement parent = getGeneralization(entity);
        return findPkFields(parent);
    }
    
    /** Get the target type for a relation. If the relation target has
     * a multiplicity of 0..1 or 1, this is the fully qualified type name of
     * the target. If it has a multiplicity of >1, this is the string
     * "java.util.Collection" 
     * @param rel the relation
     * @return the type name for the target end
     */ 
    public String getRelationTargetType(DirectionalAssociationEnd rel) {
        if (rel.isMany2Many() || rel.isOne2Many()) {
            return "java.util.Collection"; 
        } else {
            return findFullyQualifiedName(rel.getTarget().getParticipant());
        }
    }

    /** Get the target type for a relation. If the relation target has
     * a multiplicity of 0..1 or 1, this is the fully qualified type name of
     * the target. If it has a multiplicity of >1, this is the string
     * "java.util.Collection" 
     * @param rel the relation
     * @return the type name for the target end
     */
    public String getRelationTargetType(AssociationEnd rel) {
        return getRelationTargetType(getAssociationData(rel));    
    }
    
    /** 
     * Get the EJB relation name for <code>assoc</code>.
     *  
     * <p>The relation name follows the pattern 
     * <em>&lt;leftType&gt;&lt;seperator&gt;&lt;rightType&gt;</em>.
     * <em>&lt;leftType&gt;</em> is the type name at the left end of 
     * the relation and <em>&lt;rightType&gt;</em> the type name at 
     * the oposite end.</p>  
     * 
     * <p>Left and right ends are determined as follows:</p>
     * 
     * <ul>
     * <li>If the relation is only navigable in one direction then the
     * left end is the end that is not navigable.
     * <li>Otherwise, if the relation is an aggregation or composition, 
     * the aggregated end is the left end.
     * <li>In all other cases, the source end is the end with the type name
     * that is lexically smaller.
     * </ul>
     * 
     * <p>The type name for each association end is determined as follows:</p>
     * <ul>
     * <li>If the association end is the {@linkplain 
     * DirectionalAssociationEnd#getTarget() target} end of <code>assoc</code>, 
     * then the type name is the name of the corresponding class.
     * <li>If the association end is the {@linkplain 
     * DirectionalAssociationEnd#getSource() source} end of <code>assoc</code>,
     * the type name is <code>sourceTypeName</code>.
     * </ul>
     * 
     * <p>The seperator is generated as follows:</p>
     * <ul>
     * <li>If the association has a name, the seperator is 
     * <code>-<em>&lt;name&gt;</em>-</code>, where <em>&lt;name&gt;</em> is the
     * association name with all spaces replaced by "-"
     * <li>Otherwise, if the source end is a composition, the seperator is 
     * the string <code>-consists-of-</&code>
     * <li>Otherwise, if the source end is an aggregation, the seperator is 
     * the string <code>-has-</&code>
     * <li>Otherwise, if the source end is not navigable, the seperator is 
     * the string <code>-&gt;</&code>
     * <li>In all other cases, the seperator is the string <code>&lt;-&gt;</&code>
     * </ul>
     * 
     * @param obj the association or association end
     * @param sourceTypeName the name of the class at the source end.
     * @return the relation name or <code>null</code> if <code>obj</code> is
     * not of type {@link UmlAssociation}, {@link AssociationEnd} or 
     * {@link DirectionalAssociationEnd}
     */
    public String findEjbRelationName(DirectionalAssociationEnd assoc, String sourceName) {
        AssociationEnd source = assoc.getSource();
        AssociationEnd target = assoc.getTarget();
        
        // Get the seperator
        String seperator = source.getAssociation().getName();
        if (seperator != null) {
            seperator = seperator.trim();
            if (seperator.length() == 0) {
                seperator = null;
            } else {
                seperator = "-" + seperator.replace(' ', '-') + "-";
            }
        }        
        if (seperator == null) {
            AggregationKind aggregation = source.getAggregation();
            if (AggregationKindEnum.AK_NONE.equals(aggregation)) {
                aggregation = target.getAggregation();
            }
            if (AggregationKindEnum.AK_AGGREGATE.equals(aggregation)) {
                seperator = "-has-";
            } else if (AggregationKindEnum.AK_COMPOSITE.equals(aggregation)) {
                seperator = "-consists-of-";
            } else if (!(target.isNavigable() && source.isNavigable())) {
                seperator = "->";
            } else {
                seperator = "-";
            }
        }
        
        boolean srcIsAggregate =
            source.getAggregation() != null
                && !AggregationKindEnum.AK_NONE.equals(source.getAggregation());
        boolean targetIsAggregate =
            target.getAggregation() != null
                && !AggregationKindEnum.AK_NONE.equals(target.getAggregation());

        // Generate the names. Swap sides if necessary.
        if ((source.isNavigable() && !target.isNavigable())
            || (!srcIsAggregate && targetIsAggregate)
            || (!srcIsAggregate 
                && source.isNavigable()
                && target.isNavigable()
                && source.getParticipant().getName().compareTo(
                    target.getParticipant().getName())
                    > 0)) {
            return target.getParticipant().getName()
                + seperator
                + sourceName;
        } else {
            return sourceName
                + seperator
                + target.getParticipant().getName();
        }

    }

    /**
     * Find all associations that define relations to other entities.
     * <p>This method returns the {@linkplain AssociationEnd 
     * source association end} for all associations that define 
     * a container managed relation in <code>sourceType</code>.</p> 
     * 
     * <p>The returned collection includes both 
     * <em>direct relations</em> and <em>inherited relations</em>. A 
     * <em>direct relation</em> is an association of <code>sourceType</code> 
     * with some other class matching the following criteria:</p>
     * <ul>
     * <li>The class at the other side of the association is 
     * stereotyped &lt;&lt;Entity&gt;&gt;
     * <li>The association is navigable from <code>sourceType</code> to 
     * the other side.
     * </ul>
     * <p>An <em>inherited relation</em> is an asscoiated from an  
     * abstract super type of <code>sourceClass</code> matching 
     * the following criteria:
     * <ul>
     * <li>the inheritance path from <code>sourceType</code> to this abstract
     * super type, including this super type itself, consists only of 
     * abstract classes with stereotype &lt;&lt;Entity&gt;&gt;
     * <li>The class at the other side of the association is 
     * stereotyped &lt;&lt;Entity&gt;&gt;
     * <li>The association is navigable from this abstract super type of 
     * <code>sourceType</code> to the other side.
     * </ul>
     * 
     * <p>Relations must match the following integrity 
     * constraint:</p>
     * <ul>
     * <li>The &lt;&lt;Entity&gt;&gt; at the target end is not abstract
     * </ul>
     * <p>The integrity constraint is necessary because the target of 
     * a container managed relation in the EJB framework must be a 
     * concrete entity bean &mdash; there is no such thing as an 
     * "abstract entity bean" in the EJB specification. 
     * It is possible, however, to generate and compile code for this case, 
     * an error will only show up at deploy time. In order to catch 
     * this kind of error at the earliest possible stage, 
     * this method checks the integrity constraint and 
     * throws an exception if it is violated.</p>
     * 
     * @param sourceType the class at the source end of the associations
     * @return a collection of {@link AssociationEnd} objects. 
     * @throw IllegalStateException if a relation violates the integrity constraint
     */
    public Collection findEntityRelationsForSource(Object sourceType) {
        GeneralizableElement entity;
        if (sourceType instanceof UMLClassifier) {
            entity = (Classifier) ((UMLClassifier) sourceType).getId();
        } else if (sourceType instanceof Classifier) {
            entity = (Classifier) sourceType;
        } else {
            return Collections.EMPTY_LIST;
        }

        // Only concrete entities may have EJB relations. Return 
        // an empty collection for everything else
        if (entity.isAbstract() || !isEntity(entity)) {
            return Collections.EMPTY_LIST;
        }
        
        Collection result = new ArrayList();
        do {
            result.addAll(getEntityRelations(entity));
            entity = getGeneralization(entity);
        } while (entity != null && entity.isAbstract() && isEntity(entity));
        return result;
    }
    
    /**
     * Get the associations that define relations to other entities.
     * This is similar to {@link  #findEntityRelationsForSource()}, but gets
     * only relations that are defined directly on <code>sourceType</code> and
     * it returns relations even if <code>sourceType</code> is an abstract entity.  
     * @throw IllegalStateException if a relation violates the 
     * integrity constraint
     */
    public Collection getEntityRelations(Object sourceType) {
        GeneralizableElement entity;
        if (sourceType instanceof UMLClassifier) {
            entity = (Classifier) ((UMLClassifier) sourceType).getId();
        } else if (sourceType instanceof Classifier) {
            entity = (Classifier) sourceType;
        } else {
            return Collections.EMPTY_LIST;
        }
        
        // Only entities may have EJB relations. Return 
        // an empty collection for everything else
        if (!isEntity(entity)) {
            return Collections.EMPTY_LIST;
        }
        
        Collection result = new ArrayList();
        for (Iterator i = getAssociationEnds(entity).iterator();
            i.hasNext();
            ) {
            DirectionalAssociationEnd assoc =
                getAssociationData((AssociationEnd) i.next());
            Classifier target = assoc.getTarget().getParticipant();
            if (isEntity(target) && assoc.getTarget().isNavigable()) {
                // Check the integrity constraint
                if (target.isAbstract()
                    && !"false".equalsIgnoreCase(
                        findTagValue(
                            assoc.getTarget().getAssociation(),
                            "@andromda.ejb.generateCMR"))) {
                    throw new IllegalStateException(
                        "Relation '"
                            + findEjbRelationName(
                                assoc,
                                assoc.getSource().getParticipant().getName())
                            + "' has the abstract target '"
                            + target.getName()
                            + "'. Abstract targets are not allowed in EJB.");
                } else {
                    result.add(assoc);
                }
            }
        }
        return result;
    }

    private boolean isEntity(GeneralizableElement entity) {
        Collection stereotypes = entity.getStereotype();
        for (Iterator i = stereotypes.iterator(); i.hasNext();) {
            ModelElement stereotype = (ModelElement) i.next();
            if ("Entity".equals(stereotype.getName())) {
                return true;
            }
        }
        return false;
    }
    
    /** Check if <code>attr</code> is read only.
     * 
     * @param attr the attribute to check
     * @return <code>true</code> if <code>attr</code> is read only, 
     * <code>false</code> else
     */
    public boolean isReadOnly(Attribute attr) {
        return ChangeableKindEnum.CK_FROZEN.equals(attr.getChangeability());
    }
    
    /** Check if <code>assoc</code> is read only.
     * 
     * @param assoc the association end to check
     * @return <code>true</code> if <code>assoc</code> is read only, 
     * <code>false</code> else
     */
    public boolean isReadOnly(AssociationEnd assoc) {
        return ChangeableKindEnum.CK_FROZEN.equals(assoc.getChangeability());
    }
    
    /** Check if <code>feature</code> is declared static.
     * 
     * @param feature the structural feature to check
     * @return <code>true</code> if <code>feature</code> is static, 
     * <code>false</code> else
     */
    public boolean isStatic(Feature feature) {
        return ScopeKindEnum.SK_CLASSIFIER.equals(feature.getOwnerScope());
    }

    /** 
     * Get all attributes for <code>type</code>. The returned 
     * list includes the attributes that are 
     * inherited from super classes. The list is contains the
     * inherited attributes first, followed by the attributes 
     * defined in this class.
     * @param type the type to get the attributes for
     * @return a List of {@link Attribute} objects
     */ 
    public List getAllInstanceAttributes(Object type) {
        List retval = getInheritedInstanceAttributes(type);
        retval.addAll(getInstanceAttributes(type));
        return retval;
    }
    
    /** 
     * Get all inherited attributes for <code>type</code>.
     * The attributes are grouped by the class that defines 
     * the attributes, with attributes from the most removed super class first. 
     * @param type the type to get the inherited attributes for
     * @return a List of {@link Attribute} objects
     */ 
    public List getInheritedInstanceAttributes(Object type) {
        GeneralizableElement current = getGeneralization(type);
        if (current == null) {
            return new ArrayList();
        } else {
            List retval = getInheritedInstanceAttributes(current);
            retval.addAll(getInstanceAttributes(current));
            return retval;
        }
    }

    /**
     * Gets the non-static attributes of the specified Classifier object.
     *
     *@param  object  Classifier object
     *@return  Collection of org.omg.uml.foundation.core.Attribute
     */
    public Collection getInstanceAttributes(Object object)
    {
        if ((object == null) || !(object instanceof Classifier))
        {
            return Collections.EMPTY_LIST;
        }

        Classifier classifier = (Classifier) object;
        Collection attributes = new ArrayList(); 
        for (Iterator i = classifier.getFeature().iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof Attribute && !isStatic((Attribute)o)) {
                 attributes.add(o);
            }
        }
        return attributes;
    }

    /**
     * Gets the static attributes of the specified Classifier object.
     *
     *@param  object  Classifier object
     *@return  Collection of org.omg.uml.foundation.core.Attribute
     */
    public Collection getStaticAttributes(Object object)
    {
        if ((object == null) || !(object instanceof Classifier))
        {
            return Collections.EMPTY_LIST;
        }

        Classifier classifier = (Classifier) object;
        Collection attributes = new ArrayList(); 
        for (Iterator i = classifier.getFeature().iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof Attribute && isStatic((Attribute)o)) {
                 attributes.add(o);
            }
        }
        return attributes;
    }    


    /** Create a comma seperated list of attributes.
     * This method can be used to generated e.g. argument lists for 
     * constructors, method calls etc.
     * @param attributes a collection of {@link Attribute} objects
     * @param includeTypes if <code>true</code>, the type names of 
     * the attributes are included.
     * @param includeNames if <code>true</code>, the 
     * names of the attributes are included
     * 
     * @author richard
     */
    public String getAttributesAsList2(Collection attributes,
                                      boolean includeTypes,
                                      boolean includeNames) {
        if (!includeNames && !includeTypes) {
            return "";
        }
        
        StringBuffer sb = new StringBuffer();
        String separator = "";

        for (Iterator it = attributes.iterator(); it.hasNext();) {
            Attribute attr = (Attribute)it.next();
            sb.append(separator);
            separator = ", ";
            if (includeTypes) {
                sb.append(findFullyQualifiedName(attr.getType()));
                sb.append(" ");
            }
            if (includeNames) {
                sb.append(attr.getName());
            }
        }
        return sb.toString();
    }
    
    /** Convert the visibility of <code>feature</code> into a string
     * usable in Java source code if it is one of the predefined constants
     * in {@link VisibilityKindEnum}. If the visibility is not 
     * one of these constants, this methods returns the string representation
     * of the visibility.
     * Note: This method really belongs into {@link UMLStaticHelper}
     * @param element a UML class, attribute or operation
     * @return a string representing the visibility
     */
    public String getVisibility(ModelElement element) {
        VisibilityKind vis = element.getVisibility();
        if (VisibilityKindEnum.VK_PUBLIC.equals(vis)) {
            return "public";        
        } else if (VisibilityKindEnum.VK_PACKAGE.equals(vis)) {
            return "";
        } else if (VisibilityKindEnum.VK_PROTECTED.equals(vis)) {
            return "protected";
        } else if (VisibilityKindEnum.VK_PRIVATE.equals(vis)) {
            return "private";
        } else {
            return vis.toString();
        }
    }

    /** Get all create methods for <code>element</code>.
     * This includes create methods that are defined on a super type
     * of <code>element</code>
     */
    public Collection getAllCreateMethods(GeneralizableElement element) {
        Collection retval = new ArrayList();
        do {
            Collection ops = getOperations(element);
            for (Iterator i = ops.iterator(); i.hasNext();) {
                Operation op = (Operation) i.next();
                if (getStereotype(op).equals("CreateMethod")) {
                    retval.add(op);
                }
            }
            element = getGeneralization(element);
        } while (element != null);
        return retval;
    }

    /** Get all select methods for <code>element</code>.
     * This includes select methods that are defined on a super type
     * of <code>element</code>
     */
    public Collection getAllSelectMethods(GeneralizableElement element) {
        Collection retval = new ArrayList();
        do {
            Collection ops = getOperations(element);
            for (Iterator i = ops.iterator(); i.hasNext();) {
                Operation op = (Operation) i.next();
                if (getStereotype(op).equals("SelectMethod")) {
                    retval.add(op);
                }
            }
            element = getGeneralization(element);
        } while (element != null);
        return retval;
    }

    /**
     * Builds a comma-separated list of parameter types of an operation.
     * This really belongs in the super class.
     * @param o the operation
     * @return String the list of parameter type names
     */
    public String getOperationParameterTypeNames(Operation o)
    {
        StringBuffer sb = new StringBuffer();
        
        Iterator it = o.getParameter().iterator();
        
        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();
        
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(getFullyQualifiedName(p.getType()));
                commaNeeded = true;
            }
        }
        return sb.toString();
    }
    
    /** Replaces all occurences of <code>orig</code> in <code>str</code> with
     * <code>replace</code>. This really belongs into the string utils helper...
     * 
     * @param str the string to replace
     * @param orig the old substring
     * @param replace the replacement
     * @return the new string
     */
    public String replace(String str, String orig, String replace) {
        int idx = str.indexOf(orig);
        if (idx == -1) {
            return str;
        }
        
        StringBuffer result = new StringBuffer(str.length());
        int current = 0;
        do {
            result.append(str.substring(current, idx));
            result.append(replace);
            current = idx + orig.length();
            idx = str.indexOf(orig, current);
        } while (idx != -1);
        result.append(str.substring(current));
        return result.toString();
    }
    
    /** Filter a list of model elements by visibility
     * @param list the original list
     * @param visibility the visibility - "public" "protected", "private" or the
     * empty string (for package visibility)
     * @return a list with all elements from the original list that have 
     * a matching visibility.
     */
    public Collection filterByVisibility(Collection list, String visibility) { 
        Collection retval = new ArrayList(list.size());
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            ModelElement elem = (ModelElement)iter.next();
            if (visibility.equals(getVisibility(elem))) {
                retval.add(elem);
            }
        }
        return retval;
    }
    
}
