package org.andromda.android.ui.internal.project.wizard;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.android.core.project.cartridge.IProjectCartridgeDescriptor;
import org.andromda.android.core.project.cartridge.IPrompt;
import org.andromda.android.core.project.cartridge.IPromptGroup;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * A dynamic wizard page that display edit fields for prompts of a given prompt group in the project cartridge
 * descriptor file (andromdapp.xml).
 *
 * @author Peter Friese
 * @since 22.05.2006
 */
public class DynamicWizardPage
        extends WizardPage
{

    /** The container containg the widgets. We will later iterate all contained controls, so we need this reference. */
    private Composite container;

    /** The key for the prompt ID in the GUI. */
    private static final String PROMPT_ID_KEY = "ID";

    /** The prompt group the wizard is displaying. */
    private final IPromptGroup promptGroup;

    /**
     * This map contains all information gathered by the wizard.
     */
    private final Map projectProperties;

    /**
     * Create the wizard.
     *
     * @param promptGroup The prompt group that is displayed on this wiazrd page.
     * @param projectProperties This map contains the values the user sets up using the wizard.
     */
    public DynamicWizardPage(final IPromptGroup promptGroup,
        final Map projectProperties)
    {
        super(promptGroup.getName());
        this.promptGroup = promptGroup;
        this.projectProperties = projectProperties;
        setTitle(promptGroup.getName());
        setDescription(promptGroup.getDescription());
    }

    /**
     * Create contents of the wizard.
     *
     * @param parent The parent composite.
     */
    public void createControl(final Composite parent)
    {
        container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        container.setLayout(gridLayout);
        //
        setControl(container);

        Collection prompts = promptGroup.getPrompts();
        for (Iterator iter = prompts.iterator(); iter.hasNext();)
        {
            IPrompt prompt = (IPrompt)iter.next();

            // dynamically add all properties that we didn't already use on the BasicProjectInformtionWizardPage
            if (!ArrayUtils.contains(IProjectCartridgeDescriptor.PROJECT_BASIC_PROPERTIES, prompt.getId()))
            {
                Label label = new Label(container, SWT.NONE);
                String promptLabel = (prompt.getLabel() != null) ? prompt.getLabel() + ":" : "";
                label.setText(promptLabel);

                String tooltip = (prompt.getTooltip() != null) ? prompt.getTooltip() : "";
                label.setToolTipText(tooltip);

                label.setData(PROMPT_ID_KEY, prompt);

                // boolean -> Checkbox
                if (Boolean.class.toString().equals(prompt.getType()))
                {
                    final Button button = new Button(container, SWT.CHECK);
                    button.setData(PROMPT_ID_KEY, prompt);
                    button.addSelectionListener(getSelectionListener());
                }
                else
                {
                    // two ore more responses -> radiobutton
                    if (prompt.getOptions().size() >= 2)
                    {
                        final ComboViewer comboViewer = new ComboViewer(container, SWT.BORDER | SWT.READ_ONLY);
                        comboViewer.setContentProvider(new ArrayContentProvider());
                        comboViewer.setInput(prompt.getOptions());
                        Combo combo = comboViewer.getCombo();
                        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
                        combo.setData(PROMPT_ID_KEY, prompt);
                        combo.addSelectionListener(getSelectionListener());
                    }
                    // no responses -> text
                    else
                    {
                        final Text text = new Text(container, SWT.BORDER);
                        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
                        text.setToolTipText(tooltip);
                        text.setData(PROMPT_ID_KEY, prompt);
                        text.addSelectionListener(getSelectionListener());
                    }
                }
            }
        }
    }

    /**
     * @return a {@link SelectionAdapter} which updates the GUI upon selection.
     */
    private SelectionAdapter getSelectionListener()
    {
        return new SelectionAdapter()
        {
            /**
             * {@inheritDoc}
             */
            public void widgetSelected(final SelectionEvent e)
            {
                updateData();
            }

        };
    }

    /**
     * Updates the GUI (data and enablement states).
     */
    public void updateData()
    {
        Control[] children = container.getChildren();
        for (int i = 0; i < children.length; i++)
        {
            Control control = children[i];
            IPrompt prompt = (IPrompt)control.getData(PROMPT_ID_KEY);
            String propertyName = prompt.getId();

            // store property value
            if (propertyName != null)
            {
                String value = getFieldValue(control);
                if (value != null)
                {
                    // isSetAsTrue means that we need to store <code>true</code> to a property named after the original
                    // value of the option.
                    if (prompt.isSetAsTrue())
                    {
                        // define property name and value
                        propertyName = value;
                        value = Boolean.TRUE.toString();

                        // remove old value
                        List options = prompt.getOptions();
                        for (Iterator optionsIterator = options.iterator(); optionsIterator.hasNext();)
                        {
                            String option = (String)optionsIterator.next();
                            if (projectProperties.containsKey(option))
                            {
                                projectProperties.remove(option);
                            }
                        }
                    }
                    if (prompt.getTypeClass() == Boolean.class)
                    {
                        Boolean booleanValue = BooleanUtils.toBooleanObject(value);
                        projectProperties.put(propertyName, booleanValue);
                    }
                    else
                    {
                        projectProperties.put(propertyName, value);
                    }
                }

            }
        }

        // enable / disable input fields according to their preconditions:
        for (int i = 0; i < children.length; i++)
        {
            Control control = children[i];
            IPrompt prompt = (IPrompt)control.getData(PROMPT_ID_KEY);
            boolean promptEnabled = prompt.isPromptEnabled(projectProperties);
            control.setEnabled(promptEnabled);
        }

    }

    /**
     * Retrieves the value of the given control.
     *
     * @param control The control to read.
     * @return The value the user entered inito the control, if any.
     */
    private String getFieldValue(final Control control)
    {
        String result = null;
        if (control instanceof Text)
        {
            Text textField = (Text)control;
            result = textField.getText();
        }
        else if (control instanceof Combo)
        {
            Combo combo = (Combo)control;
            result = combo.getText();
        }
        else if (control instanceof Button)
        {
            Button button = (Button)control;
            boolean selected = button.getSelection();
            result = Boolean.toString(selected);
        }
        return result;
    }

}
