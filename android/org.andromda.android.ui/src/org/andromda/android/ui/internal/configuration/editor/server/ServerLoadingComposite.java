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
 * This composite contains control that let the user configure the model loading behaviour of the server.
 * 
 * @author Peter Friese
 * @since 07.12.2005
 */
public class ServerLoadingComposite
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

    /** The text entry field for the maximum number of load failures. */
    private Text maximumFailedLoadAttemptsText;

    /** The text entry field for the load interval. */
    private Text loadIntervalText;

    private boolean updating;

    /**
     * Creates the composite.
     * 
     * @param parent The parent container.
     * @param style The style.
     */
    public ServerLoadingComposite(SectionPart parent,
        int style)
    {
        super(parent.getSection(), style);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);

        FormToolkit toolkit = new FormToolkit(Display.getCurrent());
        toolkit.adapt(this);
        toolkit.paintBordersFor(this);

        final Label maximumFailedLoadAttemptsLabel = toolkit.createLabel(this, "Maximum failed load attempts:",
                SWT.NONE);
        maximumFailedLoadAttemptsLabel.setLayoutData(new GridData());
        maximumFailedLoadAttemptsLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        loadIntervalText = toolkit.createText(this, null, SWT.NONE);
        loadIntervalText.addModifyListener(new DirtyListener(parent));
        final GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        loadIntervalText.setLayoutData(gridData);
        
        final Label loadIntervalLabel = toolkit.createLabel(this, "Load interval:", SWT.NONE);
        loadIntervalLabel.setLayoutData(new GridData());
        loadIntervalLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

        maximumFailedLoadAttemptsText = toolkit.createText(this, null, SWT.NONE);
        maximumFailedLoadAttemptsText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
        maximumFailedLoadAttemptsText.addModifyListener(new DirtyListener(parent));

        //
    }

    /**
     * @param loadInterval
     */
    public void setLoadInterval(BigInteger loadInterval)
    {
        updating = true;
        if (loadInterval != null)
        {
            loadIntervalText.setText(loadInterval.toString());
        }
        else
        {
            loadIntervalText.setText("");
        }
        updating = false;
    }
    
    /**
     * @return
     */
    public BigInteger  getLoadInterval()
    {
        String loadInterval = loadIntervalText.getText();
        if (StringUtils.isEmpty(loadInterval))
        {
            return null;
        }
        else
        {
            return new BigInteger(loadInterval);
        }
    }

    /**
     * @param maximumFailedLoadAttempts
     */
    public void setMaximumFailedLoadAttempts(BigInteger maximumFailedLoadAttempts)
    {
        updating = true;
        if (maximumFailedLoadAttempts != null)
        {
            maximumFailedLoadAttemptsText.setText(maximumFailedLoadAttempts.toString());
        }
        else
        {
            maximumFailedLoadAttemptsText.setText("");
        }
        updating = false;
    }
    
    /**
     * @return
     */
    public BigInteger  getMaximumFailedLoadAttempts()
    {
        String maximumFailedLoadAttempts = maximumFailedLoadAttemptsText.getText();
        if (StringUtils.isEmpty(maximumFailedLoadAttempts))
        {
            return null;
        }
        else
        {
            return new BigInteger(maximumFailedLoadAttempts);
        }
    }    

}
