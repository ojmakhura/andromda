package org.andromda.repositories.emf.uml2;

import java.util.Map;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.repository.RepositoryFacadeException;
import org.andromda.metafacades.emf.uml2.UMLModelAccessFacade;
import org.andromda.repositories.emf.EMFRepositoryFacade;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.UML2Package;
import org.eclipse.uml2.util.UML2Resource;

/**
 * Implements an AndroMDA object model repository by using the <a
 * href="http://www.eclipse.org/uml2/">Eclipse UML2 API set </a>.
 * 
 * @author Steve Jerman
 * @author Chad Brandon
 * @author Matthias Bohlen (native IBM RSM file reading)
 * @author Bob Fields (Multiple model support)
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

        final ResourceSet proxyResourceSet = new EMXProxyResolvingResourceSet();
        final Map extensionToFactoryMap = proxyResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();

        // - we need to perform these registrations in order to load a UML2 model into EMF
        //   see: http://dev.eclipse.org/viewcvs/indextools.cgi/%7Echeckout%7E/uml2-home/faq.html#6
        proxyResourceSet.getPackageRegistry().put(UML2Package.eNS_URI, UML2Package.eINSTANCE);
        extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION, UML2Resource.Factory.INSTANCE);

        // if IBM's metamodel jars are on the classpath, we can register the
        // package factories
        registerOptionalRsmMetamodels(proxyResourceSet.getPackageRegistry());

        // - populate the load options
        final Map loadOptions = this.getLoadOptions();
        loadOptions.put(XMLResource.OPTION_DISABLE_NOTIFY, Boolean.FALSE);
        loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

        return proxyResourceSet;
    }

    /**
     * To read IBM Rational Software Modeler (RSM) files (*.emx, *.epx, ...) directly,
     * we need to register two additional metamodels for annotation elements
     * which are referenced inside the UML2 models created by IBM RSM.
     * 
     * @param registry the registry in which metamodels should be registered
     */
    private void registerOptionalRsmMetamodels(EPackage.Registry registry)
    {
        registerOptionalMetamodel(registry, "com.ibm.xtools.notation.NotationPackage");
        registerOptionalMetamodel(registry, "com.ibm.xtools.umlnotation.UmlnotationPackage");
    }

    /**
     * Register a metamodel in EMF so that models based on that metamodel can
     * be loaded correctly.
     * 
     * @param registry EMF package registry
     * @param ePackageClassName the class name of the package to be registered
     */
    private void registerOptionalMetamodel(EPackage.Registry registry, String ePackageClassName)
    {
        try
        {
            Class ePackageClass = Class.forName(ePackageClassName);
            if (ePackageClass != null)
            {
                // get those two famous static fields
                Object nsURI = ePackageClass.getField("eNS_URI").get(null);
                Object eInstance = ePackageClass.getField("eINSTANCE").get(null);
                registry.put((String) nsURI, eInstance);
                logger.debug("Optional metamodel registered: " + nsURI);
            }
        }
        catch (Exception e)
        {
            // do nothing when metamodels are not present -- they are entirely optional.
        }
    }

    /**
     * Overridden to check that the model is of the correct type.
     * 
     * @see org.andromda.repositories.emf.EMFRepositoryFacade#readModel(java.lang.String)
     */
    @Override
    protected void readModel(final String uri)
    {
        super.readModel(uri);
        // Just to be sure there is a valid "model" inside
        for (Resource modelResource : this.model)
        {
            EObject modelPackage = (EObject) EcoreUtil.getObjectByType(modelResource.getContents(), EcorePackage.eINSTANCE
                    .getEObject());
            if (!(modelPackage instanceof Model))
            {
                throw new RepositoryFacadeException("Model '" + uri + "' is not a valid EMF UML2 model");
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
        /*if (StringUtils.isNotEmpty(uri))
        {
                URI resource = URI.createURI(uri);
                Resource uriModel = this.resourceSet.getResource(resource, true);
                this.modelFacade.setModel(uriModel);
        }*/
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