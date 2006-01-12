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
 * This section displays the server loading behaviour edit elements.
 *
 * @author Peter Friese
 * @since 09.12.2005
 */
public class ServerLoadingSection
        extends AbstractAndromdaModelSectionPart
{

    /** This composite contains the edit fields used to edit the server model loading behaviour. */
    private ServerLoadingComposite serverLoadingComposite;

    /**
     * Creates a new section.
     *
     * @param page The hosting page.
     */
    public ServerLoadingSection(AbstractModelFormPage page)
    {
        super(page);
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
     */
    public void initialize(IManagedForm form)
    {
        super.initialize(form);
        getSection().setText("Loading behaviour");
        getSection().setDescription("Specify the server's loading behaviour.");

        // insert server loading behaviour composite
        serverLoadingComposite = new ServerLoadingComposite(this, SWT.NONE);
        getSection().setClient(serverLoadingComposite);
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
            BigInteger loadInterval = server.getLoadInterval();
            BigInteger maximumFailedLoadAttempts = server.getMaximumFailedLoadAttempts();

            serverLoadingComposite.setLoadInterval(loadInterval);
            serverLoadingComposite.setMaximumFailedLoadAttempts(maximumFailedLoadAttempts);
        }
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        {
            AndromdaDocument andromdaDocument = getAndromdaDocument();

            Server server = andromdaDocument.getAndromda().getServer();
            if (server != null)
            {
                BigInteger loadInterval = serverLoadingComposite.getLoadInterval();
                if (loadInterval != null)
                {
                    server.setLoadInterval(loadInterval);
                }
                else
                {
                    if (server.isSetLoadInterval())
                    {
                        server.unsetLoadInterval();
                    }
                }

                BigInteger maximumFailedLoadAttempts = serverLoadingComposite.getMaximumFailedLoadAttempts();
                if (maximumFailedLoadAttempts != null)
                {
                    server.setMaximumFailedLoadAttempts(maximumFailedLoadAttempts);
                }
                else
                {
                    if (server.isSetMaximumFailedLoadAttempts())
                    {
                        server.unsetMaximumFailedLoadAttempts();
                    }
                }
            }
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
