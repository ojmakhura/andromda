package org.andromda.translation.query;

import java.util.List;

import org.andromda.core.translation.BaseTranslator;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.core.translation.node.AArrowFeatureCall;
import org.andromda.core.translation.node.ADotFeatureCall;
import org.andromda.core.translation.node.ALogicalExpressionTail;
import org.andromda.core.translation.node.AOperationContextDeclaration;
import org.andromda.core.translation.node.APropertyCallExpression;
import org.andromda.core.translation.node.ARelationalExpressionTail;
import org.andromda.core.translation.node.AStandardDeclarator;
import org.andromda.core.translation.node.PRelationalExpression;
import org.andromda.core.translation.syntax.VariableDeclaration;
import org.andromda.core.translation.syntax.impl.ConcreteSyntaxUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Performs translation to the following:
 * <ul>
 *     <li>
 *          Hibernate-QL
 *     </li>
 * </ul>
 * @author Chad Brandon
 */
public class QueryTranslator extends BaseTranslator {

	/**
	 * Used to save the argument names for the operation which represents the
	 * context of the expression being translated.
	 */
	private List argumentNames = null;

	/**
	 * Contains the select clause of the query.
	 */
	private StringBuffer selectClause;

	/**
	 * Called by super class to reset any objects.
	 */
	protected void preProcess() {
		super.preProcess();
		this.selectClause = new StringBuffer();
        this.declaratorCtr = 0;
	}

    /**
     * Stores the name of the initial declarator.
     */
    private short declaratorCtr;

    /**
     * True/false whether or not its the initial declarator.
     * We care about this because we must know what the first one is
     * for differentiating between the first declarator (the beginning of the query)
     * and any other declarators (the connecting associations).
     */
    protected boolean isInitialDeclarator() {
        boolean isInitialDeclarator = (this.declaratorCtr <= 0);
        this.declaratorCtr++;
        return isInitialDeclarator;
    }

	/**
	 * Returns true if the name is one of the arguments.
	 *
	 * @return boolean true if its an argument
	 */
	protected boolean isArgument(String name) {
		boolean isArgument = this.argumentNames != null;
		if (isArgument) {
			isArgument = this.argumentNames.contains(name);
		}
		return isArgument;
    }

	/**
	 * Gets the information about the operation which is the context of this
	 * expression.
	 */
	public void inAOperationContextDeclaration(AOperationContextDeclaration declaration) {
		super.inAOperationContextDeclaration(declaration);
		VariableDeclaration[] variableDeclarations =
			ConcreteSyntaxUtils.getVariableDeclarations(
				declaration.getOperation());
		//set the argument names in a List so that we can retieve them later
		this.argumentNames = ConcreteSyntaxUtils.getArgumentNames(variableDeclarations);
	}

	/**
	 * Helps out with 'includesAll', replaces the {1} in the expression fragment
     * when a declarator is encountered (i.e. '| <variable name>')
	 */
	public void inAStandardDeclarator(AStandardDeclarator declarator) {
		final String methodName = "QueryTranslator.inAStandardDeclarator";
		if (logger.isDebugEnabled())
			logger.debug(
				"performing "
					+ methodName
					+ " with declarator --> "
					+ declarator);
               
        String temp = this.selectClause.toString();

        String declaratorName =
        	ConcreteSyntaxUtils.getVariableDeclarations(declarator)[0].getName();

        //by default we'll assume we're replacing the {1} arg.
        short replacement = 1;
        if (this.isInitialDeclarator()) {
        	//handle differently if its the initial declarator, replacement is {0}
            replacement = 0;
        }

		//now replace {1} reference
		temp =
			TranslationUtils.replacePattern(
				temp,
				String.valueOf(replacement),
				declaratorName);
		this.selectClause = new StringBuffer(temp);
	}

    /**
     * Override to handle any propertyCall expressions (
     * i.e. exists(<expression>), select(<expression>, etc.)
     */
    public void inAPropertyCallExpression(APropertyCallExpression expression) {
        this.handleTranslationFragment(expression);
    }

	/**
	 * Override to deal with logical 'and, 'or', 'xor, ... expressions.
	 */
	public void inALogicalExpressionTail(ALogicalExpressionTail logicalExpressionTail) {
        this.handleTranslationFragment(logicalExpressionTail);
	}

	/**
	 * Override to deal with relational '<, '>', '=', ... expressions.
	 */
	public void inARelationalExpressionTail(ARelationalExpressionTail relationalExpressionTail) {
		this.handleTranslationFragment(relationalExpressionTail);
	}

	/**
	 * Checks to see if the replacement is an argument and if so replaces the
	 * {index} in the fragment with the 'argument' fragment from the template.
	 * Otherwise replaces the {index} with the passed in replacement value.
	 *
	 * @param fragment the fragment to perform replacement.
	 * @param replacement the replacement string
	 * @param index the index in the string to replace.
	 * @return String the fragment with any replacements.
	 */
	protected String replaceFragment(
		String fragment,
		String replacement,
		int index) {
		fragment = StringUtils.trimToEmpty(fragment);
		replacement = StringUtils.trimToEmpty(replacement);
		//if 'replacement' is an argument use that for the replacement
        //in the fragment
		if (this.isArgument(replacement)) {
			replacement = this.getTranslationFragment("argument");
		}
		fragment =
			TranslationUtils.replacePattern(
				fragment,
				String.valueOf(index),
				replacement);
		return fragment;
	}

	/**
	 * Handles any final processing.
	 */
	protected void postProcess() {
		super.postProcess();
		//create the final translated expression
		String selectClauseTail =
			this.getTranslationFragment("selectClauseTail");
		String existingExpression =
			StringUtils.trimToEmpty(
				this.getExpression().getTranslatedExpression());

		if (StringUtils.isNotEmpty(selectClauseTail)
			&& StringUtils.isNotEmpty(existingExpression)) {
			this.selectClause.append(" ");
			this.selectClause.append(selectClauseTail);
			this.selectClause.append(" ");
		}

		this.getExpression().insertInTranslatedExpression(
			0,
			selectClause.toString());
	}


    /*------------------------- Handler methods ---------------------------------------*/

    /*------------------------- PropertyCallExpression Handler methods ---------------------*/

    public void handleSubSelect(String translation, Object node) {

        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;

        String primaryExpression =
            ConcreteSyntaxUtils.getPrimaryExpression(propertyCallExpression);

        //set the association which the 'includesAll' indicates (which
        //is the first feature call from the list of feature calls)
        translation = this.replaceFragment(
            translation,
            TranslationUtils.trimToEmpty(primaryExpression),
            0);

        this.selectClause.append(" ");
        this.selectClause.append(translation);
    }

    public void handleIsLike(String translation, Object node) {

        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;
        List featureCalls = ConcreteSyntaxUtils.getFeatureCalls(propertyCallExpression);
        
        List params = params = 
            ConcreteSyntaxUtils.getParameters((ADotFeatureCall)featureCalls.get(0));

        translation = this.replaceFragment(
                translation,
                TranslationUtils.deleteWhitespace(params.get(0)),
                0);
        translation = this.replaceFragment(
                translation,
                TranslationUtils.deleteWhitespace(params.get(1)),
                1);

        if (StringUtils.isNotEmpty(this.getExpression().getTranslatedExpression())) {
            this.getExpression().appendSpaceToTranslatedExpression();
        }
        this.getExpression().appendToTranslatedExpression(translation);

    }

    public void handleSelect(String translation, Object node) {
        this.selectClause.append(translation);
    }

    public void handleExists(String translation, Object node) {

        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;
        List featureCalls = ConcreteSyntaxUtils.getFeatureCalls(propertyCallExpression);
        
        // since the parameters can only be either dotFeatureCall or
        // arrowFeatureCall we try one or the other.
        String parameters;
        Object featureCall = featureCalls.get(0);
        if (ADotFeatureCall.class.isAssignableFrom(featureCall.getClass())) {
            parameters =
                StringUtils.deleteWhitespace(
                    ConcreteSyntaxUtils.getParametersAsString(
                        (ADotFeatureCall)featureCall));
        } else {
            parameters =
                StringUtils.deleteWhitespace(
                    ConcreteSyntaxUtils.getParametersAsString(
                        (AArrowFeatureCall)featureCall));            
        }

        String primaryExpression =
            ConcreteSyntaxUtils.getPrimaryExpression(propertyCallExpression);

        if (logger.isDebugEnabled()) {
            logger.debug("primaryExpression --> '" + primaryExpression + "'");
        }

        translation = this.replaceFragment(
        	translation,
            primaryExpression,
            1);
        translation = this.replaceFragment(
            translation,
            parameters,
            0);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    public void handleIsEmpty(String translation, Object node) {

        APropertyCallExpression propertyCallExpression = (APropertyCallExpression)node;

        String primaryExpression =
            ConcreteSyntaxUtils.getPrimaryExpression(propertyCallExpression);
        if (logger.isDebugEnabled()) {
            logger.debug("primaryExpression --> '" + primaryExpression + "'");
        }

    	translation = this.replaceFragment(translation, primaryExpression, 0);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);

    }

    /*------------------------- Logical Expression Handler (and, or, xor, etc.) ----------------------*/

    public void handleLogicalExpression(String translation, Object node) {
        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);
    }

    /*------------------------- Relational Expression Handler (=, <, >, >=, etc.) --------------------*/

    public void handleRelationalExpression(String translation, Object node) {

        ARelationalExpressionTail relationalExpressionTail =
            (ARelationalExpressionTail)node;

        String[] leftAndRightExpressions =
            ConcreteSyntaxUtils.getLeftAndRightExpressions(
                (PRelationalExpression) relationalExpressionTail.parent());

        String leftExpression =
            StringUtils.deleteWhitespace(leftAndRightExpressions[0]);
        String rightExpression =
            StringUtils.deleteWhitespace(leftAndRightExpressions[1]);

        translation = this.replaceFragment(translation, leftExpression, 0);
        translation = this.replaceFragment(translation, rightExpression, 1);

        this.getExpression().appendSpaceToTranslatedExpression();
        this.getExpression().appendToTranslatedExpression(translation);

    }

}
