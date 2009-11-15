package org.andromda.core.translation;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.namespace.NamespaceComponents;
import org.andromda.core.translation.library.LibraryTranslation;
import org.andromda.core.translation.library.LibraryTranslationFinder;
import org.apache.log4j.Logger;


/**
 * The <strong>expression </strong> translator class that all translations are performed through. This is the entry
 * point to expression (OCL, etc) translation.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class ExpressionTranslator
{
    private static final Logger logger = Logger.getLogger(ExpressionTranslator.class);
    private static ExpressionTranslator translator = new ExpressionTranslator();

    /**
     * Gets the shared ExpressionTranslator instance.
     *
     * @return ExpressionTranslator.
     */
    public static ExpressionTranslator instance()
    {
        return translator;
    }

    /**
     * Initializes the ExpressionTranslator. This <strong>MUST </strong> be called to find and loal all available
     * translation-libraries.
     */
    public void initialize()
    {
        // configure the logger
        AndroMDALogger.initialize();

        // discover plugins
        NamespaceComponents.instance().discover();
    }

    /**
     * Performs translation of the <code>expression</code> by looking up the
     * <code>translationName</code> from the available Translation-Libraries
     * found on the classpath.
     *
     * @param translationName the name of the translation to use for translating
     *        (i.e. a translationName like 'query.EJB-QL' would mean use the
     *        <code>EJB-QL</code> translation from the <code>query</code>
     *        library.
     * @param expression the actual expression to translate.
     * @param contextElement the element which provides the context of this
     *        expression. This is passed from the model. This can be null.
     * @return Expression the resulting expression instance which contains the
     *         translated expression as well as additional information about the
     *         expression.
     */
    public Expression translate(
        final String translationName,
        final String expression,
        final Object contextElement)
    {
        ExceptionUtils.checkEmpty("translationName", translationName);
        ExceptionUtils.checkEmpty("expression", expression);

        Expression translatedExpression = null;
        try
        {
            final LibraryTranslation libraryTranslation =
                LibraryTranslationFinder.findLibraryTranslation(translationName);

            if (libraryTranslation != null)
            {
                final Translator translator = libraryTranslation.getTranslator();
                translatedExpression = translator.translate(translationName, expression, contextElement);
            }
            else
            {
                logger.error("ERROR! No translation found with name --> '" + translationName + "'");
            }
        }
        catch (final Throwable throwable)
        {
            throw new TranslatorException(throwable);
        }
        return translatedExpression;
    }
}