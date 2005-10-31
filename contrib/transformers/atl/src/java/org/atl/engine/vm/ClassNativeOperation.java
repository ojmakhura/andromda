package org.atl.engine.vm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.atl.engine.vm.nativelib.ASMOclAny;

/**
 * @author Frédéric Jouault
 */
public class ClassNativeOperation
        extends NativeOperation
{

    // The Method must be static and must have <type> self as a first parameter.
    public ClassNativeOperation(Method method)
    {
        super(method);
    }

    public ASMOclAny exec(StackFrame frame)
    {
        ASMOclAny ret = null;

        List args = new ArrayList(frame.getArgs());
        args.add(0, frame);

        try
        {
            ret = (ASMOclAny)getMethod().invoke(null, args.toArray());
        } catch (IllegalAccessException iae)
        {
            frame.printStackTrace(iae);
            ret = null;
        } catch (IllegalArgumentException iae2)
        {
            frame.printStackTrace(iae2);
            dumpMethod(getMethod());
            dumpArgs(args);
            ret = null;
        } catch (InvocationTargetException ite)
        {
            frame.printStackTrace(ite);
            ret = null;
        }
        ((NativeStackFrame)frame).setRet(ret);
        frame.leaveFrame();

        return ret;
    }

    private void dumpMethod(Method method)
    {
        System.out.println("Formal arguments of the method:");

        Class[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++)
        {
            System.out.println(String.valueOf(i) + ":   " + parameterTypes[i]);
        }

    }

    private void dumpArgs(List args)
    {
        System.out
                .println("Actual parameter types to be passed in to the method:");
        int i = 0;
        for (Iterator iter = args.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            System.out.println(String.valueOf(i) + ":   "
                    + element.getClass().getName());
            i++;
        }
    }

}
