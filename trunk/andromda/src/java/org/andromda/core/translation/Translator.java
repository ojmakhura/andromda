package org.andromda.core.translation;


/**
 * Provides OCL translation capabilities.  Every
 * OCL translator must implement this interface.
 * 
 * @author Chad Brandon
 */
public interface Translator {
	
	/**
	 * This is the name of the element "constrained" by the OCL
	 * expression which is made available to the TemplateEngine context.
	 * (in other words, it will be made available as an scripting element
	 *  on a template processed by the TemplateEngine implementation)
	 */
	public static final String CONTEXT_ELEMENT = "element";
	
	/**
	 * Translates the OCL into a translated Expression instance.
	 * 
	 * @param translationLibrary the library and translation to lookup perform
	 *        the translation (i.e. sql.Oracle9i --> library to use would be "sql"
	 *        and translation from the sql library would be 'Oracle9i').
	 * @param contextModelElement the element in the  model to which
	 *        the OCL constraint applies. 
	 * @param oclExpression the OCL expression to translate.
	 * 
	 * @return Expression
     * 
     * @see org.andromda.core.translation.Expression
	 */
	public Expression translate(
		String translationLibrary, 
		Object contextModelElement, 
		String oclExpression);
}
