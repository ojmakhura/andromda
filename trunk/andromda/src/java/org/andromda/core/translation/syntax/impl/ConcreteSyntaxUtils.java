package org.andromda.core.translation.syntax.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.core.translation.node.AActualParameterList;
import org.andromda.core.translation.node.AArrowFeatureCall;
import org.andromda.core.translation.node.ACommaExpression;
import org.andromda.core.translation.node.ADotFeatureCall;
import org.andromda.core.translation.node.AFeatureCallParameters;
import org.andromda.core.translation.node.AOperation;
import org.andromda.core.translation.node.APropertyCallExpression;
import org.andromda.core.translation.node.ARelationalExpression;
import org.andromda.core.translation.node.ARelationalExpressionTail;
import org.andromda.core.translation.node.AStandardDeclarator;
import org.andromda.core.translation.node.ATypeDeclaration;
import org.andromda.core.translation.node.AVariableDeclaration;
import org.andromda.core.translation.node.AVariableDeclarationList;
import org.andromda.core.translation.node.AVariableDeclarationListTail;
import org.andromda.core.translation.node.PActualParameterList;
import org.andromda.core.translation.node.PEqualExpression;
import org.andromda.core.translation.node.PFeatureCallParameters;
import org.andromda.core.translation.node.POperation;
import org.andromda.core.translation.node.PRelationalExpression;
import org.andromda.core.translation.node.PVariableDeclaration;
import org.andromda.core.translation.node.PVariableDeclarationList;
import org.andromda.core.translation.node.TName;
import org.andromda.core.translation.syntax.OperationDeclaration;
import org.andromda.core.translation.syntax.VariableDeclaration;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contains some utilities for concrete syntax value retrieval.
 *
 * @author Chad Brandon
 */
public class ConcreteSyntaxUtils {

	private static final Logger logger = Logger.getLogger(ConcreteSyntaxUtils.class);

	/**
	 * Iterates through the passed in list and concates all
	 * the values of objects toString value to a StringBuffer
	 * and returns the StringBuffer.
	 *
	 * @param list the List of objects to concatinate.
	 * @return StringBuffer the concatinated contents of the list.
	 */
	public static StringBuffer concatContents(List list) {
		StringBuffer name = new StringBuffer();
		if (list != null) {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				String value = ObjectUtils.toString(iterator.next());
				name.append(value);
			}
		}
		return name;
	}

	/**
	 * Converts the passed <code>operation</code> to an
     * instance of <code>Operation</code> for the passed in operation.
	 *
	 * @param operation
	 * @return VariableDeclarations
	 */
	public static OperationDeclaration getOperationDeclaration(POperation operation) {
		final String methodName = "ConcreteSyntaxUtils.getOperationDeclaration";
		ExceptionUtils.checkNull(methodName, "operation", operation);

		OperationDeclaration operationDeclaration = null;

		AOperation op = (AOperation)operation;

		ATypeDeclaration typeDeclaration = (ATypeDeclaration)op.getReturnTypeDeclaration();
		String returnType = null;
		if (typeDeclaration != null) {
			returnType = ObjectUtils.toString(typeDeclaration .getType());
		}

		operationDeclaration =
			new OperationDeclarationImpl(
				ObjectUtils.toString(op.getName()),
				returnType,
				ConcreteSyntaxUtils.getVariableDeclarations(operation));

		if (logger.isDebugEnabled()) {
			logger.debug("completed " + methodName +
				" with operationDeclaration --> " + operationDeclaration);
		}

		return operationDeclaration;
	}

	/**
	 * Retrieves all the variable declarations for the passed in
	 * <code>operation</code>.
	 *
	 * @param operation the operation for which to retrieve the variable declarations.
	 * @return VariableDeclaration[]
	 */
	public static VariableDeclaration[] getVariableDeclarations(POperation operation) {
		final String methodName = "ConcreteSyntaxUtils.getVariableDeclarations";
		ExceptionUtils.checkNull(methodName, "operation", operation);
		return ConcreteSyntaxUtils.getVariableDeclarations(
			((AOperation)operation).getParameters());
	}

	/**
	 * Retrieves all the variable declarations for the passed in
	 * <code>standardDeclarator</code>.
	 *
	 * @param standardDeclarator the standard declartor for which to retrieve the
	 *        VariableDeclaration instances.
	 * @return VariableDeclaration[]
	 */
	public static VariableDeclaration[] getVariableDeclarations(AStandardDeclarator standardDeclarator) {
        final String methodName = "ConcreteSyntaxUtils.getVariableDeclarations";
		ExceptionUtils.checkNull(methodName, "standardDeclarator", standardDeclarator);
		return ConcreteSyntaxUtils.getVariableDeclarations(
				standardDeclarator.getVariableDeclarationList());
	}

	/**
	 * Creates a new VariableDeclaration from the passed in PVariableDeclaration.
	 * @param variableDeclaration the PVariableDeclaration that the new VariableDeclaration
	 *        will be created from.
	 * @param initialValue the initial value of the variable declaration.
	 * @return VariableDeclaration the new VariableDeclaration
	 */
	protected static VariableDeclaration newVariableDeclaration(
		PVariableDeclaration variableDeclaration,
		PEqualExpression initialValue) {
        final String methodName = "ConcreteSyntaxUtils.newVariableDeclaration";
		ExceptionUtils.checkNull(methodName, "variableDeclaration", variableDeclaration);

		AVariableDeclaration declaration = (AVariableDeclaration)variableDeclaration;
		ATypeDeclaration typeDeclaration = (ATypeDeclaration )declaration.getTypeDeclaration();
		String type = null;
		String name = ObjectUtils.toString(declaration.getName()).trim();
		if (typeDeclaration != null) {
			type = ObjectUtils.toString(typeDeclaration.getType());
		}
		return new VariableDeclarationImpl(name, type, ObjectUtils.toString(initialValue).trim());
	}

	/**
	 * Creates an array of VariableDeclaration[] from the passed in PVariableDeclarationList.
	 * @param variableDeclarationList the PVariableDeclarationList that the new VariableDeclaration
	 *        will be created from.
	 * @return VariableDeclaration[] the new VariableDeclaration array
	 */
	public static VariableDeclaration[] getVariableDeclarations(
		PVariableDeclarationList variableDeclarationList) {

		Collection declarations = new ArrayList();

		if (variableDeclarationList != null) {
			AVariableDeclarationList variables =
				(AVariableDeclarationList)variableDeclarationList;

			if (variables != null) {
				//add the first one
				declarations.add(
					ConcreteSyntaxUtils.newVariableDeclaration(
							variables.getVariableDeclaration(), variables.getVariableDeclarationValue()));

				//add the rest
				List variableTails = variables.getVariableDeclarationListTail();
				if (variableTails != null) {
					Iterator variableTailIt = variableTails.iterator();
					while (variableTailIt.hasNext()) {
						AVariableDeclarationListTail tail =
							(AVariableDeclarationListTail)variableTailIt.next();
						declarations.add(
							ConcreteSyntaxUtils.newVariableDeclaration(
								tail.getVariableDeclaration(), tail.getVariableDeclarationValue()));
					}
				}
			}
		}
		return (VariableDeclaration[])declarations.toArray(new VariableDeclaration[0]);
	}

	/**
	 * Gets all the parameters from the <code>featureCall</code> instance.
	 *
	 * @param featureCall the featureCall for which to retrieve the parameters
	 * @return List the list containing any parameters retrieved, or
     *         an empty array if none could be retrieved
	 */
	public static List getParameters(ADotFeatureCall featureCall) {
        List parameters = new ArrayList();
        if (featureCall != null) {
             parameters = getParameters(featureCall.getFeatureCallParameters());
        }
        return parameters;
	}

    /**
     * Gets all the parameters from the <code>featureCall</code> 
     * instance as a comma seperated String.
     *
     * @param featureCall the featureCall for which to retrieve the parameters
     * @return String the comma seperated String
     */
    public static String getParametersAsString(ADotFeatureCall featureCall) {
        return StringUtils.join(
            ConcreteSyntaxUtils.getParameters(
                featureCall).iterator(), ",");
    }
    
    /**
     * Gets all the parameters from the <code>featureCall</code> 
     * instance as a comma seperated String.
     *
     * @param featureCall the featureCall for which to retrieve the parameters
     * @return String the comma seperated String
     */
    public static String getParametersAsString(AArrowFeatureCall featureCall) {
        return StringUtils.join(
            ConcreteSyntaxUtils.getParameters(
                featureCall).iterator(), ",");
    }

    /**
     * Gets all the parameters from the <code>callParameters</code> instance.
     *
     * @param callParameters the callParameters for which to retrieve the parameters
     * @return List the list containing any parameters retrieved, or
     *         an empty array if none could be retrieved
     */
    private static List getParameters(PFeatureCallParameters callParameters) {
        List parameters = new ArrayList();
        if (callParameters != null) {
            PActualParameterList parameterList =
                ((AFeatureCallParameters)callParameters).getActualParameterList();
            if (parameterList != null) {
                AActualParameterList params = (AActualParameterList)parameterList;

                //add the first param if it exists
                String firstParam = TranslationUtils.trimToEmpty(params.getExpression());
                if (StringUtils.isNotEmpty(firstParam)) {
                    parameters.add(firstParam);
                }

                //now add the rest of the params which are contained in the tail
                List restOfParams = params.getCommaExpression();
                if (restOfParams != null && !restOfParams.isEmpty()) {
                    Iterator paramIt = restOfParams.iterator();
                    while (paramIt.hasNext()) {
                        ACommaExpression parameterListTail =
                            (ACommaExpression)paramIt.next();
                        parameters.add(
                            TranslationUtils.trimToEmpty(
                                parameterListTail.getExpression()));
                    }
                }
            }
        }
        return parameters;
    }
    
    /**
     * Gets all the parameters from the <code>featureCall</code> instance.
     *
     * @param featureCall the featureCall for which to retrieve the parameters
     * @return List the list containing any parameters retrieved, or
     *         an empty array if none could be retrieved
     */
    public static List getParameters(AArrowFeatureCall featureCall) {
        List parameters = new ArrayList();
        if (featureCall != null) {
             parameters = getParameters(featureCall.getFeatureCallParameters());
        }
        return parameters;
    }

	/**
	 * Gets the left and right expressions of a PRelationalExpression
	 * and puts then into a List.  The left expression will be the first
	 * expression in the list.
	 * @param relationalExpression
	 * @return the left and right parenthesis in [0] and [1] of the String[]
	 */
	public static String[] getLeftAndRightExpressions(PRelationalExpression relationalExpression) {
		String[] expressions = new String[2];
		ARelationalExpression expression = (ARelationalExpression)relationalExpression;

		//set the left expression
		expressions[0] = TranslationUtils.trimToEmpty(expression.getAdditiveExpression());

		ARelationalExpressionTail expressionTail =
			(ARelationalExpressionTail)expression.getRelationalExpressionTail();

		//set the right expression
		expressions[1] = TranslationUtils.trimToEmpty(expressionTail.getAdditiveExpression());

		return expressions;
	}

	/**
	 * Concatinates the type from the passed in name and pathNameTail.
	 * @param name the starting name of the type
	 * @param pathNameTail the tail pieces of the name
	 * @return String the concatinated name.
	 */
	public static String getType(TName name, List pathNameTail) {
		StringBuffer type =
			ConcreteSyntaxUtils.concatContents(pathNameTail);
		type.insert(0, TranslationUtils.trimToEmpty(name));
		return StringUtils.deleteWhitespace(type.toString());
	}

    /**
     * Gets the "real" primary expression, as opposed to the primary expression
     * retrieved from the parser syntax (since it leaves off any navigational
     * relationships).
     *
     * @param expression the APosfixExpression instance for which to retrieve
     *        the primary expression
     *
     * @return String the "real" primary expression or the passed in expression.
     */
    public static String getPrimaryExpression(APropertyCallExpression expression) {
        final String methodName = "ConcreteSyntaxUtils.getPrimaryExpression";
        if (logger.isDebugEnabled()) {
        	logger.debug("performing " + methodName +
                " with expression --> '" + expression + "'");
        }
    	StringBuffer primaryExpression = new StringBuffer();
    	if (expression != null) {
            //append the first part of the primary expression
            primaryExpression.append(
    	        TranslationUtils.trimToEmpty(expression.getPrimaryExpression()));

            List expressionTail = new ArrayList(expression.getPropertyCallExpressionTail());
            if (expressionTail.size() > 1) {
            	//if it its greater than one, we assume the rest of the primary expression
                //is all of the list elements except for the last, so we'll remove
                //the last link and concatinate them together to add the rest of
                //the primary expression
            	expressionTail.remove(expressionTail.size() - 1);
                primaryExpression.append(ConcreteSyntaxUtils.concatContents(expressionTail));
            }

            if (logger.isDebugEnabled()) {
                logger.debug("completed " + methodName + " with primaryExpression --> '"
                    + primaryExpression + "'");
            }
        }
        return StringUtils.deleteWhitespace(primaryExpression.toString());
    }

    /**
     * Gets all feature calls from the passed in APropertyCallExpression instance.
     *
     * @param expression the APosfixExpression instance for which to retrieve
     *        the primary expression
     *
     * @return String the "real" primary expression of the passed in expression.
     */
    public static List getFeatureCalls(APropertyCallExpression expression) {
        final String methodName = "ConcreteSyntaxUtils.getFeatureCalls";
        if (logger.isDebugEnabled()) {
            logger.debug("performing " + methodName +
                " with expression --> '" + expression + "'");
        }
        List featureCalls = new ArrayList();
        if (expression != null) {
        	List tails = expression.getPropertyCallExpressionTail();
            if (tails != null && !tails.isEmpty()) {
            	for (int ctr = 0; ctr < tails.size(); ctr++) {
                    Object tail = tails.get(ctr);
                    String featureCall = "dotFeatureCall";
                    // if the tail isn't a dotFeatureCall it must be an
                    // arrowFeatureCall                 
                    if (!TranslationUtils.hasProperty(tail, featureCall)) {
                        featureCall = "arrowFeatureCall";
                    }
                    featureCalls.add(
                        TranslationUtils.getProperty(tail,
                        featureCall));
                }
            }
        }
        return featureCalls;
    }

    /**
     * Loads a List of variable declaration names from the
     * <code>variableDeclarations</code>
     *
     * @param variableDeclarations an array of VariableDeclaration objects
     * @return List the list of argument names as Strings/
     */
    public static List getArgumentNames(VariableDeclaration[] variableDeclarations) {
        List names = new ArrayList();
        for (int ctr = 0; ctr < variableDeclarations.length; ctr++) {
            names.add(variableDeclarations[ctr].getName());
        }
        return names;
    }
}
