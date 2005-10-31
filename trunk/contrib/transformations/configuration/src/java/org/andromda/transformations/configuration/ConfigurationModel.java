/**
 * 
 */
package org.andromda.transformations.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.andromda.core.configuration.Namespaces;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.log4j.Logger;
import org.atl.engine.repositories.mdr4atl.ASMMDRModel;
import org.atl.engine.vm.ClassNativeOperation;
import org.atl.engine.vm.ModelLoader;
import org.atl.engine.vm.StackFrame;
import org.atl.engine.vm.nativelib.ASMModel;
import org.atl.engine.vm.nativelib.ASMModelElement;
import org.atl.engine.vm.nativelib.ASMString;

/**
 * Represents an AndroMDA configuration in the form of a model that can be
 * transformed by ATL.
 * 
 * This implementation assumes that the corresponding metamodel is managed by
 * another class, e.g. MDR or EMF model elements.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * 
 */
public class ConfigurationModel
        extends ASMModel
{

    private static final Logger logger = Logger
            .getLogger(ConfigurationModel.class);

    /**
     * This is just for testing. It has to replaced by a lookup call for the
     * current AndroMDA namespace we are running in.
     */
    private static final String CURRENT_NAMESPACE = "spring";

    static
    {
        registerMofOperations();
    }

    /**
     * Constructs a new AndroMDA configuration model.
     * 
     * @param name
     *            the name of the model
     * @param metamodel
     *            link to the configuration metamodel
     * @param isTarget
     *            whether it's source or target of a transformation
     * @param ml
     *            (unused) ModelLoader
     */
    public ConfigurationModel(String name, ASMModel metamodel,
            boolean isTarget, ModelLoader ml)
    {
        super(name, metamodel, isTarget, ml);
        // the following statement cannot be compiled from inside Maven ?????
        // assert !isTarget : "Configuration models may only be used as a source
        // but not as a target of a transformation.";
    }

    private static void registerMofOperations()
    {
        try
        {
            // this is for Namespaces.getProperty(namespaceName, propertyName)
            registerMOFOperation("Classifier", "theInstance", new Class[] {});

            // this is for Namespaces.getProperty(namespaceName, propertyName)
            registerMOFOperation("Classifier", "getProperty",
                    new Class[] { ASMString.class });

            // this is for TypeMappings.getTo(typeName)
            registerMOFOperation("Classifier", "getLanguageMapping",
                    new Class[] { ASMString.class });
        } catch (Exception e)
        {
            logger.error("Exception while registering native operations", e);
        }
    }

    private static void registerMOFOperation(String modelelementName,
            String methodName, Class args[]) throws SecurityException,
            NoSuchMethodException
    {
        List realArgs = new ArrayList(Arrays.asList(args));
        realArgs.add(0, ASMModelElement.class);
        realArgs.add(0, StackFrame.class);
        ClassNativeOperation no = new ClassNativeOperation(
                ConfigurationModel.class.getMethod(methodName,
                        (Class[])realArgs.toArray(args)));
        // the reference to ASMMDRModel in the following line is ugly!
        // because this class should not depend on any concrete
        // model class but on ASMModel alone! Otherwise, we would be
        // bound to a concrete repository type, i.e. MDR in this case.
        ASMModelElement amme = ASMMDRModel.getMOF().findModelElement(
                modelelementName);
        logger.debug("Registering on " + amme + " : " + no);
        amme.registerVMOperation(no);
    }

    /**
     * Finds a model element for a given metamodel class.
     * 
     * @param frame
     *            the Stackframe we run on
     * @param self
     *            the metaclass we are looking for
     * @return the model element found (or null if not found)
     */
    public static ASMModelElement theInstance(StackFrame frame,
            ASMModelElement self)
    {
        ASMModel model = findModelInStackframe(frame, self.getModel());
        return model == null ? null : model.findModelElement(self.getName());
    }

    /**
     * Searches the stack frame for a model that is an instance of the given
     * metamodel.
     * 
     * @param frame
     *            the stack frame to search
     * @param metamodel
     *            the metamodel to search for
     * @return the (first) model that has been found
     */
    private static ASMModel findModelInStackframe(StackFrame frame,
            ASMModel metamodel)
    {
        for (Iterator i = frame.getModels().keySet().iterator(); i.hasNext();)
        {
            String mname = (String)i.next();
            ASMModel am = (ASMModel)frame.getModels().get(mname);
            if (am.getMetamodel().equals(metamodel))
            {
                return am;
            }
        }
        return null;
    }

    /**
     * Returns a Property from an AndroMDA namespace. Syntax:
     * Configuration!Namespaces->getProperty('myProperty')
     * 
     * @param frame
     *            the stack frame where the method was called
     * @param self
     *            the metamodel element on which it was called
     * @param propertyName
     *            the property to get
     * @return the property object wrapped as a model element
     */
    public static ASMModelElement getProperty(StackFrame frame,
            ASMModelElement self, ASMString propertyName)
    {
        ASMModel model = findModelInStackframe(frame, self.getModel());
        return new ConfigurationModelElement(model, model.getMetamodel()
                .findModelElement("Configuration!Property"), Namespaces
                .instance().getProperty(CURRENT_NAMESPACE,
                        propertyName.getSymbol()));
    }

    /**
     * Maps a model type to a target language type, e.g. in the Java language.
     * Syntax:
     * Configuration!Typemappings->getLanguageMapping('datatype::String')
     * 
     * @param frame
     *            the stack frame where the method was called
     * @param self
     *            the metamodel element on which it was called
     * @param typeNameFromModel
     *            the type name to be mapped
     * @return the mapped type name in the target language
     */
    public static ASMString getLanguageMapping(StackFrame frame,
            ASMModelElement self, ASMString typeNameFromModel)
    {
        String languageType = getLanguageMappings().getTo(
                typeNameFromModel.getSymbol());
        return new ASMString(languageType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.vm.nativelib.ASMModel#getElementsByType(org.atl.engine.vm.nativelib.ASMModelElement)
     */
    public Set getElementsByType(ASMModelElement type)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.vm.nativelib.ASMModel#findModelElement(java.lang.String)
     */
    public ASMModelElement findModelElement(String name)
    {
        if ("Configuration!Namespaces".equals(name))
        {
            Namespaces namespaces = Namespaces.instance();
            ConfigurationModelElement configurationModelElement = new ConfigurationModelElement(
                    this, this.getMetamodel().findModelElement("Namespaces"),
                    namespaces);
            logger.debug("found " + configurationModelElement.toString());
            return configurationModelElement;
        } else if ("Configuration!TypeMappings".equals(name))
        {
            TypeMappings typeMappings = getLanguageMappings();
            ConfigurationModelElement configurationModelElement = new ConfigurationModelElement(
                    this, this.getMetamodel().findModelElement("TypeMappings"),
                    typeMappings);
            logger.debug("found " + configurationModelElement.toString());
            return configurationModelElement;
        } else
        {
            logger.warn("no configuration model element found with this name: "
                    + name);
        }
        return null;
    }

    /**
     * Returns a reference to the current language mappings.
     * 
     * @return the current language mappings
     */
    private static TypeMappings getLanguageMappings()
    {
        String languageMappingsUri = Namespaces.instance().getPropertyValue(
                CURRENT_NAMESPACE,
                UMLMetafacadeProperties.LANGUAGE_MAPPINGS_URI);
        return TypeMappings.getInstance(languageMappingsUri);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.vm.nativelib.ASMModel#newModelElement(org.atl.engine.vm.nativelib.ASMModelElement)
     */
    public ASMModelElement newModelElement(ASMModelElement arg0)
    {
        throw new UnsupportedOperationException(
                "This model does not support creation of new model elements.");
    }

}
