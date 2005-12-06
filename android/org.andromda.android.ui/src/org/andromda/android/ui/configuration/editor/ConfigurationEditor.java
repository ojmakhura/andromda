package org.andromda.android.ui.configuration.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.ui.AndroidUIPlugin;
import org.andromda.android.ui.internal.configuration.editor.CartridgeConfigurationPage;
import org.andromda.core.configuration.AndromdaDocument;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.FileEditorInput;

/**
 * 
 * @author Peter Friese
 * @since 08.11.2005
 */
public class ConfigurationEditor
        extends FormEditor
{

    private AndromdaDocument document;

    /**
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    protected void addPages()
    {
        try
        {
            CartridgeConfigurationPage cartridgeConfigurationPage = new CartridgeConfigurationPage(this,
                    CartridgeConfigurationPage.PAGE_ID, "Cartridges");
            addPage(cartridgeConfigurationPage);
        }
        catch (Exception e)
        {
            AndroidUIPlugin.log(e);
        }
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor)
    {
        commitFormPages(true);

        IEditorInput input = getEditorInput();
        if (input instanceof FileEditorInput)
        {
            FileEditorInput fileEditorInput = (FileEditorInput)input;
            IPath path = fileEditorInput.getPath();
            String absoluteFileName = path.toOSString();
            File file = path.toFile();
            try
            {
                XmlOptions options = setupDefaultNamespace();
                getDocument().save(file, options);
                editorDirtyStateChanged();
            }
            catch (Exception e)
            {
                AndroidUIPlugin.log(e);
            }
        }

        editorDirtyStateChanged();
    }

    private void commitFormPages(boolean onSave)
    {
        IFormPage[] pages = getPages();
        for (int i = 0; i < pages.length; i++)
        {
            IFormPage page = pages[i];
            IManagedForm mform = page.getManagedForm();
            if (mform != null && mform.isDirty())
                mform.commit(true);
        }
    }

    private IFormPage[] getPages()
    {
        ArrayList formPages = new ArrayList();
        for (int i = 0; i < pages.size(); i++)
        {
            Object page = pages.get(i);
            if (page instanceof IFormPage)
                formPages.add(page);
        }
        return (IFormPage[])formPages.toArray(new IFormPage[formPages.size()]);
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed()
    {
        return false;
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
     */
    protected void setInput(IEditorInput input)
    {
        super.setInput(input);
        updateModel();
    }

    public boolean updateModel()
    {
        IEditorInput input = getEditorInput();
        boolean clean = false;
        if (input instanceof FileEditorInput)
        {
            FileEditorInput fileEditorInput = (FileEditorInput)input;
            IPath path = fileEditorInput.getPath();
            String absoluteFileName = path.toOSString();
            File file = path.toFile();
            try
            {
                XmlOptions options = setupDefaultNamespace();
                document = AndromdaDocument.Factory.parse(file, options);
                clean = true;
            }
            catch (Exception e)
            {
                AndroidUIPlugin.log(e);
            }
        }
        return clean;
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
        options.setSavePrettyPrint().setSavePrettyPrintIndent(4);
        return options;
    }

    /**
     * @return Returns the document being edited.
     */
    public AndromdaDocument getDocument()
    {
        return document;
    }

}
