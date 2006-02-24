package org.andromda.android.core.internal.cartridge;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.cartridge.ICartridgeDescriptor;
import org.andromda.android.core.cartridge.ICartridgeMetafacadeVariableDescriptor;
import org.andromda.android.core.cartridge.ICartridgeVariableContainer;
import org.andromda.android.core.cartridge.ICartridgeVariableDescriptor;
import org.andromda.core.cartridge.CartridgeDocument;
import org.andromda.core.cartridge.CartridgeDocument.Cartridge;
import org.andromda.core.cartridge.ModelElementDocument.ModelElement;
import org.andromda.core.cartridge.ModelElementDocument.ModelElement.Type;
import org.andromda.core.cartridge.ModelElementsDocument.ModelElements;
import org.andromda.core.cartridge.PropertyDocument.Property;
import org.andromda.core.cartridge.TemplateDocument.Template;
import org.andromda.core.cartridge.TemplateObjectDocument.TemplateObject;
import org.andromda.core.metafacade.MetafacadeDocument;
import org.andromda.core.metafacade.MetafacadeDocument.Metafacade;
import org.andromda.core.namespace.NamespaceDocument;
import org.andromda.core.namespace.NamespaceDocument.Namespace;
import org.andromda.core.namespace.PropertiesDocument.Properties;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Provides access to the cartridge descriptor documents.
 * 
 * @author Peter Friese
 * @since 30.01.2006
 */
public class CartridgeDescriptor
        implements ICartridgeDescriptor
{

    /** The default XML namespace for <code>namespace.xml</code> files in AndroMDA cartridges. */
    private static final String XML_NAMESPACE_ANDROMDA_NAMESPACE = "http://andromda.org/core/namespace";

    /** The default XML namespace for <code>cartridge.xml</code> files in AndroMDA cartridges. */
    private static final String XML_NAMESPACE_ANDROMDA_CARTRIDGE = "http://andromda.org/core/cartridge";

    /** The default XML namespace for <code>metafacades</code> files in AndroMDA cartridges. */
    private static final String XML_NAMESPACE_ANDROMDA_METAFACADES = "http://andromda.org/core/metafacades";

    /** Namespace options for the <code>namespace.xml</code> file. */
    private XmlOptions namespaceXmlOptions;

    /** Namespace options for the <code>cartridge.xml</code> file. */
    private XmlOptions cartridgeXmlOptions;

    /** Namespace options for the <code>metafacades.xml</code> file. */
    private XmlOptions metafacadesXmlOptions;

    /** The location of the cartridge. */
    private final String cartridgeLocation;

    /** Indicates whether this cartridge descriptor is located inside a jar file. */
    private boolean jar;

    /** The Java project this cartridge is located in. */
    private IJavaProject javaProject;

    /** Cache for the cartridge variables. */
    private CartridgeVariableContainer cartridgeVariables;

    /**
     * Creates a new CartridgeDescriptor.
     * 
     * @param location The location of the cartridge.
     * @param insideJar Indicates whether the location is a jar file.
     */
    public CartridgeDescriptor(final String location,
        final boolean insideJar)
    {
        cartridgeLocation = location;
        jar = insideJar;
        setupDefaultNamespaces();
    }

    /**
     * Creates a new CartridgeDescriptor.
     * 
     * @param cartridgeRootFolder The root folder of the cartridge.
     * @param insideJar Indicates whether the location is a jar file.
     */
    public CartridgeDescriptor(final IContainer cartridgeRootFolder,
        final boolean insideJar)
    {
        cartridgeLocation = cartridgeRootFolder.getLocation().toOSString();

        IProject project = cartridgeRootFolder.getProject();
        javaProject = JavaCore.create(project);
        jar = insideJar;
        setupDefaultNamespaces();
    }

    /**
     * Creates a new CartridgeDescriptor.
     * 
     * @param location The location of the cartridge.
     * @param cartridgeName The cartridge name, such as "spring" or "hibernate".
     * @param cartridgeVersion The cartridge version, e.g. "3.2-RC1-SNAPSHOT".
     */
    public CartridgeDescriptor(final String location,
        final String cartridgeName,
        final String cartridgeVersion)
    {
        this.cartridgeLocation = "file:/" + location + "/andromda-" + cartridgeName + "-cartridge-" + cartridgeVersion
                + ".jar";
        jar = true;
        setupDefaultNamespaces();
    }

    /**
     * Creates a new CartridgeDescriptor.
     * 
     * @param location The location of the cartridge.
     * @param cartridgeName The cartridge name, such as "spring" or "hibernate".
     */
    public CartridgeDescriptor(final String location,
        final String cartridgeName)
    {
        this.cartridgeLocation = location + "/andromda-" + cartridgeName;
        jar = true;
        setupDefaultNamespaces();
    }

    /**
     * {@inheritDoc}
     */
    public Cartridge getCartridge() throws CartridgeParsingException
    {
        String fileName = "cartridge.xml";
        String documentLocation = getDocumentLocation(fileName);
        try
        {
            URL cartridgeURL = new URL(documentLocation);
            CartridgeDocument document = CartridgeDocument.Factory.parse(cartridgeURL, cartridgeXmlOptions);
            return document.getCartridge();
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Namespace getNamespace() throws CartridgeParsingException
    {
        String fileName = "namespace.xml";
        String documentLocation = getDocumentLocation(fileName);
        try
        {
            URL namespaceURL = new URL(documentLocation);
            NamespaceDocument document = NamespaceDocument.Factory.parse(namespaceURL, namespaceXmlOptions);
            return document.getNamespace();
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws CartridgeParsingException
     */
    public Metafacade getMetafacade() throws CartridgeParsingException
    {
        String fileName = "metafacades.xml";
        String documentLocation = getDocumentLocation(fileName);
        try
        {
            URL metafacadesURL = new URL(documentLocation);
            MetafacadeDocument document = MetafacadeDocument.Factory.parse(metafacadesURL, metafacadesXmlOptions);
            return document.getMetafacade();
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * Setup an XmlOptions instance so the parser will assume the default namespace for the config documents even if it
     * has no namespace set.
     */
    private void setupDefaultNamespaces()
    {
        namespaceXmlOptions = new XmlOptions();
        Map namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_NAMESPACE);
        namespaceXmlOptions.setLoadSubstituteNamespaces(namespaceMapping);

        cartridgeXmlOptions = new XmlOptions();
        namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_CARTRIDGE);
        cartridgeXmlOptions.setLoadSubstituteNamespaces(namespaceMapping);

        metafacadesXmlOptions = new XmlOptions();
        namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_METAFACADES);
        metafacadesXmlOptions.setLoadSubstituteNamespaces(namespaceMapping);

    }

    /**
     * Constructs the location of the requested file depending on whether it is located inside a cartridge jar or rather
     * in the AndroMDA development workspace.
     * 
     * @param fileName The filename we're interested in.
     * @return A string with the correct path to the requested file.
     */
    private String getDocumentLocation(final String fileName)
    {
        String documentLocation;
        if (jar)
        {
            documentLocation = "jar:" + cartridgeLocation + "!/META-INF/andromda/" + fileName;
        }
        else
        {
            documentLocation = "file:/" + cartridgeLocation + "/src/META-INF/andromda/" + fileName;
        }
        return documentLocation;
    }

    /**
     * {@inheritDoc}
     */
    public ICartridgeVariableContainer getVariableDescriptors() throws CartridgeParsingException
    {
        if (cartridgeVariables == null)
        {
            cartridgeVariables = new CartridgeVariableContainer();
            retrieveCartridgeProperties();
            retrieveCartridgeTemplateObjects();
            retrieveMetafacadeVariables();
        }
        return cartridgeVariables;
    }

    /**
     * Collects the metafaceade variables.
     * 
     * @throws CartridgeParsingException If the cartridge could not be parsed.
     */
    private void retrieveMetafacadeVariables() throws CartridgeParsingException
    {
        Cartridge cartridge = getCartridge();
        Template[] templateArray = cartridge.getTemplateArray();
        for (int i = 0; i < templateArray.length; i++)
        {
            Template template = templateArray[i];

            // if the cartridge collects more than one instance of the metafacade, the variable is a collection:
            boolean isCollection = template.getOutputToSingleFile();

            // the variables we're going to find now are valid only in the template specified by this path:
            String templatePath = template.getPath();

            ModelElements modelElements = template.getModelElements();
            ModelElement[] modelElementArray = modelElements.getModelElementArray();
            for (int j = 0; j < modelElementArray.length; j++)
            {
                ModelElement modelElement = modelElementArray[j];
                if (modelElement.getVariable() != null)
                {
                    // the name of the variable:
                    String variableName = modelElement.getVariable().getStringValue();

                    Type[] typeArray = modelElement.getTypeArray();
                    for (int k = 0; k < typeArray.length; k++)
                    {
                        Type modelElementType = typeArray[k];
                        if (modelElementType.getName() != null)
                        {
                            String className = modelElementType.getName().getStringValue();
                            IType type;
                            if (javaProject != null)
                            {
                                try
                                {
                                    type = javaProject.findType(className);
                                    
                                    // TODO we need a true mapping from templates to variables !!!!
                                    
                                    ICartridgeMetafacadeVariableDescriptor descriptor = new CartridgeMetafacadeVariableDescriptor(
                                            variableName, "", type, isCollection, templatePath);
                                    cartridgeVariables.put(descriptor);
                                }
                                catch (JavaModelException e)
                                {
                                    throw new CartridgeParsingException(e);
                                }
                            }
                        }

                    }
                }
            }
        }

    }

    /**
     * @param variables
     * @throws CartridgeParsingException
     */
    private void retrieveCartridgeTemplateObjects() throws CartridgeParsingException
    {
        Cartridge cartridge = getCartridge();
        TemplateObject[] templateObjectArray = cartridge.getTemplateObjectArray();
        for (int i = 0; i < templateObjectArray.length; i++)
        {
            TemplateObject templateObject = templateObjectArray[i];
            String objectName = templateObject.getName();
            String objectClassName = templateObject.getClassName();
            String documentation = ""; // TODO we should retrieve the javadoc of the type!

            IType type;
            try
            {
                if (javaProject != null)
                {
                    type = javaProject.findType(objectClassName);
                    ICartridgeVariableDescriptor descriptor = new CartridgeJavaVariableDescriptor(objectName,
                            documentation, type);
                    cartridgeVariables.put(descriptor);
                }
            }
            catch (JavaModelException e)
            {
                throw new CartridgeParsingException(e);
            }

        }
    }

    /**
     * @throws CartridgeParsingException
     */
    private void retrieveCartridgeProperties() throws CartridgeParsingException
    {
        Namespace namespace = getNamespace();
        Properties[] propertiesArray = namespace.getPropertiesArray();
        for (int i = 0; i < propertiesArray.length; i++)
        {
            Properties properties = propertiesArray[i];
            PropertyGroup[] propertyGroupArray = properties.getPropertyGroupArray();

            for (int j = 0; j < propertyGroupArray.length; j++)
            {
                PropertyGroup group = propertyGroupArray[j];
                XmlObject groupDocumentation = group.getDocumentation();
                String groupName = group.getName();
                org.andromda.core.namespace.PropertyDocument.Property[] propertyArray = group.getPropertyArray();
                for (int k = 0; k < propertyArray.length; k++)
                {
                    org.andromda.core.namespace.PropertyDocument.Property property = propertyArray[k];
                    String propertyName = property.getName();

                    if (isPropertyUsedInCartridge(propertyName))
                    {
                        String propertyDocumentation = property.getDocumentation().toString();

                        ICartridgeVariableDescriptor descriptor = new CartridgeVariableDescriptor(propertyName,
                                propertyDocumentation);

                        cartridgeVariables.put(descriptor);
                    }
                }
            }
        }
    }

    /**
     * Find out whether the given property is referenced in the cartridge.
     * 
     * @param propertyName The property to look for.
     * @return <code>true</code> if the property is used, <code>false</code> otherwise.
     * @throws CartridgeParsingException If the cartridge could not be parsed.
     */
    private boolean isPropertyUsedInCartridge(final String propertyName) throws CartridgeParsingException
    {
        Cartridge cartridge = getCartridge();
        Property[] propertyArray = cartridge.getPropertyArray();

        for (int i = 0; i < propertyArray.length; i++)
        {
            Property property = propertyArray[i];
            XmlAnySimpleType referenceXml = property.getReference();
            String reference = referenceXml.getStringValue();
            if (reference.equals(propertyName))
            {
                return true;
            }
        }

        return false;
    }

}
