package org.andromda.cartridges.angular.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

public class AngularHelper {

    public static Collection<String> getArgumentsAsList(final String args) {
        StringTokenizer st = new StringTokenizer(args, ",");
        Collection<String> retval = new ArrayList<String>(st.countTokens());
        while (st.hasMoreTokens()) {
            retval.add(st.nextToken().trim());
        }
        return retval;
    }


    public static String getArgumentAsString(final Collection args, String token) {
        String stringArgument = "";
        int counter = 0;
        Iterator<String> iter1 = args.iterator();
        while (iter1.hasNext()) {
            if (counter == 0) {
                stringArgument = iter1.next().toString();
            } else {
                stringArgument = stringArgument + token + iter1.next().toString();
            }
            counter++;
        }

        return stringArgument;
    }

}
