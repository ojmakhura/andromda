package org.andromda.translation.validation;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import org.andromda.core.ModelProcessorException;
import org.andromda.core.translation.BaseTranslator;
import org.andromda.core.translation.TranslationUtils;
import org.andromda.core.translation.node.*;
import org.andromda.core.translation.syntax.impl.ConcreteSyntaxUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Provides translation of OCL validation constraints to the Java language.
 * </p>
 * 
 * @author Wouter Zoons
 * @author Chad Brandon
 */
public class ValidationJavaTranslator
    extends BaseTranslator
{

    private static Properties features = null;

    /**
     * The prefix for converting expressions to boolean expressions
     */
    private static final String BOOLEAN_WRAP_PREFIX = "Boolean.valueOf(String.valueOf(";

    /**
     * The suffix for converting expressions to boolean expressions;
     */
    private static final String BOOLEAN_WRAP_SUFFIX = ")).booleanValue()";

    static
    {
        try
        {
            URL featuresUri = ValidationJavaTranslator.class
                .getResource("features.properties");
            if (featuresUri == null)
            {
                throw new ModelProcessorException("Could not load file --> '"
                    + featuresUri + "'");
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
     * This is the start of a new constraint. We prepare everything by resetting
     * and initializing the required objects.
     */
    public void caseAContextDeclaration(AContextDeclaration node)
    {
        newTranslationLayer();
        {
            Object temp[] = node.getContextDeclaration().toArray();
            for (int i = 0; i < temp.length; i++)
            {
                ((PContextDeclaration)temp[i]).apply(this);
            }
        }
        mergeTranslationLayers();
        this.getExpression().appendToTranslatedExpression(
            translationLayers.peek());
        translationLayers.clear();
    }

    public void caseAClassifierContextDeclaration(
        AClassifierContextDeclaration node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inAClassifierContextDeclaration(node);
        Object temp[] = node.getClassifierExpressionBody().toArray();
        for (int i = 0; i < temp.length; i++)
            ((PClassifierExpressionBody)temp[i]).apply(this);
    }

    public void caseAOperationContextDeclaration(
        AOperationContextDeclaration node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inAOperationContextDeclaration(node);
        Object temp[] = node.getOperationExpressionBody().toArray();
        for (int i = 0; i < temp.length; i++)
            ((POperationExpressionBody)temp[i]).apply(this);
    }

    public void caseAAttributeOrAssociationContextDeclaration(
        AAttributeOrAssociationContextDeclaration node)
    {
        Object temp[] = node.getAttributeOrAssociationExpressionBody()
            .toArray();
        for (int i = 0; i < temp.length; i++)
            ((PAttributeOrAssociationExpressionBody)temp[i]).apply(this);
    }

    public void caseAInvClassifierExpressionBody(
        AInvClassifierExpressionBody node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inAInvClassifierExpressionBody(node);
        node.getExpression().apply(this);
    }

    public void caseADefClassifierExpressionBody(
        ADefClassifierExpressionBody node)
    {
        // explicity call super method so
        // that we can set the type of the expression
        super.inADefClassifierExpressionBody(node);
        node.getDefinitionExpression().apply(this);
    }

    /**
     * We need to keep track that what follows is in the scope of an arrow
     * feature call, this is important because it means it is a feature that is
     * implied by the OCL language, rather than the model on which the
     * constraint applies.
     */
    public void inAArrowPropertyCallExpressionTail(
        AArrowPropertyCallExpressionTail node)
    {
        arrowPropertyCallStack.push(Boolean.TRUE);
    }

    /**
     * Undo the arrow feature call trace.
     */
    public void outAArrowPropertyCallExpressionTail(
        AArrowPropertyCallExpressionTail node)
    {
        arrowPropertyCallStack.pop();
    }

    /**
     * This indicates we have entered a feature call, we need to mark this to
     * counterpart any previous arrow feature call flags.
     */
    public void inADotPropertyCallExpressionTail(
        ADotPropertyCallExpressionTail node)
    {
        arrowPropertyCallStack.push(Boolean.FALSE);
    }

    /**
     * Undo the dot feature call trace.
     */
    public void outADotPropertyCallExpressionTail(
        ADotPropertyCallExpressionTail node)
    {
        arrowPropertyCallStack.pop();
    }

    /**
     * Here we need to make sure the equals sign '=' is not translated into the
     * 'equal' keyword. OCL uses '=' for comparison as well as for assignment,
     * Java uses '==', '=' and .equals() so we override the default OCL value
     * here to use '=' instead of 'equal'
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

        AVariableDeclaration variableDeclaration = (AVariableDeclaration)node
            .getVariableDeclaration();
        String variableName = variableDeclaration.getName().getText();

        newTranslationLayer();
        node.getExpression().apply(this);

        String variableValue = translationLayers.pop().toString();

        addLetVariableToContext(variableName, variableValue);
    }

    /**
     * In Java we need to end the declaration statement with a semicolon, this
     * is handled here.
     */
    public void outALetVariableDeclaration(ALetVariableDeclaration node)
    {
        write(";");
        translationLayers.pop();
    }

    /**
     * Renders a variable declaration. Missing types will imply the
     * java.lang.Object type.
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
        for (int i = 0; i < temp.length; i++)
            ((PVariableDeclarationListTail)temp[i]).apply(this);
    }

    public void caseAVariableDeclarationListTail(
        AVariableDeclarationListTail node)
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
        for (int i = 0; i < temp.length; i++)
            ((PPropertyCallExpressionTail)temp[i]).apply(this);
        mergeTranslationLayerAfter();
    }

    public void caseADotPropertyCallExpressionTail(
        ADotPropertyCallExpressionTail node)
    {
        inADotPropertyCallExpressionTail(node);
        String expression = TranslationUtils.trimToEmpty(node);
        // we prepend an introspection call if the expression is
        // an operation call
        if (expression.matches(OCLIntrospector.OPERATION_FEATURE))
        {
            PFeatureCall featureCall = node.getFeatureCall();
            if (featureCall instanceof AStandardFeatureCall)
            {
                this.handleDotStandardFeatureCall((AStandardFeatureCall)node
                    .getFeatureCall());
            }
            else if (featureCall instanceof AOcliskindofFeatureCall)
            {
                this
                    .handleDotOclIsKindOfFeatureCall((AOcliskindofFeatureCall)node
                        .getFeatureCall());
            }
        }
        outADotPropertyCallExpressionTail(node);
    }

    /**
     * oclIsKindOf(type) is a special feature defined by OCL on all objects.
     */
    private void handleDotOclIsKindOfFeatureCall(
        AOcliskindofFeatureCall featureCall)
    {
        write(" instanceof ");
        StringBuffer pattern = new StringBuffer("\\s*");
        pattern.append(PATH_SEPERATOR);
        pattern.append("\\s*");
        write(TranslationUtils.trimToEmpty(featureCall.getPathName())
            .replaceAll(pattern.toString(), "."));
    }

    /**
     * Handles an <strong>dot </strong> feature call. Its expected that this
     * <code>featureCall</code>'s parent is a ADotPropertyCallExpressionTail.
     * This is here because dot feature calls must be handled differently than
     * <code>arrow<code> feature calls.
     *
     * @param featureCall the <strong>dot</strong>
     *        <code>featureCall</code> to handle.
     */
    private void handleDotStandardFeatureCall(AStandardFeatureCall featureCall)
    {
        this
            .prependToTranslationLayer("org.andromda.translation.validation.OCLIntrospector.invoke(");
        String propertyCallExpression = TranslationUtils
            .deleteWhitespace(featureCall.parent().parent());
        if (propertyCallExpression.matches(".*\\s?self\\..*"))
        {
            write(CONTEXT_ELEMENT_NAME);
        }
        this.appendToTranslationLayer(",\"");
        this.appendToTranslationLayer(TranslationUtils
            .deleteWhitespace(featureCall));
        this.appendToTranslationLayer("\"");
        if (featureCall.getFeatureCallParameters() != null)
        {
            List parameters = ConcreteSyntaxUtils.getParameters(featureCall);
            if (parameters != null && !parameters.isEmpty())
            {
                write(",new Object[]{");
                this
                    .appendToTranslationLayer("org.andromda.translation.validation.OCLIntrospector.invoke(");
                this.appendToTranslationLayer(CONTEXT_ELEMENT_NAME);
                this.appendToTranslationLayer(",\"");
                this.appendToTranslationLayer(ConcreteSyntaxUtils
                    .getParameters(featureCall).get(0));
                this.appendToTranslationLayer("\")}");
            }
        }
        this.appendToTranslationLayer(")");
    }

    public void caseAArrowPropertyCallExpressionTail(
        AArrowPropertyCallExpressionTail node)
    {
        inAArrowPropertyCallExpressionTail(node);
        node.getArrow().apply(this);
        this
            .handleArrowFeatureCall((AStandardFeatureCall)node.getFeatureCall());
        outAArrowPropertyCallExpressionTail(node);
    }

    /**
     * @todo: improve implementation to reduce the code duplication (avoid
     *        having two write statements)
     */
    public void caseAFeaturePrimaryExpression(AFeaturePrimaryExpression node)
    {
        inAFeaturePrimaryExpression(node);
        if (node.getPathName() != null)
        {
            final String variableName = ((APathName)node.getPathName())
                .getName().getText();
            final String variableValue = getDeclaredLetVariableValue(variableName);
            final boolean isDeclaredAsLetVariable = (variableValue != null);

            String featureExpression = TranslationUtils.deleteWhitespace(node);
            if (isDeclaredAsLetVariable)
            {
                write(variableValue);
            }
            else if (node.getFeatureCallParameters() == null
                || featureExpression.matches(OCLIntrospector.OPERATION_FEATURE))
            {
                APropertyCallExpression expression = (APropertyCallExpression)node
                    .parent();
                String expressionAsString = ConcreteSyntaxUtils
                    .getPrimaryExpression(expression);
                expressionAsString = expressionAsString.replaceAll(
                    "self\\.|self",
                    "");
                if (StringUtils.isNotBlank(expressionAsString))
                {
                    write("org.andromda.translation.validation.OCLIntrospector.invoke(");
                    String invokedObject = CONTEXT_ELEMENT_NAME;
                    // if we're in an arrow call we assume the invoked object
                    // is the object for which the arrow call applies
                    if (arrowPropertyCallStack.peek().equals(Boolean.TRUE))
                    {
                        invokedObject = "object";
                    }
                    write(invokedObject);
                    write(",\"");
                    // remove any references to 'self.' as we write
                    write(expressionAsString);
                    write("\")");
                    if (this.requiresBooleanWrap)
                    {
                        this.write(BOOLEAN_WRAP_SUFFIX);
                        this.requiresBooleanWrap = false;
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
            if (arrowPropertyCallStack.peek().equals(Boolean.FALSE))
            {
                node.getQualifiers().apply(this);
            }
        }
        outAFeaturePrimaryExpression(node);
    }

    public void inAFeaturePrimaryExpression(AFeaturePrimaryExpression node)
    {}

    /**
     * Handles an <strong>arrow </strong> feature call. Its expected that this
     * <code>featureCall</code>'s parent is a
     * AArrowPropertyCallExpressionTail. This is here because arrow feature
     * calls must be handled differently than <code>dot<code> feature calls.
     *
     * @param featureCall the <strong>arrow</strong>
     *        <code>featureCall</code> to handle.
     */
    public void handleArrowFeatureCall(AStandardFeatureCall featureCall)
    {
        AFeatureCallParameters params = (AFeatureCallParameters)featureCall
            .getFeatureCallParameters();
        AActualParameterList list = (AActualParameterList)params
            .getActualParameterList();
        boolean arrow = arrowPropertyCallStack.peek().equals(Boolean.TRUE)
            && !"".equals(String.valueOf(list).trim());
        {
            newTranslationLayer();
            write("org.andromda.translation.validation.OCLCollections.");
            inAStandardFeatureCall(featureCall);
            if (featureCall.getPathName() != null)
            {
                featureCall.getPathName().apply(this);
            }
            String featureCallName = TranslationUtils.trimToEmpty(featureCall
                .getPathName());
            AFeatureCallParameters parameters = (AFeatureCallParameters)featureCall
                .getFeatureCallParameters();
            if (parameters.getLParen() != null)
            {
                parameters.getLParen().apply(this);
            }
            mergeTranslationLayerBefore();
            AActualParameterList parameterList = (AActualParameterList)parameters
                .getActualParameterList();
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
                    if (arrow)
                        write(";}}");
                }
            }
            if (parameters.getRParen() != null)
            {
                parameters.getRParen().apply(this);
            }
            this.outAStandardFeatureCall(featureCall);
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

    public void caseAVariableDeclarationLetExpSub(
        AVariableDeclarationLetExpSub node)
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
        write("org.andromda.translation.validation.OCLExpressions.");
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
     * A flag indicating if the expression needs to be converted/wrapped in a
     * Java Boolean instance.
     */
    private boolean requiresBooleanWrap = false;

    public void inARelationalExpression(ARelationalExpression node)
    {
        // in this block of code, we determine whether or not
        // the next appended expression needs to be
        // converted/wrapped by a Java Boolean
        if (node.getRelationalExpressionTail() == null)
        {
            Object parent = node.parent();
            String pattern = "[\\w*[\\.|\\s]*]*";
            String expression = null;
            if (parent instanceof ALogicalExp)
            {
                List tails = ((ALogicalExp)parent).getLogicalExpressionTail();
                if (tails != null && !tails.isEmpty())
                {
                    expression = TranslationUtils.trimToEmpty(tails.get(0));
                }
            }
            else if (parent instanceof ALogicalExpressionTail)
            {
                expression = TranslationUtils.trimToEmpty(node);
            }
            requiresBooleanWrap = expression != null
                && expression.matches(pattern);
            if (requiresBooleanWrap)
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
    {}

    public void caseTSequence(TSequence tSequence)
    {}

    public void caseTSet(TSet tSet)
    {}

    /**
     * @todo: this method very naively replaces every single quote by a double
     *        quote, this should be updated
     */
    public void caseTStringLit(TStringLit tStringLit)
    {
        final StringBuffer buffer = new StringBuffer(tStringLit.getText()
            .replace('\'', '\"'));
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
     * Contains Boolean.TRUE on the top when the most recent property call was
     * an arrow property call, contains Boolean.FALSE otherwise.
     */
    private final Stack arrowPropertyCallStack = new Stack();

    /**
     * This stack contains elements implementing the Map interface. For each
     * definition of variables a new Map element will be pushed onto the stack.
     * This element contains the variables defined in the definition.
     * <p>
     * The keys and values contained in the Map are the names of the variables
     * only (String instances).
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
        while (mergeTranslationLayerAfter() != null);
        return (StringBuffer)translationLayers.peek();
    }

    private String getDeclaredLetVariableValue(String variableName)
    {
        for (Iterator iterator = letVariableStack.iterator(); iterator
            .hasNext();)
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

    private void addLetVariableToContext(
        String variableName,
        String variableValue)
    {
        ((Map)letVariableStack.peek()).put(variableName, variableValue);
    }

    public ValidationJavaTranslator()
    {
        arrowPropertyCallStack.push(Boolean.FALSE);
    }

    /**
     * Stores the actual element that is the context of the expression (within
     * the translated expression).
     */
    private static final String CONTEXT_ELEMENT_NAME = "contextElement";

    /**
     * Seperates path names in OCL expressions.
     */
    private static final String PATH_SEPERATOR = "::";

    /**
     * We need to wrap every expression with a converter so that any expressions
     * that return just objects are converted to boolean values.
     * 
     * @see org.andromda.core.translation.BaseTranslator#postProcess()
     */
    protected void postProcess()
    {
        this.getExpression().insertInTranslatedExpression(
            0,
            "org.andromda.translation.validation.OCLResultEnsurer.ensure(");
        this.getExpression().insertInTranslatedExpression(
            0,
            "boolean constraintValid = ");
        this.getExpression().insertInTranslatedExpression(
            0,
            "final java.lang.Object " + CONTEXT_ELEMENT_NAME + " = this; ");
        this.getExpression().appendToTranslatedExpression(");");

    }
}
