package org.andromda.core.translation;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Contains the translated expression, 
 * 
 * @author Chad Brandon
 */
public class Expression {
	
	/**   
	 * The resulting translated expression.
	 */
	private StringBuffer translatedExpression;
	
	/**
	 * The OCL expression before translation
	 */
	private String originalExpression;
	
	/**
	 * The element which is constained by the the OCL expression.
	 */
	private String contextElement;
	
	/**
	 * The kind of the OCL that was translated.
	 */
	private String kind;

	/**
	 * The name of the constraint.
	 */
	private String name;
	
	/**
	 * Creates a new instance of this Expression object
	 * @param originalExpression - the OCL expression that will be translated.
	 */
	public Expression(String originalExpression) {
		final String methodName = "Expression";
		ExceptionUtils.checkEmpty(methodName, "originalExpression", originalExpression);
		this.originalExpression = StringUtils.trimToEmpty(originalExpression);
		this.translatedExpression = new StringBuffer();
	}	
	
	/**
	 * Appends the value of the value of the <code>object</code>'s toString
     * result to the current translated expression String Buffer.
	 * @param object the object to append.
	 */
	public void appendToTranslatedExpression(Object object) {
		this.translatedExpression.append(object);
	}

	/**
	 * Appends a space charater to the current translated
	 * expression String Buffer.
	 */
	public void appendSpaceToTranslatedExpression() {
		this.translatedExpression.append(' ');
	}
	
	/**
	 * Replaces the pattern with the replacement within the translated expression
	 * buffer.
	 * @param pattern the pattern to search for.
	 * @param replacement the replacement.
	 */
	public void replaceInTranslatedExpression(String pattern, String replacement) {
		this.translatedExpression = 
			new StringBuffer(TranslationUtils.replacePattern(
				this.getTranslatedExpression(), pattern, replacement));
	}
	
	/**
	 * Performs replacement of the value of the <code>object</code>'s 
     * toString result at the start and end positions of the buffer 
     * containing the Expression.
	 * 
	 * @param position the position at which to insert
	 * @param object the 
     * 
	 * @see java.lang.StringBuffer#insert(int,java.lang.String)
	 */
	public void insertInTranslatedExpression(int position, Object object) {
		this.translatedExpression.insert(position, object);
	}

	/**
	 * Returns the expression after translation.
	 * 
	 * @return String
	 */
	public String getTranslatedExpression() {
		return TranslationUtils.removeExtraWhitespace(this.translatedExpression.toString());
	}

	/**
	 * Returns the expression before translation.
     * 
	 * @return String
	 */
	public String getOriginalExpression() {
		return TranslationUtils.removeExtraWhitespace(this.originalExpression);
	}

	/**
	 * Returns the element which is the context of this
     * expression.
	 * @return String the context element element.
	 */
	public String getContextElement() {
		final String methodName = "Expression.getContextElement";
		if (this.contextElement == null) {
			throw new ExpressionException(
				methodName + " - contextElement can not be null");
		}
		return this.contextElement;
	}

	/**
	 * Returns the Kind of this Expression (inv, post, or pre)
	 * @return String returns the Kind of this translation
	 */
	public String getKind() {
		final String methodName = "Expression.getKind";
		if (this.contextElement == null) {
			throw new ExpressionException(
				methodName + " - kind can not be null");
		}
		return this.kind;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
     * Sets the name.
	 * @param name the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the context element (the element to which the expression applies -->
	 * the element declared after the <code>context</code>)
     * 
	 * @param contextElement the name of the element which is the context element.
	 */
	public void setContextElement(String contextElement) {
		this.contextElement = contextElement;
	}

	/**
	 * Sets the "kind" of the OCL contraint 
     * (i.e, "pre", "post", "inv", etc.)
     * 
	 * @param kind the kind to set.
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}
	
    /**
     * @see java.lang.Object#toString()
     */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
