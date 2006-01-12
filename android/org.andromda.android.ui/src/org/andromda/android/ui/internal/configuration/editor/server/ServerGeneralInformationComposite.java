package org.andromda.android.ui.internal.configuration.editor.server;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * This composite contains controls that let the user configure the AndroMDA server.
 * 
 * @author Peter Friese
 * @since 07.12.2005
 */
public class ServerGeneralInformationComposite
        extends Composite
{

    private final class DirtyListener
            implements ModifyListener
    {
        private final AbstractFormPart parent;

        private DirtyListener(AbstractFormPart parent)
        {
            super();
            this.parent = parent;
        }

        public void modifyText(ModifyEvent e)
        {
            if (!updating)
            {
                parent.markDirty();
            }
        }
    }

    /** The text entry field for the server port. */
    private Text portText;

    /** The text entry field for the server host. */
    private Text hostText;

    protected boolean updating = false;

    /**
     * Creates the composite.
     * 
     * @param section The parent of the composite.
     * @param style The style.
     */
    public ServerGeneralInformationComposite(final SectionPart parent,
        int style)
    {
        super(parent.getSection(), style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        final Label hostLabel = toolkit.createLabel(this, "Host:", SWT.NONE);
        hostLabel.setLayoutData(new GridData());
        hostLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        hostText = toolkit.createText(this, null, SWT.NONE);
        hostText.addModifyListener(new DirtyListener(parent));
        hostText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Label portLabel = toolkit.createLabel(this, "Port:", SWT.NONE);
        portLabel.setLayoutData(new GridData());
        portLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        portText = toolkit.createText(this, null, SWT.NONE);
        portText.addModifyListener(new DirtyListener(parent));
        portText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        //
    }

    public String getHost()
    {
        return hostText.getText();
    }

    public void setHost(String host)
    {
        updating = true;
        hostText.setText(host);
        updating = false;
    }

    public BigInteger getPort()
    {
        String port = portText.getText();
        if (StringUtils.isEmpty(port))
        {
            return null;
        }
        else
        {
            return new BigInteger(port);
        }
    }

    public void setPort(BigInteger port)
    {
        updating = true;
        if (port != null)
        {
            portText.setText(port.toString());
        }
        else
        {
            portText.setText("");
        }
        updating = false;
    }

}
