package org.andromda.core.translation;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.analysis.DepthFirstAdapter;
import org.andromda.core.translation.lexer.Lexer;
import org.andromda.core.translation.lexer.LexerException;
import org.andromda.core.translation.library.LibraryTranslation;
import org.andromda.core.translation.library.LibraryTranslationFinder;
import org.andromda.core.translation.node.AClassifierContextDeclaration;
import org.andromda.core.translation.node.ADefClassifierExpressionBody;
import org.andromda.core.translation.node.AInvClassifierExpressionBody;
import org.andromda.core.translation.node.AOperationContextDeclaration;
import org.andromda.core.translation.node.AOperationExpressionBody;
import org.andromda.core.translation.node.Node;
import org.andromda.core.translation.node.Start;
import org.andromda.core.translation.parser.OclParser;
import org.andromda.core.translation.parser.OclParserException;
import org.andromda.core.translation.parser.ParserException;
import org.andromda.core.translation.syntax.impl.ConcreteSyntaxUtils;
import org.apache.log4j.Logger;

/**
 * The "base" translator which all Translator's should extend,
 * provides some basic functionality, such as the retrieveal of translation
 * fragments from the translation template file, pre-processing, post-processing, etc.
 * 
 * <p>
 *  The primary methods (in addition to methods you'll extend to handle expression parsing) 
 *  to take note of when extending this class are:
 *      <ul>
 *          <li>
 *              <a href="#handleTranslationFragment(java.lang.String, org.andromda.core.parser.parser.node.Node)">handleTranslationFragment(java.lang.String, Node node)</a> 
 *          </li>
 *          <li>
 *              <a href="#getTranslationFragment(java.lang.String)">getTranslationFragment(java.lang.String)</a>
 *          </li>
 *          <li>
 *              <a href="#getExpression()">getExpression()</a> 
 *          </li>
 *          <li>
 *              <a href="#preProcess()">preProcess()</a> 
 *          </li>
 *          <li>
 *              <a href="#postProcess()">postProcess()</a> 
 *          </li>
 *      </ul>
 * </p>
 * @author Chad Brandon
 */
public abstract class BaseTranslator extends DepthFirstAdapter implements Translator {
	
    /**
     * The logger instance that can be used
     * by all decendant classes.
     */
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * This is set by the "translate" method in order
	 * to provide any Translator classes extending this class
	 * access to the element that is the context for the expression.
	 */
	private Object contextElement = null;
	
	/**
	 * Contains the Expression that was/will be populated
	 * during execution of the translation method
	 */
	private Expression translatedExpression = null;
	
	/**
	 * The processed Translation file.
	 */
	private LibraryTranslation libraryTranslation = null;
	
	/**
	 * Called by the translate method to set the element which
	 * provides the context for the traslated expression.
	 * 
	 * @param contextElement
	 */
	private void setContextElement(Object contextElement) {
		this.contextElement = contextElement;
	}
	
	/**
	 * Returns the current value of the Expression.
	 * Subclasses will update the translatedExpression of this object
	 * as translation occurs.
	 * @return Expression
	 */
	protected Expression getExpression() {
		final String methodName = "BaseTranslator.getExpression";
		if (this.translatedExpression == null) {
			throw new TranslatorException(methodName +
				" - translatedExpression can not be null");
		}
		return translatedExpression;
	}
   
	/**
	 * Returns the context element for the expression
	 * being translated.
	 * 
	 * @return the context element.
	 */
	protected Object getContextElement() {
		String methodName = "getContextElement";
		if (this.contextElement == null) {
			throw new TranslatorException(methodName
				+ " - the contextElement can not be null");
		}
		return this.contextElement;
	}
	
	/**
	 * Calls the handlerMethod defined on the &lt;fragment/&gt; element
     * if <code>fragmentName</code> matches one the fragments defined
     * within the current translation file.  
     * 
     * <p>
     *      <a name="#handlerMethod"/>
     *      A handlerMethod must have two arguments:
     *      <ol>
     *          <li>
     *              The first argument must be a <code>java.lang.String</code> which will
     *              be the body of the corresponding <code>kind</code> element 
     *              for the matching fragment. (i.e. 
     *              if <code>'context LegalAgreement inv: allInstances -> 
     *              isUnique(documentTitle')'</code> is being translated, then the body of 
     *              the element &lt;kind name="inv"/&gt; would be returned)
     *          </li>
     *          <li>
     *             The second argument is of type <code>java.lang.Object</code>
     *             and is the node that is currently being parsed
     *             at the time the matching of the <code>fragmentName</code> occurred.
     *          </li>
     *      </ol>
     * <p>
     * <p>
     *      For example this handlerMethod might be defined within your translation file to
     *      handle the 'allInstances' expression:
     *      <pre>
     *      &lt;fragment name="(\s*${elementName}\s*\.)?\s*allInstances.*"
     *                   handlerMethod="handleAllInstances"&gt;
     *          &lt;kind name="body"&gt;
     *              from $completeElementName as $lowerCaseElementName 
     *          &lt;/kind&gt;
     *      &lt;/fragment&gt;
     *      </pre>
     * </p>
     * <p>
     *      And the implementation of the <code>handleAllInstances</code>
     *      method would be:
     *      <pre>
     *      public void handleAllInstances(String translation, Object node) {
     *          //some handling code
     *      }
     *      </pre> 
     * </p>
	 * 
     * @param node the node being parsed, the toString value of this node is 
     *        what is matched against the translation fragment. 
     *        We also need to pass the node to our 
     *        <a href="#handlerMethod">handlerMethod</a>
     *        so that it can be used it for additional processing (if we need it).
     * 
     * @see getTranslationFragment(java.lang.String)
	 */
	protected void handleTranslationFragment(Node node) {
		final String methodName = "BaseTranslator.getTranslatedFragment";
		ExceptionUtils.checkNull(methodName, "node", node);
		if (this.libraryTranslation!= null) {
			this.libraryTranslation.handleTranslationFragment(
                TranslationUtils.trimToEmpty(node), 
                this.getExpression().getKind(),
                node);
		}
	}
    
    /**
     * Finds the "fragment" with the specified <code>fragmentName</code> 
     * from the library translation file.  
     * 
     * <p>
     *  <strong>IMPORTANT:</strong> as a best practice, it is recommended that you use 
     *  <a href="#handleTranslationFragment(java.lang.String, org.andromda.core.translation.parser.node.Node)">handleTranslationFragment(java.lang.String, Node node)</a> 
     *  if at all possible (instead of this method), it will help  your code be cleaner and the methods smaller and 
     *  more maintainable.
     * </p>
     * <p>
     * Will retrieve the contents of the fragment <code>kind</code> 
     * that corresponds to the kind of expression currently being translated. 
     * (i.e. if <code>'context LegalAgreement inv: allInstances -> 
     * isUnique(documentTitle')</code>' is being translated, then the body of the 
     * element &lt;kind name="inv"&gt; would be returned).  
     * </p>
     * <p>
     * <strong>NOTE:</strong>You would use this method <strong>instead</strong> of
     * <a href="#handleTranslationFragment(java.lang.String, org.andromda.core.translation.parser.node.Node)">handleTranslationFragment(java.lang.String, Node node)</a>
     * if you just want to retrieve the value of the fragment and don't want to have a <a href="#handlerMethod">handlerMethod</a> 
     * which actually handles the processing of the output.  For example you may want 
     * to add a fragment called 'constraintTail' which would always be added to your 
     * translation at the end of the constraint, the 'tail'.  There isn't any part of the 
     * expression that matches this, but you still want to store it in a translation 
     * template since it could be different between translations within your Translation-Library.
     * </p> 
     * 
     * @param fragmentName the name of the fragment to retrieve from the translation
     * @return String the output String from the translated fragment.
     * 
     * @see handleTranslationFragment(Node node)
     */
    protected String getTranslationFragment(String fragmentName) {
        final String methodName = "BaseTranslator.getTranslatedFragment";
        ExceptionUtils.checkEmpty(methodName, "fragmentName", fragmentName);
        String fragmentString = null;        
        if (this.libraryTranslation!= null) {
            fragmentString = this.libraryTranslation.getTranslationFragment(
                fragmentName, this.getExpression().getKind());
        }
        return fragmentString;
    }

	/**
	 * Performs any initlization. Subclasses
     * should override this method if they want to provide
     * any initilization before translation begins.
	 */
	protected void preProcess() {
		this.contextElement = null;
	}

	/**
	 * @see org.andromda.core.translation.ExpressionTranslator#translate(
     * java.lang.String, java.lang.Object, java.lang.String)
     * 
     * <strong>NOTE:</strong> null is allowed for contextElement (even though 
     * it isn't within ExpressionTranslator.translate() since the TraceTranslator
     * doesn't need a <code>contextElement</code> and we don't want to slow
     * down the trace by having to read and load a model each time.
	 */
	public Expression translate(String translationName, Object contextElement, String expression) {
        final String methodName = "BaseTranslator.translate";
		ExceptionUtils.checkEmpty(methodName, "translationName", translationName);
		ExceptionUtils.checkEmpty(methodName, "expression", expression);
		try {
			//pre processing 
			this.preProcess();
			//set the context element so translators extending this have the 
			//context element available
			this.setContextElement(contextElement);
			Map templateObjects = new HashMap();
			templateObjects.put(Translator.CONTEXT_ELEMENT, contextElement);
			this.libraryTranslation = 
				LibraryTranslationFinder.findLibraryTranslation(translationName);
			if (this.libraryTranslation != null) {
                libraryTranslation.getLibrary().init();                
				libraryTranslation.processTranslation(templateObjects);	
				this.process(expression);
                libraryTranslation.getLibrary().shutdown();
			}
			//post processing
			this.postProcess();
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName 
				+ " with translationName '" + translationName 
				+ "', contextElement '" 
				+ contextElement 
				+ "' and expression --> '" 
				+ expression + "'";

            //if the exception is something other than a parser
            //error wrap in a TranslatorException, otherwise
            //just print the error so its more user friendly.
            if (!OclParserException.class.isAssignableFrom(ex.getClass())) {
                logger.error(errMsg, ex);
                throw new TranslatorException(errMsg, ex);
            }
            logger.error(errMsg 
               + "\n MESSAGE --> '" 
               + ex.getMessage() + "'");
		}
		return translatedExpression;
	}
	
	/**
	 * Parses the expression and applies this Translator to it.
	 * 
	 * @param expression the expression to process.
	 * @throws IOException if an IO error occurs during processing.
	 */
	protected void process(String expression) throws IOException {
        final String methodName = "BaseTranslator.process";
		ExceptionUtils.checkEmpty(methodName, "expression", expression);
		try {
			Lexer lexer =
				new Lexer(new PushbackReader(new StringReader(expression)));
			OclParser parser = new OclParser(lexer);
			Start startNode = parser.parse();
			this.translatedExpression = new Expression(expression);
			startNode.apply(this);
		} catch (ParserException ex) {
			throw new OclParserException(ex.getMessage());
		} catch (LexerException ex) {
			throw new OclParserException(ex.getMessage());
		}
	}
	
	/**
     * Performs any post processing.
	 * Subclasses should override to perform
	 * any final cleanup/processing.
	 */
	protected void postProcess() {
		//currently does nothing --> sub classes should override to perform
		//final processing
	}
	
	/**  The Following Are Overriden Parser Generated Methods **/

	/**
	 * Sets the kind and name of the expression for <code>inv</code> expressions.  If subclasses 
	 * override this method, they <strong>MUST</strong> call this method before their 
	 * own implementation.
	 * @param expressionBody
	 */
	public void inAInvClassifierExpressionBody(AInvClassifierExpressionBody expressionBody) {
        final String methodName = "BaseTranslator.inAInvClassifierExpressionBody";
		ExceptionUtils.checkNull(methodName, "expressionBody", expressionBody);
		if (this.translatedExpression != null) {
			this.translatedExpression.setName(TranslationUtils.trimToEmpty(expressionBody.getName()));
			this.translatedExpression.setKind(ExpressionKinds.INV);
		}
	}

	/**
	 * Sets the kind and name of the expression for <code>def</code> expressions.  If subclasses 
	 * override this method, they <strong>MUST</strong> call this method before their 
	 * own implementation.
	 * @param expressionBody
	 */
	public void inADefClassifierExpressionBody(ADefClassifierExpressionBody expressionBody) {
		final String methodName = "BaseTranslator.inADefClassifierExpressionBody";
		ExceptionUtils.checkNull(methodName, "expressionBody", expressionBody);
		if (this.translatedExpression != null) {
			this.translatedExpression.setName(TranslationUtils.trimToEmpty(expressionBody.getName()));
			this.translatedExpression.setKind(ExpressionKinds.DEF);
		}
	}
	
	/**
     * Sets the kind and name of the expression for classifier contexts.  If subclasses 
     * override this method, they <strong>MUST</strong> call this method before their 
     * own implementation.
	 * @param classifierExpressionBody
	 *
	public void inAClassifierExpressionBody(AClassifierExpressionBody classifierExpressionBody) {
		String methodName = "inAClassifierExpressionBody";
		if (logger.isDebugEnabled()) {
			logger.debug("performing " + methodName + 
				" with classifierExpressionBody --> " + classifierExpressionBody);
		}		
		ExceptionUtils.checkNull(methodName, "classifierExpressionBody", classifierExpressionBody);
		
        if (this.translatedExpression != null) {
    		//sets the name of the expression
    		this.translatedExpression.setName(
                 TranslationUtils.getPropertyAsString(classifierExpressionBody, "name"));
    		
    		//sets the kind of the expression (inv, def)
    		this.translatedExpression.setKind(
                 TranslationUtils.getPropertyAsString(classifierExpressionBody, "classifierStereotype"));
        }
	
	}*/
	
	/**
	 * Sets the kind and name of the expression for operation contexts.  If subclasses 
     * override this method, they <strong>MUST</strong> call this method before their 
     * own implementation.
	 * @param operationExpressionBody
	 */
	public void inAOperationExpressionBody(AOperationExpressionBody operationExpressionBody) {
		final String methodName = "BaseTranslator.inAOperationExpressionBody";
		if (logger.isDebugEnabled()) {
			logger.debug("performing " + methodName + 
				" with operationExpressionBody --> " + operationExpressionBody);
		}		
		ExceptionUtils.checkNull(methodName, "operationExpressionBody", operationExpressionBody);
        
        if (this.translatedExpression != null) {
    		//sets the name of the expression
    		this.translatedExpression.setName(
                TranslationUtils.getPropertyAsString(operationExpressionBody, "name"));		
    		
    		//sets the kind of the expression (body, post, or pre)
    		this.translatedExpression.setKind(
                TranslationUtils.getPropertyAsString(operationExpressionBody, "operationStereotype"));
        }
	}
	
	/**
	 * Sets the element type which represents the context of the expression for expressions
     * having operations as their context.  If subclasses override this method, 
     * they <strong>MUST</strong> call this method before their own implementation.
     * 
     * @param declaration the AOperationContextDeclaration instance from which
     *        we retrieve the element type.
	 */
	public void inAOperationContextDeclaration(AOperationContextDeclaration declaration) {
		final String methodName = "BaseTranslator.inAOperationContextDeclaration";
		if (logger.isDebugEnabled()) {
			logger.debug("performing " + methodName + 
				" with declaration --> " + declaration);
		}		
        if (this.translatedExpression != null) {   
    		this.translatedExpression.setContextElement(ConcreteSyntaxUtils.getType(
    			declaration.getName(), 
    			declaration.getPathNameTail()));
        }
	}
	
	/**
     * Sets the element type which represents the context of the expression for expressions
     * having classifiers as their context. If subclasses override this method, 
     * they <strong>MUST</strong> call this method before their own implementation.
     * 
     * @param declaration the AClassifierContextDeclaration instance from which we retrieve
     *        the element type.
	 */
	public void inAClassifierContextDeclaration(AClassifierContextDeclaration declaration) {
		final String methodName = "BaseTranslator.inAClassifierContextDeclaration";
		if (logger.isDebugEnabled()) {
			logger.debug("performing " + methodName + 
				" with declaration --> " + declaration);
		}		
        if (this.translatedExpression != null) {
    		this.translatedExpression.setContextElement(ConcreteSyntaxUtils.getType(
    			declaration.getName(), 
    			declaration.getPathNameTail()));
        }
	}
	
}