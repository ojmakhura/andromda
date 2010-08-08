/**
 * 
 */
package org.andromda.timetracker.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author vancek
 *
 */
public class PasswordEncoder
{
    public static String getMD5Base64EncodedPassword(String plaintext) 
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plaintext.getBytes("UTF8"));
        byte raw[] = md.digest();
        String hash = new sun.misc.BASE64Encoder().encode(raw);
        return hash;
    }
}
