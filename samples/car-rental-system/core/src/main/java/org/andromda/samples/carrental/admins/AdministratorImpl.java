// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.carrental.admins;

import java.util.Collection;

/**
 * @see Administrator
 */
public class AdministratorImpl
    extends Administrator
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 2896231750025115135L;

    /**
     * @see org.andromda.samples.carrental.admins.Administrator#create(long, String, String, String)
     */
    public Administrator create(long id, String name, String accountNo, String password)
        throws AdminException
    {
        //@todo implement public Administrator create(long id, String name, String accountNo, String password)
        return null;
    }

    public Collection findByAccountNo(String accountNo) throws AdminException
    {
        return null;
    }

    public Collection findAll() throws AdminException
    {
        return null;
    }
}
