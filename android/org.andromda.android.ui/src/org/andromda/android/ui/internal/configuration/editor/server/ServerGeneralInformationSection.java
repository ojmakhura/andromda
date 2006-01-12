package org.andromda.android.ui.internal.configuration.editor.server;

import java.math.BigInteger;

import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelSectionPart;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.andromda.core.configuration.AndromdaDocument;
import org.andromda.core.configuration.ServerDocument.Server;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.forms.IManagedForm;

/**
 * This section contains general information about the AndroMDA server.
 * 
 * @author Peter Friese
 * @since 08.12.2005
 */
public class ServerGeneralInformationSection
        extends AbstractAndromdaModelSectionPart
{

    /** The composite with the edit controls. */
    private ServerGeneralInformationComposite servergeneralInformationComposite;

    /**
     * Creates a new section hosted on the specified <code>page</code>.
     * 
     * @param page The page this section is to be hosted on.
     */
    public ServerGeneralInformationSection(AbstractModelFormPage page)
    {
        super(page);
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
     */
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
        AndromdaDocument andromdaDocument = getAndromdaDocument();

        Server server = andromdaDocument.getAndromda().getServer();
        if (server != null)
        {

            String host = server.getHost();
            BigInteger port = server.getPort();

            servergeneralInformationComposite.setHost(host);
            servergeneralInformationComposite.setPort(port);
        }
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        AndromdaDocument andromdaDocument = getAndromdaDocument();
        Server server = andromdaDocument.getAndromda().getServer();
        if (server != null)
        {
            String host = servergeneralInformationComposite.getHost();
            server.setHost(host);

            BigInteger port = servergeneralInformationComposite.getPort();
            server.setPort(port);
        }
        super.commit(onSave);
    }

    /**
     * @see org.andromda.android.ui.internal.editor.AbstractModelSectionPart#modelChanged(org.andromda.android.core.model.IModelChangedEvent)
     */
    public void modelChanged(IModelChangedEvent event)
    {
        refresh();
    }

}
