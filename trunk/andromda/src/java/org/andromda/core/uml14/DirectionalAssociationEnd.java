package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;

/**
 * Implements a set of operations that are useful for querying
 * an association from the perspective of 
 * a given association end.
 * 
 * <p> Useful for answering question such as: isMany2Many, isOne2Many, ...</p>
 * 
 * @author Anthony Mowers
 */
public class DirectionalAssociationEnd
{
    protected AssociationEnd associationEnd;

    public DirectionalAssociationEnd(
        AssociationEnd associationEnd)
    {
        this.associationEnd = associationEnd;
    }

    /**
     * get the near end of the association
     */
    public AssociationEnd getSource()
    {
        return associationEnd;
    }

    /**
     * get the far end of the association
     */
    public AssociationEnd getTarget()
    {
        return getOtherEnd(); 
    }
    
    /**
     * get the name of the association
     */
    public String getName()
    {
        return associationEnd.getAssociation().getName();
    }
    
    /**
     * get a string that can be used to uniquely id this association
     */ 
    public String getId()
    {
        return associationEnd.getAssociation().refMofId();
    }
    
    public boolean isOne2Many()
    {
        return !isMany(associationEnd) && isMany(getOtherEnd());
    }
    
    public boolean isMany2Many()
    {
        return isMany(associationEnd) && isMany(getOtherEnd());
    }

    public boolean isOne2One()
    {
        return !isMany(associationEnd) && !isMany(getOtherEnd());
    }
    
    public boolean isMany2One()
    {
        return isMany(associationEnd) && !isMany(getOtherEnd());
    }
    
    static protected boolean isMany(AssociationEnd ae)
    {
        Multiplicity multiplicity = ae.getMultiplicity();
        if (multiplicity == null)
        {
            return false;  // no multiplicity means multiplicity==1
        }
        Collection ranges = multiplicity.getRange();
        
        for (Iterator i = ranges.iterator(); i.hasNext() ; )
        {
            MultiplicityRange range = (MultiplicityRange)i.next();
            if ( range.getUpper() > 1 )
            {
                return true;
            }
            
            int rangeSize = range.getUpper() - range.getLower();
            if (rangeSize < 0)
            {
                return true;
            }
            
        }
        
        return false;
    }
    
    protected AssociationEnd getOtherEnd()
    {
        AssociationEnd otherEnd;
        
        Collection ends = associationEnd.getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext(); )
        {
            AssociationEnd ae = (AssociationEnd)i.next();
            if (!associationEnd.equals(ae))
            {
                return ae;
            }
        }
        
        return null;
    }
    
        
}
