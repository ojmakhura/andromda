package org.andromda.android.ui.editors.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.andromda.android.internal.ui.editors.configuration.pages.CartridgeConfigurationPage;
import org.andromda.android.internal.ui.editors.configuration.pages.ConfigurationOverviewPage;
import org.andromda.android.internal.ui.editors.configuration.pages.ModelPage;
import org.andromda.core.configuration.Configuration;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

/**
 */
public class ConfigurationEditor
        extends FormEditor
{

    private ConfigurationOverviewPage overviewPage;

    private ModelPage modelsPage;

    private CartridgeConfigurationPage cartridgesConfigurationPage;

    void createOverviewPage() throws PartInitException
    {
        overviewPage = new ConfigurationOverviewPage(this, "configurationOverviewPage", "Overview");
        int index = addPage(overviewPage);
    }

    void createModelsPage() throws PartInitException
    {
        modelsPage = new ModelPage(this, "modelPage", "Models");
        int index = addPage(modelsPage);
    }

    void createCartridgeConfigurationPage() throws PartInitException
    {
        cartridgesConfigurationPage = new CartridgeConfigurationPage(this,
                "cartridgeConfigurationPage", "Cartridges");
        int index = addPage(cartridgesConfigurationPage);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    protected void addPages()
    {
        try
        {
            createOverviewPage();
            createModelsPage();
            createCartridgeConfigurationPage();
        }
        catch (PartInitException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
     */
    protected void setInput(IEditorInput input)
    {
        // TODO Auto-generated method stub
        super.setInput(input);

        if (input instanceof FileEditorInput)
        {
            FileEditorInput fileEditorInput = (FileEditorInput)input;
            IPath path = fileEditorInput.getPath();
            String absoluteFileName = path.toOSString();
            File file = path.toFile();
            URL url;
            try
            {
                url = file.toURL();
                // System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                // "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");

                ClassLoader current = getClass().getClassLoader();
                ClassLoader androMDAClassLoader = Configuration.class.getClassLoader();
                try
                {
                    Thread.currentThread().setContextClassLoader(androMDAClassLoader);
                    System.setProperty("javax.xml.parsers.SAXParserFactory",
                            "org.apache.xerces.jaxp.SAXParserFactoryImpl");
                    Configuration configuration = Configuration.getInstance(url);
                    configuration.toString();
                }
                finally
                {
                    Thread.currentThread().setContextClassLoader(current);
                }
            }
            catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}
