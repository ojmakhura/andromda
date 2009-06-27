package org.andromda.metafacades.uml14;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.translation.ocl.ExpressionKinds;


/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class EntityQueryOperationLogicImpl
    extends EntityQueryOperationLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EntityQueryOperationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityQueryOperation#getQuery(String)
     */
    @Override
    protected String handleGetQuery(String translation)
    {
        ExceptionUtils.checkEmpty("translation", translation);
        final String[] translatedExpressions = this.translateConstraints(ExpressionKinds.BODY, translation);
        String query = null;

        // we just get the first body constraint found
        if (translatedExpressions != null && translatedExpressions.length > 0)
        {
            query = translatedExpressions[0];
        }
        return query;
    }
}