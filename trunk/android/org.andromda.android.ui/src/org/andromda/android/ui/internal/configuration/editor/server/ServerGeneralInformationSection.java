package org.andromda.android.ui.internal.configuration.editor.server;

import java.math.BigInteger;

import org.andromda.android.core.model.IModel;
import org.andromda.android.core.model.configuration.IAndromdaDocumentModel;
import org.andromda.android.ui.configuration.editor.ConfigurationEditor;
import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelFormPage;
import org.andromda.android.ui.internal.editor.AbstractModelFormEditor;
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
 * @since 08.12.2005
 */
public class ServerGeneralInformationSection
        extends AbstractModelSectionPart
{

    /**
     * @param page
     */
    public ServerGeneralInformationSection(AbstractModelFormPage page)
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
        IAndromdaDocumentModel andromdaDocumentModel = ((AbstractAndromdaModelFormPage)getPage())
                .getAndromdaDocumentModel();
        AndromdaDocument andromdaDocument = andromdaDocumentModel.getAndromdaDocument();

        Server server = andromdaDocument.getAndromda().getServer();

        String host = server.getHost();
        BigInteger port = server.getPort();

        servergeneralInformationComposite.setHost(host);
        servergeneralInformationComposite.setPort(port);
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        AbstractModelFormEditor editor = getModelFormEditor();
        if (editor instanceof ConfigurationEditor)
        {
            IModel model = editor.getModel();
            if (model instanceof IAndromdaDocumentModel)
            {
                IAndromdaDocumentModel andromdaDocumentModel = (IAndromdaDocumentModel)model;
                AndromdaDocument andromdaDocument = andromdaDocumentModel.getAndromdaDocument();
                Server server = andromdaDocument.getAndromda().getServer();

                String host = servergeneralInformationComposite.getHost();
                server.setHost(host);

                BigInteger port = servergeneralInformationComposite.getPort();
                server.setPort(port);
            }
        }

        super.commit(onSave);
    }

}
