package org.andromda.core.translation;

/**
 * Contains the possible Expression Kinds.
 * 
 * @author Chad Brandon
 */
public class ExpressionKinds
{

    /**
     * Signafies a <code>&lt;&lt;postcondition&gt;&gt;</code> expression.
     */
    public static final String POST = "post";

    /**
     * Signafies an <code>&lt;&lt;invariant&gt;&gt;</code> expression.
     */
    public static final String INV = "inv";

    /**
     * Signafies a <code>&lt;&lt;precondition&gt;&gt;</code> expression.
     */
    public static final String PRE = "pre";

    /**
     * Signafies a <code>&lt;&lt;definition&gt;&gt;</code> expression
     */
    public static final String DEF = "def";

    /**
     * Signafies a <code>&lt;&lt;body&gt;&gt;</code> expression
     */
    public static final String BODY = "body";

}