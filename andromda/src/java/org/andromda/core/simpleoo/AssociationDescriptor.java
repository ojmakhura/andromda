package org.andromda.core.simpleoo;


import java.util.List;
import org.andromda.core.xml.Association;

/**
 * <p>This is a helper class to access the data of an association
 * from a Velocity script.  The data is already pre-formatted in
 * printable form.</p>
 * 
 * @author Matthias Bohlen
 */
public class AssociationDescriptor
{
    private String                     id;
    private String                     name;
    private AssociationEndDescriptor[] ends = new AssociationEndDescriptor[2];
    private List                       taggedValues;


    public AssociationDescriptor(Association assoc, Object2SimpleOOConverter resolver)
    {
        id = assoc.getId();
        name = assoc.getName();

        ends[0] = new AssociationEndDescriptor(assoc.getEnd1(), resolver);
        ends[1] = new AssociationEndDescriptor(assoc.getEnd2(), resolver);

        taggedValues = assoc.getTaggedValues();
    }


    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * <p>Returns the XMI id of this association.  Purpose: Just to
     * have a unique ID for the association so that XDoclet can find
     * both ends belonging to the same association.</p>
     *
     * @return String the XMI id
     */
    public String getId()
    {
        return id;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * <p>Returns the name of the association.</p>
     *
     * @return String the name
     */
    public String getName()
    {
        return name;
    }


    public void setMultiplicities(String multiplicityCharacteristics)
    {
        int pos = multiplicityCharacteristics.indexOf(':');
        if (-1 != pos) {
            getEnd1().setMultiplicity(multiplicityCharacteristics.substring(0, pos));
            getEnd2().setMultiplicity(multiplicityCharacteristics.substring(pos+1));
        }
    }

    /**
     * <p>Returns the multiplicity characteristics of this
     * association.  They will be either <code>"One:One"</code>,
     * <code>"One:Many"</code>, <code>"Many:One"</code> or
     * <code>"Many:Many"</code>.</p>
     *
     * @return String the characteristics
     */
    public String getMultiplicities()
    {
        return getEnd1().getMultiplicity() + ':' + getEnd2().getMultiplicity();
    }

    /**
     * <p>Checks if this association is one to one.</p>
     *
     * @see #getMultiplicities()
     *
     * @return <code>true</code> if this association is One:One;
     *         <code>false</code> otherwise
     */
    public boolean isOne2One() {
        return "One:One".equals(getMultiplicities());
    }

    /**
     * <p>Checks if this association is one to many.</p>
     *
     * @see #getMultiplicities()
     *
     * @return <code>true</code> if this association is One:Many;
     *         <code>false</code> otherwise
     */
    public boolean isOne2Many() {
        return "One:Many".equals(getMultiplicities());
    }

    /**
     * <p>Checks if this association is many to one.</p>
     *
     * @see #getMultiplicities()
     *
     * @return <code>true</code> if this association is Many:One;
     *         <code>false</code> otherwise
     */
    public boolean isMany2One() {
        return "Many:One".equals(getMultiplicities());
    }

    /**
     * <p>Checks if this association is many to many.</p>
     *
     * @see #getMultiplicities()
     *
     * @return <code>true</code> if this association is Many:Many;
     *         <code>false</code> otherwise
     */
    public boolean isMany2Many() {
        return "Many:Many".equals(getMultiplicities());
    }


    public void setEnd1(AssociationEndDescriptor end)
    {
        setEnd(0, end);
    }

    /**
     * <p>Returns the first end of this association.</p>
     *
     * @return AssociationEnd source end of this association
     */
    public AssociationEndDescriptor getEnd1()
    {
        return getEnd(0);
    }


    public void setEnd2(AssociationEndDescriptor end)
    {
        setEnd(1, end);
    }

    /**
     * <p>Returns the second end of this association.</p>
     *
     * @return AssociationEnd source end of this association
     */
    public AssociationEndDescriptor getEnd2()
    {
        return getEnd(1);
    }

    protected void setEnd(int index, AssociationEndDescriptor end) {
        this.ends[index] = end;
    }

    protected AssociationEndDescriptor getEnd(int index) {
        return ends[index];
    }


    public void setTaggedValues(List taggedValues)
    {
        this.taggedValues = taggedValues;
    }

    /**
     * <p>Returns all tagged values of this association.</p>
     *
     * @return List of TagValues
     */
    public List getTaggedValues()
    {
        return taggedValues;
    }
}
