package org.andromda.translation.ocl;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.Expression;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.core.translation.Translator;
import org.andromda.core.translation.TranslatorException;
import org.andromda.core.translation.library.LibraryTranslation;
import org.andromda.core.translation.library.LibraryTranslationFinder;
import org.andromda.translation.ocl.analysis.DepthFirstAdapter;
import org.andromda.translation.ocl.lexer.Lexer;
import org.andromda.translation.ocl.lexer.LexerException;
import org.andromda.translation.ocl.node.AClassifierContextDeclaration;
import org.andromda.translation.ocl.node.ADefClassifierExpressionBody;
import org.andromda.translation.ocl.node.AInvClassifierExpressionBody;
import org.andromda.translation.ocl.node.AOperationContextDeclaration;
import org.andromda.translation.ocl.node.AOperationExpressionBody;
import org.andromda.translation.ocl.node.Start;
import org.andromda.translation.ocl.parser.OclParser;
import org.andromda.translation.ocl.parser.OclParserException;
import org.andromda.translation.ocl.parser.ParserException;
import org.andromda.translation.ocl.syntax.ConcreteSyntaxUtils;
import org.andromda.translation.ocl.syntax.OperationDeclaration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * The "base" translator which all Translator's should extend, provides some basic functionality, such as the retrieveal
 * of translation fragments from the translation template file, pre-processing, post-processing, etc.
 * <p/>
 * The primary methods (in addition to methods you'll extend to handle expression parsing) to take note of when
 * extending this class are: <ul> <li><a href="#handleTranslationFragment(org.andromda.core.translation.node.Node)">handleTranslationFragment(String,
 * Node node) </a></li> <li><a href="#getTranslationFragment(String)">getTranslationFragment(String)
 * </a></li> <li><a href="#getExpression()">getExpression() </a></li> <li><a href="#preProcess()">preProcess() </a></li>
 * <li><a href="#postProcess()">postProcess() </a></li> </ul> </p>
 *
 * @author Chad Brandon
 */
public abstract class BaseTranslator
        extends DepthFirstAdapter
        implements Translator
{

    /**
     * The logger instance that can be used by all decendant classes.
     */
    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * This is set by the "translate" method in order to provide any Translator classes extending this class access to
     * the element that is the context for the expression.
     */
    private Object contextElement = null;

    /**
     * Contains the Expression that was/will be populated during execution of the translation method
     */
    private Expression translatedExpression = null;

    /**
     * The processed Translation file.
     */
    private LibraryTranslation libraryTranslation = null;

    /**
     * Called by the translate method to set the element which provides the context for the traslated expression.
     *
     * @param contextElement
     */
    private void setContextElement(Object contextElement)
    {
        this.contextElement = contextElement;
    }

    /**
     * Returns the current value of the Expression. This stores the translation being translated.
     *
     * @return Expression stores the the translation.
     */
    public Expression getExpression()
    {
        final String methodName = "BaseTranslator.getExpression";
        if (this.translatedExpression == null)
        {
            throw new TranslatorException(methodName + " - translatedExpression can not be null");
        }
        return translatedExpression;
    }

    /**
     * Returns the context element for the expression being translated.
     *
     * @return the context element.
     */
    public Object getContextElement()
    {
        String methodName = "getContextElement";
        if (this.contextElement == null)
        {
            throw new TranslatorException(methodName + " - the contextElement can not be null");
        }
        return this.contextElement;
    }

    /**
     * Calls the handlerMethod defined on the &lt;fragment/&gt; element if <code>fragmentName</code> matches one the
     * fragments defined within the current translation file.
     * <p/>
     * <a name="#handlerMethod"/> A handlerMethod must have two arguments: <ol> <li>The first argument must be a
     * <code>String</code> which will be the body of the corresponding <code>kind</code> element for the
     * matching fragment. (i.e. if <code>'context LegalAgreement inv: allInstances -> isUnique(documentTitle')'</code>
     * is being translated, then the body of the element &lt;kind name="inv"/&gt; would be returned)</li> <li>The second
     * argument is of type <code>Object</code> and is the node that is currently being parsed at the time the
     * matching of the <code>fragmentName</code> occurred.</li> </ol>
     * <p/>
     * <p/>
     * For example this handlerMethod might be defined within your translation file to handle the 'allInstances'
     * expression:
     * <p/>
     * <pre>
     * <p/>
     *                 &lt;fragment name=&quot;(\s*${elementName}\s*\.)?\s*allInstances.*&quot;
     *                              handlerMethod=&quot;handleAllInstances&quot;&gt;
     *                     &lt;kind name=&quot;body&quot;&gt;
     *                         from $completeElementName as $lowerCaseElementName
     *                     &lt;/kind&gt;
     *                 &lt;/fragment&gt;
     * <p/>
     * </pre>
     * <p/>
     * </p>
     * <p/>
     * And the implementation of the <code>handleAllInstances</code> method would be:
     * <p/>
     * <pre>
     * public void handleAllInstances(String translation, Object node)
     * {
     * //some handling code
     * }
     * </pre>
     * <p/>
     * </p>
     *
     * @param node the node being parsed, the toString value of this node is what is matched against the translation
     *             fragment. We also need to pass the node to our <a href="#handlerMethod">handlerMethod </a> so that it
     *             can be used it for additional processing (if we need it).
     * @see getTranslationFragment(String)
     */
    protected void handleTranslationFragment(Object node)
    {
        ExceptionUtils.checkNull("node", node);
        if (this.libraryTranslation != null)
        {
            this.libraryTranslation.handleTranslationFragment(TranslationUtils.trimToEmpty(node), this.getExpression()
                    .getKind(), node);
        }
    }

    /**
     * Finds the "fragment" with the specified <code>fragmentName</code> from the library translation file.
     * <p/>
     * <strong>IMPORTANT: </strong> as a best practice, it is recommended that you use <a
     * href="#handleTranslationFragment(org.andromda.core.translation.parser.node.Node)">handleTranslationFragment(Node
     * node) </a> if at all possible (instead of this method), it will help your code be cleaner and the methods smaller
     * and more maintainable. </p>
     * <p/>
     * Will retrieve the contents of the fragment <code>kind</code> that corresponds to the kind of expression currently
     * being translated. (i.e. if <code>'context LegalAgreement inv: allInstances -> isUnique(documentTitle')</code>' is
     * being translated, then the body of the element &lt;kind name="inv"&gt; would be returned). </p>
     * <p/>
     * <strong>NOTE: </strong>You would use this method <strong>instead </strong> of <a
     * href="#handleTranslationFragment(org.andromda.core.translation.parser.node.Node)">handleTranslationFragment(Node
     * node) </a> if you just want to retrieve the value of the fragment and don't want to have a <a
     * href="#handlerMethod">handlerMethod </a> which actually handles the processing of the output. For example you may
     * want to add a fragment called 'constraintTail' which would always be added to your translation at the end of the
     * constraint, the 'tail'. There isn't any part of the expression that matches this, but you still want to store it
     * in a translation template since it could be different between translations within your Translation-Library. </p>
     *
     * @param fragmentName the name of the fragment to retrieve from the translation
     * @return String the output String from the translated fragment.
     * @see #handleTranslationFragment(Node node)
     */
    protected String getTranslationFragment(String fragmentName)
    {
        ExceptionUtils.checkEmpty("fragmentName", fragmentName);
        String fragmentString = null;
        if (this.libraryTranslation != null)
        {
            fragmentString = this.libraryTranslation.getTranslationFragment(fragmentName,
                    this.getExpression().getKind());
        }
        return fragmentString;
    }

    /**
     * Performs any initialization. Subclasses should override this method if they want to provide any initilization
     * before translation begins.
     */
    public void preProcess()
    {
        this.contextElement = null;
    }

    /**
     * <p/>
     * <strong>NOTE: </strong> null is allowed for contextElement (even though it isn't within
     * ExpressionTranslator.translate() since the TraceTranslator doesn't need a <code>contextElement</code> and we
     * don't want to slow down the trace by having to read and load a model each time. </p>
     *
     * @see org.andromda.core.translation.ExpressionTranslator#translate( String, Object,
            *      String)
     */
    public Expression translate(String translationName, String expression, Object contextElement)
    {
        ExceptionUtils.checkEmpty("translationName", translationName);
        ExceptionUtils.checkEmpty("expression", expression);
        try
        {
            // pre processing
            this.preProcess();
            // set the context element so translators extending this have the
            // context element available
            this.setContextElement(contextElement);
            Map templateObjects = new HashMap();
            this.libraryTranslation = LibraryTranslationFinder.findLibraryTranslation(translationName);
            final String variable = this.libraryTranslation.getVariable();
            if (variable != null)
            {
                templateObjects.put(variable, contextElement);
            }
            if (this.libraryTranslation != null)
            {
                libraryTranslation.getLibrary().initialize();
                libraryTranslation.processTranslation(templateObjects);
                this.process(expression);
                libraryTranslation.getLibrary().shutdown();
            }
            // post processing
            this.postProcess();
        }
        catch (Exception ex)
        {
            String errMsg = "Error translating with translation '" + translationName + "'," + " contextElement '" +
                    contextElement +
                    "' and expression --> '" +
                    expression +
                    "'" +
                    "\nMESSAGE --> '" +
                    ex.getMessage() +
                    "'";
            logger.error(errMsg);
            throw new TranslatorException(errMsg, ex);
        }
        return translatedExpression;
    }

    /**
     * Parses the expression and applies this Translator to it.
     *
     * @param expression the expression to process.
     * @throws IOException if an IO error occurs during processing.
     */
    protected void process(final String expression) throws IOException
    {
        ExceptionUtils.checkEmpty("expression", expression);
        try
        {
            Lexer lexer = new Lexer(new PushbackReader(new StringReader(expression)));
            OclParser parser = new OclParser(lexer);
            Start startNode = parser.parse();
            this.translatedExpression = new Expression(expression);
            startNode.apply(this);
        }
        catch (ParserException ex)
        {
            throw new OclParserException(ex.getMessage());
        }
        catch (LexerException ex)
        {
            throw new OclParserException(ex.getMessage());
        }
    }

    /**
     * Performs any post processing. Subclasses should override to perform any final cleanup/processing.
     */
    public void postProcess()
    {
        // currently does nothing --> sub classes should override to perform
        // final processing
    }

    /* The Following Are Overriden Parser Generated Methods */

    /**
     * Sets the kind and name of the expression for <code>inv</code> expressions. If subclasses override this method,
     * they <strong>MUST </strong> call this method before their own implementation.
     *
     * @param expressionBody
     */
    public void inAInvClassifierExpressionBody(AInvClassifierExpressionBody expressionBody)
    {
        ExceptionUtils.checkNull("expressionBody", expressionBody);
        if (this.translatedExpression != null)
        {
            this.translatedExpression.setName(TranslationUtils.trimToEmpty(expressionBody.getName()));
            this.translatedExpression.setKind(ExpressionKinds.INV);
        }
    }

    /**
     * Sets the kind and name of the expression for <code>def</code> expressions. If subclasses override this method,
     * they <strong>MUST </strong> call this method before their own implementation.
     *
     * @param expressionBody
     */
    public void inADefClassifierExpressionBody(ADefClassifierExpressionBody expressionBody)
    {
        ExceptionUtils.checkNull("expressionBody", expressionBody);
        if (this.translatedExpression != null)
        {
            this.translatedExpression.setName(TranslationUtils.trimToEmpty(expressionBody.getName()));
            this.translatedExpression.setKind(ExpressionKinds.DEF);
        }
    }

    /**
     * Sets the kind and name of the expression for operation contexts. If subclasses override this method, they
     * <strong>MUST </strong> call this method before their own implementation.
     *
     * @param operationExpressionBody
     */
    public void inAOperationExpressionBody(AOperationExpressionBody operationExpressionBody)
    {
        ExceptionUtils.checkNull("operationExpressionBody", operationExpressionBody);

        if (this.translatedExpression != null)
        {
            // sets the name of the expression
            this.translatedExpression.setName(TranslationUtils.getPropertyAsString(operationExpressionBody, "name"));

            // sets the kind of the expression (body, post, or pre)
            this.translatedExpression.setKind(TranslationUtils.getPropertyAsString(operationExpressionBody,
                    "operationStereotype"));
        }
    }

    /**
     * Sets the element type which represents the context of the expression for expressions having operations as their
     * context. If subclasses override this method, they <strong>MUST </strong> call this method before their own
     * implementation.
     *
     * @param declaration the AOperationContextDeclaration instance from which we retrieve the element type.
     */
    public void inAOperationContextDeclaration(AOperationContextDeclaration declaration)
    {
        final String methodName = "BaseTranslator.inAOperationContextDeclaration";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + methodName + " with declaration --> " + declaration);
        }
        if (this.translatedExpression != null)
        {
            this.translatedExpression.setContextElement(ConcreteSyntaxUtils.getType(declaration.getName(),
                    declaration.getPathNameTail()));
        }
        this.operation = ConcreteSyntaxUtils.getOperationDeclaration(declaration.getOperation());
    }

    /**
     * Stores the operation declartion of constraint (if the context is an operation).
     */
    private OperationDeclaration operation;

    /**
     * Gets the operation declaration of the constraint (if the context is an operation), otherwise returns null.
     *
     * @return the operation declaration or null.
     */
    protected OperationDeclaration getOperation()
    {
        return this.operation;
    }

    /**
     * Indicates if the given <code>argument</code> is an operation argument (if the context declaration is an
     * operation)
     *
     * @param argument the argument to check.
     * @return true/false
     */
    protected boolean isOperationArgument(final String argument)
    {
        return this.operation != null && ConcreteSyntaxUtils.getArgumentNames(operation.getArguments()).contains(
                argument);
    }

    /**
     * Sets the element type which represents the context of the expression for expressions having classifiers as their
     * context. If subclasses override this method, they <strong>MUST </strong> call this method before their own
     * implementation.
     *
     * @param declaration the AClassifierContextDeclaration instance from which we retrieve the element type.
     */
    public void inAClassifierContextDeclaration(AClassifierContextDeclaration declaration)
    {
        final String methodName = "BaseTranslator.inAClassifierContextDeclaration";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + methodName + " with declaration --> " + declaration);
        }
        if (this.translatedExpression != null)
        {
            this.translatedExpression.setContextElement(ConcreteSyntaxUtils.getType(declaration.getName(),
                    declaration.getPathNameTail()));
        }
    }
}