// license-header java merge-point
package org.andromda.demo.common;

/**
 * Interceptor class DefaultInterceptor1
 */
public class DefaultInterceptor1 
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
        System.out.println("Default interceptor1");
        
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
