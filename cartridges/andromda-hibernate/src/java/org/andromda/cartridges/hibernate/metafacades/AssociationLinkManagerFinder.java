package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.metafacades.uml.AssociationEndFacade;

/**
 * <p>
 * Extra class for a single method to make the method testable independently
 * without the need to load a model.
 * </p>
 * 
 * @since 25.10.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 */
public abstract class AssociationLinkManagerFinder {

    /**
     * <p>
     * An association link connects two classifiers. One of the two classifiers
     * is the "manager" of the link - this means at runtime, an instance of the
     * manager assigns a value to the foreign key field in its own table or adds
     * a row to the association table (in the case of many-to-many). The O/R
     * mapper (i.e. Hibernate) will do the update of the field when it saves the
     * manager instance to persistent store.
     * 
     * <p>
     * This method finds the "manager" among the classifiers which are connected
     * to the two association ends. For Hibernate, the manager side of the assoc
     * will have inverse="false", the non-manager side will have inverse="true".
     * </p>
     * 
     * <p>
     * See <a href="http://www.hibernate.org/155.html">the Hibernate docs </a>
     * for details.
     * </p>
     * 
     * @param self
     *            the association end that has to be checked
     * @return true or false: is this end the "manager" or not?
     */
    public static boolean managesRelationalLink(AssociationEndFacade self) {
        AssociationEndFacade otherEnd = self.getOtherEnd();
        boolean bidirectional = self.isNavigable() && otherEnd.isNavigable();

        if (!bidirectional) {
            /*
             * If I am not navigable but the opposite end is, then I am the
             * manager! This is important because if I am not navigable, I
             * contain the getter/setter for the field that represents the
             * association. I am the only one who has a chance to manage the
             * relational link because the O/R mapper (i.e. Hibernate) can
             * detect that I (or one of my fields) has changed. The O/R mapper
             * will update the link during *my* save() method.
             */
            // System.out.println("-- 1 --");
            return !self.isNavigable();
        }

        // from now on, only bidirectional associations are left to check!

        if (self.isOne2One()) {
            // System.out.println("-- 2 --");
            if (self.isAggregation() || self.isComposition()) {
                // System.out.println("-- 3 --");
                return false;
            }
            if (otherEnd.isAggregation() || otherEnd.isComposition()) {
                // System.out.println("-- 4 --");
                return true;
            }
            // System.out.println("-- 5 --");
            return isDefaultManager(self, otherEnd);
        }

        if (self.isOne2Many()) {
            // System.out.println("-- 6 --");
            return false; // the "one" side does not manage
        }

        if (self.isMany2One()) {
            // System.out.println("-- 7 --");
            return true; // the "many" side always manages
        }

        // must be many:many now!
        // System.out.println("-- 8 --");
        return isDefaultManager(self, otherEnd);

    }

    /**
     * Chooses a random end of the assoc as a default manager of the relational
     * link. In this case, chooses the one with the lexically smaller f.q. name.
     * This is a random but reproducible choice.
     * 
     * @param self
     *            this end of the association
     * @param otherEnd
     *            the other end of the association
     * @return is "self" the manager?
     */
    private static boolean isDefaultManager(AssociationEndFacade self,
            AssociationEndFacade otherEnd) {

        String name1 = self.getType().getFullyQualifiedName();
        String name2 = otherEnd.getType().getFullyQualifiedName();
        return name1.compareTo(name2) < 0;
    }

}