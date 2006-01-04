package org.andromda.mdr.xmi.uml20.load.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.jmi.model.Classifier;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import javax.xml.parsers.ParserConfigurationException;

import org.andromda.mdr.xmi.uml20.load.configuration.Configuration;
import org.andromda.mdr.xmi.uml20.load.configuration.LoadConfigurationFactory;
import org.andromda.mdr.xmi.uml20.load.core.HREFInfo;
import org.andromda.mdr.xmi.uml20.load.core.ReferenceInfo;
import org.andromda.mdr.xmi.uml20.load.handlers.MainHandler;
import org.andromda.mdr.xmi.uml20.load.handlers.ParserHandler;
import org.andromda.mdr.xmi.uml20.load.handlers.XMIVersionException;
import org.andromda.mdr.xmi.uml20.load.handlers.model.DelegateHandler;
import org.andromda.mdr.xmi.uml20.load.utils.ModulesResolver;
import org.andromda.mdr.xmi.uml20.load.utils.PropertiesSetter;
import org.apache.log4j.Logger;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.xmi.InputConfig;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * Responsible for parsing xmi files. It is responsible also for loading
 * dependent modules and establish the references.
 */
public class Xmi2Reader
{
    private static final String MODULES_DIRS = "modules.dirs";
    private InputConfig mXMIInputConfig;
    private Logger logger = Logger.getLogger(Xmi2Reader.class);

    /**
     * Complete load process. Parse, load modules and establish references.
     *
     * @param info
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private void loadLoop(LoadInformationContainer info)
        throws IOException, ParserConfigurationException, SAXException
    {
        parse(info);
        loadModules(info);
        updateReferences(info);
    }

    /**
     * @param parentInfo
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    private void loadModules(LoadInformationContainer parentInfo)
        throws IOException, ParserConfigurationException, SAXException
    {
        ModulesResolver referenceResolver = parentInfo.getReferenceResolver();
        Collection externalElements = referenceResolver.getExternalFilesToLoad();
        for (Iterator iter = externalElements.iterator(); iter.hasNext();)
        {
            String externalFile = (String)iter.next();
            referenceResolver.addLoaded(externalFile);
            loadModule(
                externalFile,
                parentInfo);
        }
    }

    /**
     * Establish external references
     */
    private void updateReferences(LoadInformationContainer info)
    {
        PropertiesSetter propertiesSetter = info.getPropertiesSetter();

        ArrayList refs = new ArrayList(propertiesSetter.getRefs());
        propertiesSetter.getRefs().clear();
        for (int i = 0; i < refs.size(); i++)
        {
            ReferenceInfo r = (ReferenceInfo)refs.get(i);
            propertiesSetter.setReference(r);
        }

        if (propertiesSetter.getRefs().size() > 0)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("There are: " + propertiesSetter.getRefs().size() + " references not set:");
            }
            for (Iterator i = propertiesSetter.getRefs().iterator(); i.hasNext();)
            {
                ReferenceInfo next = (ReferenceInfo)i.next();

                RefObject owner = next.getOwner();
                String representedTex = "";
                String ownerText =
                    ((Classifier)owner.refClass().refMetaObject()).getName() + " " + representedTex + " (" +
                    info.getIDRegistry().getSavedID(owner.refMofId()) + ")";
                String lostElementText = "";
                HREFInfo refInfo = next.getHRefInfo();
                if (refInfo != null)
                {
                    String module = ModulesResolver.getFileNameFromHREF(next.getIDRef());
                    String string = "from module: " + module;
                    lostElementText += " " + string;
                    lostElementText += " " + refInfo.getType();
                    if (refInfo.getPath() != null)
                    {
                        lostElementText += " " + refInfo.getPath();
                    }
                }
                lostElementText += " (" + next.getIDRef() + ")";
                String text = "feature not set:" + next.getFeatureName() + " " + ownerText + " " + lostElementText;
                logger.error(text);
                if (logger.isDebugEnabled())
                {
                    logger.debug(
                        "feature: " + next.getFeatureName() + " not set: " + next.getIDRef() + " for element id: " +
                        info.getIDRegistry().getSavedID(owner.refMofId()));
                }
            }
        }
    }

    /**
     * Parse the xmi file represented by the given parameter.
     *
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws Exception
     */
    private void parse(LoadInformationContainer infoContainer)
        throws IOException, ParserConfigurationException, SAXException
    {
        // Collection externalFiles;
        MainHandler mainHandler = new MainHandler(infoContainer);
        mainHandler.pushHandler(new DelegateHandler());
        ParserHandler handler = new ParserHandler(mainHandler,
                mainHandler.getInfoContainer());
        handler.parseXmi(
            infoContainer.getStream(),
            infoContainer.getURI());
        infoContainer.getConfiguration().handleAfterParsing(infoContainer.getXMIConfig());
    }

    /**
     * Load module with the given name.
     *
     * @param moduleName Module name
     * @param currentResourceID id of the current resource. Will be used to set
     *        the modules hierarchy.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws Exception
     */
    private void loadModule(
        String moduleName,
        LoadInformationContainer info)
        throws IOException, ParserConfigurationException, SAXException
    {
        String properResourceName = moduleName;
        if (logger.isDebugEnabled())
        {
            logger.debug("loading module: " + properResourceName);
        }

        LoadInformationContainer infoContainer = (LoadInformationContainer)info.clone();

        Configuration action = LoadConfigurationFactory.createLoadingModuleConfiguration();
        infoContainer.setConfiguration(action);

        InputStream stream = infoContainer.getReferenceResolver().getStream(properResourceName);
        if (stream == null)
        {
            logger.error("loaded without module");
            return;
        }

        infoContainer.setStream(stream);
        infoContainer.setURI(infoContainer.getReferenceResolver().getURI(properResourceName).toString());
        Xmi2Reader xmiReader2 = new Xmi2Reader();
        xmiReader2.loadLoop(infoContainer);
    }

    public void setConfiguratio(XMIInputConfig configuration)
    {
        if (configuration instanceof InputConfig)
        {
            mXMIInputConfig = (InputConfig)configuration;
        }
    }

    public XMIInputConfig getConfiguration()
    {
        if (mXMIInputConfig == null)
        {
            mXMIInputConfig = new InputConfig();
            mXMIInputConfig.setUnknownElementsIgnored(true);
        }
        return mXMIInputConfig;
    }

    public Collection read(
        InputStream stream,
        String URI,
        RefPackage extent)
        throws IOException, XMIVersionException
    {
        LoadInformationContainer info =
            new LoadInformationContainer(extent,
                LoadConfigurationFactory.createLoadingConfiguration(), (InputConfig)getConfiguration());
        info.setStream(stream);
        info.setURI(URI);
        info.getReferenceResolver().setModulesDirectories(getModulesDirectories());

        try
        {
            info.getRepository().beginTrans(true);
            loadLoop(info);
        }
        catch (SAXParseException ex)
        {
            throw new IOException(ex.getException().getMessage() + " at " + ex.getLineNumber() + ":" +
                ex.getColumnNumber());
        }
        catch (XMIVersionException ex)
        {
            throw ex;
        }
        catch (SAXException sax)
        {
            if (sax.getException() != null)
            {
                throw new IOException(sax.getException().getMessage());
            }
            throw new IOException(sax.getMessage());
        }
        catch (ParserConfigurationException ex)
        {
            throw new IOException(ex.getMessage());
        }
        finally
        {
            info.getRepository().endTrans();
            if (info.getStream() != null)
            {
                info.getStream().close();
            }
        }
        return info.getCreatedRootElements();
    }

    private String getModulesDirectories()
    {
        XMIInputConfig configuration = getConfiguration();
        if (configuration instanceof ModulesConfig)
        {
            ModulesConfig config = (ModulesConfig)configuration;
            return config.getModulesDirectories();
        }
        return System.getProperty(
            MODULES_DIRS,
            ".");
    }

    public Collection read(
        String URI,
        RefPackage extent)
        throws IOException, XMIVersionException
    {
        return read(
            null,
            URI,
            extent);
    }

    public Collection read(
        File file,
        RefPackage extent)
        throws IOException, XMIVersionException
    {
        return read(
            ModulesResolver.createInputStreamFromFile(file.getAbsolutePath()),
            file.toURI().toString(),
            extent);
    }
}