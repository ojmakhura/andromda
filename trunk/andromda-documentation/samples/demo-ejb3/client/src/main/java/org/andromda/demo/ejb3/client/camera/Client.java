package org.andromda.demo.ejb3.client.camera;

import java.util.Date;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.andromda.demo.ejb3.camera.Camera;
import org.andromda.demo.ejb3.camera.CameraException;
import org.andromda.demo.ejb3.camera.CameraManagerDelegate;
import org.andromda.demo.ejb3.camera.CameraPK;

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
    
    
    
    
    public void insertCamera()
    {
        System.out.println("Inserting camera...");
        
        Camera camera = new Camera(6.3, false, new Date());
        camera.setPk(new CameraPK("cannon", "300d"));
        
        CameraManagerDelegate manager = new CameraManagerDelegate(prop);
        try
        {
            manager.addCamera(camera);
        } catch (CameraException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Camera camera2 = new Camera(8, false, new Date());
        camera2.setPk(new CameraPK("cannon", "20d"));

        try
        {
            manager.addCamera(camera2);
        } catch (CameraException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
        
        System.out.println("Insert complete.");
    }

    
    
    
    
    public void deleteCamera()
    {
        System.out.println("deleting camera...");
        
        CameraManagerDelegate ams = new CameraManagerDelegate(prop);
        try
        {
            ams.deleteCamera("cannon", "20d");
        } 
        catch (CameraException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ams.close();
        }
        
        System.out.println("delete complete.");
    }
    
    
    
    
    public void getCamera()
    {
        System.out.println("getting camera...");
        
        CameraManagerDelegate ams = new CameraManagerDelegate(prop);
        try
        {
            Camera camera = ams.getCamera("cannon", "300d");
            System.out.println("Camera " + camera.getPk().getMake() + ", " + camera.getMegapixels() + ", " + camera.getSlr());
        } 
        catch (CameraException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            ams.close();
        }
    }

    
    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
        Client client = new Client();
        client.init();
        client.getCamera();
    }

}
