package org.andromda.repositories.emf.uml22;

import java.net.URL;
import java.util.Map;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.andromda.metafacades.emf.uml22.UMLModelAccessFacade;
import org.andromda.repositories.emf.EMFRepositoryFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.mapping.ecore2xml.Ecore2XMLPackage;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UML22UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * Implements an AndroMDA object model repository by using the <a
 * href="http://www.eclipse.org/uml2/">Eclipse UML2 API set </a>.
 * 
 * @author Steve Jerman
 * @author Chad Brandon
 * @author Matthias Bohlen (native IBM RSM file reading)
 * @author Bob Fields (Multiple model support, RSM Profiles)
 */
public class EMFUML2RepositoryFacade extends EMFRepositoryFacade
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EMFUML2RepositoryFacade.class);

    /**
     * Perform required registrations for EMF/UML2.
     * 
     * @see org.andromda.core.repository.RepositoryFacade#open()
     */
    @Override
    protected ResourceSet createNewResourceSet()
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Registering resource factories");
        }

        // Use our own proxy resolver which extends the standard UML2 resolver, to load moduleSearchLocations URLs
        final ResourceSet proxyResourceSet = new EMXProxyResolvingResourceSet();
        // Maps from file extension to resource for XML deserialization
        final Map extensionToFactoryMap = proxyResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();

        // - we need to perform these registrations in order to load a UML model into EMF
        //   see: http://dev.eclipse.org/viewcvs/indextools.cgi/%7Echeckout%7E/uml2-home/faq.html#6 OR http://wiki.eclipse.org/MDT/UML2/FAQ
        Map packageRegistry = proxyResourceSet.getPackageRegistry();
        // EcorePackage.eNS_URI=http://www.eclipse.org/emf/2002/Ecore
        packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
        // UMLPackage.eNS_URI=http://www.eclipse.org/uml2/2.1.0/UML
        // This gives a ConnectException when loading the model unless 2.0.0 namespace is also registered
        packageRegistry.put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        packageRegistry.put("http://www.eclipse.org/uml2/2.0.0/UML", UMLPackage.eINSTANCE);
        packageRegistry.put(Ecore2XMLPackage.eNS_URI, Ecore2XMLPackage.eINSTANCE);
        // register the UML2 schema against the standard UML namespace for UML 2.0 and 2.1
        // see: http://dev.eclipse.org/newslists/news.eclipse.tools.uml2/msg03392.html
        packageRegistry.put("http://schema.omg.org/spec/UML/2.0", UMLPackage.eINSTANCE);
        packageRegistry.put("http://schema.omg.org/spec/UML/2.1", UMLPackage.eINSTANCE);
        // Register all files with all extensions as .uml resources, for loading purposes
        extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION, UMLResource.Factory.INSTANCE);

        // if IBM's metamodel jars are on the classpath, we can register the package factories.
        // This appears to have no effect, emx models are processed anyway.
        boolean registered = registerOptionalRsmMetamodels(proxyResourceSet.getPackageRegistry());
        // RSM profiles Default and Deployment.epx are dependencies referred to by com/ibm/rsm/7.5/pom.
        // UML2 Standard resources are located under org/eclipse/uml2/uml/resources, referred to by metafacade dependency so it is in the plugin classpath.
        // Eclipse examples show URI.create with a hard-coded jar file location like jar:file:/C:/eclipse/plugins/org.eclipse.uml2.uml.resources_2.0.0.v200606221411.jar!/
        // Find the UML2 resources on the plugin classpath and set proxy URI based on actual found location.
        URL url = this.getClass().getClassLoader().getResource("/libraries/UMLPrimitiveTypes.library.uml");
        if (url!=null)
        {
            // Need to create a pathmap location map for UML2 Resources, to load standard profiles. 
            String path = url.getPath().substring(0, url.getPath().indexOf("libraries"));
            URI uri = URI.createURI("jar:" + path);

            // URI umlResourcePluginURI = URI.createURI("jar:file:/" + umlResourcePath + "!/");
            // UML_LIBRARIES referenced by model, UML_METAMODELS referenced by profiles
            // = UMLResource.LIBRARIES_PATHMAP=pathmap://UML_LIBRARIES/
            // UML Resources are loaded from maven org/eclipse/uml2/uml/resources
            URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
                uri.appendSegment("libraries").appendSegment(""));
            // UMLResource.METAMODELS_PATHMAP=pathmap://UML_LIBRARIES/
            URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
                uri.appendSegment("metamodels").appendSegment(""));
            // UMLResource.PROFILES_PATHMAP=UMLResource.PROFILES_PATHMAP
            URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
                uri.appendSegment("profiles").appendSegment(""));
        }
        // Local implementation which delegates to the global map, so registrations are local
        Map uriMap = proxyResourceSet.getURIConverter().getURIMap();
        uriMap.putAll(UML22UMLExtendedMetaData.getURIMap());
        uriMap.put(URI.createURI("http://schema.omg.org/spec/UML/2.0"),
            URI.createURI("http://www.eclipse.org/uml2/1.0.0/UML"));
        // Add pathmap for RSM UML2_MSL_PROFILES in com/ibm/xtools/uml/msl/7.10.500/msl-7.10.500.jar 
        url = this.getClass().getClassLoader().getResource("/profiles/Default.epx");
        if (url!=null)
        {            
            // Need to create a pathmap location map for UML2_MSL_PROFILES, to load additional RSM profiles.
            String path = url.getPath().substring(0, url.getPath().indexOf("profiles"));
            URI uri = URI.createURI("jar:" + path);
            //URI uri = URI.createURI("jar:file:/C:/Programs/IBM/SDP70Shared/plugins/com.ibm.xtools.uml.msl_7.5.0.v20080731_1905.jar!/");
            //URIConverter.URI_MAP.put(URI.createURI("pathmap://UML2_MSL_PROFILES/"), uri.appendSegment("profiles").appendSegment(""));
            // UML2_MSL_PROFILES are used in IBM RSM models.
            uriMap.put(URI.createURI("pathmap://UML2_MSL_PROFILES/"), uri.appendSegment("profiles").appendSegment(""));
        }
        // Add pathmap for RUP_PROFILES in com/ibm/xtools/modeler/ui/templates/7.5.500/templates-7.5.500.jar
        url = this.getClass().getClassLoader().getResource("/profiles/RUPAnalysis.epx");
        if (url!=null)
        {            
            String path = url.getPath().substring(0, url.getPath().indexOf("profiles"));
            URI uri = URI.createURI("jar:" + path);
            //URIConverter.URI_MAP.put(URI.createURI("pathmap://UML2_MSL_PROFILES/"), uri.appendSegment("profiles").appendSegment(""));
            // UML2_MSL_PROFILES are used in IBM RSM models.
            uriMap.put(URI.createURI("pathmap://RUP_PROFILES/"), uri.appendSegment("profiles").appendSegment(""));
        }
        
        //TODO This doesn't seem to help to resolve the pathmap.
        // moduleSearchLocations values must be added to andromda.xml
        //TODO Enable <pathmaps><pathmap name= value=/> in andromda.xml configuration
        // pathmap://m2repository is used in starter models to reference profiles deployed in maven local repository
        String m2repository = System.getenv("M2_REPO");
        if (m2repository!=null)
        {
            URI uri = URI.createURI(m2repository.replace("\\", "/") + '/');
            // This doesn't seem to load the pathmap resources from the m2repository.
            uriMap.put(URI.createURI("pathmap://m2repository/"), uri.appendSegment(""));
            // m2repository conflicts with pathmap variable added by Sonatype eclipse plugin, use M2_REPO instead.
            uriMap.put(URI.createURI("pathmap://M2_REPO/"), uri.appendSegment(""));
        }

        // - populate the load options
        final Map loadOptions = this.getLoadOptions();
        // Enable notifications during load. Profiles not found do not generate a notification
        loadOptions.put(XMLResource.OPTION_DISABLE_NOTIFY, Boolean.FALSE);
        loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
        loadOptions.put(XMLResource.OPTION_DOM_USE_NAMESPACES_IN_SCOPE, Boolean.TRUE);

        return proxyResourceSet;
    }

    /**
     * To read IBM Rational Software Modeler (RSM) files (*.emx, *.epx, ...) directly,
     * we need to register two additional metamodels for annotation elements
     * which are referenced inside the UML2 models created by IBM RSM.
     * 
     * @param registry the registry in which metamodels should be registered
     * @return 
     */
    private boolean registerOptionalRsmMetamodels(EPackage.Registry registry)
    {
        // RSM6 uses xtools NotationPackage, RSM7 uses gmf NotationPackage
        //boolean registered = registerOptionalMetamodel(registry, "com.ibm.xtools.notation.NotationPackage");
        boolean registered = registerOptionalMetamodel(registry, "com.ibm.xtools.umlnotation.UmlnotationPackage");
        //registered = registerOptionalMetamodel(registry, "com.ibm.xtools.topic.Topic");
        registered = registerOptionalMetamodel(registry, "org.eclipse.gmf.runtime.notation.NotationPackage");
        return registered;
    }

    /**
     * Register a metamodel in EMF so that models based on that metamodel can
     * be loaded correctly. This appears to have no effect on model processing.
     * 
     * @param registry EMF package registry
     * @param ePackageClassName the class name of the package to be registered
     */
    private boolean registerOptionalMetamodel(EPackage.Registry registry, String ePackageClassName)
    {
        boolean registered = false;
        try
        {
            //Including the additional model dependencies in the project pom.xml does not make the class visible to the classloader.
            // Need to include the dependency with repository emf-uml22.
            Class ePackageClass = Class.forName(ePackageClassName);
            if (ePackageClass != null)
            {
                // get those two famous static fields
                String nsURI = (String) ePackageClass.getField("eNS_URI").get(null);
                Object eInstance = ePackageClass.getField("eINSTANCE").get(null);
                registry.put(nsURI, eInstance);
                if (logger.isDebugEnabled())
                {
                    logger.debug("Optional metamodel registered: " + nsURI);
                }
                registered = true;
            }
        }
        catch (Exception e)
        {
            // do nothing when metamodels are not present -- they are entirely optional.
        }
        return registered;
    }

    /**
     * Overridden to check that the model is of the correct type.
     * 
     * @see org.andromda.repositories.emf.EMFRepositoryFacade#readModel(String)
     */
    @Override
    protected void readModel(final String uri)
    {
        super.readModel(uri);
        // Just to be sure there is a valid "model" inside
        for (Resource modelResource : this.model)
        {
            //EObject modelPackage = modelResource.getEObject(modelResource.getURI().toString());
            /*Collection<EObject> modelPackages = EcoreUtil.getObjectsByType(
                modelResource.getContents(), EcorePackage.eINSTANCE.getEObject());
            // UML2 Model is a Package
            for (EObject modelPkg : modelPackages )
            {
                System.out.println("Model '" + uri + "' package " + modelPkg);
            }*/
            EObject modelPackage = (EObject) EcoreUtil.getObjectByType(
                modelResource.getContents(), EcorePackage.eINSTANCE.getEObject());
            if (modelPackage==null)
            {
                throw new RepositoryFacadeException("Model '" + uri + "' is not a valid EMF UML2 model: Model element not found");
            }
            /*else if (modelPackage instanceof org.eclipse.uml2.uml.Package)
            {
                throw new RepositoryFacadeException("Model '" + uri + "' package " + modelPackage + " is not a valid EMF UML2 model: top level Package must be a Model, not a Package.");
            }*/
            // UML2 model top level element can be either a Model or Package. Package messes up processing because metafacades assume Model is at the top.
            else if (!(modelPackage instanceof Model) && !(modelPackage instanceof org.eclipse.uml2.uml.Package))
            {
                throw new RepositoryFacadeException("Model '" + uri + "' package " + modelPackage + " is not a valid EMF UML2 model");
            }
        }
    }

    /**
     * @see org.andromda.core.repository.RepositoryFacade#getModel()
     */
    public ModelAccessFacade getModel()
    {
        return this.getModel(null);
    }

    /**
     * @param uri 
     * @return this.modelFacade
     * @see org.andromda.core.repository.RepositoryFacade#getModel()
     */
    public ModelAccessFacade getModel(String uri)
    {
        if (this.modelFacade == null)
        {
            try
            {
                this.modelFacade = (ModelAccessFacade) ComponentContainer.instance().newComponent(
                        UMLModelAccessFacade.class, ModelAccessFacade.class);
            }
            catch (final Throwable throwable)
            {
                throw new RepositoryFacadeException(throwable);
            }
        }
        if (StringUtils.isNotEmpty(uri))
        {
            URI resource = URI.createURI(uri);
            Resource uriModel = this.resourceSet.getResource(resource, true);
            this.modelFacade.setModel(uriModel);
        }
        if (this.model != null)
        {
            this.modelFacade.setModel(this.model);
        }
        else
        {
            this.modelFacade = null;
        }
        return this.modelFacade;
    }
}