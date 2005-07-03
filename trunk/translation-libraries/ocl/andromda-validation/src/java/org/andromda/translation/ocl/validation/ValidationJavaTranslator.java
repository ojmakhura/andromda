package org.andromda.translation.ocl.validation;

import org.andromda.core.engine.ModelProcessorException;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.translation.ocl.BaseTranslator;
import org.andromda.translation.ocl.node.AActualParameterList;
import org.andromda.translation.ocl.node.AArrowPropertyCallExpressionTail;
import org.andromda.translation.ocl.node.AAttributeOrAssociationContextDeclaration;
import org.andromda.translation.ocl.node.ABodyOperationStereotype;
import org.andromda.translation.ocl.node.AClassifierContextDeclaration;
import org.andromda.translation.ocl.node.AContextDeclaration;
import org.andromda.translation.ocl.node.ADefClassifierExpressionBody;
import org.andromda.translation.ocl.node.ADotPropertyCallExpressionTail;
import org.andromda.translation.ocl.node.AEqualExpression;
import org.andromda.translation.ocl.node.AFeatureCall;
import org.andromda.translation.ocl.node.AFeatureCallParameters;
import org.andromda.translation.ocl.node.AFeaturePrimaryExpression;
import org.andromda.translation.ocl.node.AIfExpression;
import org.andromda.translation.ocl.node.AImpliesLogicalOperator;
import org.andromda.translation.ocl.node.AInvClassifierExpressionBody;
import org.andromda.translation.ocl.node.ALetExp;
import org.andromda.translation.ocl.node.ALetVariableDeclaration;
import org.andromda.translation.ocl.node.ALogicalExp;
import org.andromda.translation.ocl.node.ALogicalExpressionTail;
import org.andromda.translation.ocl.node.AMessageExpression;
import org.andromda.translation.ocl.node.ANotUnaryOperator;
import org.andromda.translation.ocl.node.AOperationContextDeclaration;
import org.andromda.translation.ocl.node.APathName;
import org.andromda.translation.ocl.node.APostOperationStereotype;
import org.andromda.translation.ocl.node.APreOperationStereotype;
import org.andromda.translation.ocl.node.APropertyCallExpression;
import org.andromda.translation.ocl.node.ARelationalExpression;
import org.andromda.translation.ocl.node.ARelationalExpressionTail;
import org.andromda.translation.ocl.node.ATypeDeclaration;
import org.andromda.translation.ocl.node.AUnaryExpression;
import org.andromda.translation.ocl.node.AVariableDeclaration;
import org.andromda.translation.ocl.node.AVariableDeclarationLetExpSub;
import org.andromda.translation.ocl.node.AVariableDeclarationList;
import org.andromda.translation.ocl.node.AVariableDeclarationListTail;
import org.andromda.translation.ocl.node.Node;
import org.andromda.translation.ocl.node.PAttributeOrAssociationExpressionBody;
import org.andromda.translation.ocl.node.PClassifierExpressionBody;
import org.andromda.translation.ocl.node.PContextDeclaration;
import org.andromda.translation.ocl.node.POperationExpressionBody;
import org.andromda.translation.ocl.node.PPropertyCallExpressionTail;
import org.andromda.translation.ocl.node.PVariableDeclarationListTail;
import org.andromda.translation.ocl.node.TAnd;
import org.andromda.translation.ocl.node.TApostrophe;
import org.andromda.translation.ocl.node.TArrow;
import org.andromda.translation.ocl.node.TAttr;
import org.andromda.translation.ocl.node.TBag;
import org.andromda.translation.ocl.node.TBar;
import org.andromda.translation.ocl.node.TBlank;
import org.andromda.translation.ocl.node.TBody;
import org.andromda.translation.ocl.node.TBoolean;
import org.andromda.translation.ocl.node.TCollection;
import org.andromda.translation.ocl.node.TColon;
import org.andromda.translation.ocl.node.TComma;
import org.andromda.translation.ocl.node.TCommercialAt;
import org.andromda.translation.ocl.node.TContext;
import org.andromda.translation.ocl.node.TDef;
import org.andromda.translation.ocl.node.TDerive;
import org.andromda.translation.ocl.node.TDiv;
import org.andromda.translation.ocl.node.TDot;
import org.andromda.translation.ocl.node.TElse;
import org.andromda.translation.ocl.node.TEndif;
import org.andromda.translation.ocl.node.TEndpackage;
import org.andromda.translation.ocl.node.TEnum;
import org.andromda.translation.ocl.node.TEqual;
import org.andromda.translation.ocl.node.TGt;
import org.andromda.translation.ocl.node.TGteq;
import org.andromda.translation.ocl.node.TIf;
import org.andromda.translation.ocl.node.TImplies;
import org.andromda.translation.ocl.node.TIn;
import org.andromda.translation.ocl.node.TInit;
import org.andromda.translation.ocl.node.TInt;
import org.andromda.translation.ocl.node.TInv;
import org.andromda.translation.ocl.node.TIsSentOperator;
import org.andromda.translation.ocl.node.TLBrace;
import org.andromda.translation.ocl.node.TLBracket;
import org.andromda.translation.ocl.node.TLParen;
import org.andromda.translation.ocl.node.TLet;
import org.andromda.translation.ocl.node.TLt;
import org.andromda.translation.ocl.node.TLteq;
import org.andromda.translation.ocl.node.TMessageOperator;
import org.andromda.translation.ocl.node.TMinus;
import org.andromda.translation.ocl.node.TMult;
import org.andromda.translation.ocl.node.TName;
import org.andromda.translation.ocl.node.TNewLine;
import org.andromda.translation.ocl.node.TNot;
import org.andromda.translation.ocl.node.TNotEqual;
import org.andromda.translation.ocl.node.TOper;
import org.andromda.translation.ocl.node.TOr;
import org.andromda.translation.ocl.node.TOrderedset;
import org.andromda.translation.ocl.node.TPackage;
import org.andromda.translation.ocl.node.TPlus;
import org.andromda.translation.ocl.node.TPost;
import org.andromda.translation.ocl.node.TPre;
import org.andromda.translation.ocl.node.TRBrace;
import org.andromda.translation.ocl.node.TRBracket;
import org.andromda.translation.ocl.node.TRParen;
import org.andromda.translation.ocl.node.TRange;
import org.andromda.translation.ocl.node.TReal;
import org.andromda.translation.ocl.node.TScopeOperator;
import org.andromda.translation.ocl.node.TSemicolon;
import org.andromda.translation.ocl.node.TSequence;
import org.andromda.translation.ocl.node.TSet;
import org.andromda.translation.ocl.node.TSingleLineComment;
import org.andromda.translation.ocl.node.TStringLit;
import org.andromda.translation.ocl.node.TTab;
import org.andromda.translation.ocl.node.TThen;
import org.andromda.translation.ocl.node.TTuple;
import org.andromda.translation.ocl.node.TTupletype;
import org.andromda.translation.ocl.node.TUnknown;
import org.andromda.translation.ocl.node.TXor;
import org.andromda.translation.ocl.syntax.ConcreteSyntaxUtils;
import org.andromda.translation.ocl.syntax.OCLFeatures;
import org.andromda.translation.ocl.syntax.OCLPatterns;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

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
                throw new ModelProcessorException("Could not load file --> '" + featuresUri + "'");
            }
            features = new Properties();
            InputStream stream = featuresUri.openStream();
            features.load(stream);
            stream.close();
            stream = null;
        }
        catch (Throwable th)
        {
            throw new ValidationTranslatorException(th);
        }
    }

    /**
     * The package to which the OCL translator classes belong.
     */
    private static final String OCL_TRANSLATOR_PACKAGE = "org.andromda.translation.ocl.validation";

    /**
     * This is the start of a new constraint. We prepare everything by resetting and initializing the required objects.
     */
    public void caseAContextDeclaration(AContextDeclaration node)
    {
        newTranslationLayer();
        {
            Object temp[] = node.getContextDeclaration().toArray();
            for (int ctr = 0; ctr < temp.length; ctr++)
            {
                ((PContextDeclaration)temp[ctr]).apply(this);
            }
        }
        mergeTranslationLayers();
        this.getExpression().appendToTranslatedExpression(translationLayers.peek());
        translationLayers.clear();
    }

    public void caseAClassifierContextDeclaration(AClassifierContextDeclaration node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inAClassifierContextDeclaration(node);
        Object temp[] = node.getClassifierExpressionBody().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PClassifierExpressionBody)temp[ctr]).apply(this);
    }

    public void caseAOperationContextDeclaration(AOperationContextDeclaration node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inAOperationContextDeclaration(node);
        Object temp[] = node.getOperationExpressionBody().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((POperationExpressionBody)temp[ctr]).apply(this);
    }

    public void caseAAttributeOrAssociationContextDeclaration(AAttributeOrAssociationContextDeclaration node)
    {
        super.inAAttributeOrAssociationContextDeclaration(node);
        Object temp[] = node.getAttributeOrAssociationExpressionBody().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PAttributeOrAssociationExpressionBody)temp[ctr]).apply(this);
    }

    public void caseAInvClassifierExpressionBody(AInvClassifierExpressionBody node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inAInvClassifierExpressionBody(node);
        node.getExpression().apply(this);
    }

    public void caseADefClassifierExpressionBody(ADefClassifierExpressionBody node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inADefClassifierExpressionBody(node);
        node.getDefinitionExpression().apply(this);
    }

    /**
     * We need to keep track that what follows is in the scope of an arrow feature call, this is important because it
     * means it is a feature that is implied by the OCL language, rather than the model on which the constraint
     * applies.
     */
    public void inAArrowPropertyCallExpressionTail(AArrowPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.push(Boolean.TRUE);
    }

    /**
     * Undo the arrow feature call trace.
     */
    public void outAArrowPropertyCallExpressionTail(AArrowPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.pop();
    }

    /**
     * This indicates we have entered a feature call, we need to mark this to counterpart any previous arrow feature
     * call flags.
     */
    public void inADotPropertyCallExpressionTail(ADotPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.push(Boolean.FALSE);
    }

    /**
     * Undo the dot feature call trace.
     */
    public void outADotPropertyCallExpressionTail(ADotPropertyCallExpressionTail node)
    {
        this.arrowPropertyCallStack.pop();
    }

    /**
     * Here we need to make sure the equals sign '=' is not translated into the 'equal' keyword. OCL uses '=' for
     * comparison as well as for assignment, Java uses '==', '=' and .equals() so we override the default OCL value here
     * to use '=' instead of 'equal'
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
     */
    public void inALetVariableDeclaration(ALetVariableDeclaration node)
    {
        newTranslationLayer(); // this layer will be disposed later on, we do
        // not write variable declarations

        AVariableDeclaration variableDeclaration = (AVariableDeclaration)node.getVariableDeclaration();
        String variableName = variableDeclaration.getName().getText();

        newTranslationLayer();
        node.getExpression().apply(this);

        String variableValue = translationLayers.pop().toString();

        addLetVariableToContext(variableName, variableValue);
    }

    /**
     * In Java we need to end the declaration statement with a semicolon, this is handled here.
     */
    public void outALetVariableDeclaration(ALetVariableDeclaration node)
    {
        write(";");
        translationLayers.pop();
    }

    /**
     * Renders a variable declaration. Missing types will imply the java.lang.Object type.
     */
    public void caseAVariableDeclaration(AVariableDeclaration node)
    {
        if (node.getTypeDeclaration() == null)
            write("java.lang.Object");
        else
            node.getTypeDeclaration().apply(this);

        write(" "); // we need to add a space between the type and the name

        node.getName().apply(this);
    }

    public void caseATypeDeclaration(ATypeDeclaration node)
    {
        node.getType().apply(this);
    }

    public void caseAVariableDeclarationList(AVariableDeclarationList node)
    {
        node.getVariableDeclaration().apply(this);

        if (node.getVariableDeclarationValue() != null)
            node.getVariableDeclarationValue().apply(this);

        Object temp[] = node.getVariableDeclarationListTail().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PVariableDeclarationListTail)temp[ctr]).apply(this);
    }

    public void caseAVariableDeclarationListTail(AVariableDeclarationListTail node)
    {
        node.getComma().apply(this);
        node.getVariableDeclaration().apply(this);

        if (node.getVariableDeclarationValue() != null)
            node.getVariableDeclarationValue().apply(this);
    }

    public void caseAEqualExpression(AEqualExpression node)
    {
        node.getEqual().apply(this);
        node.getExpression().apply(this);
    }

    public void caseABodyOperationStereotype(ABodyOperationStereotype node)
    {}

    public void caseAPreOperationStereotype(APreOperationStereotype node)
    {}

    public void caseAPostOperationStereotype(APostOperationStereotype node)
    {}

    public void caseAMessageExpression(AMessageExpression node)
    {}

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

    public void caseAPropertyCallExpression(APropertyCallExpression node)
    {
        newTranslationLayer();
        node.getPrimaryExpression().apply(this);
        Object temp[] = node.getPropertyCallExpressionTail().toArray();
        for (int ctr = 0; ctr < temp.length; ctr++)
            ((PPropertyCallExpressionTail)temp[ctr]).apply(this);
        mergeTranslationLayerAfter();
    }

    public void caseADotPropertyCallExpressionTail(ADotPropertyCallExpressionTail node)
    {
        inADotPropertyCallExpressionTail(node);
        String expression = TranslationUtils.trimToEmpty(node);
        // we prepend an introspection call if the expression is
        // an operation call
        if (OCLPatterns.isOperation(expression))
        {
            AFeatureCall featureCall = (AFeatureCall)node.getFeatureCall();
            String featureCallExpression = TranslationUtils.trimToEmpty(node.getFeatureCall());
            if (OCLFeatures.isOclIsKindOf(featureCallExpression))
            {
                this.handleOclIsKindOf(featureCall);
            }
            else if (OCLFeatures.isOclIsTypeOf(featureCallExpression))
            {
                this.handleOclIsTypeOf(featureCall);
            }
            else if (OCLFeatures.isConcat(featureCallExpression))
            {
                this.handleConcat(featureCall);
            }
            else
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
            type = ConcreteSyntaxUtils.getParametersAsString((AFeatureCall)node);
        }
        else if (node instanceof AFeatureCallParameters)
        {
            type = ConcreteSyntaxUtils.getParametersAsString((AFeatureCallParameters)node);
        }
        if (type != null)
        {
            type = type.replaceAll("\\s*::\\s*", ".");
            // if we don't have a package define, attempt to find the model
            // element
            // in the same package as the context element.
            if (type.indexOf(".") == -1)
            {
                if (this.getModelElement() != null)
                {
                    type = this.getModelElement().getPackageName() + "." + type;
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

    public void caseAArrowPropertyCallExpressionTail(AArrowPropertyCallExpressionTail node)
    {
        inAArrowPropertyCallExpressionTail(node);
        node.getArrow().apply(this);
        this.handleArrowFeatureCall((AFeatureCall)node.getFeatureCall());
        outAArrowPropertyCallExpressionTail(node);
    }

    /**
     * @see org.andromda.translation.ocl.BaseTranslator#isOperationArgument(java.lang.String)
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
        return StringUtils.trimToEmpty(navigationalPath).replaceAll(".*\\.", "");
    }

    /**
     * @todo: improve implementation to reduce the code duplication (avoid having two write statements)
     */
    public void caseAFeaturePrimaryExpression(AFeaturePrimaryExpression node)
    {
        inAFeaturePrimaryExpression(node);
        if (node.getPathName() != null)
        {
            final String variableName = ((APathName)node.getPathName()).getName().getText();
            final String variableValue = getDeclaredLetVariableValue(variableName);
            final boolean isDeclaredAsLetVariable = (variableValue != null);

            String featureExpression = TranslationUtils.deleteWhitespace(node);
            if (isDeclaredAsLetVariable)
            {
                write(variableValue);
            }
            else if (node.getFeatureCallParameters() == null || OCLPatterns.isOperation(featureExpression))
            {
                APropertyCallExpression expression = (APropertyCallExpression)node.parent();
                String expressionAsString = ConcreteSyntaxUtils.getPrimaryExpression(expression);
                // remove any references to 'self.' as we write
                expressionAsString = expressionAsString.replaceAll("self\\.", "");
                if (OCLFeatures.isSelf(expressionAsString))
                {
                    write(CONTEXT_ELEMENT_NAME);
                }
                else if (StringUtils.isNotBlank(expressionAsString))
                {
                    boolean convertToBoolean = false;
                    if (node.parent().parent() instanceof AUnaryExpression)
                    {
                        AUnaryExpression unaryExpression = (AUnaryExpression)node.parent().parent();
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
                    }
                    else if (OCLFeatures.isOclIsTypeOf(expressionAsString))
                    {
                        this.write("object");
                        this.handleOclIsTypeOf(node.getFeatureCallParameters());
                    }
                    else
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
                            // if the expression is an argument, then 
                            // that becomes the invoked object
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
            }
            else
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
        AFeatureCallParameters params = (AFeatureCallParameters)featureCall.getFeatureCallParameters();
        AActualParameterList list = null;
        if (params != null)
        {
            list = (AActualParameterList)params.getActualParameterList();
        }
        boolean arrow = this.arrowPropertyCallStack.peek().equals(Boolean.TRUE) &&
                !String.valueOf(list).trim().equals("");
        {
            newTranslationLayer();
            final String navigationalPath = ConcreteSyntaxUtils.getArrowFeatureCallResultNavigationalPath(
                    (APropertyCallExpression)featureCall.parent().parent());
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
            AFeatureCallParameters parameters = (AFeatureCallParameters)featureCall.getFeatureCallParameters();
            if (parameters != null)
            {
                if (parameters.getLParen() != null)
                {
                    parameters.getLParen().apply(this);
                }
                mergeTranslationLayerBefore();
                AActualParameterList parameterList = (AActualParameterList)parameters.getActualParameterList();
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
                        Node expression = (Node)expressions.get(ctr);
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
     */
    public void inALetExp(ALetExp node)
    {
        newLetVariableContext();
    }

    /**
     * The variables are out of scope, we need to purge their context.
     */
    public void outALetExp(ALetExp node)
    {
        dropLetVariableContext();
    }

    public void caseAVariableDeclarationLetExpSub(AVariableDeclarationLetExpSub node)
    {
        node.getComma().apply(this);
        node.getLetVariableDeclaration().apply(this);
        node.getLetExpSub().apply(this);
    }

    public void caseALogicalExp(ALogicalExp node)
    {
        newTranslationLayer();
        if (node.getRelationalExpression() != null)
        {
            node.getRelationalExpression().apply(this);
        }
        Object tails[] = node.getLogicalExpressionTail().toArray();
        for (int ctr = 0; ctr < tails.length; ctr++)
        {
            ((ALogicalExpressionTail)tails[ctr]).apply(this);
        }
        mergeTranslationLayerAfter();
    }

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
                List tails = ((ALogicalExp)parent).getLogicalExpressionTail();
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
            }
            else if (parent instanceof ALogicalExpressionTail)
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

    public void outARelationalExpression(ARelationalExpression node)
    {
        mergeTranslationLayerAfter();
    }

    public void caseTName(TName node)
    {
        write(node.getText());
    }

    public void caseTAnd(TAnd tAnd)
    {
        write("&&");
    }

    public void caseTOr(TOr tOr)
    {
        write("||");
    }

    public void caseTXor(TXor tXor)
    {
        write("^");
    }

    public void caseTImplies(TImplies tImplies)
    {
        // convert any non boolean's to boolean
        this.prependToTranslationLayer(BOOLEAN_WRAP_PREFIX);
        this.appendToTranslationLayer(BOOLEAN_WRAP_SUFFIX);
        write("?");
    }

    public void caseTNot(TNot tNot)
    {
        write("!");
    }

    public void caseTPlus(TPlus tPlus)
    {
        write("+");
    }

    public void caseTMinus(TMinus tMinus)
    {
        write("-");
    }

    public void caseTMult(TMult tMult)
    {
        write("*");
    }

    public void caseTDiv(TDiv tDiv)
    {
        write("/");
    }

    public void caseTEqual(TEqual tEqual)
    {
        write("equal");
    }

    public void caseTNotEqual(TNotEqual tNotEqual)
    {
        write("notEqual");
    }

    public void caseTLt(TLt tLt)
    {
        write("less");
    }

    public void caseTLteq(TLteq tLteq)
    {
        write("lessOrEqual");
    }

    public void caseTGt(TGt tGt)
    {
        write("greater");
    }

    public void caseTGteq(TGteq tGteq)
    {
        write("greaterOrEqual");
    }

    public void caseTInv(TInv tInv)
    {}

    public void caseTDef(TDef tDef)
    {}

    public void caseTLet(TLet tLet)
    {}

    public void caseTColon(TColon tColon)
    {}

    public void caseTLBrace(TLBrace tlBrace)
    {
        write("{");
    }

    public void caseTLBracket(TLBracket tlBracket)
    {
        write("[");
    }

    public void caseTLParen(TLParen tlParen)
    {
        write("(");
    }

    public void caseTRBrace(TRBrace trBrace)
    {
        write("}");
    }

    public void caseTRBracket(TRBracket trBracket)
    {
        write("]");
    }

    public void caseTRParen(TRParen trParen)
    {
        write(")");
    }

    public void caseTContext(TContext tContext)
    {}

    public void caseTBoolean(TBoolean tBoolean)
    {
        write(tBoolean.getText());
    }

    public void caseTApostrophe(TApostrophe tApostrophe)
    {
        write("\'");
    }

    public void caseTBlank(TBlank tBlank)
    {
        write(" ");
    }

    public void caseTCollection(TCollection tCollection)
    {
        write("java.util.Collection ");
    }

    public void caseTComment(TSingleLineComment tSingleLineComment)
    {
        write("// ");
    }

    public void caseTEndif(TEndif tEndif)
    {}

    public void caseTAttr(TAttr tAttr)
    {}

    public void caseTBag(TBag tBag)
    {}

    public void caseTBar(TBar tBar)
    {}

    public void caseTBody(TBody tBody)
    {}

    public void caseTCommercialAt(TCommercialAt tCommercialAt)
    {}

    public void caseTDerive(TDerive tDerive)
    {}

    public void caseTEndpackage(TEndpackage tEndpackage)
    {}

    public void caseTEnum(TEnum tEnum)
    {}

    public void caseTIn(TIn tIn)
    {}

    public void caseTInit(TInit tInit)
    {}

    public void caseTInt(TInt tInt)
    {
        write(tInt.getText());
    }

    public void caseTIsSentOperator(TIsSentOperator tIsSentOperator)
    {}

    public void caseTMessageOperator(TMessageOperator tMessageOperator)
    {}

    public void caseTNewLine(TNewLine tNewLine)
    {}

    public void caseTOper(TOper tOper)
    {}

    public void caseTOrderedset(TOrderedset tOrderedset)
    {}

    public void caseTPackage(TPackage tPackage)
    {}

    public void caseTPost(TPost tPost)
    {}

    public void caseTPre(TPre tPre)
    {}

    public void caseTArrow(TArrow tArrow)
    {}

    public void caseTIf(TIf tIf)
    {
        write("if");
    }

    public void caseTElse(TElse tElse)
    {
        write("else");
    }

    public void caseTThen(TThen tThen)
    {}

    public void caseTRange(TRange tRange)
    {}

    public void caseTReal(TReal tReal)
    {
        write(tReal.getText());
    }

    public void caseTComma(TComma tComma)
    {
        write(", ");
    }

    public void caseTDot(TDot tDot)
    {
        write(".");
    }

    public void caseTSemicolon(TSemicolon tSemicolon)
    {}

    public void caseTUnknown(TUnknown tUnknown)
    {}

    public void caseTScopeOperator(TScopeOperator tScopeOperator)
    {
        write(".");
    }

    public void caseTSequence(TSequence tSequence)
    {}

    public void caseTSet(TSet tSet)
    {}

    /**
     * @todo: this method very naively replaces every single quote by a double quote, this should be updated
     */
    public void caseTStringLit(TStringLit tStringLit)
    {
        final StringBuffer buffer = new StringBuffer(tStringLit.getText().replace('\'', '\"'));
        write(buffer);
    }

    public void caseTTab(TTab tTab)
    {}

    public void caseTTuple(TTuple tTuple)
    {}

    public void caseTTupletype(TTupletype tTupletype)
    {}

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
        return (StringBuffer)translationLayers.push(new StringBuffer());
    }

    private StringBuffer appendToTranslationLayer(Object appendix)
    {
        return ((StringBuffer)translationLayers.peek()).append(appendix);
    }

    private StringBuffer prependToTranslationLayer(Object appendix)
    {
        return ((StringBuffer)translationLayers.peek()).insert(0, appendix);
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
        return (StringBuffer)translationLayers.peek();
    }

    private String getDeclaredLetVariableValue(String variableName)
    {
        for (final Iterator iterator = letVariableStack.iterator(); iterator.hasNext();)
        {
            Map variableMap = (Map)iterator.next();
            if (variableMap.containsKey(variableName))
            {
                return (String)variableMap.get(variableName);
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
        ((Map)letVariableStack.peek()).put(variableName, variableValue);
    }

    /**
     * Gets the current context element as a {@link org.andromda.uml.metafacades.ModelElementFacade}.
     *
     * @return the context element as a model element facade
     */
    private ModelElementFacade getModelElement()
    {
        return (ModelElementFacade)this.getContextElement();
    }

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
                "final java.lang.Object " + CONTEXT_ELEMENT_NAME + " = this; ");
        this.getExpression().appendToTranslatedExpression(");");

    }
}