package org.andromda.android.core.internal;

/**
 *
 * @author Peter Friese
 * @since 07.10.2005
 */
public class AndroidModelManager
{

    private static AndroidModelManager instance;

    private final AndroidModel androidModel;

    private AndroidModelManager()
    {
        androidModel = new AndroidModel();
    }

    public static AndroidModelManager getInstance()
    {
        if (instance == null)
        {
            instance = new AndroidModelManager();
        }
        return instance;
    }

    public AndroidModel getAndroidModel()
    {
        return androidModel;
    }

}
