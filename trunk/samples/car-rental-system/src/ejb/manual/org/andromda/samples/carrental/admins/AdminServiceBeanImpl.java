package org.andromda.samples.carrental.admins;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

public class AdminServiceBeanImpl
    extends AdminServiceBean
    implements SessionBean
{
    // concrete business methods that were declared
    // abstract in class AdminServiceBean ...

    public java.lang.String authenticateAsAdministrator(
        java.lang.String accountNo,
        java.lang.String name,
        java.lang.String password)
        throws AdminsException
    {
        try
        {
            AdministratorLocalHome klh = getAdministratorLocalHome();

            Collection administrators = klh.findByAccountNo(accountNo);
            if (administrators.size() == 0)
            {
                administrators = klh.findAll();
                if (administrators.size() == 0)
                { // empty database, create 1st admin account
                    Administrator al =
                        klh.create(name, accountNo, password);
                    return al.getId();
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
        catch (CreateException e)
        {
            throw new EJBException(e);
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (FinderException e)
        {
            throw new AdminsException(
                "Administrator with account # "
                    + accountNo
                    + " could not be found");
        }
    }

    public java.lang.String createAdministrator(
        java.lang.String name,
        java.lang.String accountNo,
        java.lang.String password)
        throws AdminsException
    {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ---------- the usual session bean stuff... ------------

    private SessionContext context;

    public void setSessionContext(SessionContext ctx)
    {
        //Log.trace("AdminServiceBean.setSessionContext...");
        context = ctx;
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
