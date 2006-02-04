package org.andromda.android.ui.templateeditor.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.android.core.cartridge.CartridgeParsingException;
import org.andromda.android.core.cartridge.CartridgeRegistry;
import org.andromda.android.core.cartridge.CartridgeUtils;
import org.andromda.android.core.cartridge.ICartridgeDescriptor;
import org.andromda.android.core.cartridge.ICartridgeJavaVariableDescriptor;
import org.andromda.android.core.cartridge.ICartridgeVariableDescriptor;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;

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

                case VelocityTextGuesser.TYPE_MEMBER_QUALIFIER:
                    result.addAll(getMemberProposals(cartridgeDescriptor, prefix, offset));
                    break;

                case VelocityTextGuesser.TAG_DIRECTIVE:
                    text = "tag_directive: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TAG_CLOSE:
                    text = "tag_close: " + prefix.getText();
                    result.add(createSimpleCompletionProposal(prefix.getText(), offset, text));
                    break;

                case VelocityTextGuesser.TYPE_VARIABLE:
                    result.addAll(getPropertyProposals(cartridgeDescriptor, prefix.getText(), offset));
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
     * @return
     * @throws CartridgeParsingException
     */
    private Collection getMemberProposals(ICartridgeDescriptor cartridgeDescriptor,
        VelocityTextGuesser prefix,
        int offset) throws CartridgeParsingException
    {
        ArrayList result = new ArrayList();

        Collection variableDescriptors = cartridgeDescriptor.getVariableDescriptors();
        for (Iterator iter = variableDescriptors.iterator(); iter.hasNext();)
        {
            ICartridgeVariableDescriptor descriptor = (ICartridgeVariableDescriptor)iter.next();
            if (descriptor instanceof ICartridgeJavaVariableDescriptor)
            {
                ICartridgeJavaVariableDescriptor cartridgeJavaVariableDescriptor = (ICartridgeJavaVariableDescriptor)descriptor;
                String name = cartridgeJavaVariableDescriptor.getName();
                if (name.startsWith(prefix.getVariable()))
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
                            if (elementName.startsWith(prefix.getText()))
                            {
                                result.add(createSimpleCompletionProposal(prefix.getText(), offset, elementName));
                            }
                        }
                    }
                    catch (JavaModelException e)
                    {
                        throw new CartridgeParsingException(e);
                    }
                }

            }
        }
        return result;
    }

    /**
     * @param cartridgeDescriptor
     * @param String
     * @return
     * @throws CartridgeParsingException
     */
    private Collection getPropertyProposals(ICartridgeDescriptor cartridgeDescriptor,
        String prefix,
        int offset) throws CartridgeParsingException
    {
        ArrayList result = new ArrayList();

        Collection variableDescriptors = cartridgeDescriptor.getVariableDescriptors();
        for (Iterator iter = variableDescriptors.iterator(); iter.hasNext();)
        {
            ICartridgeVariableDescriptor descriptor = (ICartridgeVariableDescriptor)iter.next();
            String proposalText = descriptor.getName();
            if (proposalText.startsWith(prefix))
            {
                result.add(createSimpleCompletionProposal(prefix, offset, descriptor.getName()));
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

}
