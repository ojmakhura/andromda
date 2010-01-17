package org.andromda.cartridges.jsf.portlet.myfaces.tomahawk.support;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.servlet.ServletOutputStream;

/**
 * This class is a dummy ServletOutputStream.
 *
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 *
 */
public class ServletOutputStreamWrapper extends ServletOutputStream {
    private OutputStream outputStream;

    public ServletOutputStreamWrapper(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Writes a <code>String</code> to the client, without a carriage
     * return-line feed (CRLF) character at the end.
     *
     *
     * @param s
     *            the <code>String</code> to send to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void print(String s) throws IOException {
        if (s == null)
            s = "null";
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);

            //
            // XXX NOTE: This is clearly incorrect for many strings,
            // but is the only consistent approach within the current
            // servlet framework. It must suffice until servlet output
            // streams properly encode their output.
            //
            if ((c & 0xff00) != 0) { // high order byte must be zero
                String errMsg = "Not an ISO 8859-1 character: {0}";
                Object[] errArgs = new Object[1];
                errArgs[0] = new Character(c);
                errMsg = MessageFormat.format(errMsg, errArgs);
                throw new CharConversionException(errMsg);
            }
            write(c);
        }
    }

    /**
     * Writes a <code>boolean</code> value to the client, with no carriage
     * return-line feed (CRLF) character at the end.
     *
     * @param b
     *            the <code>boolean</code> value to send to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void print(boolean b) throws IOException {
        String msg;
        if (b) {
            msg = "true";
        } else {
            msg = "false";
        }
        print(msg);
    }

    /**
     * Writes a character to the client, with no carriage return-line feed
     * (CRLF) at the end.
     *
     * @param c
     *            the character to send to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void print(char c) throws IOException {
        print(String.valueOf(c));
    }

    /**
     *
     * Writes an int to the client, with no carriage return-line feed (CRLF) at
     * the end.
     *
     * @param i
     *            the int to send to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void print(int i) throws IOException {
        print(String.valueOf(i));
    }

    /**
     *
     * Writes a <code>long</code> value to the client, with no carriage
     * return-line feed (CRLF) at the end.
     *
     * @param l
     *            the <code>long</code> value to send to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void print(long l) throws IOException {
        print(String.valueOf(l));
    }

    /**
     *
     * Writes a <code>float</code> value to the client, with no carriage
     * return-line feed (CRLF) at the end.
     *
     * @param f
     *            the <code>float</code> value to send to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     *
     */
    public void print(float f) throws IOException {
        print(String.valueOf(f));
    }

    /**
     *
     * Writes a <code>double</code> value to the client, with no carriage
     * return-line feed (CRLF) at the end.
     *
     * @param d
     *            the <code>double</code> value to send to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void print(double d) throws IOException {
        print(String.valueOf(d));
    }

    /**
     * Writes a carriage return-line feed (CRLF) to the client.
     *
     *
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println() throws IOException {
        print("\r\n");
    }

    /**
     * Writes a <code>String</code> to the client, followed by a carriage
     * return-line feed (CRLF).
     *
     *
     * @param s
     *            the <code>String</code> to write to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println(String s) throws IOException {
        print(s);
        println();
    }

    /**
     *
     * Writes a <code>boolean</code> value to the client, followed by a
     * carriage return-line feed (CRLF).
     *
     *
     * @param b
     *            the <code>boolean</code> value to write to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println(boolean b) throws IOException {
        print(b);
        println();
    }

    /**
     *
     * Writes a character to the client, followed by a carriage return-line feed
     * (CRLF).
     *
     * @param c
     *            the character to write to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println(char c) throws IOException {
        print(c);
        println();
    }

    /**
     *
     * Writes an int to the client, followed by a carriage return-line feed
     * (CRLF) character.
     *
     *
     * @param i
     *            the int to write to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println(int i) throws IOException {
        print(i);
        println();
    }

    /**
     *
     * Writes a <code>long</code> value to the client, followed by a carriage
     * return-line feed (CRLF).
     *
     *
     * @param l
     *            the <code>long</code> value to write to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println(long l) throws IOException {
        print(l);
        println();
    }

    /**
     *
     * Writes a <code>float</code> value to the client, followed by a carriage
     * return-line feed (CRLF).
     *
     * @param f
     *            the <code>float</code> value to write to the client
     *
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println(float f) throws IOException {
        print(f);
        println();
    }

    /**
     *
     * Writes a <code>double</code> value to the client, followed by a
     * carriage return-line feed (CRLF).
     *
     *
     * @param d
     *            the <code>double</code> value to write to the client
     *
     * @exception IOException
     *                if an input or output exception occurred
     *
     */
    public void println(double d) throws IOException {
        print(d);
        println();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.OutputStream#close()
     */
    public void close() throws IOException {
        outputStream.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return outputStream.equals(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.OutputStream#flush()
     */
    public void flush() throws IOException {
        outputStream.flush();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return outputStream.hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return outputStream.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

}
