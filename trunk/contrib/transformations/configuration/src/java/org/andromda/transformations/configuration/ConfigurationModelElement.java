package org.andromda.transformations.configuration;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.atl.engine.vm.StackFrame;
import org.atl.engine.vm.nativelib.ASMBoolean;
import org.atl.engine.vm.nativelib.ASMInteger;
import org.atl.engine.vm.nativelib.ASMModel;
import org.atl.engine.vm.nativelib.ASMModelElement;
import org.atl.engine.vm.nativelib.ASMOclAny;
import org.atl.engine.vm.nativelib.ASMOclType;
import org.atl.engine.vm.nativelib.ASMOclUndefined;
import org.atl.engine.vm.nativelib.ASMReal;
import org.atl.engine.vm.nativelib.ASMSequence;
import org.atl.engine.vm.nativelib.ASMSet;
import org.atl.engine.vm.nativelib.ASMString;

/**
 * Represents an element of an AndroMDA configuration model. Wraps an original
 * AndroMDA configuration entry (namespace property, mapping, etc.) so that ATL
 * can access it.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * 
 */
public class ConfigurationModelElement
        extends ASMModelElement
{
    private static final Logger logger = Logger.getLogger(ConfigurationModelElement.class);

    /**
     * The entry in the original AndroMDA configuration; can be a namespace
     * property or a mapping or whatever configuration elements we have. Will be
     * accessed by reflection, using PropertyUtils.
     */
    private Object configurationEntry;

    /**
     * Constructs a new model element.
     * 
     * @param model
     *            the model to which it belongs
     * @param metaobject
     *            the metamodel object that it instantiates
     * @param configurationEntry
     *            real configuration object wrapped by this model element
     */
    public ConfigurationModelElement(ASMModel model,
            ASMModelElement metaobject, Object configurationEntry)
    {
        super(model, metaobject);
        this.configurationEntry = configurationEntry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.vm.nativelib.ASMModelElement#get(org.atl.engine.vm.StackFrame,
     *      java.lang.String)
     */
    public ASMOclAny get(StackFrame frame, String name)
    {
        logger.debug("get: frame=" + frame + ", name=" + name);
        ASMOclAny ret = null;

        /** The call to isHelper() crashes, so I commented that out!
        if ((frame != null) && isHelper(frame, name))
        {
            ret = getHelper(frame, name);
        } else
        **/ 
        {
            try
            {
                Object o = PropertyUtils.getProperty(this.configurationEntry,
                        name);
                ret = java2ASM(frame, o);
            } catch (Exception e)
            {
                if (frame != null)
                    frame.printStackTrace("ConfigurationModelElement.get: this = " + this + " ; name = "
                            + name, e);
            } catch (Error e)
            {
                if (frame != null)
                    frame.printStackTrace("ConfigurationModelElement.get: this = " + this + " ; name = "
                            + name + " ; " + e.toString());
            }
        }

        logger.debug("ConfigurationModelElement.get: frame=" + frame + ", name=" + name + ", result=" + ret);
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.vm.nativelib.ASMModelElement#conformsTo(org.atl.engine.vm.nativelib.ASMOclType)
     */
    public ASMBoolean conformsTo(ASMOclType other)
    {
        throw new UnsupportedOperationException(
                "This method is only implemented for metamodels.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.vm.nativelib.ASMModelElement#getProperty(java.lang.String)
     */
    public ASMModelElement getProperty(String name)
    {
        throw new UnsupportedOperationException(
                "This method is only implemented for metamodels.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.vm.nativelib.ASMModelElement#getPropertyType(java.lang.String)
     */
    public ASMModelElement getPropertyType(String name)
    {
        throw new UnsupportedOperationException(
                "This method is only implemented for metamodels.");
    }

    /**
     * Converts a Java data type to an ASMOclAny. The basic types should not be
     * converted here --> refactoring is necessary.
     * 
     * @param frame
     *            the stack frame
     * @param o
     *            the object to be converted
     * @return the converted object
     */
    private static ASMOclAny java2ASM(StackFrame frame, Object o)
    {
        ASMOclAny ret = null;

        if (o instanceof String)
        {
            ret = new ASMString((String)o);
        } else if (o instanceof Integer)
        {
            ret = new ASMInteger(((Integer)o).intValue());
        } else if (o instanceof Boolean)
        {
            ret = new ASMBoolean(((Boolean)o).booleanValue());
        } else if (o instanceof Double)
        {
            ret = new ASMReal(((Double)o).doubleValue());
        } else if (o instanceof List)
        {
            ret = new ASMSequence();
            for (Iterator i = ((List)o).iterator(); i.hasNext();)
            {
                ((ASMSequence)ret).add(java2ASM(frame, i.next()));
            }
        } else if (o instanceof Collection)
        {
            ret = new ASMSet();
            for (Iterator i = ((Collection)o).iterator(); i.hasNext();)
            {
                ((ASMSet)ret).add(java2ASM(frame, i.next()));
            }
        } else if (o == null)
        {
            ret = new ASMOclUndefined();
        } else
        {
            frame.printStackTrace("ERROR: could not convert " + o + " : "
                    + o.getClass());
        }

        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String result = "ConfigurationModelElement: [ metaobject=";
        result += this.getMetaobject() == null ? "null" : this.getMetaobject()
                .toString();
        result += "; wrapped configuration entry = ";
        result += this.configurationEntry == null ? "null"
                : this.configurationEntry.toString();
        return result + " ]";

    }
}
