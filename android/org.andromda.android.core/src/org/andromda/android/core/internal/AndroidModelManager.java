package org.andromda.android.core.internal;

/**
 * The Android model manager.
 * 
 * @author Peter Friese
 * @since 07.10.2005
 */
public final class AndroidModelManager
{

    /** The singleton model manager instance. */
    private static AndroidModelManager instance;

    /** The Android model. */
    private final AndroidModel androidModel;

    /**
     * Creates a new AndroidModelManager.
     */
    private AndroidModelManager()
    {
        androidModel = new AndroidModel();
    }

    /**
     * Provides static access to the model manager.
     * 
     * @return The singleton model manager instance.
     */
    public static AndroidModelManager getInstance()
    {
        if (instance == null)
        {
            instance = new AndroidModelManager();
        }
        return instance;
    }

    /**
     * @return The Android model.
     */
    public AndroidModel getAndroidModel()
    {
        return androidModel;
    }

}
