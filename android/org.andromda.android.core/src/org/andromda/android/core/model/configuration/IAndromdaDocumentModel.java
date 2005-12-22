package org.andromda.android.core.model.configuration;

import org.andromda.android.core.internal.model.configuration.AndromdaDocumentModel;
import org.andromda.android.core.model.IModel;
import org.andromda.core.configuration.AndromdaDocument;

/**
 * Model wrapper interface for the AndroMDA configuration model.
 * 
 * @author Peter Friese
 * @since 15.12.2005
 */
public interface IAndromdaDocumentModel
        extends IModel
{

    /**
     * @return The AndroMDA configuration document.
     */
    AndromdaDocument getAndromdaDocument();

    /**
     * Factory for {@link IAndromdaDocumentModel} instances.
     */
    public static final class Factory
    {
        /**
         * Creates a new {@link IAndromdaDocumentModel} instance.
         * 
         * @param andromdaDocument The document wrapped in the model.
         * @return A new model instance.
         */
        public static IAndromdaDocumentModel newInstance(AndromdaDocument andromdaDocument)
        {
            return new AndromdaDocumentModel(andromdaDocument);
        }
    }

}
