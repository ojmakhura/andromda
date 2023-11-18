package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.ClassifierTemplateParameter;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.BindingFacade.
 *
 * @see org.andromda.metafacades.uml.BindingFacade
 */
public class BindingFacadeLogicImpl
    extends BindingFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public BindingFacadeLogicImpl(
        final TemplateBinding metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    public TemplateBinding getTemplateBinding()
    {
        return (TemplateBinding) this.metaObject;
    }

    /**
     * @see org.andromda.metafacades.uml.BindingFacade#getArguments()
     */
    @Override
    protected Collection<TemplateParameterSubstitution> handleGetArguments()
    {
        // org.eclipse.uml2.uml.internal.impl.Class ttt;
        EList<TemplateParameterSubstitution> lst = this.metaObject.getParameterSubstitutions();
        
        return lst;
    }

    @Override
    protected String handleGetArgumentsString() {

        StringBuilder sb = new StringBuilder();

        EList<TemplateParameterSubstitution> lst = this.metaObject.getParameterSubstitutions();
        
        for(TemplateParameterSubstitution tps : lst) {
            ClassifierTemplateParameter ctp = (ClassifierTemplateParameter) tps.getFormal();
        }
        
        return sb.toString();
    }

    @Override
    protected Map handleGetArgumentsMap() {
        EList<TemplateParameterSubstitution> lst = this.metaObject.getParameterSubstitutions();

        Map<String, String> map = new HashMap<String, String>();
        for(TemplateParameterSubstitution tps : lst) {
            NamedElement actual = (NamedElement) tps.getActual();
            ClassifierTemplateParameter ctp = (ClassifierTemplateParameter) tps.getFormal();
            NamedElement formal = (NamedElement) ctp.getParameteredElement();

            map.put(formal.getName(), actual.getName());
        }

        return map;
    }
}
