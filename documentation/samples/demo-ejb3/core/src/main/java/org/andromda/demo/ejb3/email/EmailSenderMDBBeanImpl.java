// license-header java merge-point
package org.andromda.demo.ejb3.email;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;

/**
 * @see org.andromda.demo.ejb3.email.EmailSenderMDBBean
 */
public class EmailSenderMDBBeanImpl
    extends org.andromda.demo.ejb3.email.EmailSenderMDBBean
{


    // --------- Default Constructor ----------
    
    public EmailSenderMDBBeanImpl()
    {
        super();
    }
    
    /**
     * MessageListener callback on arrival of a JMS message
     *
     * @param message The inbound JMS message to process
     */
    public void onMessage(javax.jms.Message message)
    {
        try
        {
            System.out.println("Received new message: " + message.getJMSTimestamp());
            
            TextMessage txtMessage = null;
            if (message != null && message instanceof TextMessage)
            {
                txtMessage = (TextMessage)message;
                
                System.out.println("Waiting...");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException e)
                {
                }
                
                System.out.println("Message = " + txtMessage.getText());
            }
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //insertAccount();
    }

    public void insertAccount()
    {
        System.out.println("Inserting account...");
        
        Account account = new Account("test");

        try
        {
            accountManager.addAccount(account);
        } 
        catch (AccountException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("Insert complete.");
    }
}