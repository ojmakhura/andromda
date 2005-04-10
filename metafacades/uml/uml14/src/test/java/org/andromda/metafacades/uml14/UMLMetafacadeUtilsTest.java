package org.andromda.metafacades.uml14;

import junit.framework.TestCase;
import org.andromda.translation.ocl.ExpressionKinds;

/**
 * @author Chad Brandon
 */
public class UMLMetafacadeUtilsTest extends TestCase
{
    public void testIsConstraintKind()
    {
        final String emptyExpression = "";
        assertFalse(UML14MetafacadeUtils.isConstraintKind(emptyExpression, ExpressionKinds.BODY));
        final String bodyExpressionWithName = "context DecisionItem::findRoot():Collection(DecisionItem) body findByRootBody : allInstances() -> select (decisionItem | decisionItem.rootItem = true)";
        assertTrue(UML14MetafacadeUtils.isConstraintKind(bodyExpressionWithName, ExpressionKinds.BODY));
        final String bodyExpressionNoName = "context DecisionItem::findRoot():Collection(DecisionItem) body : allInstances() -> select (decisionItem | decisionItem.rootItem = true)";
        assertTrue(UML14MetafacadeUtils.isConstraintKind(bodyExpressionNoName, ExpressionKinds.BODY));
        final String bodyExpressionNoColon = "context DecisionItem::findRoot():Collection(DecisionItem) body allInstances() -> select (decisionItem | decisionItem.rootItem = true)";
        assertFalse(UML14MetafacadeUtils.isConstraintKind(bodyExpressionNoColon, ExpressionKinds.BODY));
        final String invExpressionNoName = "context CustomerCard\r\n" + "inv: validFrom.isBefore(goodThru)";
        assertTrue(UML14MetafacadeUtils.isConstraintKind(invExpressionNoName, ExpressionKinds.INV));
    }
}