package org.andromda.demo.ejb3.client.animal;

import java.util.Collection;
import java.util.Properties;
import org.andromda.demo.ejb3.animal.Animal;
import org.andromda.demo.ejb3.animal.AnimalCreateException;
import org.andromda.demo.ejb3.animal.AnimalServiceDelegate;

/**
 *
 */
public class Client
{
    private Properties prop;

    /**
     *
     */
    public void init()
    {
        this.prop = new Properties();
        this.prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        this.prop.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        this.prop.put("java.naming.provider.url", "localhost");
    }

    /**
     *
     */
    public void insertAnimal()
    {
        System.out.println("Inserting animal...");

        Animal animal = new Animal("sheep", "farm", false);

        AnimalServiceDelegate manager = new AnimalServiceDelegate(this.prop);
        try
        {
            manager.addAnimal(animal);
        } catch (AnimalCreateException e)
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

    /**
     *
     */
    public void getAnimals()
    {
        System.out.println("getting animals...");

        AnimalServiceDelegate manager = new AnimalServiceDelegate(this.prop);
        try
        {
            Collection<Animal> animals = manager.getAllAnimals();
            for (Animal animal : animals)
            {
                System.out.println("Account " + animal.getName() + ", " + animal.getTypes() + ", " + animal.isCarnivor());
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Client client = new Client();
        client.init();
        client.getAnimals();
    }
}
