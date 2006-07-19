package org.andromda.android.core.internal.project.cartridge;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.project.cartridge.IProjectCartridgeDescriptor;
import org.andromda.android.core.project.cartridge.IPrompt;
import org.andromda.android.core.util.XmlUtils;
import org.andromda.core.andromdapp.AndromdappDocument;
import org.andromda.core.andromdapp.AndromdappDocument.Andromdapp;
import org.andromda.core.andromdapp.ConditionDocument.Condition;
import org.andromda.core.andromdapp.ConditionsType.Enum;
import org.andromda.core.andromdapp.PreconditionsDocument.Preconditions;
import org.andromda.core.andromdapp.PromptsDocument.Prompts;
import org.andromda.core.andromdapp.ResponsesDocument.Responses;
import org.apache.xmlbeans.XmlOptions;

/**
 * Descriptor for a project cartridge.
 *
 * @author Peter Friese
 * @since 21.05.2006
 */
public class ProjectCartridgeDescriptor
        implements IProjectCartridgeDescriptor
{

    /** The default XML namespace for <code>namespace.xml</code> files in AndroMDA cartridges. */
    private static final String XML_NAMESPACE_ANDROMDA_ANDROMDAPP = "http://andromda.org/core/andromdapp";

    private final String projectCartridgeLocation;

    private boolean jar = true;

    private XmlOptions andromdappXmlOptions;

    /**
     * Creates a new ProjectCartridgeDescriptor.
     *
     * @param location The location of the project cartridge.
     */
    public ProjectCartridgeDescriptor(String location)
    {
        this.projectCartridgeLocation = location;
        setupDefaultNamespaces();
    }

    /**
     * Constructs the location of the requested file depending on whether it is located inside a cartridge jar or rather
     * in the AndroMDA development workspace.
     *
     * @param fileName The filename we're interested in.
     * @return A string with the correct path to the requested file.
     */
    private String getDocumentLocation(final String fileName)
    {
        String documentLocation;
        if (jar)
        {
            documentLocation = "jar:" + projectCartridgeLocation + "!/META-INF/andromdapp/" + fileName;
        }
        else
        {
            documentLocation = "file:/" + projectCartridgeLocation + "/src/META-INF/andromdapp/" + fileName;
        }
        return documentLocation;
    }

    /**
     * Setup an XmlOptions instance so the parser will assume the default namespace for the config documents even if it
     * has no namespace set.
     */
    private void setupDefaultNamespaces()
    {
        andromdappXmlOptions = new XmlOptions();
        Map namespaceMapping = new HashMap();
        namespaceMapping.put("", XML_NAMESPACE_ANDROMDA_ANDROMDAPP);
        andromdappXmlOptions.setLoadSubstituteNamespaces(namespaceMapping);
    }

    /**
     * @return
     * @throws CartridgeParsingException
     */
    private Andromdapp getProjectCartridge() throws CartridgeParsingException
    {
        String fileName = "andromdapp.xml";
        String documentLocation = getDocumentLocation(fileName);
        try
        {
            URL projectCartridgeURL = new URL(documentLocation);
            AndromdappDocument document = AndromdappDocument.Factory.parse(projectCartridgeURL, andromdappXmlOptions);
            return document.getAndromdapp();
        }
        catch (Exception e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Collection getPromptGroups() throws CartridgeParsingException
    {
        Collection result = new ArrayList();

        Andromdapp projectCartridge = getProjectCartridge();
        Prompts prompts = projectCartridge.getPrompts();
        org.andromda.core.andromdapp.PromptGroupDocument.PromptGroup[] promptGroupArray = prompts.getPromptGroupArray();
        for (int i = 0; i < promptGroupArray.length; i++)
        {
            org.andromda.core.andromdapp.PromptGroupDocument.PromptGroup promptGroup = promptGroupArray[i];

            // create new prompt group
            org.andromda.android.core.internal.project.cartridge.PromptGroup newGroup = new org.andromda.android.core.internal.project.cartridge.PromptGroup();
            newGroup.setName(promptGroup.getName());
            newGroup.setDescription(XmlUtils.getTextValueFromElement(promptGroup.getDocumentation()));
            result.add(newGroup);

            org.andromda.core.andromdapp.PromptDocument.Prompt[] promptArray = promptGroup.getPromptArray();
            for (int j = 0; j < promptArray.length; j++)
            {
                org.andromda.core.andromdapp.PromptDocument.Prompt prompt = promptArray[j];

                // create new prompt
                IPrompt newPrompt = new org.andromda.android.core.internal.project.cartridge.Prompt();
                newPrompt.setId(prompt.getId());
                newPrompt.setLabel(prompt.getShortText());
                newPrompt.setTooltip(prompt.getText());

                // get valid options and their type
                Responses responses = prompt.getResponses();
                if (responses != null)
                {
                    newPrompt.setType(responses.getType());
                    String[] responseArray = responses.getResponseArray();
                    for (int k = 0; k < responseArray.length; k++)
                    {
                        String response = responseArray[k];
                        newPrompt.addOption(response);
                    }
                }

                Preconditions preconditions = prompt.getPreconditions();
                if (preconditions != null)
                {
                    String conditionsType = preconditions.getType().toString();
                    newPrompt.setConditionsType(conditionsType);
                    Condition[] conditionArray = preconditions.getConditionArray();
                    for (int k = 0; k < conditionArray.length; k++)
                    {
                        Condition condition = conditionArray[k];
                        String conditionId = condition.getId();
                        String conditionEqual = condition.getEqual();
                        String conditionNotEqual = condition.getNotEqual();

                        Precondition precondition = new Precondition(conditionId, conditionEqual, conditionNotEqual);
                        newPrompt.addPrecondition(precondition);
                    }
                }

                // add it to the prompt group
                newGroup.addPrompt(newPrompt);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String getType() throws CartridgeParsingException
    {
        Andromdapp projectCartridge = getProjectCartridge();
        String type = projectCartridge.getType();
        return type;
    }
}
