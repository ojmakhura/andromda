package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.datatypes.MultiplicityRange;

/**
 * @author amowers
 *
 * 
 */
public class DirectionalAssociationEnd
{
    protected AssociationEnd associationEnd;

    public DirectionalAssociationEnd(
        AssociationEnd associationEnd)
    {
        this.associationEnd = associationEnd;
    }

    public AssociationEnd getSource()
    {
        return associationEnd;
    }


    public AssociationEnd getTarget()
    {
        return getOtherEnd(); 
    }
    
    public String getName()
    {
        return associationEnd.getAssociation().getName();
    }
    
    public String getId()
    {
        return associationEnd.getAssociation().refMofId();
    }
    
    public boolean isOne2Many()
    {
        return !isMany(associationEnd) & isMany(getOtherEnd());
    }
    
    public boolean isMany2Many()
    {
        return isMany(associationEnd) & isMany(getOtherEnd());
    }

    public boolean isOne2One()
    {
        return !isMany(associationEnd) & !isMany(getOtherEnd());
    }
    
    public boolean isMany2One()
    {
        return isMany(associationEnd) & !isMany(getOtherEnd());
    }
    
    static public boolean isMany(AssociationEnd ae)
    {
        Collection ranges = ae.getMultiplicity().getRange();
        
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
