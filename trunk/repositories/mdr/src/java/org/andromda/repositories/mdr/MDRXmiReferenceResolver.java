package org.andromda.repositories.mdr;

import javax.jmi.reflect.RefPackage;

import org.netbeans.api.xmi.XMIReferenceResolver;
import org.netbeans.lib.jmi.xmi.InputConfig;

/**
 * @author Matthias Bohlen
 * @author Chad Brandon
 */
public class MDRXmiReferenceResolver extends InputConfig {
	
	private XMIReferenceResolver referenceResolver;

	/**
	 * Constructs an instance of this class.
	 * 
	 * @param extents
	 */
	public MDRXmiReferenceResolver(RefPackage extents[], String[] moduleSearchPath) {
		this.referenceResolver = new MDRXmiReferenceResolverContext(extents, this, moduleSearchPath);
	}

	/**
	 * @see org.netbeans.api.xmi.XMIInputConfig#setReferenceResolver(org.netbeans.api.xmi.XMIReferenceResolver)
	 */
	public void setReferenceResolver(XMIReferenceResolver arg0) {
		throw new IllegalStateException("MDRXmiReferenceResolver.setReferenceResolver must not be implemented!");
	}

	/**
	 * @see org.netbeans.api.xmi.XMIInputConfig#getReferenceResolver()
	 */
	public XMIReferenceResolver getReferenceResolver() {
		return referenceResolver;
	}

}
