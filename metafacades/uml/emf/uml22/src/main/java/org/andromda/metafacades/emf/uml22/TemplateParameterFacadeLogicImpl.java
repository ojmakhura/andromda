package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.ClassifierTemplateParameter;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.TemplateParameter;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TemplateParameterFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateParameterFacade
 */
public class TemplateParameterFacadeLogicImpl
    extends TemplateParameterFacadeLogic
{
    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(TemplateParameterFacadeLogicImpl.class);
    private static final long serialVersionUID = 3750035061795880358L;

    /**
     * @param metaObject
     * @param context
     */
    public TemplateParameterFacadeLogicImpl(
        final TemplateParameter metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getParameter()
     */
    @Override
    protected ParameterableElement handleGetParameter()
    {
        return this.metaObject.getOwnedParameteredElement();
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getDefaultElement()
     */
    @Override
    protected ParameterableElement handleGetDefaultElement()
    {
        return this.metaObject.getDefault();
    }

    // TemplateParameter no longer inherits from Parameter in UML2
    // so the Parameter methods normally part of the FacadeLogic must be implemented here. Model hierarchy is:
    // ownedTemplateSignature (type=RedefinableTemplateSignature) with ownedParameteredElement
    //   -> ownedParameter (type=ClassifierTemplateParameter) with constrainingClassifier
    //     -> ownedParameteredElement (type=ParameterableElement) with name and type DataType
    /**
     * @return getOwnedParameteredElement().getName()
     */
    //@Override
    public final String getName()
    {
        // ParameterableElement is actually uml:DataType or uml:Class in the model, even though it doesn't inherit from
        final NamedElement type = (NamedElement) this.metaObject.getOwnedParameteredElement();
        // Assumes no templating of template types, no array types, same declared/impl types
        return type.getName();
    }

    /**
     * @return getOwnedParameteredElement()
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetType()
     */
    //@Override
    public final ClassifierFacade getType()
    {
        ClassifierFacade getType2r = null;
        Classifier type = null;
        TemplateParameter param = this.metaObject;
        ParameterableElement element = this.metaObject.getOwnedParameteredElement();
        // param.getConstrainingClassifiers()) for UML2 3.0, allows multiple classifiers
        if (param instanceof ClassifierTemplateParameter)
        {
            final ClassifierTemplateParameter clasifierParameter = (ClassifierTemplateParameter)param;
            type = clasifierParameter.getConstrainingClassifier(this.getName());
            if (type==null && clasifierParameter.getConstrainingClassifiers()!=null && clasifierParameter.getConstrainingClassifiers().size()>0)
            {
                type = clasifierParameter.getConstrainingClassifiers().get(0);
            }
        }
        if (type == null && element instanceof Classifier)
        {
            type = (Classifier)element;
        }
        if (type == null)
        {
            System.out.println(this.getFullyQualifiedName() + " type=" + type + " metaObject=" + this.metaObject + " element=" + element);
        }
        final Object result = this.shieldedElement(type);
        try
        {
            getType2r = (ClassifierFacade)result;
        }
        catch (ClassCastException ex)
        {
            // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
            TemplateParameterFacadeLogicImpl.LOGGER.warn("incorrect metafacade cast for TemplateParameterFacadeLogicImpl.getType ClassifierFacade " + handleGetParameter() + ": " + result);
        }
        if (type==null)
        {
            System.out.println("NULL " + this.getFullyQualifiedName() + " type=" + type + " metaObject=" + this.metaObject + " element=" + element);
        }
        return getType2r;
    }

    /**
     * @return getOwnedParameteredElement().getName()
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetGetterSetterTypeName()
     */
    //@Override
    public final String getGetterSetterTypeName()
    {
        String type = null;
        // ParameterableElement is actually uml:DataType in the model, even though it doesn't inherit from
        // Assumes no templating of template types, no array types, same declared/impl types
        // Classifier name is the parameter type
        if (this.getType() != null)
        {
            type = this.getType().getFullyQualifiedName();
        }
        else
        {
            type = "java.lang.Class";
        }
        return type;
    }

    /**
     * @return getOwnedParameteredElement().getName()
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetFullyQualifiedName()
     */
    //@Override
    public final String getFullyQualifiedName()
    {
        String name = this.getName();
        final String className = ((NamedElement)this.metaObject.getOwner().getOwner()).getName();
        if (className != null)
        {
            name = className + '.' + name;
        }
        final String pkg = this.metaObject.getNearestPackage().getName();
        if (pkg != null)
        {
            name = pkg + '.' + name;
        }
        return name;
    }

    /**
     * @return getOwnedParameteredElement().getName()
     */
    public final int getLower()
    {
        int lower = 0;
        final ClassifierFacade type = this.getType();
        if (type != null && type.isPrimitive())
        {
            lower = 1;
        }
        return lower;
    }

    /**
     * Takes lower bound and datatype primitive/wrapped into account.
     * @return GetterName
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetGetterName()
     */
    public final String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetSetterName()
     */
    public final String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetConstrainingClassifiers()
     */
    //@Override
    public final List<Classifier> getConstrainingClassifiers()
    {
        TemplateParameter param = this.metaObject;
        // param.getConstrainingClassifiers()) for UML2 3.0, allows multiple classifiers
        if (param instanceof ClassifierTemplateParameter)
        {
            return ((ClassifierTemplateParameter)param).getConstrainingClassifiers();
        }
        else
        {
            return new ArrayList<Classifier>();
        }
    }

    /**
     * UML2 only. UML14 has no template/template parameter owner
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetOwner()
     */
    //@Override
    protected ModelElementFacade getOwner()
    {
        return (ModelElementFacade)this.shieldedElement(this.metaObject.getOwner());
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetDocumentation(java.lang.String)
     */
    //@Override
    protected String getDocumentation(String indent)
    {
        return this.getDocumentation(
                indent,
                100 - indent.length());
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetDocumentation(java.lang.String, int)
     */
    //@Override
    protected String getDocumentation(String indent, int lineLength)
    {
        // We really don't want the start/end paragraph returns on the first/last lines. Very annoying. Makes the docs too long with mixed html.
        // That's the only thing that htmlStyle does: convert each line to <p>line</p>. Not necessary.
        return this.getDocumentation(
            indent,
            lineLength,
            false);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.TemplateParameterFacadeLogic#handleGetDocumentation(java.lang.String, int, boolean)
     */
    //@Override
    protected String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        final StringBuilder documentation = new StringBuilder();

        if (lineLength < 1)
        {
            lineLength = Integer.MAX_VALUE;
        }

        // Try to get doc comments from the template parameter, the template, and the template signature
        Collection<Comment> comments = this.metaObject.getOwnedComments();
        if (comments == null || comments.isEmpty())
        {
            comments = this.metaObject.getParameteredElement().getOwnedComments();
        }
        if (comments == null || comments.isEmpty())
        {
            comments = this.metaObject.getSignature().getOwnedComments();
        }
        if (comments != null && !comments.isEmpty())
        {
            for (final Comment comment : comments)
            {
                String commentString = StringUtils.trimToEmpty(comment.getBody());
                documentation.append(StringUtils.trimToEmpty(commentString));
                documentation.append(SystemUtils.LINE_SEPARATOR);
            }
        }

        // if there still isn't anything, create a todo tag.
        if (StringUtils.isEmpty(documentation.toString()))
        {
            if (Boolean.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_FOR_MISSING_DOCUMENTATION)))
            {
                String todoTag = (String)this.getConfiguredProperty(UMLMetafacadeProperties.TODO_TAG);
                documentation.append(todoTag).append(": Model Documentation for " + this.getFullyQualifiedName());
            }
        }

        return StringUtilsHelper.format(
            StringUtils.trimToEmpty(documentation.toString()),
            indent,
            lineLength,
            htmlStyle);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName
     */
    @Override
    public String getValidationName()
    {
        return this.getName();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner
     */
    @Override
    public ModelElementFacade getValidationOwner()
    {
        return (ModelElementFacade)this.shieldedElement(this.metaObject.getOwner());
    }
}
