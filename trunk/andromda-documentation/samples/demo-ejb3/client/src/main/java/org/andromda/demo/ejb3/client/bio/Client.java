package org.andromda.demo.ejb3.client.bio;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;
import org.andromda.demo.ejb3.account.AccountManagerDelegate;
import org.andromda.demo.ejb3.account.AccountManagerRemote;
import org.andromda.demo.ejb3.bio.Bio;
import org.andromda.demo.ejb3.bio.BioException;
import org.andromda.demo.ejb3.bio.BioReference;
import org.andromda.demo.ejb3.bio.BioServiceDelegate;
import org.andromda.demo.ejb3.user.User;
import org.andromda.demo.ejb3.user.UserException;
import org.andromda.demo.ejb3.user.UserManagerDelegate;
import org.andromda.demo.ejb3.vehicle.Motocycle;
import org.andromda.demo.ejb3.vehicle.VehicleException;
import org.andromda.demo.ejb3.vehicle.VehicleManagerDelegate;

public class Client 
{

	private Properties prop;
	
	public void init() 
    {
		prop = new Properties();
		prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		prop.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		prop.put("java.naming.provider.url", "localhost:1099");
	}
    
    
    
    
    public void insertBio()
    {
        System.out.println("Inserting bio...");
        
        Bio bio = new Bio("joee", "Justin Oee Bio", "abc".getBytes(), "test".toCharArray());
        
        BioReference bioRef = new BioReference("joee", "test ref");
        
        Collection<BioReference> bioRefs = bio.getBioReferences();
        bioRefs.add(bioRef);
        
        BioServiceDelegate bsd = new BioServiceDelegate(prop);
        try
        {
            bsd.addBio(bio);
        }
        catch (BioException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bsd != null)
            {
                bsd.close();
            }
        }
        System.out.println("Insert complete.");
    }
    
    
    
    
    
    public void addMoreBioReferences()
    {
        System.out.println("Inserting bio references...");
        
        Bio bio = new Bio("vaka", "vaka Oee Bio", "hia".getBytes(), "test".toCharArray());
        
        BioReference bioRef1 = new BioReference("vaka2 ref", "test ref2");
        BioReference bioRef2 = new BioReference("vaka3 ref", "test ref3");
        
        Collection<BioReference> bioRefs = bio.getBioReferences();
        bioRefs.add(bioRef1);
        bioRefs.add(bioRef2);
        
        BioServiceDelegate bsd = new BioServiceDelegate(prop);
        try
        {
            bsd.addBio(bio);
        }
        catch (BioException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bsd != null)
            {
                bsd.close();
            }
        }
        System.out.println("Insert complete.");
    }
    
    
    
    
    public void getAllBios()
    {
        System.out.println("gettings all bios...");
        
        BioServiceDelegate bsd = new BioServiceDelegate(prop);
        try
        {
            Collection<Bio> bios = bsd.getAllBios();
            
            for (Bio bio : bios)
            {
                System.out.println("Bio - name: " + bio.getName() + ", " + bio.getInfo() + ", " + bio.getAdvert());
                
                for (BioReference bioRef : bio.getBioReferences())
                {
                    System.out.println("BioRef - name: " + bioRef.getName() + ", " + bioRef.getUrl());
                }
            }
        } 
        catch (BioException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            bsd.close();
        }
        
    }

    
    
    
    public void getBioReferencesForBio()
    {
        System.out.println("gettings all bio references...");
        
        BioServiceDelegate bsd = new BioServiceDelegate(prop);
        try
        {
            Bio bio = bsd.getBio(Long.valueOf(4));
            
            for (BioReference bioRef : bio.getBioReferences())
            {
                System.out.println("Bio reference - id: " + bioRef.getId() + ", name: " + bioRef.getName() + ", url: " + bioRef.getUrl());
            }
        } 
        catch (BioException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            bsd.close();
        }
        
    }
    
    
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) 
    {
		Client client = new Client();
		client.init();
		client.getBioReferencesForBio();
	}

}
