package org.andromda.android.core.internal.model.configuration;

import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.internal.model.AbstractEditorModel;
import org.andromda.android.core.model.configuration.IAndromdaDocumentEditorModel;
import org.andromda.core.configuration.AndromdaDocument;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.eclipse.jface.text.IDocument;

/**
 * A model wrapper for the AndroMDA configuration document.
 * 
 * @author Peter Friese
 * @since 15.12.2005
 */
public class AndromdaDocumentEditorModel
        extends AbstractEditorModel
        implements IAndromdaDocumentEditorModel
{

    /** The AndroMDA configuration document wrapper by this model wrapper. */
    private AndromdaDocument andromdaDocument;

    /** The number of spaces rendered in the XML file for one indentation. */
    private static final int NUMBER_OF_SPACES = 4;

    /**
     * Creates a new model wrapper for the given AndroMDA configuration document.
     * 
     * @param document the document to wrap.
     */
    public AndromdaDocumentEditorModel(final IDocument document)
    {
        super(document);
    }

    /**
     * Parses the document and updates the model.
     * 
     * @param document the document to parse.
     */
    protected void parseDocument(final IDocument document)
    {
        try
        {
            XmlOptions options = setupXmlOptions();
            andromdaDocument = AndromdaDocument.Factory.parse(document.get(), options);
        }
        catch (XmlException e)
        {
            AndroidCore.log(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void updateDocument()
    {
        IDocument document = getDocument();
        if (document != null)
        {
            if (andromdaDocument != null)
            {
                document.set(andromdaDocument.toString());
            }
        }
    }

    /**
     * Setup an XmlOptions instance so the parser will assume the default namespace for the config document even if is
     * has no namespace set.
     * 
     * @return an XmlOptions instance suitable for parsing AndroMDA configuration documents.
     */
    private XmlOptions setupXmlOptions()
    {
        XmlOptions options = new XmlOptions();
        Map namespaceMapping = new HashMap();
        namespaceMapping.put("", "http://andromda.org/core/configuration");
        options.setLoadSubstituteNamespaces(namespaceMapping);

        options.setUseDefaultNamespace();
        options.setSavePrettyPrint().setSavePrettyPrintIndent(NUMBER_OF_SPACES);
        return options;
    }

    /**
     * {@inheritDoc}
     */
    public AndromdaDocument getAndromdaDocument()
    {
        return andromdaDocument;
    }

}
