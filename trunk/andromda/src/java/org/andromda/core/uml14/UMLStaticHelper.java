package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.StructuralFeature;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * Extends the UMLDefaultHelper with a set of operations that are useful
 * for exploring the static parts of UML v1.4 based object models.
 *
 *@author    Anthony Mowers
 */
public class UMLStaticHelper extends UMLDefaultHelper implements ScriptHelper {
    
    /** 
    * Returns the name of a model element fully qualified by the 
    * name of the package that contains it. If the model element 
    * is a primitive type it will return the primitive type itself. 
    * 
    *@param object model element 
    *@return fully qualifed name 
    */
    public String getFullyQualifiedName(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return null;
        }

        ModelElement modelElement = (ModelElement) object;

        String fullName = modelElement.getName();

        if (StringUtilsHelper.isPrimitiveType(fullName)) {
            return fullName;
        }

        String packageName = getPackageName(modelElement);
        fullName =
            "".equals(packageName) ? fullName : packageName + "." + fullName;

        return fullName;
    }

    /**
     * Returns the collection of taggedValues for a given modelElement
     * 
     * @param object model element
     * @return Collection of org.omg.uml.foundation.core.TaggedValue
     * 
     */
    public Collection getTaggedValues(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return Collections.EMPTY_LIST;
        }

        ModelElement modelElement = (ModelElement) object;

        return modelElement.getTaggedValue();
    }

    /**
     * Searches a collection of tag values for one with a particular
     * name
     * 
     * @param Collection of taggedValues
     * @param tagName name of tag for which to search
     * 
     * @return value of tag, null if tag not found
     */
    public String findTagValue(Collection taggedValues, String tagName) {
        for (Iterator i = taggedValues.iterator(); i.hasNext();) {
            TaggedValue taggedValue = (TaggedValue) i.next();
            String tgvName = getName(taggedValue);

            if (tagName.equals(tgvName)) {
                Iterator it = taggedValue.getDataValue().iterator();
                if (it.hasNext()) {
                    return it.next().toString();
                }
                return null;
            }
        }

        return null;
    }

    /**
     * Searches for and returns the value of a given tag on
     * the specified model element.
     * 
     * @param modelElement model element
     * @param tagName  name of the tag
     * @return String value of tag, <b>null</b> if tag not found
     * 
     */
    public String findTagValue(ModelElement modelElement, String tagName) {
        return findTagValue(getTaggedValues(modelElement), tagName);
    }

    /**
     * Returns Association information from the perspective of
     * a particular end of the association.
     * 
     * <p>The returned object can answers information about
     * whether the assocation is one2many, many2one, ...
     * </p>
     * 
     * @param object assocation end
     * @return DirectionalAssociationEnd directional association data
     * 
     */
    public DirectionalAssociationEnd getAssociationData(Object object) {
        if ((object == null) || !(object instanceof AssociationEnd)) {
            return null;
        }

        AssociationEnd ae = (AssociationEnd) object;

        return new DirectionalAssociationEnd(ae);
    }

    /**
     * Searches the given class feature (operation or attribute) for
     * the specified tag.
     * 
     * <p>If the follow boolean is set to true then the search will
     * continue from the class feature to the class itself and then
     * up the class hiearchy.</p>
     * 
     * @param feature attribute or operation object
     * @param tagName name of the tag to search for
     * @param follow <b>true</b> if search should follow inheritance
     * hierarchy
     * @return String value of tag, <b>null</b> if tag not found
     * 
     */
    public String findTagValue(
        StructuralFeature feature,
        String tagName,
        boolean follow) {
        if (feature == null)
            return null;

        String value = findTagValue(feature, tagName);
        ModelElement element = feature.getType();
        while ((value == null) && (element != null)) {
            value = findTagValue(element, tagName);
            element = getGeneralization(element);
        }

        return value;
    }

    /**
     * Returns the collection of dependencies for a given model element.
     * 
     * <p>Abstraction/Interface implements dependencies will not be 
     * included in this collection.</b>
     * 
     *@param  object  model element
     *@return Collection of org.omg.uml.foundation.core.Dependency
     */
    public Collection getDependencies(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return Collections.EMPTY_LIST;
        }

        ModelElement modelElement = (ModelElement) object;

        Collection clientDependencies =
            model.getCore().getAClientClientDependency().getClientDependency(
                modelElement);

        return new FilteredCollection(clientDependencies) {
            protected boolean accept(Object object) {
                return (object instanceof Dependency)
                    && !(object instanceof Abstraction);
            }
        };
    }

    /**
     *  Gets the attributes of the specified Classifier object.
     *
     *@param  object  Classifier object
     *@return  Collection of org.omg.uml.foundation.core.Attribute
     */
    public Collection getAttributes(Object object) {
        if ((object == null) || !(object instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }

        Classifier classifier = (Classifier) object;
        Collection features = new FilteredCollection(classifier.getFeature()) {
            protected boolean accept(Object object) {
                return object instanceof Attribute;
            }
        };

        return features;
    }

    /**
     *  Gets the operations of the specified Classifier object.
     *
     *@param  object  Classifier object
     *@return  Collection of org.omg.uml.foundation.core.Operation
     */
    public Collection getOperations(Object object) {
        if ((object == null) || !(object instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }

        Classifier classifier = (Classifier) object;
        Collection features = new FilteredCollection(classifier.getFeature()) {
            protected boolean accept(Object object) {
                return object instanceof Operation;
            }
        };

        return features;
    }

    /**
     * Gets the assocation ends that are attached to the specified
     * Classifier object.
     *
     *@param  object  Classifier object
     *@return  Collection of org.omg.uml.foundation.core.AssociationEnd
     */
    public Collection getAssociationEnds(Object object) {
        if ((object == null) || !(object instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }

        Classifier classifier = (Classifier) object;
        return model.getCore().getAParticipantAssociation().getAssociation(
            classifier);
    }

    /**
     * Returns the generalization/superclass for the given model generalizable
     * model element (i.e. Class).
     *
     * @param object model element
     * @return GeneralizableElement super class
     */
    public GeneralizableElement getGeneralization(Object object) {
        if ((object == null) || !(object instanceof GeneralizableElement)) {
            return null;
        }

        GeneralizableElement element = (GeneralizableElement) object;
        Iterator i =
            model
                .getCore()
                .getAChildGeneralization()
                .getGeneralization(element)
                .iterator();
        if (i.hasNext()) {
            Generalization generalization = (Generalization) i.next();
            return generalization.getParent();
        }

        return null;
    }

    /**
     * Returns the collection of interfaces implemented by the given
     * Classifier object.
     * 
     * @param object  Class
     * @return Collection of Interfaces
     */
    public Collection getAbstractions(Object object) {
        if ((object == null) || !(object instanceof Classifier)) {
            return Collections.EMPTY_LIST;
        }

        ModelElement modelElement = (ModelElement) object;

        Collection clientDependencies =
            model.getCore().getAClientClientDependency().getClientDependency(
                modelElement);

        return new FilteredCollection(clientDependencies) {
            public boolean add(Object object) {
                Abstraction abstraction = (Abstraction) object;
                return super.add(abstraction.getSupplier().iterator().next());
            }

            protected boolean accept(Object object) {
                return object instanceof Abstraction;
            }
        };
    }

    /**
     *  Filters a collection of objects so that the collection
     * contains only those objects that pass the 'accept' test.
     * 
     * <p>It is useful for filtering the results of a query.</p>
     *
     *@author    Anthony Mowers
     */
    private abstract static class FilteredCollection extends Vector {
        /**
         *  Constructor for the FilterCollection object
         *
         *@param  c  Description of the Parameter
         */
        public FilteredCollection(Collection c) {
            for (Iterator i = c.iterator(); i.hasNext();) {
                Object object = i.next();
                if (accept(object)) {
                    add(object);
                }
            }
        }

        /**
         *  Description of the Method
         *
         *@param  object  Description of the Parameter
         *@return         Description of the Return Value
         */
        protected abstract boolean accept(Object object);
    }

}
