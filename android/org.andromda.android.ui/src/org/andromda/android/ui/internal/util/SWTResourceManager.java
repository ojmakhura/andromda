/*
 * ResourceManager.java created on Feb 1, 2005
 *
 * Copyright (c) 2004, 2005 Lufthansa Systems
 * All rights reserved.
 *
 * This program and the accompanying materials are proprietary information
 * of Lufthansa Systems Business Solutions GmbH.
 * Use is subject to license terms.
 */
package org.andromda.android.ui.internal.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for managing OS resources associated with SWT controls such as colors, fonts, images, etc. !!!
 * IMPORTANT !!! Application code must explicitly invoke the <code>dispose()</code> method to release the operating
 * system resources managed by cached objects when those objects and OS resources are no longer needed (e.g. on
 * application shutdown) This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2003 - 2004, Instantiations, Inc. <br>
 * All Rights Reserved
 *
 * @author scheglov_ke
 * @author Dan Rubel
 */
public final class SWTResourceManager
{

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private SWTResourceManager()
    {
        // no implementation
    }

    /**
     * Dispose of cached objects and their underlying OS resources. This should only be called when the cached objects
     * are no longer needed (e.g. on application shutdown)
     */
    public static void dispose()
    {
        disposeColors();
        disposeFonts();
        disposeImages();
        disposeCursors();
    }

    // ////////////////////////////
    // Color support
    // ////////////////////////////

    /** Maps RGB values to colors. */
    private static HashMap colorMap = new HashMap();

    /**
     * Returns the system color matching the specific ID.
     *
     * @param systemColorID int The ID value for the color
     * @return Color The system color matching the specific ID
     */
    public static Color getColor(int systemColorID)
    {
        Display display = Display.getCurrent();
        return display.getSystemColor(systemColorID);
    }

    /**
     * Returns a color given its red, green and blue component values.
     *
     * @param r int The red component of the color
     * @param g int The green component of the color
     * @param b int The blue component of the color
     * @return Color The color matching the given red, green and blue componet values
     */
    public static Color getColor(int r,
        int g,
        int b)
    {
        return getColor(new RGB(r, g, b));
    }

    /**
     * Returns a color given its RGB value.
     *
     * @param rgb RGB The RGB value of the color
     * @return Color The color matching the RGB value
     */
    public static Color getColor(RGB rgb)
    {
        Color color = (Color)colorMap.get(rgb);
        if (color == null)
        {
            Display display = Display.getCurrent();
            color = new Color(display, rgb);
            colorMap.put(rgb, color);
        }
        return color;
    }

    /**
     * Dispose of all the cached colors.
     */
    public static void disposeColors()
    {
        for (Iterator iter = colorMap.values().iterator(); iter.hasNext();)
        {
            ((Color)iter.next()).dispose();
        }
        colorMap.clear();
    }

    // ////////////////////////////
    // Image support
    // ////////////////////////////

    /** Maps image names to images. */
    private static HashMap classImageMap = new HashMap();

    /** Maps image descriptors to images. */
    private static HashMap descriptorImageMap = new HashMap();

    /** Maps images to image decorators. */
    private static HashMap imageToDecoratorMap = new HashMap();

    /**
     * Returns an image encoded by the specified input stream.
     *
     * @param is InputStream The input stream encoding the image data
     * @return Image The image encoded by the specified input stream
     */
    private static Image getImage(InputStream is)
    {
        Display display = Display.getCurrent();
        ImageData data = new ImageData(is);
        if (data.transparentPixel > 0)
        {
            return new Image(display, data, data.getTransparencyMask());
        }
        return new Image(display, data);
    }

    /**
     * Returns an image stored in the file at the specified path.
     *
     * @param path String The path to the image file
     * @return Image The image stored in the file at the specified path
     */
    public static Image getImage(String path)
    {
        return getImage("default", path);
    }

    /**
     * Returns an image stored in the file at the specified path.
     *
     * @param section The section to which belongs specified image
     * @param path String The path to the image file
     * @return Image The image stored in the file at the specified path
     */
    public static Image getImage(String section,
        String path)
    {
        String key = section + "|" + SWTResourceManager.class.getName() + "|" + path;
        Image image = (Image)classImageMap.get(key);
        if (image == null)
        {
            try
            {
                FileInputStream fis = new FileInputStream(path);
                image = getImage(fis);
                classImageMap.put(key, image);
                fis.close();
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return image;
    }

    /**
     * Returns an image stored in the file at the specified path relative to the specified class.
     *
     * @param clazz Class The class relative to which to find the image
     * @param path String The path to the image file
     * @return Image The image stored in the file at the specified path
     */
    public static Image getImage(Class clazz,
        String path)
    {
        String key = clazz.getName() + "|" + path;
        Image image = (Image)classImageMap.get(key);
        if (image == null)
        {
            if (path.length() > 0 && path.charAt(0) == '/')
            {
                String newPath = path.substring(1, path.length());
                image = getImage(new BufferedInputStream(clazz.getClassLoader().getResourceAsStream(newPath)));
            }
            else
            {
                image = getImage(clazz.getResourceAsStream(path));
            }
            classImageMap.put(key, image);
        }
        return image;
    }

    /**
     * Returns an image descriptor stored in the file at the specified path relative to the specified class.
     *
     * @param clazz Class The class relative to which to find the image descriptor
     * @param path String The path to the image file
     * @return ImageDescriptor The image descriptor stored in the file at the specified path
     */
    public static ImageDescriptor getImageDescriptor(Class clazz,
        String path)
    {
        return ImageDescriptor.createFromFile(clazz, path);
    }

    /**
     * Returns an image descriptor stored in the file at the specified path.
     *
     * @param path String The path to the image file
     * @return ImageDescriptor The image descriptor stored in the file at the specified path
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        try
        {
            return ImageDescriptor.createFromURL((new File(path)).toURL());
        }
        catch (MalformedURLException e)
        {
            return null;
        }
    }

    /**
     * Returns an image based on the specified image descriptor.
     *
     * @param descriptor ImageDescriptor The image descriptor for the image
     * @return Image The image based on the specified image descriptor
     */
    public static Image getImage(ImageDescriptor descriptor)
    {
        if (descriptor == null)
        {
            return null;
        }
        Image image = (Image)descriptorImageMap.get(descriptor);
        if (image == null)
        {
            image = descriptor.createImage();
            descriptorImageMap.put(descriptor, image);
        }
        return image;
    }

    /**
     * Returns an image composed of a base image decorated by another image.
     *
     * @param baseImage Image The base image that should be decorated
     * @param decorator Image The image to decorate teh base image
     * @return Image The resulting decorated image
     */
    public static Image decorateImage(Image baseImage,
        Image decorator)
    {
        HashMap decoratedMap = (HashMap)imageToDecoratorMap.get(baseImage);
        if (decoratedMap == null)
        {
            decoratedMap = new HashMap();
            imageToDecoratorMap.put(baseImage, decoratedMap);
        }
        Image result = (Image)decoratedMap.get(decorator);
        if (result == null)
        {
            ImageData bid = baseImage.getImageData();
            ImageData did = decorator.getImageData();
            result = new Image(Display.getCurrent(), bid.width, bid.height);
            GC gc = new GC(result);
            //
            gc.drawImage(baseImage, 0, 0);
            gc.drawImage(decorator, bid.width - did.width - 1, bid.height - did.height - 1);
            //
            gc.dispose();
            decoratedMap.put(decorator, result);
        }
        return result;
    }

    /**
     * Dispose all of the cached images.
     */
    public static void disposeImages()
    {
        for (Iterator it = classImageMap.values().iterator(); it.hasNext();)
        {
            ((Image)it.next()).dispose();
        }
        classImageMap.clear();
        for (Iterator it = descriptorImageMap.values().iterator(); it.hasNext();)
        {
            ((Image)it.next()).dispose();
        }
        descriptorImageMap.clear();
    }

    /**
     * Dispose cached images in specified section.
     *
     * @param section the section do dispose
     */
    public static void disposeImages(String section)
    {
        for (Iterator it = classImageMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String)it.next();
            if (!key.startsWith(section + "|"))
            {
                continue;
            }
            Image image = (Image)classImageMap.get(key);
            image.dispose();
            it.remove();
        }
    }

    // ////////////////////////////
    // Plugin images support
    // ////////////////////////////

    /** Maps URL to images. */
    private static HashMap urlImageMap = new HashMap();

    /**
     * Retuns an image based on a plugin and file path.
     *
     * @param plugin Object The plugin containing the image
     * @param name String The path to th eimage within the plugin
     * @return Image The image stored in the file at the specified path
     */
    public static Image getPluginImage(Plugin plugin,
        String name)
    {
        try
        {
            try
            {
                URL url = getPluginImageURL(plugin, name);
                if (urlImageMap.containsKey(url))
                {
                    return (Image)urlImageMap.get(url);
                }
                InputStream is = url.openStream();
                Image image;
                try
                {
                    image = getImage(is);
                    urlImageMap.put(url, image);
                }
                finally
                {
                    is.close();
                }
                return image;
            }
            catch (Throwable e)
            {
                AndroidUIPlugin.log(e);
            }
        }
        catch (Throwable e)
        {
            AndroidUIPlugin.log(e);
        }
        return null;
    }

    /**
     * Retuns an image descriptor based on a plugin and file path.
     *
     * @param plugin Object The plugin containing the image
     * @param name String The path to th eimage within the plugin
     * @return ImageDescriptor The image descriptor stored in the file at the specified path
     */
    public static ImageDescriptor getPluginImageDescriptor(Plugin plugin,
        String name)
    {
        try
        {
            try
            {
                URL url = getPluginImageURL(plugin, name);
                return ImageDescriptor.createFromURL(url);
            }
            catch (Throwable e)
            {
                AndroidUIPlugin.log(e);
            }
        }
        catch (Throwable e)
        {
            AndroidUIPlugin.log(e);
        }
        return null;
    }

    /**
     * Retuns an URL based on a plugin and file path.
     *
     * @param plugin Object The plugin containing the file path
     * @param name String The file path
     * @return URL The URL representing the file at the specified path
     * @throws Exception in case of a problem...
     */
    private static URL getPluginImageURL(Plugin plugin,
        String name) throws Exception
    {
        return plugin.getBundle().getEntry(name);
    }

    // ////////////////////////////
    // Font support
    // ////////////////////////////

    /** Maps font names to fonts. */
    private static HashMap fontMap = new HashMap();

    /** Maps fonts to their bold versions. */
    private static HashMap fontToBoldFontMap = new HashMap();

    /** Maps fonts to their italic versions. */
    private static HashMap fontToItalicFontMap = new HashMap();

    /** Maps fonts to their "other-sized" versions. */
    private static HashMap fontToHeightFontMap = new HashMap();

    /**
     * Returns a font based on its name, height and style.
     *
     * @param name String The name of the font
     * @param height int The height of the font
     * @param style int The style of the font
     * @return Font The font matching the name, height and style
     */
    public static Font getFont(String name,
        int height,
        int style)
    {
        String fullName = name + "|" + height + "|" + style;
        Font font = (Font)fontMap.get(fullName);
        if (font == null)
        {
            font = new Font(Display.getCurrent(), name, height, style);
            fontMap.put(fullName, font);
        }
        return font;
    }

    /**
     * Return a bold version of the give font.
     *
     * @param baseFont Font The font for whoch a bold version is desired
     * @return Font The bold version of the give font
     */
    public static Font getBoldFont(Font baseFont)
    {
        Font font = (Font)fontToBoldFontMap.get(baseFont);
        if (font == null)
        {
            FontData[] fontDatas = baseFont.getFontData();
            FontData data = fontDatas[0];
            font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.BOLD);
            fontToBoldFontMap.put(baseFont, font);
        }
        return font;
    }

    /**
     * Return a italic version of the give font.
     *
     * @param baseFont Font The font for whoch a italic version is desired
     * @return Font The italic version of the give font
     */
    public static Font getItalicFont(Font baseFont)
    {
        Font font = (Font)fontToItalicFontMap.get(baseFont);
        if (font == null)
        {
            FontData[] fontDatas = baseFont.getFontData();
            FontData data = fontDatas[0];
            font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.ITALIC);
            fontToItalicFontMap.put(baseFont, font);
        }
        return font;
    }

    /**
     * Returns a new sized version of the given font.
     *
     * @param baseFont the font to be used as base
     * @param height the requested height
     * @return the new sized base font
     */
    public static Font getSizedFont(Font baseFont,
        int height)
    {
        String key = baseFont.toString() + height;
        Font font = (Font)fontToHeightFontMap.get(key);
        if (font == null)
        {
            FontData[] fontDatas = baseFont.getFontData();
            FontData data = fontDatas[0];
            font = new Font(Display.getCurrent(), data.getName(), height, data.getStyle());
            fontToHeightFontMap.put(key, font);
        }
        return font;
    }

    /**
     * Dispose all of the cached fonts.
     */
    public static void disposeFonts()
    {
        for (Iterator iter = fontMap.values().iterator(); iter.hasNext();)
        {
            ((Font)iter.next()).dispose();
        }
        fontMap.clear();
        for (Iterator iter = fontToBoldFontMap.values().iterator(); iter.hasNext();)
        {
            ((Font)iter.next()).dispose();
        }
        fontToBoldFontMap.clear();
        for (Iterator iter = fontToItalicFontMap.values().iterator(); iter.hasNext();)
        {
            ((Font)iter.next()).dispose();
        }
        fontToItalicFontMap.clear();
        for (Iterator iter = fontToHeightFontMap.values().iterator(); iter.hasNext();)
        {
            ((Font)iter.next()).dispose();
        }
        fontToHeightFontMap.clear();
    }

    // ////////////////////////////
    // CoolBar support
    // ////////////////////////////

    /** Size for fixed CoolItem. */
    private static final int COOL_BAR_FIX_SIZE = 20;

    /**
     * Fix the layout of the specified CoolBar.
     *
     * @param bar CoolBar The CoolBar that shgoud be fixed
     */
    public static void fixCoolBarSize(CoolBar bar)
    {
        CoolItem[] items = bar.getItems();
        // ensure that each item has control (at least empty one)
        for (int i = 0; i < items.length; i++)
        {
            CoolItem item = items[i];
            if (item.getControl() == null)
            {
                item.setControl(new Canvas(bar, SWT.NONE)
                {

                    public Point computeSize(int wHint,
                        int hHint,
                        boolean changed)
                    {
                        return new Point(COOL_BAR_FIX_SIZE, COOL_BAR_FIX_SIZE);
                    }
                });
            }
        }
        // compute size for each item
        for (int i = 0; i < items.length; i++)
        {
            CoolItem item = items[i];
            Control control = item.getControl();
            control.pack();
            Point size = control.getSize();
            item.setSize(item.computeSize(size.x, size.y));
        }
    }

    // ////////////////////////////
    // Cursor support
    // ////////////////////////////

    /** Maps IDs to cursors. */
    private static HashMap idToCursorMap = new HashMap();

    /**
     * Returns the system cursor matching the specific ID.
     *
     * @param id int The ID value for the cursor
     * @return Cursor The system cursor matching the specific ID
     */
    public static Cursor getCursor(int id)
    {
        Integer key = new Integer(id);
        Cursor cursor = (Cursor)idToCursorMap.get(key);
        if (cursor == null)
        {
            cursor = new Cursor(Display.getDefault(), id);
            idToCursorMap.put(key, cursor);
        }
        return cursor;
    }

    /**
     * Dispose all of the cached cursors.
     */
    public static void disposeCursors()
    {
        for (Iterator iter = idToCursorMap.values().iterator(); iter.hasNext();)
        {
            ((Cursor)iter.next()).dispose();
        }
        idToCursorMap.clear();
    }
}