package org.andromda.core.simpleoo;

import org.andromda.core.xml.AssociationEnd;
import org.andromda.core.xml.Multiplicity;
import org.andromda.core.xml.Type;


/**
 * <p>This is a helper class to conveniently access the data of an
 * end point of an association from a Velocity script.  The data is
 * already pre-formatted in printable form.</p>
 * 
 * @author Stefan Kuehnel
 */
public class AssociationEndDescriptor
{
    private AssociationEnd end;
    private Multiplicity   m;
    private String         multiplicityCharacteristic;
    private boolean        optional;
    private String         navigable;
    private String         aggregation;
    private Type           type;
    private String         roleName;
    private String         id;


    public AssociationEndDescriptor(AssociationEnd ae, Object2SimpleOOConverter resolver) {
        end = ae;
        id = ae.getId();
        m = ae.getProperties().getMultiplicity();
        multiplicityCharacteristic = transformMultiplicity(m);
        optional = transformOptional(m);
        navigable = ae.getProperties().getNavigable();
        aggregation = ae.getProperties().getAggregation();
        type = resolver.convertToType(ae.getType());
        roleName = ae.getProperties().getRolename();
        if (roleName.length() == 0)
        {
            roleName = "the" + type.getName();
        }
    }


    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * <p>Returns the XMI id of this association end.</p>
     *
     * @return String the id
     */
    public String getId()
    {
        return id;
    }


    public void setMultiplicity(String multiplicityCharacteristic)
    {
        this.multiplicityCharacteristic = multiplicityCharacteristic;
    }

    /**
     * <p>Returns the multiplicity characteristics of this end of the
     * underlying association.  They will be either <code>"One"</code>
     * or <code>"Many"</code>.</p>
     *
     * @return String the characteristics
     */
    public String getMultiplicity()
    {
        return multiplicityCharacteristic;
    }


    public void setOptional(boolean optional)
    {
        this.optional = optional;
    }

    /**
     * <p>Tells if there can be no element at this end of the
     * association.  This means, this end has a lower multiplicity of
     * 0 in the underlying UML model.</p>
     *
     * @return <code>true</code> if there has to be no element at this
     *         end of the association; <code>false</code> otherwise
     */
    public boolean isOptional()
    {
        return optional;
    }


    public void setNavigable(String navigable)
    {
        this.navigable = navigable;
    }

    /**
     * <p>Tells whether on can navigate from the other end of the
     * underlying association to this end.</p>
     *
     * @return String "true" or "false"
     */
    public String getNavigable()
    {
        return navigable;
    }


    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    /**
     * <p>Returns the role name on this end of the association.</p>
     *
     * @return String the role name
     */
    public String getRoleName()
    {
        return roleName;
    }


    public void setType(Type type)
    {
        this.type = type;
    }

    /**
     * <p>Returns the type of the object on this end of the underlying
     * association.</p>
     *
     * @return Type the type of the object on this end of the underlying
     *              association
     */
    public Type getType()
    {
        return type;
    }


    public void setAggregation(String aggregation)
    {
        this.aggregation = aggregation;
    }

    /**
     * <p>Tells whether the underlying association is an aggregation,
     * a composite or none, seen from this end to the other end.</p>
     *
     * @return String <code>"none"</code>, <code>"aggregation"</code>
     *                or <code>"composite"</code>
     */
    public String getAggregation()
    {
        return aggregation;
    }


    public void setEnd(AssociationEnd end)
    {
        this.end = end;
    }

    /**
     * <p>Returns the underlying AssociationEnd as created by JAXB
     * that is wrapped by this class.</p>
     *
     * @return AssociationEnd the end point of the underlying association
     */
    public AssociationEnd getEnd()
    {
        return end;
    }


    /**
     * <p>Internal helper.</p>
     *
     * @param m de.mbohlen.tools.uml2ejb.xml.Multiplicity
     */
    private String transformMultiplicity(Multiplicity m)
    {
        return "1".equals(m.getUpper()) ? "One" : "Many";
    }

    /**
     * <p>Internal helper.</p>
     *
     * @param m de.mbohlen.tools.uml2ejb.xml.Multiplicity
     */
    private boolean transformOptional(Multiplicity m)
    {
        return "0".equals(m.getLower());
    }
};
