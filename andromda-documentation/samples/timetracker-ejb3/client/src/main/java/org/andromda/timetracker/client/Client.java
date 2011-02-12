package org.andromda.timetracker.client;

import java.util.Date;
import java.util.Properties;

import org.andromda.timetracker.domain.Role;
import org.andromda.timetracker.service.UserServiceDelegate;
import org.andromda.timetracker.service.UserServiceException;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.andromda.timetracker.vo.UserRoleVO;
import org.andromda.timetracker.vo.UserVO;

public class Client 
{

    private Properties prop;
    
    public void init() 
    {
        prop = new Properties();
        prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        prop.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        prop.put("java.naming.provider.url", "localhost");
    }
    
    
    public void getAllUsersAsVO()
    {
        System.out.println("Getting all users...");
        
        UserVO[] users = null;
        
        UserServiceDelegate usd = new UserServiceDelegate(prop);
        try
        {
            users = usd.getAllUsers();
        } 
        catch (UserServiceException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            usd.close();
        }
        
        if (users != null && users.length > 0)
        {
            for (UserVO userVO : users)
            {
                System.out.println("user: " + userVO.toString() + " - " + userVO.getId());
            }
        }
        
        System.out.println("Got all users complete.");
    }

    
    public void addUser()
    {
        UserDetailsVO userDetailsVO = new UserDetailsVO();
        userDetailsVO.setFirstName("vance");
        userDetailsVO.setLastName("Karimi");
        userDetailsVO.setUsername("vancek");
        userDetailsVO.setPassword("monkey1");
        userDetailsVO.setEmail("test@test.com");
        userDetailsVO.setIsActive(true);
        userDetailsVO.setCreationDate(new Date());
        
        UserRoleVO[] roles = new UserRoleVO[1];
        roles[0] = new UserRoleVO();
        roles[0].setRole(Role.ADMIN);
        
        userDetailsVO.setRoles(roles);
        
        UserServiceDelegate usd = null;
        try
        {
            usd = new UserServiceDelegate(prop);
            usd.registerUser(userDetailsVO);
        }
        finally
        {
            if (usd != null)
            {
                usd.close();
            }
        }
    }
    

}
