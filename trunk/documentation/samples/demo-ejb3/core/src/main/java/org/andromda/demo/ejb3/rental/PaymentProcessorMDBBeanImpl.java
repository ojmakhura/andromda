// license-header java merge-point
package org.andromda.demo.ejb3.rental;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * @see org.andromda.demo.ejb3.rental.PaymentProcessorMDBBean
 */
public class PaymentProcessorMDBBeanImpl
    extends org.andromda.demo.ejb3.rental.PaymentProcessorMDBBean
{


    // --------- Default Constructor ----------
    
    public PaymentProcessorMDBBeanImpl()
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
            
            ObjectMessage msg = null;
            if (message != null && message instanceof ObjectMessage)
            {
                msg = (ObjectMessage)message;
                RentalCar car = (RentalCar)msg.getObject();
                
                if (car != null)
                {
                    System.out.println("Message = " + car.getId() + ", " + car.getSerial() + ", " + car.getName());
                    this.paymentService.register(car, 10);
                }
                else
                {
                    System.out.println("Car is null");
                }
            }
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}