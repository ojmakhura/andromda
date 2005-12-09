package org.andromda.android.ui.internal.configuration.editor.server;

import java.math.BigInteger;

import org.andromda.android.ui.configuration.editor.ConfigurationEditor;
import org.andromda.android.ui.internal.editor.BaseSectionPart;
import org.andromda.core.configuration.AndromdaDocument;
import org.andromda.core.configuration.ServerDocument.Server;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

/**
 *
 * @author Peter Friese
 * @since 08.12.2005
 */
public class ServerGeneralInformationSection
        extends BaseSectionPart
{
    
    /**
     * @param page
     */
    public ServerGeneralInformationSection(FormPage page)
    {
        super(page);
    }

    private ServerGeneralInformationComposite servergeneralInformationComposite;

    public void initialize(IManagedForm form)
    {
        super.initialize(form);
        getSection().setText("General information");
        getSection().setDescription("Specify the server's general settings.");

        // insert server loading behaviour composite
        servergeneralInformationComposite = new ServerGeneralInformationComposite(this, SWT.NONE);
        getSection().setClient(servergeneralInformationComposite);
        getSection().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#refresh()
     */
    public void refresh()
    {
        super.refresh();
        FormEditor editor = getEditor();
        if (editor instanceof ConfigurationEditor)
        {
            ConfigurationEditor configurationEditor = (ConfigurationEditor)editor;
            AndromdaDocument document = configurationEditor.getDocument();
            Server server = document.getAndromda().getServer();
            
            String host = server.getHost();
            BigInteger port = server.getPort();
            
            servergeneralInformationComposite.setHost(host);
            servergeneralInformationComposite.setPort(port);
        }
    }
    
}

