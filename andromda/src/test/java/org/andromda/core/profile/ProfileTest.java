package org.andromda.core.profile;

import java.util.Collection;

import junit.framework.TestCase;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.namespace.NamespaceComponents;

/**
 * Tests {@link org.andromda.core.profile.Profile}
 * @author Chad Brandon
 */
public class ProfileTest
    extends TestCase
{
    
    private Profile profile;
    
    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        NamespaceComponents.instance().discover();
        Collection profiles = ComponentContainer.instance().findComponentsOfType(Profile.class);
        assertNotNull(profiles);
        this.profile = (Profile)profiles.iterator().next();
    }
    
    public void testGetValue()
    {
        assertEquals("Entity", this.profile.getValue("ENTITY"));
        assertEquals("@andromda.tagged.value", this.profile.getValue("ANDROMDA_TAGGED_VALUE"));
        assertEquals("datatype::String", this.profile.getValue("STRING_TYPE"));
    }
}
