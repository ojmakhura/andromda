// license-header java merge-point
package org.andromda.demo.ejb3.email;

/**
 * Interceptor class EmailSenderInterceptor3
 */
public class EmailSenderInterceptor3 
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
        System.out.println("interceptor 3");
        
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
