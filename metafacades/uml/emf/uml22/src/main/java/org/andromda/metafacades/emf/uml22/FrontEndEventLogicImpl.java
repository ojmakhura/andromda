package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UseCase;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndEvent.
 *
 * @see org.andromda.metafacades.uml.FrontEndEvent
 */
public class FrontEndEventLogicImpl
    extends FrontEndEventLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndEventLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        // Be careful. Should return true only when it has an owning transition
        // contained in front end usecase
        // from UML1.4: return this.getTransition() instanceof FrontEndForward;
        // Causes stack overflow...
        Element owner = (Element)this.metaObject;
        if (!(owner.getOwner() instanceof Transition))
        {
            return false;
        }
        while (owner != null)
        {
            if (owner instanceof UseCase)
            {
                if (this.shieldedElement(owner) instanceof FrontEndUseCase)
                {
                    return true;
                }
            }
            owner = owner.getOwner();
        }
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCall()
     */
    @Override
    protected FrontEndControllerOperation handleGetControllerCall()
    {
        final List<FrontEndControllerOperation> operations = this.getControllerCalls();
        return (operations.isEmpty() ? null : operations.iterator().next());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getControllerCalls()
     */
    @Override
    public List<Operation> handleGetControllerCalls()
    {
        // - get every operation from each CallOperationAction instance.
        // - Note: this is the same implementation as CallEvent.getOperationCall()
        final Activity activity = (Activity)this.metaObject;
        final List<Operation> operations = new ArrayList<Operation>();
        Collection<ActivityNode> nodes = activity.getNodes();
        // UML2 v3: What previously was in getNodes is now in getOwnedNodes, while getNodes returns null
        // This causes JSF cartridge to fail unless implemented
        if (nodes==null || nodes.isEmpty())
        {
            nodes = activity.getOwnedNodes();
        }
        for (final Iterator<ActivityNode> iterator = nodes.iterator(); iterator.hasNext();)
        {
            final Object nextNode = iterator.next();
            if (nextNode instanceof CallOperationAction)
            {
                final Operation operation = ((CallOperationAction)nextNode).getOperation();
                if (operation != null)
                {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndEvent#getAction()
     */
    @Override
    protected FrontEndAction handleGetAction()
    {
        FrontEndAction action = null;
        final TransitionFacade transition = this.getTransition();
        if (transition instanceof FrontEndAction)
        {
            action = (FrontEndAction)transition;
        }
        return action;
    }

    /**
     * UML2 v5: ActivityImpl returns NULL for namespace. Need another way to get package name
     * @see org.andromda.metafacades.uml.FrontEndEvent#getPackageName()
     */
    @Override
    protected String handleGetPackageName()
    {
        String packageName = UmlUtilities.getPackageName((NamedElement)this.metaObject, ".", false);
        if (StringUtils.isBlank(packageName))
        {
            packageName = this.getPackage().getFullyQualifiedName();
        }
        return packageName;
    }

    @Override
    protected String handleGetMessageValue() {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    @Override
    protected String handleGetResetMessageKey() {
        return this.getMessageKey() + ".reset.message";
    }

    @Override
    protected String handleGetResetMessageValue() {
        return "Reset";
    }

    @Override
    protected String handleGetMessageKey() {
        String triggerKey = StringUtilsHelper.toResourceMessageKey(getName());
        if (!this.isNormalizeMessages())
        {
            final FrontEndAction action = (FrontEndAction)this.getAction();
            if (action != null)
            {
                final FrontEndView view = (FrontEndView)action.getInput();
                if (view != null)
                {
                    triggerKey = view.getMessageKey() + '.' + triggerKey;
                }
            }
        }
        return triggerKey;
    }

    /**
     * Indicates whether or not we should normalize messages.
     * @return normalizeMessages true/false
     */
    private boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(MetafacadeWebGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
}
