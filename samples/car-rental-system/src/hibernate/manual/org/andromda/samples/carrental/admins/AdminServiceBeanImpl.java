package org.andromda.samples.carrental.admins;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;


public class AdminServiceBeanImpl
    extends AdminServiceBean
    implements javax.ejb.SessionBean
{
    // concrete business methods that were declared
    // abstract in class AdminServiceBean ...

    protected java.lang.String handleAuthenticateAsAdministrator(
        Session sess,
        java.lang.String name,
        java.lang.String accountNo,
        java.lang.String password)
        throws AdminsException
    {
        try
        {
            Collection administrators = AdministratorFactory.findByAccountNo(sess, accountNo);

            if (administrators.size() == 0)
            {
                administrators = AdministratorFactory.findAll(sess);
                if (administrators.size() == 0)
                { // empty database, create 1st admin account
                    return handleCreateAdministrator(sess, name, accountNo, password);
                }
            }
            if (administrators.size() != 1)
            {
                throw new AdminsException(
                    "Administrator with account # "
                        + accountNo
                        + " could not be found");
            }

            Administrator theAdministrator =
                (Administrator) administrators.iterator().next();
            if (theAdministrator.getPassword().equals(password))
            {
                return theAdministrator.getId();
            }

            return null;
        }
        catch (HibernateException e)
        {
            throw new EJBException(e);
        }
        catch (SQLException e)
        {
            throw new EJBException(e);
        }
    }

    protected java.lang.String handleCreateAdministrator(
        Session sess,
        java.lang.String name,
        java.lang.String accountNo,
        java.lang.String password)
        throws AdminsException
    {
        try
        {
            Administrator admin = AdministratorFactory.create(name, accountNo, password);

            String id = (String) sess.save(admin);
            return id;
        }
        catch (HibernateException e)
        {
            throw new EJBException(e);
        }
    }

    // ---------- the usual session bean stuff... ------------

    public void setSessionContext(javax.ejb.SessionContext ctx)
    {
        //Log.trace("AdminServiceBean.setSessionContext...");
        super.setSessionContext(ctx);
    }

    public void ejbRemove()
    {
        //Log.trace(
        //    "AdminServiceBean.ejbRemove...");
    }

    public void ejbPassivate()
    {
        //Log.trace("AdminServiceBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("AdminServiceBean.ejbActivate...");
    }
}
