package org.andromda.repositories.emf.uml2;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * Special resource set implementation that can resolve proxies
 * created by Rational Software Modeler. RSM uses a special syntax
 * (e.g. "resource.emx#someXmiId?someNonstandardString") that cannot
 * be resolved by standard EMF resource sets.
 * 
 * @author Matthias Bohlen
 *
 */
public class EMXProxyResolvingResourceSet extends ResourceSetImpl
{
    /**
     * @param uri
     * @param loadOnDemand
     * @return {@link org.eclipse.emf.ecore.EObject}
     * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getEObject(org.eclipse.emf.common.util.URI, boolean)
     */
    @Override
    public EObject getEObject(URI uri, boolean loadOnDemand)
    {
        EObject possiblyResolvedObject = super.getEObject(uri, loadOnDemand);
        if (possiblyResolvedObject == null)
        {
            // if it is still a proxy, try this special fix for RSM:
            String uriString = uri.toString();
            int separatorIndex = uriString.lastIndexOf('?');
            if (separatorIndex > 0)
            {
                uriString = uriString.substring(0, separatorIndex);
                possiblyResolvedObject = super.getEObject(URI.createURI(uriString), loadOnDemand);
            }
        }
        return possiblyResolvedObject;
    }

}
