package org.andromda.translation.validation;

import java.util.List;
import java.util.Stack;

import org.andromda.core.translation.BaseTranslator;
import org.andromda.core.translation.node.*;

public class ValidationJavaTranslator extends BaseTranslator
{
    public void caseAContextDeclaration(AContextDeclaration node)
    {
        newTranslationLayer();
        {
            Object temp[] = node.getContextDeclaration().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PContextDeclaration) temp[i]).apply(this);
            }
        }
        mergeTranslationLayers();
        this.getExpression().appendToTranslatedExpression(translationLayers.peek());
        translationLayers.clear();
    }

    public void caseAClassifierContextDeclaration(AClassifierContextDeclaration node)
    {
        Object temp[] = node.getClassifierExpressionBody().toArray();
        for(int i = 0; i < temp.length; i++)
            ((PClassifierExpressionBody) temp[i]).apply(this);
    }

    public void caseAOperationContextDeclaration(AOperationContextDeclaration node)
    {
        Object temp[] = node.getOperationExpressionBody().toArray();
        for(int i = 0; i < temp.length; i++)
            ((POperationExpressionBody) temp[i]).apply(this);
    }

    public void caseAAttributeOrAssociationContextDeclaration(AAttributeOrAssociationContextDeclaration node)
    {
        Object temp[] = node.getAttributeOrAssociationExpressionBody().toArray();
        for(int i = 0; i < temp.length; i++)
            ((PAttributeOrAssociationExpressionBody) temp[i]).apply(this);
    }

    public void caseAInvClassifierExpressionBody(AInvClassifierExpressionBody node)
    {
        node.getExpression().apply(this);
        write(";");
    }

    public void caseADefClassifierExpressionBody(ADefClassifierExpressionBody node)
    {
        node.getDefinitionExpression().apply(this);
    }

    public void caseAPathName(APathName node)
    {
        final String name = node.getName().getText();
        write("self".equals(name) ? "this" : name);
    }

    public void caseAVariableDeclaration(AVariableDeclaration node)
    {
        if (node.getTypeDeclaration() == null)
            write("Object ");
        else
            node.getTypeDeclaration().apply(this);

        node.getName().apply(this);
    }

    public void caseATypeDeclaration(ATypeDeclaration node)
    {
        node.getType().apply(this);
    }

    public void caseAVariableDeclarationList(AVariableDeclarationList node)
    {
        node.getVariableDeclaration().apply(this);

        if(node.getVariableDeclarationValue() != null)
            node.getVariableDeclarationValue().apply(this);

        Object temp[] = node.getVariableDeclarationListTail().toArray();
        for(int i = 0; i < temp.length; i++)
            ((PVariableDeclarationListTail) temp[i]).apply(this);
    }

    public void caseAVariableDeclarationListTail(AVariableDeclarationListTail node)
    {
        node.getComma().apply(this);
        node.getVariableDeclaration().apply(this);

        if(node.getVariableDeclarationValue() != null)
            node.getVariableDeclarationValue().apply(this);
    }

    public void caseAEqualExpression(AEqualExpression node)
    {
        node.getEqual().apply(this);
        node.getExpression().apply(this);
    }

    public void caseABodyOperationStereotype(ABodyOperationStereotype node)
    {
    }

    public void caseAPreOperationStereotype(APreOperationStereotype node)
    {
    }

    public void caseAPostOperationStereotype(APostOperationStereotype node)
    {
    }

    public void caseAMessageExpression(AMessageExpression node)
    {
    }

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
        for(int i = 0; i < temp.length; i++)
            ((PPropertyCallExpressionTail) temp[i]).apply(this);
        mergeTranslationLayerAfter();
    }

    public void caseADotPropertyCallExpressionTail(ADotPropertyCallExpressionTail node)
    {
        node.getDot().apply(this);
        node.getDotFeatureCall().apply(this);
    }

    public void caseALetExp(ALetExp node)
    {
        node.getLetVariableDelaration().apply(this);
        node.getLetExpSub().apply(this);
    }

    public void caseAVariableDeclarationLetExpSub(AVariableDeclarationLetExpSub node)
    {
        node.getComma().apply(this);
        node.getLetVariableDelaration().apply(this);
        node.getLetExpSub().apply(this);
    }

    public void caseALogicalExp(ALogicalExp node)
    {
        newTranslationLayer();
        if(node.getRelationalExpression() != null)
        {
            node.getRelationalExpression().apply(this);
        }
        {
            Object temp[] = node.getLogicalExpressionTail().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PLogicalExpressionTail) temp[i]).apply(this);
            }
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
        if(node.getRelationalExpression() != null)
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
        newTranslationLayer();
        write("OCLExpressions.");
        node.getRelationalOperator().apply(this);
        write("(");
        mergeTranslationLayerBefore();
        if(node.getAdditiveExpression() != null)
        {
            write(",");
            node.getAdditiveExpression().apply(this);
        }
        write(")");
    }

    public void inARelationalExpression(ARelationalExpression node)
    {
        newTranslationLayer();
    }

    public void outARelationalExpression(ARelationalExpression node)
    {
        mergeTranslationLayerAfter();
    }

    /*public void caseAEmptyArrowFeatureCall(AEmptyArrowFeatureCall node)
    {
        newTranslationLayer();
        write("OCLCollections.");
        inAEmptyArrowFeatureCall(node);
        if(node.getArrowNoParameterFeatureCall() != null)
        {
            node.getArrowNoParameterFeatureCall().apply(this);
        }
        if(node.getLParen() != null)
        {
            node.getLParen().apply(this);
        }
        mergeTranslationLayerBefore();
        if(node.getRParen() != null)
        {
            node.getRParen().apply(this);
        }
        outAEmptyArrowFeatureCall(node);
    }

    public void caseASingleArrowFeatureCall(ASingleArrowFeatureCall node)
    {
        newTranslationLayer();
        write("OCLCollections.");
        inASingleArrowFeatureCall(node);
        if(node.getArrowSingleParameterFeatureCall() != null)
        {
            node.getArrowSingleParameterFeatureCall().apply(this);
        }
        if(node.getLParen() != null)
        {
            node.getLParen().apply(this);
        }
        mergeTranslationLayerBefore();
        write(",");
        if(node.getFirstParam() != null)
        {
            node.getFirstParam().apply(this);
        }
        if(node.getRParen() != null)
        {
            node.getRParen().apply(this);
        }
        outASingleArrowFeatureCall(node);
    }

    public void caseADoubleArrowFeatureCall(ADoubleArrowFeatureCall node)
    {
        newTranslationLayer();
        write("OCLCollections.");
        inADoubleArrowFeatureCall(node);
        if(node.getArrowDoubleParameterFeatureCall() != null)
        {
            node.getArrowDoubleParameterFeatureCall().apply(this);
        }
        if(node.getLParen() != null)
        {
            node.getLParen().apply(this);
        }
        mergeTranslationLayerBefore();
        write(",");
        if(node.getFirstParam() != null)
        {
            node.getFirstParam().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getSecondParam() != null)
        {
            node.getSecondParam().apply(this);
        }
        if(node.getRParen() != null)
        {
            node.getRParen().apply(this);
        }
        outADoubleArrowFeatureCall(node);
    }*/
    
    public void caseAArrowFeatureCall(AArrowFeatureCall node)
    {
        newTranslationLayer();
        write("OCLCollections.");
        inAArrowFeatureCall(node);
        if(node.getArrowFeature() != null)
        {
            node.getArrowFeature().apply(this);
        }
        AFeatureCallParameters parameters = 
            (AFeatureCallParameters)node.getFeatureCallParameters();
        if(parameters.getLParen() != null)
        {
            parameters.getLParen().apply(this);
        }
        mergeTranslationLayerBefore();
        AActualParameterList parameterList = 
            (AActualParameterList)parameters.getActualParameterList();
        if (parameterList != null) {
            if (parameterList.getExpression() != null) {
                write(",");
                parameterList.getExpression().apply(this);
            }
            List expressions = parameterList.getCommaExpression();
            for (int ctr = 0; ctr < expressions.size(); ctr++) {
                Node expression = (Node)expressions.get(ctr);
                if (expression != null) {
                    write(",");
                    expression.apply(this);
                }
            }
        }
        if(parameters.getRParen() != null)
        {
            parameters.getRParen().apply(this);
        }
        outAArrowFeatureCall(node);
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
    {
    }

    public void caseTDef(TDef tDef)
    {
    }

    public void caseTLet(TLet tLet)
    {
    }

    public void caseTColon(TColon tColon)
    {
    }

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
    {
    }

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

    public void caseTComment(TComment tComment)
    {
        write("// ");
    }

    public void caseTEndif(TEndif tEndif)
    {
    }

    public void caseTAttr(TAttr tAttr)
    {
    }

    public void caseTBag(TBag tBag)
    {
    }

    public void caseTBar(TBar tBar)
    {
    }

    public void caseTBody(TBody tBody)
    {
    }

    public void caseTCommercialAt(TCommercialAt tCommercialAt)
    {
    }

    public void caseTDerive(TDerive tDerive)
    {
    }

    public void caseTEndpackage(TEndpackage tEndpackage)
    {
    }

    public void caseTEnum(TEnum tEnum)
    {
    }

    public void caseTIn(TIn tIn)
    {
    }

    public void caseTInit(TInit tInit)
    {
    }

    public void caseTInt(TInt tInt)
    {
        write(tInt.getText());
    }

    public void caseTIsSentOperator(TIsSentOperator tIsSentOperator)
    {
    }

    public void caseTMessageOperator(TMessageOperator tMessageOperator)
    {
    }

    public void caseTNewLine(TNewLine tNewLine)
    {
    }

    public void caseTOper(TOper tOper)
    {
    }

    public void caseTOrderedset(TOrderedset tOrderedset)
    {
    }

    public void caseTPackage(TPackage tPackage)
    {
    }

    public void caseTPost(TPost tPost)
    {
    }

    public void caseTPre(TPre tPre)
    {
    }

    public void caseTArrow(TArrow tArrow)
    {
    }

    public void caseTIf(TIf tIf)
    {
        write("if");
    }

    public void caseTElse(TElse tElse)
    {
        write("else");
    }

    public void caseTThen(TThen tThen)
    {
    }

    public void caseTIsEmpty(TIsEmpty tIsEmpty)
    {
        write("isEmpty");
    }

    public void caseTAny(TAny tAny)
    {
        write("isAny");
    }

    public void caseTAppend(TAppend tAppend)
    {
        write("append");
    }

    public void caseTAsBag(TAsBag tAsBag)
    {
        write("asBag");
    }

    public void caseTAsOrderedSet(TAsOrderedSet tAsOrderedSet)
    {
        write("asOrderedSet");
    }

    public void caseTAsSequence(TAsSequence tAsSequence)
    {
        write("asSequence");
    }

    public void caseTAsSet(TAsSet tAsSet)
    {
        write("asSet");
    }

    public void caseTAt(TAt tAt)
    {
        write("at");
    }

    public void caseTCollect(TCollect tCollect)
    {
        write("collect");
    }

    public void caseTCollectNested(TCollectNested tCollectNested)
    {
        write("collectNested");
    }

    public void caseTPrepend(TPrepend tPrepend)
    {
        write("prepend");
    }

    public void caseTRange(TRange tRange)
    {
    }

    public void caseTReal(TReal tReal)
    {
        write(tReal.getText());
    }

    public void caseTComma(TComma tComma)
    {
        write(", ");
    }

    public void caseTCount(TCount tCount)
    {
        write("count");
    }

    public void caseTDot(TDot tDot)
    {
        write(".");
    }

    public void caseTExcludes(TExcludes tExcludes)
    {
        write("exclude");
    }

    public void caseTExcludesAll(TExcludesAll tExcludesAll)
    {
        write("excludesAll");
    }

    public void caseTExcluding(TExcluding tExcluding)
    {
        write("excluding");
    }

    public void caseTExists(TExists tExists)
    {
        write("exists");
    }

    public void caseTFlatten(TFlatten tFlatten)
    {
        write("flatten");
    }

    public void caseTForAll(TForAll tForAll)
    {
        write("forAll");
    }

    public void caseTIncludes(TIncludes tIncludes)
    {
        write("includes");
    }

    public void caseTIncludesAll(TIncludesAll tIncludesAll)
    {
        write("includesAll");
    }

    public void caseTIncluding(TIncluding tIncluding)
    {
        write("including");
    }

    public void caseTIndexOf(TIndexOf tIndexOf)
    {
        write("indexOf");
    }

    public void caseTInsertAt(TInsertAt tInsertAt)
    {
        write("insertAt");
    }

    public void caseTIntersection(TIntersection tIntersection)
    {
        write("intersection");
    }

    public void caseTIsUnique(TIsUnique tIsUnique)
    {
        write("isUnique");
    }

    public void caseTIterate(TIterate tIterate)
    {
        write("iterate");
    }

    public void caseTLast(TLast tLast)
    {
        write("last");
    }

    public void caseTNotEmpty(TNotEmpty tNotEmpty)
    {
        write("notEmpty");
    }

    public void caseTOne(TOne tOne)
    {
        write("one");
    }

    public void caseTReject(TReject tReject)
    {
        write("reject");
    }

    public void caseTSelect(TSelect tSelect)
    {
        write("select");
    }

    public void caseTSortedBy(TSortedBy tSortedBy)
    {
        write("sortedBy");
    }

    public void caseTSubOrderedSet(TSubOrderedSet tSubOrderedSet)
    {
        write("subOrderedSet");
    }

    public void caseTSubSequence(TSubSequence tSubSequence)
    {
        write("subSequence");
    }

    public void caseTSymmetricDifference(TSymmetricDifference tSymmetricDifference)
    {
        write("symmetricDifference");
    }

    public void caseTUnion(TUnion tUnion)
    {
        write("union");
    }

    public void caseTSum(TSum tSum)
    {
        write("sum");
    }

    public void caseTSize(TSize tSize)
    {
        write("size");
    }

    public void caseTSemicolon(TSemicolon tSemicolon)
    {
    }

    public void caseTUnknown(TUnknown tUnknown)
    {
    }

    public void caseTOclAsType(TOclAsType tOclAsType)
    {
    }

    public void caseTOclInState(TOclInState tOclInState)
    {
    }

    public void caseTOclIsKindOf(TOclIsKindOf tOclIsKindOf)
    {
    }

    public void caseTOclIsNew(TOclIsNew tOclIsNew)
    {
    }

    public void caseTOclIsTypeOf(TOclIsTypeOf tOclIsTypeOf)
    {
    }

    public void caseTScopeOperator(TScopeOperator tScopeOperator)
    {
    }

    public void caseTSequence(TSequence tSequence)
    {
    }

    public void caseTSet(TSet tSet)
    {
    }

    public void caseTStringLit(TStringLit tStringLit)
    {
        final StringBuffer buffer = new StringBuffer(tStringLit.getText());

/*
        // remove the leading and trailing single quotes
        buffer.deleteCharAt(0);
        buffer.deleteCharAt(buffer.length()-1);

        String string = buffer.toString().replaceAll("\\", "\\\\").replaceAll("\'","\\\'").replaceAll("\"","\\\"");

        buffer.delete(0, buffer.length());
        buffer.append('\"').append(string).append('\"');
*/
        write(buffer);

/*
        Matcher matcher = null;

        // first replace the backslashes
        matcher = backslashPattern.matcher(buffer);
        matcher.replaceAll("\\\\");

        // second replace the single quotes
        matcher = singleQuotePattern.matcher(buffer);
        matcher.replaceAll("\\\'");

        // thirdly replace the double quotes
        matcher = doubleQuotePattern.matcher(buffer);
        matcher.replaceAll("\\\"");

        // prepend and append the string with double quotes
        buffer.insert(0, '\"');
        buffer.append('\"');

        write(buffer);
*/
    }

    public void caseTTab(TTab tTab)
    {
    }

    public void caseTTuple(TTuple tTuple)
    {
    }

    public void caseTTupletype(TTupletype tTupletype)
    {
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */

    public static final String CRLF = System.getProperty("line.separator");
    private final Stack translationLayers = new Stack();

    // prepare matching for backslashes, single quotes and double quotes
/*
    private Pattern backslashPattern = Pattern.compile("\\");
    private Pattern singleQuotePattern = Pattern.compile("\'");
    private Pattern doubleQuotePattern = Pattern.compile("\"");
*/


    private void write(Object object)
    {
        appendToTranslationLayer(String.valueOf(object));
    }

    private void writeln(Object object)
    {
        write(object);
//        write(CRLF);
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
        return ((StringBuffer)translationLayers.peek()).insert(0,appendix);
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
        while ( mergeTranslationLayerAfter() != null );
        return (StringBuffer)translationLayers.peek();
    }

}


