package org.andromda.android.ui.internal.project.wizard;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.andromda.android.core.project.cartridge.IProjectCartridgeDescriptor;
import org.andromda.android.core.project.cartridge.IPrompt;
import org.andromda.android.core.project.cartridge.IPromptGroup;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Peter Friese
 * @since 22.05.2006
 */
public class DynamicWizardPage
        extends WizardPage
{

    private Combo combo;
    private Composite container;

    /** <code>PROMPT_ID_KEY</code> */
    private static final String PROMPT_ID_KEY = "ID";

    /** The prompt group the wizard is displaying. */
    private final IPromptGroup promptGroup;

    /**
     * This map contains all information gathered by the wizard.
     */
    private final Map projectProperties;

    /**
     * Create the wizard
     */
    public DynamicWizardPage(final IPromptGroup promptGroup,
        Map projectProperties)
    {
        super(promptGroup.getName());
        this.promptGroup = promptGroup;
        this.projectProperties = projectProperties;
        setTitle(promptGroup.getName());
        setDescription(promptGroup.getDescription());
    }

    /**
     * Create contents of the wizard
     * 
     * @param parent
     */
    public void createControl(Composite parent)
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
                label.setText(prompt.getLabel());
                label.setToolTipText(prompt.getTooltip());
                label.setData(PROMPT_ID_KEY, prompt.getId());

                // boolean -> Checkbox
                if (Boolean.class.toString().equals(prompt.getType()))
                {
                    final Button button = new Button(container, SWT.CHECK);
                    button.setData(PROMPT_ID_KEY, prompt.getId());
                }
                else
                {
                    // two ore more responses -> radiobutton
                    if (prompt.getOptions().size() >= 2)
                    {
                        final ComboViewer comboViewer = new ComboViewer(container, SWT.BORDER);
                        comboViewer.setContentProvider(new ArrayContentProvider());
                        comboViewer.setInput(prompt.getOptions());
                        combo = comboViewer.getCombo();
                        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
                        combo.setData(PROMPT_ID_KEY, prompt.getId());
                    }
                    // no respones -> text
                    else
                    {
                        final Text text = new Text(container, SWT.BORDER);
                        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
                        text.setToolTipText(prompt.getTooltip());
                        text.setData(PROMPT_ID_KEY, prompt.getId());
                    }
                }
            }
        }
    }

    public void updateData()
    {
        Control[] children = container.getChildren();
        for (int i = 0; i < children.length; i++)
        {
            Control control = children[i];
            String propertyName = (String)control.getData(PROMPT_ID_KEY);
            String value = null;
            if (propertyName != null) {
                if (control instanceof Text)
                {
                    Text textField = (Text)control;
                    value = textField.getText();
                }
                else if (control instanceof Combo) {
                    Combo combo = (Combo)control;
                    value = combo.getText();
                }
                else if(control instanceof Button) {
                    Button button = (Button)control;
                    boolean selected = button.getSelection();
                    value = Boolean.toString(selected);
                }
                projectProperties.put(propertyName, value);
            }
        }

    }

}
