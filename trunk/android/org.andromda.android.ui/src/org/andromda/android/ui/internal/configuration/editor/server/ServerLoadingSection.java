package org.andromda.android.ui.internal.configuration.editor.server;

import java.math.BigInteger;

import org.andromda.android.ui.configuration.editor.ConfigurationEditor;
import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelFormPage;
import org.andromda.android.ui.internal.configuration.editor.IAndromdaDocumentModel;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.andromda.android.ui.internal.editor.AbstractModelSectionPart;
import org.andromda.core.configuration.AndromdaDocument;
import org.andromda.core.configuration.ServerDocument.Server;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * 
 * @author Peter Friese
 * @since 09.12.2005
 */
public class ServerLoadingSection
        extends AbstractModelSectionPart
{

    /** This composite contains the edit fields used to edit the server model loading behaviour. */
    private ServerLoadingComposite serverLoadingComposite;

    /**
     * @param page
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
        IAndromdaDocumentModel andromdaDocumentModel = ((AbstractAndromdaModelFormPage)getPage())
                .getAndromdaDocumentModel();
        AndromdaDocument andromdaDocument = andromdaDocumentModel.getAndromdaDocument();
        Server server = andromdaDocument.getAndromda().getServer();
        BigInteger loadInterval = server.getLoadInterval();
        BigInteger maximumFailedLoadAttempts = server.getMaximumFailedLoadAttempts();

        serverLoadingComposite.setLoadInterval(loadInterval);
        serverLoadingComposite.setMaximumFailedLoadAttempts(maximumFailedLoadAttempts);
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        {
            IAndromdaDocumentModel andromdaDocumentModel = ((AbstractAndromdaModelFormPage)getPage())
                    .getAndromdaDocumentModel();
            AndromdaDocument andromdaDocument = andromdaDocumentModel.getAndromdaDocument();

            Server server = andromdaDocument.getAndromda().getServer();

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
        super.commit(onSave);
    }

}
