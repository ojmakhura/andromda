package org.andromda.metafacades.uml;

import junit.framework.TestCase;
import org.andromda.translation.ocl.ExpressionKinds;

/**
 * @author Chad Brandon
 */
public class UMLMetafacadeUtilsTest
        extends TestCase
{
    public void testIsConstraintKind()
    {
        final String emptyExpression = "";
        assertFalse(UMLMetafacadeUtils.isConstraintKind(emptyExpression, ExpressionKinds.BODY));
        final String bodyExpressionWithName = "context DecisionItem::findRoot():Collection(DecisionItem) body findByRootBody : allInstances() -> select (decisionItem | decisionItem.rootItem = true)";
        assertTrue(UMLMetafacadeUtils.isConstraintKind(bodyExpressionWithName, ExpressionKinds.BODY));
        final String bodyExpressionNoName = "context DecisionItem::findRoot():Collection(DecisionItem) body : allInstances() -> select (decisionItem | decisionItem.rootItem = true)";
        assertTrue(UMLMetafacadeUtils.isConstraintKind(bodyExpressionNoName, ExpressionKinds.BODY));
        final String bodyExpressionNoColon = "context DecisionItem::findRoot():Collection(DecisionItem) body allInstances() -> select (decisionItem | decisionItem.rootItem = true)";
        assertFalse(UMLMetafacadeUtils.isConstraintKind(bodyExpressionNoColon, ExpressionKinds.BODY));
        final String invExpressionNoName = "context CustomerCard\r\n" + "inv: validFrom.isBefore(goodThru)";
        assertTrue(UMLMetafacadeUtils.isConstraintKind(invExpressionNoName, ExpressionKinds.INV));
    }
}