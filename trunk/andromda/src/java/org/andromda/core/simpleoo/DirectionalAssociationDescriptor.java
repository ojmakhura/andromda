package org.andromda.core.simpleoo;

import org.andromda.core.xml.Association;
import org.andromda.core.xml.AssociationLink;


/**
 * <p>This is a helper class to access the data of an association as
 * seen from a given source end from a Velocity script.  The data is
 * already pre-formatted in printable form.</p>
 * 
 * @author Stefan Kuehnel
 */
public class DirectionalAssociationDescriptor
    extends AssociationDescriptor
{
    private int sourceEndIndex;


    public DirectionalAssociationDescriptor(Association assoc, String sourceId, Object2SimpleOOConverter resolver)
    {
        super(assoc, resolver);

        sourceEndIndex = (getEnd(0).getId().equals(sourceId)) ? 0 : 1;
    }


    public DirectionalAssociationDescriptor(AssociationLink assocLink, Object2SimpleOOConverter resolver)
    {
        this(resolver.convertToAssociation(assocLink.getAssocid()), assocLink.getThisend(), resolver);
    }


    /**
     * <p>Returns the index for accessing the source end of this
     * association.</p>
     *
     * @return int the index for accessing the source end of this association
     */
    private int getSourceEndIndex()
    {
        return sourceEndIndex;
    }

    /**
     * <p>Returns the index for accessing the target end of this
     * association.</p>
     *
     * @return int the index for accessing the target end of this association
     */
    private int getTargetEndIndex()
    {
        return 1-sourceEndIndex;
    }


    public void setEnd1(AssociationEndDescriptor end)
    {
        setSource(end);
    }

    /**
     * <p>Returns the source end of this association.</p>
     *
     * @return AssociationEnd source end of this association
     */
    public AssociationEndDescriptor getEnd1()
    {
        return getSource();
    }


    public void setEnd2(AssociationEndDescriptor end)
    {
        setTarget(end);
    }

    /**
     * <p>Returns the target end of this association.</p>
     *
     * @return AssociationEndDescriptor target end of this association
     */
    public AssociationEndDescriptor getEnd2()
    {
        return getTarget();
    }


    public void setSource(AssociationEndDescriptor sourceEnd)
    {
        setEnd(getSourceEndIndex(), sourceEnd);
    }

    /**
     * <p>Returns the source end of this association.</p>
     *
     * @return AssociationEnd source end of this association
     */
    public AssociationEndDescriptor getSource()
    {
        return getEnd(getSourceEndIndex());
    }


    public void setTarget(AssociationEndDescriptor targetEnd)
    {
        setEnd(getTargetEndIndex(), targetEnd);
    }

    /**
     * <p>Returns the target end of this association.</p>
     *
     * @return AssociationEndDescriptor target end of this association
     */
    public AssociationEndDescriptor getTarget()
    {
        return getEnd(getTargetEndIndex());
    }
}
