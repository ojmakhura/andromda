package org.andromda.cartridges.support.webservice.client;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;


/**
 * FactoryBean for a specific port of a webservice.
 */
public class Axis2ProxyFactoryBean
    extends Axis2PortClientInterceptor
    implements FactoryBean
{
    private Object serviceProxy;

    /**
     * @see org.axis2test4.Axis2PortClientInterceptor#afterPropertiesSet()
     */
    public void afterPropertiesSet()
    {
        if (this.getServiceInterface() == null)
        {
            throw new IllegalArgumentException("serviceInterface is required");
        }
        if (this.getWsdlUrl() == null)
        {
            throw new IllegalArgumentException("WSDL URL is required");
        }
        super.afterPropertiesSet();
        this.serviceProxy =
            ProxyFactory.getProxy(
                getServiceInterface(),
                this);
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Object getObject()
    {
        return this.serviceProxy;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class getObjectType()
    {
        return getServiceInterface();
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton()
    {
        return true;
    }
}