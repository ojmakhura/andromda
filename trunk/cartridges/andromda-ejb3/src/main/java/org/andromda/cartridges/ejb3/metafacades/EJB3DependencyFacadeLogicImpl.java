package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade
 */
public class EJB3DependencyFacadeLogicImpl
    extends EJB3DependencyFacadeLogic
{

    /**
     * The suffix for the transformation annonymous name.
     */
    private static final String TRANSFORMATION_ANONYMOUS_NAME_SUFFIX = "_TRANSFORMER";
    
    /**
     * The suffix for the transformation to entity method name.
     */
    private static final String TRANSFORMATION_TO_ENTITY_METHOD_NAME_SUFFIX = "ToEntity";
    
    /**
     * The suffix for the value object to entity transformer.
     */
    private static final String VALUE_OBJECT_TO_ENTITY_TRANSFORMER_SUFFIX = "Transformer";
    
    public EJB3DependencyFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getTransformationConstantName()
     */
    protected java.lang.String handleGetTransformationConstantName()
    {
        return EJB3Globals.TRANSFORMATION_CONSTANT_PREFIX + this.getName().toUpperCase();
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getTransformationMethodName()
     */
    protected java.lang.String handleGetTransformationMethodName()
    {
        return EJB3Globals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getTransformationAnonymousName()
     */
    protected java.lang.String handleGetTransformationAnonymousName()
    {
        return this.getName().toUpperCase() + TRANSFORMATION_ANONYMOUS_NAME_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#isCircularReference()
     */
    protected boolean handleIsCircularReference()
    {
        boolean circularReference = false;
        final ModelElementFacade sourceElement = this.getSourceElement();
        final ModelElementFacade targetElement = this.getTargetElement();
        final Collection sourceDependencies = targetElement.getSourceDependencies();
        if (sourceDependencies != null && !sourceDependencies.isEmpty())
        {
            circularReference = CollectionUtils.find(sourceDependencies, new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    return dependency != null && dependency.getTargetElement().equals(sourceElement);
                }
            }) != null;
        }
        return circularReference;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getTransformationConstantValue()
     */
    protected int handleGetTransformationConstantValue()
    {
        int value = 0;
        ModelElementFacade element = this.getSourceElement();
        if (element instanceof EJB3EntityFacade)
        {
            final List hierarchy = new ArrayList();
            for (EJB3EntityFacade entity = (EJB3EntityFacade)element; entity != null; entity = (EJB3EntityFacade)entity.getGeneralization())
            {
                hierarchy.add(entity);
            }
            boolean breakOut = false;
            for (int ctr = hierarchy.size() - 1; ctr >= 0; ctr--)
            {
                final EJB3EntityFacade generalization = (EJB3EntityFacade)hierarchy.get(ctr);
                for (final Iterator referenceIterator = generalization.getValueObjectReferences().iterator(); referenceIterator.hasNext();)
                {
                    final Object reference = referenceIterator.next();
                    value++;
                    if (reference.equals(this))
                    {
                        breakOut = true;
                        break;
                    }
                }
                if (breakOut)
                {
                    break;
                }
            }
        }
        return value;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getTransformationToCollectionMethodName()
     */
    protected java.lang.String handleGetTransformationToCollectionMethodName()
    {
        return EJB3Globals.TRANSFORMATION_METHOD_PREFIX + StringUtils.capitalize(this.getName()) +
            EJB3Globals.TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getDaoName()
     */
    protected java.lang.String handleGetDaoName()
    {
        return MessageFormat.format(
                getDaoNamePattern(),
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the value of the {@link SpringGlobals#DAO_PATTERN}.
     *
     * @return the DAO name pattern.
     */
    private String getDaoNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(EJB3Globals.DAO_PATTERN));
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getDaoGetterName()
     */
    protected java.lang.String handleGetDaoGetterName()
    {
        return "get" + StringUtils.capitalize(this.getDaoName());
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getDaoSetterName()
     */
    protected java.lang.String handleGetDaoSetterName()
    {
        return "set" + StringUtils.capitalize(this.getDaoName());
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getTransformationToEntityCollectionMethodName()
     */
    protected java.lang.String handleGetTransformationToEntityCollectionMethodName()
    {
        return this.getTransformationToEntityMethodName() + EJB3Globals.TRANSFORMATION_TO_COLLECTION_METHOD_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getTransformationToEntityMethodName()
     */
    protected java.lang.String handleGetTransformationToEntityMethodName()
    {
        return this.getName() + TRANSFORMATION_TO_ENTITY_METHOD_NAME_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3DependencyFacade#getValueObjectToEntityTransformerName()
     */
    protected java.lang.String handleGetValueObjectToEntityTransformerName()
    {
        return StringUtils.capitalize(this.getTransformationToEntityMethodName()) + 
            VALUE_OBJECT_TO_ENTITY_TRANSFORMER_SUFFIX;
    }

}