// license-header java merge-point
package org.andromda.demo.ejb3.animal;

/**
 * Interceptor class AnimalInterceptor1
 */
public class AnimalInterceptor1 
{
    /**
     * Default interceptor execution method
     *
     * @param ctx the invocation context
     * @return 
     */
    @javax.interceptor.AroundInvoke
    public Object execute(javax.interceptor.InvocationContext ctx)
        throws Exception 
    {
        System.out.println("interceptor 1");
        
        try
        {
            return ctx.proceed();
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
