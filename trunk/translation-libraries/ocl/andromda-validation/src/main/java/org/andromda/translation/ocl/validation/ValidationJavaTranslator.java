package org.andromda.translation.ocl.validation;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import org.andromda.core.engine.ModelProcessorException;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.translation.ocl.BaseTranslator;
import org.andromda.translation.ocl.node.*;
import org.andromda.translation.ocl.syntax.ConcreteSyntaxUtils;
import org.andromda.translation.ocl.syntax.OCLFeatures;
import org.andromda.translation.ocl.syntax.OCLPatterns;
import org.apache.commons.lang.StringUtils;

/**
 * <p/>
 * Provides translation of OCL validation constraints to the Java language. </p>
 *
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public class ValidationJavaTranslator
        extends BaseTranslator
{
    private static Properties features = null;

    static
    {
        try
        {
            URL featuresUri = ValidationJavaTranslator.class.getResource("features.properties");
            if (featuresUri == null)
            {
                throw new ModelProcessorException("Could not load file --> '" + featuresUri + '\'');
            }
            features = new Properties();
            InputStream stream = featuresUri.openStream();
            features.load(stream);
            stream.close();
            stream = null;

        }
        catch (final Throwable throwable)
        {
            throw new ValidationTranslatorException(throwable);
        }

    }

    /**
     * The package to which the OCL translator classes belong.
     */
    private static final String OCL_TRANSLATOR_PACKAGE = "org.andromda.translation.ocl.validation";

    /**
     * This is the start of a new constraint. We prepare everything by resetting and initializing the required objects.
     * @param node 
     */
    public void caseAContextDeclaration(AContextDeclaration node)
    {
        newTranslationLayer();
        {
            Object[] temp = node.getContextDeclaration().toArray();
            for (int ctr = 0; ctr < temp.length; ctr++)
            {
                ((PContextDeclaration) temp[ctr]).apply(this);
            }
        }
        mergeTranslationLayers();
        this.getExpression().appendToTranslatedExpression(translationLayers.peek());
        translationLayers.clear();
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAClassifierContextDeclaration(org.andromda.translation.ocl.node.AClassifierContextDeclaration)
     */
    public void caseAClassifierContextDeclaration(AClassifierContextDeclaration node)
    {
        // explicitly call super method so
        // that we can set the type of the expression
        super.inAClassifierContextDeclaration(node);
        Object[] temp = node.getClassifierExpressionBody().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PClassifierExpressionBody) temp[ctr]).apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAOperationContextDeclaration(org.andromda.translation.ocl.node.AOperationContextDeclaration)
     */
    public void caseAOperationContextDeclaration(AOperationContextDeclaration node)
    {
        // explicitly call super method so
        // that we can set the type of the expression
        super.inAOperationContextDeclaration(node);
        Object[] temp = node.getOperationExpressionBody().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((POperationExpressionBody) temp[ctr]).apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAAttributeOrAssociationContextDeclaration(org.andromda.translation.ocl.node.AAttributeOrAssociationContextDeclaration)
     */
    public void caseAAttributeOrAssociationContextDeclaration(AAttributeOrAssociationContextDeclaration node)
    {
        super.inAAttributeOrAssociationContextDeclaration(node);
        Object[] temp = node.getAttributeOrAssociationExpressionBody().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PAttributeOrAssociationExpressionBody) temp[ctr]).apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAInvClassifierExpressionBody(org.andromda.translation.ocl.node.AInvClassifierExpressionBody)
     */
    public void caseAInvClassifierExpressionBody(AInvClassifierExpressionBody node)
    {
        // explicitly call super method so
        // that we can set the type of the expression
        super.inAInvClassifierExpressionBody(node);
        node.getExpression().apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseADefClassifierExpressionBody(org.andromda.translation.ocl.node.ADefClassifierExpressionBody)
     */
    public void caseADefClassifierExpressionBody(ADefClassifierExpressionBody node)
    {
        // explicitly call super method so
        // that we can set the type of the expression
        super.inADefClassifierExpressionBody(node);
        node.getDefinitionExpression().apply(this);
    }

    /**
     * We need to keep track that what follows is in the scope of an arrow feature call, this is important because it
     * means it is a feature that is implied by the OCL language, rather than the model on which the constraint
     * applies.
     * @param node 
     */
    public void inAArrowPropertyCallExpressionTail(AArrowPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.push(Boolean.TRUE);
    }

    /**
     * Undo the arrow feature call trace.
     * @param node 
     */
    public void outAArrowPropertyCallExpressionTail(AArrowPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.pop();
    }

    /**
     * This indicates we have entered a feature call, we need to mark this to counterpart any previous arrow feature
     * call flags.
     * @param node 
     */
    public void inADotPropertyCallExpressionTail(ADotPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.push(Boolean.FALSE);
    }

    /**
     * Undo the dot feature call trace.
     * @param node 
     */
    public void outADotPropertyCallExpressionTail(ADotPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.pop();
    }

    /**
     * Here we need to make sure the equals sign '=' is not translated into the 'equal' keyword. OCL uses '=' for
     * comparison as well as for assignment, Java uses '==', '=' and .equals() so we override the default OCL value here
     * to use '=' instead of 'equal'
     * @param node 
     */
    public void caseALetVariableDeclaration(ALetVariableDeclaration node)
    {
        inALetVariableDeclaration(node);
        if (node.getVariableDeclaration() != null)
        {
            node.getVariableDeclaration().apply(this);
        }
        if (node.getEqual() != null)
        {
            write("=");
        }
        if (node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
        outALetVariableDeclaration(node);
    }

    /**
     * Add a variable to the context.
     * @param node 
     */
    public void inALetVariableDeclaration(ALetVariableDeclaration node)
    {
        newTranslationLayer(); // this layer will be disposed later on, we do
        // not write variable declarations

        AVariableDeclaration variableDeclaration = (AVariableDeclaration) node.getVariableDeclaration();
        String variableName = variableDeclaration.getName().getText();

        newTranslationLayer();
        node.getExpression().apply(this);

        String variableValue = translationLayers.pop().toString();

        addLetVariableToContext(variableName, variableValue);
    }

    /**
     * In Java we need to end the declaration statement with a semicolon, this is handled here.
     * @param node 
     */
    public void outALetVariableDeclaration(ALetVariableDeclaration node)
    {
        write(";");
        translationLayers.pop();
    }

    /**
     * Renders a variable declaration. Missing types will imply the Object type.
     * @param node 
     */
    public void caseAVariableDeclaration(AVariableDeclaration node)
    {
        if (node.getTypeDeclaration() == null)
            write("Object");
        else
            node.getTypeDeclaration().apply(this);

        write(" "); // we need to add a space between the type and the name

        node.getName().apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseATypeDeclaration(org.andromda.translation.ocl.node.ATypeDeclaration)
     */
    public void caseATypeDeclaration(ATypeDeclaration node)
    {
        node.getType().apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAVariableDeclarationList(org.andromda.translation.ocl.node.AVariableDeclarationList)
     */
    public void caseAVariableDeclarationList(AVariableDeclarationList node)
    {
        node.getVariableDeclaration().apply(this);

        if (node.getVariableDeclarationValue() != null)
            node.getVariableDeclarationValue().apply(this);

        Object[] temp = node.getVariableDeclarationListTail().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PVariableDeclarationListTail) temp[ctr]).apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAVariableDeclarationListTail(org.andromda.translation.ocl.node.AVariableDeclarationListTail)
     */
    public void caseAVariableDeclarationListTail(AVariableDeclarationListTail node)
    {
        node.getComma().apply(this);
        node.getVariableDeclaration().apply(this);

        if (node.getVariableDeclarationValue() != null)
            node.getVariableDeclarationValue().apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAEqualExpression(org.andromda.translation.ocl.node.AEqualExpression)
     */
    public void caseAEqualExpression(AEqualExpression node)
    {
        node.getEqual().apply(this);
        node.getExpression().apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseABodyOperationStereotype(org.andromda.translation.ocl.node.ABodyOperationStereotype)
     */
    public void caseABodyOperationStereotype(ABodyOperationStereotype node)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAPreOperationStereotype(org.andromda.translation.ocl.node.APreOperationStereotype)
     */
    public void caseAPreOperationStereotype(APreOperationStereotype node)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAPostOperationStereotype(org.andromda.translation.ocl.node.APostOperationStereotype)
     */
    public void caseAPostOperationStereotype(APostOperationStereotype node)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAMessageExpression(org.andromda.translation.ocl.node.AMessageExpression)
     */
    public void caseAMessageExpression(AMessageExpression node)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAIfExpression(org.andromda.translation.ocl.node.AIfExpression)
     */
    public void caseAIfExpression(AIfExpression node)
    {
        node.getIf().apply(this);

        write("(");
        node.getIfBranch().apply(this);
        write(")");

        node.getThen().apply(this);

        write("{");
        node.getThenBranch().apply(this);
        write(";");
        write("}");

        node.getElse().apply(this);

        write("{");
        node.getElseBranch().apply(this);
        write(";");
        write("}");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAPropertyCallExpression(org.andromda.translation.ocl.node.APropertyCallExpression)
     */
    public void caseAPropertyCallExpression(APropertyCallExpression node)
    {
        newTranslationLayer();
        node.getPrimaryExpression().apply(this);
        Object[] temp = node.getPropertyCallExpressionTail().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PPropertyCallExpressionTail) temp[ctr]).apply(this);
        mergeTranslationLayerAfter();
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseADotPropertyCallExpressionTail(org.andromda.translation.ocl.node.ADotPropertyCallExpressionTail)
     */
    public void caseADotPropertyCallExpressionTail(ADotPropertyCallExpressionTail node)
    {
        inADotPropertyCallExpressionTail(node);
        String expression = TranslationUtils.trimToEmpty(node);
        // we prepend an introspection call if the expression is
        // an operation call
        if (OCLPatterns.isOperation(expression))
        {
            AFeatureCall featureCall = (AFeatureCall) node.getFeatureCall();
            String featureCallExpression = TranslationUtils.trimToEmpty(node.getFeatureCall());
            if (OCLFeatures.isOclIsKindOf(featureCallExpression))
            {
                this.handleOclIsKindOf(featureCall);
            } else if (OCLFeatures.isOclIsTypeOf(featureCallExpression))
            {
                this.handleOclIsTypeOf(featureCall);
            } else if (OCLFeatures.isConcat(featureCallExpression))
            {
                this.handleConcat(featureCall);
            } else
            {
                this.handleDotFeatureCall(featureCall);
            }
        }
        outADotPropertyCallExpressionTail(node);
    }

    /**
     * oclIsKindOf(type) is a special feature defined by OCL on all objects.
     */
    private void handleOclIsKindOf(Object node)
    {
        String type = this.getParametersAsType(node);
        if (type != null)
        {
            write(" instanceof ");
            write(type);
        }
    }

    /**
     * oclIsTypeOf(type) is a special feature defined by OCL on all objects.
     */
    private void handleOclIsTypeOf(Object node)
    {
        String type = this.getParametersAsType(node);
        if (type != null)
        {
            write(".getClass().getName().equals(");
            write(type);
            write(".class.getName())");
        }
    }

    /**
     * Extracts the parameters from the given <code>node</code> and returns the parameters as a type (or null if none
     * can be extracted).
     *
     * @param node the node from which to extrac the parameters
     * @return the fully qualified type name.
     */
    private String getParametersAsType(Object node)
    {
        String type = null;
        if (node instanceof AFeatureCall)
        {
            type = ConcreteSyntaxUtils.getParametersAsString((AFeatureCall) node);
        } else if (node instanceof AFeatureCallParameters)
        {
            type = ConcreteSyntaxUtils.getParametersAsString((AFeatureCallParameters) node);
        }
        if (type != null)
        {
            type = type.replaceAll("\\s*::\\s*", ".");
            // if we don't have a package define, attempt to find the model
            // element
            // in the same package as the context element.
            if (type.indexOf('.') == -1)
            {
                if (this.getModelElement() != null)
                {
                    type = this.getModelElement().getPackageName() + '.' + type;
                }
            }
        }
        return type;
    }

    /**
     * contact(string) is a special feature defined by OCL on strings.
     */
    private void handleConcat(AFeatureCall featureCall)
    {
        write(" + \"\" + ");
        write(OCL_INTROSPECTOR_INVOKE_PREFIX);
        write(CONTEXT_ELEMENT_NAME);
        write(",\"");
        write(ConcreteSyntaxUtils.getParametersAsString(featureCall).replaceAll("\\s*", ""));
        write("\")");
    }

    /**
     * Handles a <strong>dot </strong> feature call. Its expected that this <code>featureCall</code>'s parent is a
     * ADotPropertyCallExpressionTail. This is here because dot feature calls must be handled differently than
     * <code>arrow<code> feature calls.
     *
     * @param featureCall the <strong>dot</strong> <code>featureCall</code> to handle.
     */
    public void handleDotFeatureCall(AFeatureCall featureCall)
    {
        this.prependToTranslationLayer(OCL_INTROSPECTOR_INVOKE_PREFIX);
        this.appendToTranslationLayer(",\"");
        this.appendToTranslationLayer(TranslationUtils.deleteWhitespace(featureCall));
        this.appendToTranslationLayer("\"");
        if (featureCall.getFeatureCallParameters() != null)
        {
            List parameters = ConcreteSyntaxUtils.getParameters(featureCall);
            if (parameters != null && !parameters.isEmpty())
            {
                write(",new Object[]{");
                this.appendToTranslationLayer(OCL_INTROSPECTOR_INVOKE_PREFIX);
                this.appendToTranslationLayer(CONTEXT_ELEMENT_NAME);
                this.appendToTranslationLayer(",\"");
                this.appendToTranslationLayer(ConcreteSyntaxUtils.getParameters(featureCall).get(0));
                this.appendToTranslationLayer("\")}");
            }
        }
        this.appendToTranslationLayer(")");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAArrowPropertyCallExpressionTail(org.andromda.translation.ocl.node.AArrowPropertyCallExpressionTail)
     */
    public void caseAArrowPropertyCallExpressionTail(AArrowPropertyCallExpressionTail node)
    {
        inAArrowPropertyCallExpressionTail(node);
        node.getArrow().apply(this);
        this.handleArrowFeatureCall((AFeatureCall) node.getFeatureCall());
        outAArrowPropertyCallExpressionTail(node);
    }

    /**
     * @see org.andromda.translation.ocl.BaseTranslator#isOperationArgument(String)
     */
    protected boolean isOperationArgument(String argument)
    {
        return super.isOperationArgument(this.getRootName(argument));
    }

    /**
     * Gets the root path name from the given <code>navigationalPath</code> (by trimming off any additional navigational
     * path).
     *
     * @param navigationalPath the navigational property path (i.e. car.door)
     * @return the root of the path name.
     */
    private String getRootName(String navigationalPath)
    {
        return StringUtils.trimToEmpty(navigationalPath).replaceAll("\\..*", "");
    }

    /**
     * Gets the tail of the navigational path (that is it removes the root from the path and returns the tail).
     *
     * @param navigationalPath the navigational property path (i.e. car.door)
     * @return the tail of the path name.
     */
    private String getPathTail(String navigationalPath)
    {
        final int dotIndex = navigationalPath.indexOf('.');
        return dotIndex != -1 ? navigationalPath.substring(dotIndex + 1, navigationalPath.length()) : navigationalPath;
    }

    /**
     * TODO: improve implementation to reduce the code duplication (avoid having two write statements)
     * @param node 
     */
    public void caseAFeaturePrimaryExpression(AFeaturePrimaryExpression node)
    {
        inAFeaturePrimaryExpression(node);
        if (node.getPathName() != null)
        {
            final String variableName = ((APathName) node.getPathName()).getName().getText();
            final String variableValue = getDeclaredLetVariableValue(variableName);
            final boolean isDeclaredAsLetVariable = (variableValue != null);
            String featureExpression = TranslationUtils.deleteWhitespace(node);
            if (isDeclaredAsLetVariable)
            {
                write(variableValue);
            } else if (node.getFeatureCallParameters() == null || OCLPatterns.isOperation(featureExpression))
            {
                APropertyCallExpression expression = (APropertyCallExpression) node.parent();
                String expressionAsString = ConcreteSyntaxUtils.getPrimaryExpression(expression);
                // remove any references to 'self.' as we write
                expressionAsString = expressionAsString.replaceAll("self\\.", "");
                if (OCLFeatures.isSelf(expressionAsString))
                {
                    write(CONTEXT_ELEMENT_NAME);
                } else if (StringUtils.isNotBlank(expressionAsString))
                {
                    boolean convertToBoolean = false;
                    if (node.parent().parent() instanceof AUnaryExpression)
                    {
                        AUnaryExpression unaryExpression = (AUnaryExpression) node.parent().parent();
                        // we convert each unary not expression to boolean
                        convertToBoolean = unaryExpression.getUnaryOperator() instanceof ANotUnaryOperator;
                        if (convertToBoolean)
                        {
                            this.write(BOOLEAN_WRAP_PREFIX);
                        }
                    }
                    if (OCLFeatures.isOclIsKindOf(expressionAsString))
                    {
                        this.write("object");
                        this.handleOclIsKindOf(node.getFeatureCallParameters());
                    } else if (OCLFeatures.isOclIsTypeOf(expressionAsString))
                    {
                        this.write("object");
                        this.handleOclIsTypeOf(node.getFeatureCallParameters());
                    } else
                    {
                        // whether or not an introspector call is required
                        boolean introspectorCall = true;
                        String invokedObject = CONTEXT_ELEMENT_NAME;
                        // if we're in an arrow call we assume the invoked
                        // object is the object for which the arrow call applies
                        if (this.arrowPropertyCallStack.peek().equals(Boolean.TRUE))
                        {
                            invokedObject = "object";
                        }
                        if (this.isOperationArgument(expressionAsString))
                        {
                            // - if the expression is an argument, then 
                            //   that becomes the invoked object
                            invokedObject = this.getRootName(expressionAsString);
                            expressionAsString = this.getPathTail(expressionAsString);
                            introspectorCall = !invokedObject.equals(expressionAsString);
                        }
                        if (introspectorCall)
                        {
                            write(OCL_INTROSPECTOR_INVOKE_PREFIX);
                        }
                        write(invokedObject);
                        if (introspectorCall)
                        {
                            write(",\"");
                            write(expressionAsString);
                        }
                        if (introspectorCall)
                        {
                            write("\")");
                        }
                        if (convertToBoolean)
                        {
                            this.write(BOOLEAN_WRAP_SUFFIX);
                        }
                    }
                    if (this.requiresBooleanConversion)
                    {
                        this.write(BOOLEAN_WRAP_SUFFIX);
                        this.requiresBooleanConversion = false;
                    }
                }
            } else
            {
                node.getPathName().apply(this);
            }
        }
        if (node.getIsMarkedPre() != null)
        {
            node.getIsMarkedPre().apply(this);
        }
        if (node.getQualifiers() != null)
        {
            // we use introspection when in an arrow, so passing
            // feature name as a String without parentheses
            if (this.arrowPropertyCallStack.peek().equals(Boolean.FALSE))
            {
                node.getQualifiers().apply(this);
            }
        }
        outAFeaturePrimaryExpression(node);
    }

    /**
     * Handles an <strong>arrow </strong> feature call. Its expected that this <code>featureCall</code>'s parent is a
     * AArrowPropertyCallExpressionTail. This is here because arrow feature calls must be handled differently than
     * <code>dot<code> feature calls.
     *
     * @param featureCall the <strong>arrow</strong> <code>featureCall</code> to handle.
     */
    public void handleArrowFeatureCall(AFeatureCall featureCall)
    {
        AFeatureCallParameters params = (AFeatureCallParameters) featureCall.getFeatureCallParameters();
        AActualParameterList list = null;
        if (params != null)
        {
            list = (AActualParameterList) params.getActualParameterList();
        }
        boolean arrow = this.arrowPropertyCallStack.peek().equals(Boolean.TRUE) &&
                StringUtils.isNotBlank(String.valueOf(list));
        {
            newTranslationLayer();
            final String navigationalPath = ConcreteSyntaxUtils.getArrowFeatureCallResultNavigationalPath(
                    (APropertyCallExpression) featureCall.parent().parent());
            // if the result of an arrow feature (collection operation) has a
            // navigational
            // path, retrieve it and wrap the current expression with an OCL
            // introspector call
            boolean resultNavigationalPath = StringUtils.isNotBlank(navigationalPath);
            if (resultNavigationalPath)
            {
                write(OCL_INTROSPECTOR_INVOKE_PREFIX);
            }
            write(OCL_TRANSLATOR_PACKAGE);
            write(".OCLCollections.");
            inAFeatureCall(featureCall);
            if (featureCall.getPathName() != null)
            {
                featureCall.getPathName().apply(this);
            }
            String featureCallName = TranslationUtils.trimToEmpty(featureCall.getPathName());
            AFeatureCallParameters parameters = (AFeatureCallParameters) featureCall.getFeatureCallParameters();
            if (parameters != null)
            {
                if (parameters.getLParen() != null)
                {
                    parameters.getLParen().apply(this);
                }
                mergeTranslationLayerBefore();
                AActualParameterList parameterList = (AActualParameterList) parameters.getActualParameterList();
                if (parameterList != null)
                {
                    List expressions = parameterList.getCommaExpression();

                    if (parameterList.getExpression() != null)
                    {
                        if (arrow)
                        {
                            write(",");
                            write(features.getProperty(featureCallName));
                            write(" ");
                            if (OCLPredicateFeatures.isPredicateFeature(featureCallName))
                            {
                                write(BOOLEAN_WRAP_PREFIX);
                            }
                        }
                        parameterList.getExpression().apply(this);
                    }
                    for (int ctr = 0; ctr < expressions.size(); ctr++)
                    {
                        Node expression = (Node) expressions.get(ctr);
                        if (expression != null)
                        {
                            write(",");
                            expression.apply(this);
                        }
                    }
                    if (parameterList.getExpression() != null)
                    {
                        if (OCLPredicateFeatures.isPredicateFeature(featureCallName))
                        {
                            write(BOOLEAN_WRAP_SUFFIX);
                        }
                        if (arrow)
                            write(";}}");
                    }
                }
                if (parameters.getRParen() != null)
                {
                    parameters.getRParen().apply(this);
                }
            }
            // now since we have a navigational path off of the
            // result we need to write the path and close off
            // the call to the OCL introspector.
            if (resultNavigationalPath)
            {
                write(",\"");
                write(navigationalPath);
                write("\"");
                write(")");
            }
            this.outAFeatureCall(featureCall);
        }
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseALetExp(org.andromda.translation.ocl.node.ALetExp)
     */
    public void caseALetExp(ALetExp node)
    {
        inALetExp(node);
        if (node.getLet() != null)
        {
            node.getLet().apply(this);
        }
        if (node.getLetVariableDeclaration() != null)
        {
            node.getLetVariableDeclaration().apply(this);
        }
        if (node.getLetExpSub() != null)
        {
            node.getLetExpSub().apply(this);
        }
        outALetExp(node);
    }

    /**
     * We are ready to store a new context of variables
     * @param node 
     */
    public void inALetExp(ALetExp node)
    {
        newLetVariableContext();
    }

    /**
     * The variables are out of scope, we need to purge their context.
     * @param node 
     */
    public void outALetExp(ALetExp node)
    {
        dropLetVariableContext();
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseAVariableDeclarationLetExpSub(org.andromda.translation.ocl.node.AVariableDeclarationLetExpSub)
     */
    public void caseAVariableDeclarationLetExpSub(AVariableDeclarationLetExpSub node)
    {
        node.getComma().apply(this);
        node.getLetVariableDeclaration().apply(this);
        node.getLetExpSub().apply(this);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseALogicalExp(org.andromda.translation.ocl.node.ALogicalExp)
     */
    public void caseALogicalExp(ALogicalExp node)
    {
        newTranslationLayer();
        if (node.getRelationalExpression() != null)
        {
            node.getRelationalExpression().apply(this);
        }
        Object[] tails = node.getLogicalExpressionTail().toArray();
        for (int ctr = 0; ctr < tails.length; ctr++)
        {
            ((ALogicalExpressionTail) tails[ctr]).apply(this);
        }
        mergeTranslationLayerAfter();
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseALogicalExpressionTail(org.andromda.translation.ocl.node.ALogicalExpressionTail)
     */
    public void caseALogicalExpressionTail(ALogicalExpressionTail node)
    {
        node.getLogicalOperator().apply(this);
        if (node.getLogicalOperator() instanceof AImpliesLogicalOperator)
        {
            prependToTranslationLayer("(");
        }
        if (node.getRelationalExpression() != null)
        {
            node.getRelationalExpression().apply(this);
        }
        if (node.getLogicalOperator() instanceof AImpliesLogicalOperator)
        {
            write(":true)");
        }
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#caseARelationalExpressionTail(org.andromda.translation.ocl.node.ARelationalExpressionTail)
     */
    public void caseARelationalExpressionTail(ARelationalExpressionTail node)
    {
        inARelationalExpressionTail(node);

        newTranslationLayer();
        write(OCL_TRANSLATOR_PACKAGE);
        write(".OCLExpressions.");
        node.getRelationalOperator().apply(this);
        write("(");
        mergeTranslationLayerBefore();
        if (node.getAdditiveExpression() != null)
        {
            write(",");
            node.getAdditiveExpression().apply(this);
        }
        write(")");
        outARelationalExpressionTail(node);
    }

    /**
     * A flag indicating if the expression needs to be converted/wrapped in a Java Boolean instance.
     */
    private boolean requiresBooleanConversion = false;

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#inARelationalExpression(org.andromda.translation.ocl.node.ARelationalExpression)
     */
    public void inARelationalExpression(ARelationalExpression node)
    {
        // in this block of code, we determine whether or not
        // the next appended expression needs to be
        // converted/wrapped by a Java Boolean
        if (node.getRelationalExpressionTail() == null)
        {
            Object parent = node.parent();
            Object expression = null;
            if (parent instanceof ALogicalExp)
            {
                List tails = ((ALogicalExp) parent).getLogicalExpressionTail();
                if (tails != null && !tails.isEmpty())
                {
                    expression = tails.get(0);
                    // if it's an AND or OR expression set the expression
                    // to the node
                    if (OCLPatterns.isAndOrOrExpression(expression))
                    {
                        expression = node;
                    }
                }
            } else if (parent instanceof ALogicalExpressionTail)
            {
                expression = node;
            }
            requiresBooleanConversion = expression != null
                    && OCLPatterns.isNavigationalPath(expression)
                    && !OCLPatterns.isOperation(node);
            if (this.requiresBooleanConversion)
            {
                this.write(BOOLEAN_WRAP_PREFIX);
            }
        }
        newTranslationLayer();
    }

    /**
     * @see org.andromda.translation.ocl.analysis.DepthFirstAdapter#outARelationalExpression(org.andromda.translation.ocl.node.ARelationalExpression)
     */
    public void outARelationalExpression(ARelationalExpression node)
    {
        mergeTranslationLayerAfter();
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTName(org.andromda.translation.ocl.node.TName)
     */
    public void caseTName(TName node)
    {
        write(node.getText());
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTAnd(org.andromda.translation.ocl.node.TAnd)
     */
    public void caseTAnd(TAnd tAnd)
    {
        write("&&");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTOr(org.andromda.translation.ocl.node.TOr)
     */
    public void caseTOr(TOr tOr)
    {
        write("||");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTXor(org.andromda.translation.ocl.node.TXor)
     */
    public void caseTXor(TXor tXor)
    {
        write("^");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTImplies(org.andromda.translation.ocl.node.TImplies)
     */
    public void caseTImplies(TImplies tImplies)
    {
        // convert any non boolean's to boolean
        this.prependToTranslationLayer(BOOLEAN_WRAP_PREFIX);
        this.appendToTranslationLayer(BOOLEAN_WRAP_SUFFIX);
        write("?");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTNot(org.andromda.translation.ocl.node.TNot)
     */
    public void caseTNot(TNot tNot)
    {
        write("!");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTPlus(org.andromda.translation.ocl.node.TPlus)
     */
    public void caseTPlus(TPlus tPlus)
    {
        write("+");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTMinus(org.andromda.translation.ocl.node.TMinus)
     */
    public void caseTMinus(TMinus tMinus)
    {
        write("-");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTMult(org.andromda.translation.ocl.node.TMult)
     */
    public void caseTMult(TMult tMult)
    {
        write("*");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTDiv(org.andromda.translation.ocl.node.TDiv)
     */
    public void caseTDiv(TDiv tDiv)
    {
        write("/");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTEqual(org.andromda.translation.ocl.node.TEqual)
     */
    public void caseTEqual(TEqual tEqual)
    {
        write("equal");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTNotEqual(org.andromda.translation.ocl.node.TNotEqual)
     */
    public void caseTNotEqual(TNotEqual tNotEqual)
    {
        write("notEqual");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTLt(org.andromda.translation.ocl.node.TLt)
     */
    public void caseTLt(TLt tLt)
    {
        write("less");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTLteq(org.andromda.translation.ocl.node.TLteq)
     */
    public void caseTLteq(TLteq tLteq)
    {
        write("lessOrEqual");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTGt(org.andromda.translation.ocl.node.TGt)
     */
    public void caseTGt(TGt tGt)
    {
        write("greater");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTGteq(org.andromda.translation.ocl.node.TGteq)
     */
    public void caseTGteq(TGteq tGteq)
    {
        write("greaterOrEqual");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTInv(org.andromda.translation.ocl.node.TInv)
     */
    public void caseTInv(TInv tInv)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTDef(org.andromda.translation.ocl.node.TDef)
     */
    public void caseTDef(TDef tDef)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTLet(org.andromda.translation.ocl.node.TLet)
     */
    public void caseTLet(TLet tLet)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTColon(org.andromda.translation.ocl.node.TColon)
     */
    public void caseTColon(TColon tColon)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTLBrace(org.andromda.translation.ocl.node.TLBrace)
     */
    public void caseTLBrace(TLBrace tlBrace)
    {
        write("{");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTLBracket(org.andromda.translation.ocl.node.TLBracket)
     */
    public void caseTLBracket(TLBracket tlBracket)
    {
        write("[");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTLParen(org.andromda.translation.ocl.node.TLParen)
     */
    public void caseTLParen(TLParen tlParen)
    {
        write("(");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTRBrace(org.andromda.translation.ocl.node.TRBrace)
     */
    public void caseTRBrace(TRBrace trBrace)
    {
        write("}");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTRBracket(org.andromda.translation.ocl.node.TRBracket)
     */
    public void caseTRBracket(TRBracket trBracket)
    {
        write("]");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTRParen(org.andromda.translation.ocl.node.TRParen)
     */
    public void caseTRParen(TRParen trParen)
    {
        write(")");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTContext(org.andromda.translation.ocl.node.TContext)
     */
    public void caseTContext(TContext tContext)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTBoolean(org.andromda.translation.ocl.node.TBoolean)
     */
    public void caseTBoolean(TBoolean tBoolean)
    {
        write(tBoolean.getText());
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTApostrophe(org.andromda.translation.ocl.node.TApostrophe)
     */
    public void caseTApostrophe(TApostrophe tApostrophe)
    {
        write("\'");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTBlank(org.andromda.translation.ocl.node.TBlank)
     */
    public void caseTBlank(TBlank tBlank)
    {
        write(" ");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTCollection(org.andromda.translation.ocl.node.TCollection)
     */
    public void caseTCollection(TCollection tCollection)
    {
        write("java.util.Collection ");
    }

    /**
     * @param tSingleLineComment
     */
    public void caseTComment(TSingleLineComment tSingleLineComment)
    {
        write("// ");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTEndif(org.andromda.translation.ocl.node.TEndif)
     */
    public void caseTEndif(TEndif tEndif)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTAttr(org.andromda.translation.ocl.node.TAttr)
     */
    public void caseTAttr(TAttr tAttr)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTBag(org.andromda.translation.ocl.node.TBag)
     */
    public void caseTBag(TBag tBag)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTBar(org.andromda.translation.ocl.node.TBar)
     */
    public void caseTBar(TBar tBar)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTBody(org.andromda.translation.ocl.node.TBody)
     */
    public void caseTBody(TBody tBody)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTCommercialAt(org.andromda.translation.ocl.node.TCommercialAt)
     */
    public void caseTCommercialAt(TCommercialAt tCommercialAt)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTDerive(org.andromda.translation.ocl.node.TDerive)
     */
    public void caseTDerive(TDerive tDerive)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTEndpackage(org.andromda.translation.ocl.node.TEndpackage)
     */
    public void caseTEndpackage(TEndpackage tEndpackage)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTEnum(org.andromda.translation.ocl.node.TEnum)
     */
    public void caseTEnum(TEnum tEnum)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTIn(org.andromda.translation.ocl.node.TIn)
     */
    public void caseTIn(TIn tIn)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTInit(org.andromda.translation.ocl.node.TInit)
     */
    public void caseTInit(TInit tInit)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTInt(org.andromda.translation.ocl.node.TInt)
     */
    public void caseTInt(TInt tInt)
    {
        write(tInt.getText());
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTIsSentOperator(org.andromda.translation.ocl.node.TIsSentOperator)
     */
    public void caseTIsSentOperator(TIsSentOperator tIsSentOperator)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTMessageOperator(org.andromda.translation.ocl.node.TMessageOperator)
     */
    public void caseTMessageOperator(TMessageOperator tMessageOperator)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTNewLine(org.andromda.translation.ocl.node.TNewLine)
     */
    public void caseTNewLine(TNewLine tNewLine)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTOper(org.andromda.translation.ocl.node.TOper)
     */
    public void caseTOper(TOper tOper)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTOrderedset(org.andromda.translation.ocl.node.TOrderedset)
     */
    public void caseTOrderedset(TOrderedset tOrderedset)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTPackage(org.andromda.translation.ocl.node.TPackage)
     */
    public void caseTPackage(TPackage tPackage)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTPost(org.andromda.translation.ocl.node.TPost)
     */
    public void caseTPost(TPost tPost)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTPre(org.andromda.translation.ocl.node.TPre)
     */
    public void caseTPre(TPre tPre)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTArrow(org.andromda.translation.ocl.node.TArrow)
     */
    public void caseTArrow(TArrow tArrow)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTIf(org.andromda.translation.ocl.node.TIf)
     */
    public void caseTIf(TIf tIf)
    {
        write("if");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTElse(org.andromda.translation.ocl.node.TElse)
     */
    public void caseTElse(TElse tElse)
    {
        write("else");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTThen(org.andromda.translation.ocl.node.TThen)
     */
    public void caseTThen(TThen tThen)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTRange(org.andromda.translation.ocl.node.TRange)
     */
    public void caseTRange(TRange tRange)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTReal(org.andromda.translation.ocl.node.TReal)
     */
    public void caseTReal(TReal tReal)
    {
        write(tReal.getText());
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTComma(org.andromda.translation.ocl.node.TComma)
     */
    public void caseTComma(TComma tComma)
    {
        write(", ");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTDot(org.andromda.translation.ocl.node.TDot)
     */
    public void caseTDot(TDot tDot)
    {
        write(".");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTSemicolon(org.andromda.translation.ocl.node.TSemicolon)
     */
    public void caseTSemicolon(TSemicolon tSemicolon)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTUnknown(org.andromda.translation.ocl.node.TUnknown)
     */
    public void caseTUnknown(TUnknown tUnknown)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTScopeOperator(org.andromda.translation.ocl.node.TScopeOperator)
     */
    public void caseTScopeOperator(TScopeOperator tScopeOperator)
    {
        write(".");
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTSequence(org.andromda.translation.ocl.node.TSequence)
     */
    public void caseTSequence(TSequence tSequence)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTSet(org.andromda.translation.ocl.node.TSet)
     */
    public void caseTSet(TSet tSet)
    {
    }

    /**
     * @param tStringLit 
     * TODO: this method very naively replaces every single quote by a double quote, this should be updated
     */
    public void caseTStringLit(TStringLit tStringLit)
    {
        final StringBuffer buffer = new StringBuffer(tStringLit.getText().replace('\'', '\"'));
        write(buffer);
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTTab(org.andromda.translation.ocl.node.TTab)
     */
    public void caseTTab(TTab tTab)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTTuple(org.andromda.translation.ocl.node.TTuple)
     */
    public void caseTTuple(TTuple tTuple)
    {
    }

    /**
     * @see org.andromda.translation.ocl.analysis.AnalysisAdapter#caseTTupletype(org.andromda.translation.ocl.node.TTupletype)
     */
    public void caseTTupletype(TTupletype tTupletype)
    {
    }

    private final Stack translationLayers = new Stack();

    /**
     * Contains Boolean.TRUE on the top when the most recent property call was an arrow property call, contains
     * Boolean.FALSE otherwise.
     */
    private final Stack arrowPropertyCallStack = new Stack();

    /**
     * This stack contains elements implementing the Map interface. For each definition of variables a new Map element
     * will be pushed onto the stack. This element contains the variables defined in the definition.
     * <p/>
     * The keys and values contained in the Map are the names of the variables only (String instances).
     */
    private final Stack letVariableStack = new Stack();

    private void write(Object object)
    {
        appendToTranslationLayer(String.valueOf(object));
    }

    private StringBuffer newTranslationLayer()
    {
        return (StringBuffer) translationLayers.push(new StringBuffer());
    }

    private StringBuffer appendToTranslationLayer(Object appendix)
    {
        return ((StringBuffer) translationLayers.peek()).append(appendix);
    }

    private StringBuffer prependToTranslationLayer(Object appendix)
    {
        return ((StringBuffer) translationLayers.peek()).insert(0, appendix);
    }

    private StringBuffer mergeTranslationLayerAfter()
    {
        StringBuffer newTop = null;

        if (translationLayers.size() > 1)
        {
            newTop = appendToTranslationLayer(translationLayers.pop());
        }

        return newTop;
    }

    private StringBuffer mergeTranslationLayerBefore()
    {
        StringBuffer newTop = null;

        if (translationLayers.size() > 1)
        {
            newTop = prependToTranslationLayer(translationLayers.pop());
        }

        return newTop;
    }

    private StringBuffer mergeTranslationLayers()
    {
        while (mergeTranslationLayerAfter() != null) ;
        return (StringBuffer) translationLayers.peek();
    }

    private String getDeclaredLetVariableValue(String variableName)
    {
        for (final Iterator iterator = letVariableStack.iterator(); iterator.hasNext();)
        {
            Map variableMap = (Map) iterator.next();
            if (variableMap.containsKey(variableName))
            {
                return (String) variableMap.get(variableName);
            }
        }
        return null;
    }

    private void newLetVariableContext()
    {
        letVariableStack.push(new HashMap(4));
    }

    private void dropLetVariableContext()
    {
        if (!letVariableStack.isEmpty())
            letVariableStack.pop();
    }

    private void addLetVariableToContext(String variableName, String variableValue)
    {
        ((Map) letVariableStack.peek()).put(variableName, variableValue);
    }

    /**
     * Gets the current context element as a {@link org.andromda.uml.metafacades.ModelElementFacade}.
     *
     * @return the context element as a model element facade
     */
    private ModelElementFacade getModelElement()
    {
        return (ModelElementFacade) this.getContextElement();
    }

    /**
     * 
     */
    public ValidationJavaTranslator()
    {
        arrowPropertyCallStack.push(Boolean.FALSE);
    }

    /**
     * The name of the context element within the translated expression.
     */
    private static final String CONTEXT_ELEMENT_NAME = "contextElement";

    /**
     * The prefix for calling the OCLIntrospector to invoke a property or method on an element.
     */
    private static final String OCL_INTROSPECTOR_INVOKE_PREFIX = OCL_TRANSLATOR_PACKAGE + ".OCLIntrospector.invoke(";

    /**
     * The prefix for converting expressions to boolean expressions
     */
    private static final String BOOLEAN_WRAP_PREFIX = "Boolean.valueOf(String.valueOf(";

    /**
     * The suffix for converting expressions to boolean expressions;
     */
    private static final String BOOLEAN_WRAP_SUFFIX = ")).booleanValue()";

    /**
     * We need to wrap every expression with a converter so that any expressions that return just objects are converted
     * to boolean values.
     *
     * @see org.andromda.translation.ocl.BaseTranslator#postProcess()
     */
    public void postProcess()
    {
        this.getExpression().insertInTranslatedExpression(0, OCL_TRANSLATOR_PACKAGE + ".OCLResultEnsurer.ensure(");
        this.getExpression().insertInTranslatedExpression(0, "boolean constraintValid = ");
        this.getExpression().insertInTranslatedExpression(0,
                "final Object " + CONTEXT_ELEMENT_NAME + " = this; ");
        this.getExpression().appendToTranslatedExpression(");");
    }
}