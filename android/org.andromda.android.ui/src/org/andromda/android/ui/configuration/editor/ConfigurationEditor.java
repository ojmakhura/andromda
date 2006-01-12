package org.andromda.android.ui.configuration.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.model.IEditorModel;
import org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel;
import org.andromda.android.ui.internal.configuration.editor.cartridge.CartridgeConfigurationPage;
import org.andromda.android.ui.internal.configuration.editor.model.ModelConfigurationPage;
import org.andromda.android.ui.internal.configuration.editor.server.ServerConfigurationPage;
import org.andromda.android.ui.internal.editor.AbstractModelFormEditor;
import org.andromda.core.configuration.AndromdaDocument;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * Editor for the AndroMDA configuration file (andromda.xml).
 * 
 * @author Peter Friese
 * @since 08.11.2005
 */
public class ConfigurationEditor
        extends AbstractModelFormEditor
{

    /** The number of spaces rendered in the XML file for one indentation. */
    private static final int NUMBER_OF_SPACES = 4;

    /** The wrapped AndroMDA configuration being edited. */
    private IAndromdaDocumentEditorModel andromdaDocumentEditorModel;

    /**
     * {@inheritDoc}
     */
    protected void addFormPages() throws PartInitException
    {
        ServerConfigurationPage serverConfigurationPage = new ServerConfigurationPage(this,
                ServerConfigurationPage.PAGE_ID, "Server");
        addPage(serverConfigurationPage);

        ModelConfigurationPage modelConfigurationPage = new ModelConfigurationPage(this,
                ModelConfigurationPage.PAGE_ID, "Models");
        addPage(modelConfigurationPage);

        CartridgeConfigurationPage cartridgeConfigurationPage = new CartridgeConfigurationPage(this,
                CartridgeConfigurationPage.PAGE_ID, "Cartridges");
        addPage(cartridgeConfigurationPage);
    }

    /**
     * {@inheritDoc}
     */
    public void init(final IEditorSite site,
        final IEditorInput input) throws PartInitException
    {
        super.init(site, input);
        setPartName(input.getName());
        firePropertyChange(PROP_TITLE);
    }

    /**
     * {@inheritDoc}
     */
    public void doSave(final IProgressMonitor monitor)
    {
        commitFormPages(true);

        // TODO temporary HACK!
        XmlOptions options = setupDefaultNamespace();
        AndromdaDocument andromdaDocument = getAndromdaDocumentEditorModel().getAndromdaDocument();
        getDocument().set(andromdaDocument.toString());

        super.doSave(monitor);
    }

    /**
     * @param onSave
     */
    private void commitFormPages(final boolean onSave)
    {
        IFormPage[] pages = getPages();
        for (int i = 0; i < pages.length; i++)
        {
            IFormPage page = pages[i];
            IManagedForm mform = page.getManagedForm();
            if (mform != null && mform.isDirty())
            {
                mform.commit(true);
            }
        }
    }

    /**
     * Returns an array of all pages of this editor.
     * 
     * @return an array of all pages.
     */
    private IFormPage[] getPages()
    {
        ArrayList formPages = new ArrayList();
        for (int i = 0; i < pages.size(); i++)
        {
            Object page = pages.get(i);
            if (page instanceof IFormPage)
            {
                formPages.add(page);
            }
        }
        return (IFormPage[])formPages.toArray(new IFormPage[formPages.size()]);
    }

    /**
     * Setup an XmlOptions instance so the parser will assume the default namespace for the config document even if is
     * has no namespace set.
     * 
     * @return an XmlOptions instance suitable for parsing AndroMDA configuration documents.
     */
    private XmlOptions setupDefaultNamespace()
    {
        XmlOptions options = new XmlOptions();
        Map namespaceMapping = new HashMap();
        namespaceMapping.put("", "http://andromda.org/core/configuration");
        options.setLoadSubstituteNamespaces(namespaceMapping);

        options.setUseDefaultNamespace();
        options.setSavePrettyPrint().setSavePrettyPrintIndent(NUMBER_OF_SPACES);
        return options;
    }

    /**
     * {@inheritDoc}
     */
    public IEditorModel getEditorModel()
    {
        return getAndromdaDocumentEditorModel();
    }

    /**
     * @return
     */
    public IAndromdaDocumentEditorModel getAndromdaDocumentEditorModel()
    {
        if (andromdaDocumentEditorModel == null) {
            IDocument editorDocument = getDocument();
            andromdaDocumentEditorModel = IAndromdaDocumentEditorModel.Factory.newInstance(editorDocument);
        }        
        return andromdaDocumentEditorModel;
    }

}
