package org.andromda.core.simpleuml;

import java.util.Collection;

/**
 * defines those methods missing from the Package in the UML 1.4 schema that are 
 * needed by the UML2EJB based code generation scripts.
 * 
 * @author Anthony Mowers
 */
public interface UMLPackage
    extends UMLModelElement
{ 
    public Collection getClasses();
}
