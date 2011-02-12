package org.andromda.demo.ejb3.client.employee;

import java.util.Collection;
import java.util.Properties;
import org.andromda.demo.ejb3.employee.Employee;
import org.andromda.demo.ejb3.employee.EmployeeContractType;
import org.andromda.demo.ejb3.employee.EmployeeDeptCode;
import org.andromda.demo.ejb3.employee.EmployeeException;
import org.andromda.demo.ejb3.employee.EmployeeServiceDelegate;

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
    
    
    
    
    public void insertEmployee()
    {
        System.out.println("Inserting employee...");
        
        Employee employee = new Employee("John", EmployeeContractType.PART_TIME, EmployeeDeptCode.MARKETING);
        
        EmployeeServiceDelegate manager = new EmployeeServiceDelegate(prop);
        try
        {
            manager.addEmployee(employee);
        } catch (EmployeeException e)
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
    
    
    
    
    
    public void getEmployee()
    {
        System.out.println("getting employee...");
        
        EmployeeServiceDelegate manager = new EmployeeServiceDelegate(prop);
        try
        {
            Employee employee = manager.getEmployee(2);
            System.out.println("Employee " + employee.getId() + ", " + employee.getName() + ", " + employee.getContractType().name() + ", " + employee.getDepartmentCode().ordinal());
        } 
        catch (EmployeeException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            manager.close();
        }
    }

    
    
    
    public void getEmployeesByContract()
    {
        System.out.println("getting employees by contract...");
        
        EmployeeServiceDelegate manager = new EmployeeServiceDelegate(prop);
        try
        {
            Collection<Employee> employees = manager.getEmployeesByContract(EmployeeContractType.PART_TIME);
            for (Employee employee : employees)
            {
                System.out.println("Employee " + employee.getId() + ", " + employee.getName() + ", " + employee.getContractType().name() + ", " + employee.getDepartmentCode().getBarcode());
            }
            
        } 
        catch (EmployeeException e)
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
        client.getEmployeesByContract();
    }

}
