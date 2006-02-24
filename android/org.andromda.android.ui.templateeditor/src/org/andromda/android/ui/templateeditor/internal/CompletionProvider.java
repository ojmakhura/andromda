package org.andromda.android.ui.templateeditor.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.cartridge.CartridgeRegistry;
import org.andromda.android.core.cartridge.CartridgeUtils;
import org.andromda.android.core.cartridge.ICartridgeDescriptor;
import org.andromda.android.core.cartridge.ICartridgeJavaVariableDescriptor;
import org.andromda.android.core.cartridge.ICartridgeMetafacadeVariableDescriptor;
import org.andromda.android.core.cartridge.ICartridgeVariableContainer;
import org.andromda.android.core.cartridge.ICartridgeVariableDescriptor;
import org.andromda.android.ui.templateeditor.TemplateEditorPlugin;
import org.andromda.android.ui.util.SWTResourceManager;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.swt.graphics.Image;

import de.byteaction.velocity.editor.completion.ICompletionProvider;
import de.byteaction.velocity.vaulttec.ui.editor.text.VelocityTextGuesser;

/**
 * This completion provider calculates code completions for AndroMDA.
 *
 * @author Peter Friese
 * @since 25.01.2006
 */
public class CompletionProvider
        implements ICompletionProvider
{

    /** Icon for public methods. */
    private static final String ICON_METHOD_PUBLIC = "icons/methpub_obj.gif";

    /** Icon for private fields. */
    private static final String ICON_FIELD_PRIVATE = "icons/field_private_obj.gif";

    private static final String ICON_FIELD_PUBLIC = "icons/field_public_obj.gif";

    /**
     * {@inheritDoc}
     */
    public Collection getExtraProposals(IFile file,
        IDocument doc,
        VelocityTextGuesser prefix,
        int offset)
    {
        Collection result = new ArrayList();

        IContainer cartridgeRoot = CartridgeUtils.findCartridgeRoot(file);
        IPath templatePath = file.getProjectRelativePath();

        ICartridgeDescriptor cartridgeDescriptor = CartridgeRegistry.getInstance()
                .getCartridgeDescriptor(cartridgeRoot);

        try
        {
            String text;
            int type = prefix.getType();
            switch (type)
            {
                case VelocityTextGuesser.TYPE_END:
                    text = "type_end: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_APOSTROPHE:
                    text = "type_apostrophe: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_INVALID:
                    text = "type_invalid: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_DIRECTIVE:
                    text = "type_directive: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TAG_DIRECTIVE:
                    text = "tag_directive: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TAG_CLOSE:
                    text = "tag_close: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_MEMBER_QUALIFIER:
                    result.addAll(getMemberProposals(cartridgeDescriptor, prefix, offset, templatePath));
                    break;

                case VelocityTextGuesser.TYPE_VARIABLE:
                    result.addAll(getPropertyProposals(cartridgeDescriptor, prefix.getText(), offset, templatePath));
                    break;

            }
        }
        catch (CartridgeParsingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param cartridgeDescriptor
     * @param prefix
     * @param offset
     * @param templatePath
     * @return
     * @throws CartridgeParsingException
     */
    private Collection getMemberProposals(ICartridgeDescriptor cartridgeDescriptor,
        VelocityTextGuesser prefix,
        int offset,
        IPath templatePath) throws CartridgeParsingException
    {
        ArrayList result = new ArrayList();

        ICartridgeVariableContainer variableContainer = cartridgeDescriptor.getVariableDescriptors();
        Collection variableDescriptors = variableContainer.getAll();
        for (Iterator iter = variableDescriptors.iterator(); iter.hasNext();)
        {
            ICartridgeVariableDescriptor descriptor = (ICartridgeVariableDescriptor)iter.next();
            // System.out.println("VariableDescriptor: " + descriptor.getName() + " [" + descriptor.hashCode() + "]");
            if (descriptor instanceof ICartridgeJavaVariableDescriptor)
            {
                if (descriptor instanceof ICartridgeMetafacadeVariableDescriptor)
                {
                    // TODO maybe better first collect all unique types and THEN go and collect the member proposals!
                    ICartridgeMetafacadeVariableDescriptor cartridgeMetafacadeVariableDescriptor = (ICartridgeMetafacadeVariableDescriptor)descriptor;
                    String variableTemplatePath = cartridgeMetafacadeVariableDescriptor.getTemplatePath();
                    if (templatePath.toString().indexOf(variableTemplatePath) > 0)
                    {
                        System.out.println(variableTemplatePath + " is part of " + templatePath);
                        createMemberProposals(prefix, offset, result, cartridgeMetafacadeVariableDescriptor);
                    }
                    else
                    {
                        // System.out.println(variableTemplatePath + " is NOT part of " + templatePath);
                    }
                }
                else if (descriptor instanceof ICartridgeJavaVariableDescriptor)
                {
                    ICartridgeJavaVariableDescriptor cartridgeJavaVariableDescriptor = (ICartridgeJavaVariableDescriptor)descriptor;
                    String name = cartridgeJavaVariableDescriptor.getName();
                    if (name.startsWith(prefix.getVariable()))
                    {
                        createMemberProposals(prefix, offset, result, cartridgeJavaVariableDescriptor);
                    }
                }

            }
        }
        return result;
    }

    /**
     * @param prefix
     * @param offset
     * @param result
     * @param cartridgeJavaVariableDescriptor
     * @throws CartridgeParsingException
     */
    private void createMemberProposals(VelocityTextGuesser prefix,
        int offset,
        ArrayList result,
        ICartridgeJavaVariableDescriptor cartridgeJavaVariableDescriptor) throws CartridgeParsingException
    {
        IType type = cartridgeJavaVariableDescriptor.getType();
        IMethod[] methods;
        try
        {
            methods = type.getMethods();
            for (int i = 0; i < methods.length; i++)
            {
                IMethod method = methods[i];
                String elementName = method.getElementName();
                String pNames = "";
                String[] parameterNames = method.getParameterNames();
                if (method.getNumberOfParameters() > 0)
                {
                    for (int j = 0; j < parameterNames.length; j++)
                    {
                        pNames += parameterNames[j];
                        if (j < parameterNames.length - 1)
                        {
                            pNames += ", ";
                        }

                    }
                }
                System.out.println("Method: " + elementName + " (" + pNames + "); [" + method.hashCode() + "]");
                if (elementName.startsWith(prefix.getText()))
                {
                    result
                            .add(createSimpleCompletionProposal(prefix.getText(), offset, elementName,
                                    ICON_METHOD_PUBLIC));
                }
            }
        }
        catch (JavaModelException e)
        {
            throw new CartridgeParsingException(e);
        }
    }

    /**
     * @param cartridgeDescriptor
     * @param templatePath
     * @param String
     * @return
     * @throws CartridgeParsingException
     */
    private Collection getPropertyProposals(ICartridgeDescriptor cartridgeDescriptor,
        String prefix,
        int offset,
        IPath templatePath) throws CartridgeParsingException
    {
        ArrayList result = new ArrayList();

        ICartridgeVariableContainer variableContainer = cartridgeDescriptor.getVariableDescriptors();
        Collection variableDescriptors = variableContainer.getAll();
        for (Iterator iter = variableDescriptors.iterator(); iter.hasNext();)
        {
            ICartridgeVariableDescriptor descriptor = (ICartridgeVariableDescriptor)iter.next();
            String proposalText = descriptor.getName();
            if (prefix.length() == 0 || proposalText.startsWith(prefix))
            {
                String icon;
                if (descriptor instanceof ICartridgeJavaVariableDescriptor)
                {
                    ICartridgeJavaVariableDescriptor javaVariableDescriptor = (ICartridgeJavaVariableDescriptor)descriptor;
                    icon = ICON_FIELD_PUBLIC;
                }
                else
                {
                    icon = ICON_FIELD_PRIVATE;
                }
                String displayString = "$" + proposalText;
                result.add(createSimpleCompletionProposal(prefix, offset, proposalText, displayString, icon));
            }
        }
        return result;
    }

    /**
     * @param prefix
     * @param offset
     * @param text
     * @return
     */
    private CompletionProposal createSimpleCompletionProposal(String prefix,
        int offset,
        String text)
    {
        return new CompletionProposal(text, offset, prefix.length(), text.length(), null, text, null, null);
    }

    /**
     * @param prefix
     * @param offset
     * @param text
     * @return
     */
    private CompletionProposal createSimpleCompletionProposal(String prefix,
        int offset,
        String text,
        String icon)
    {
        Image image = SWTResourceManager.getPluginImage(TemplateEditorPlugin.getDefault(), icon);
        return new CompletionProposal(text, offset, prefix.length(), text.length(), image, text, null, null);
    }

    /**
     * @param prefix
     * @param offset
     * @param text
     * @return
     */
    private CompletionProposal createSimpleCompletionProposal(String prefix,
        int offset,
        String text,
        String displayString,
        String icon)
    {
        Image image = SWTResourceManager.getPluginImage(TemplateEditorPlugin.getDefault(), icon);
        return new CompletionProposal(text, offset, prefix.length(), text.length(), image, displayString, null, null);
    }
}
