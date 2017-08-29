package com.rw.androidutils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author        : ravindu
 * Date          : 6/07/16
 * Description   : Android Log class
 */
public class Log
{
    public enum Level
    {
        LogLevelInfo,
        LogLevelWarning,
        LogLevelError
    }

    private static boolean ENABLED = false;
    private static Level LEVEL = Level.LogLevelInfo;
    private static String TAG = "IMG";
    private static boolean isInitialised = false;
    private static String SYSTEMPROP = "log.tag.vortilla";


    /**
     * Initialize the log system
     * Logs will be enabled for 'Debug' builds and disabled for all non-debug builds
     * Logs will also be enabled if the system property log.tag.vortilla is set to "DEBUG" or "VERBOSE" regardless of build type
     */
    private static synchronized void initialize()
    {
        if(!isInitialised)
        {
            isInitialised = true;

            if(isDebugBuild())
                ENABLED = true;
            else if(getSystemProperty(SYSTEMPROP).equalsIgnoreCase("DEBUG"))
            {
                ENABLED = true;
                LEVEL = Level.LogLevelWarning;
            }
            else if(getSystemProperty(SYSTEMPROP).equalsIgnoreCase("VERBOSE"))
            {
                ENABLED = true;
                LEVEL = Level.LogLevelInfo;
            }
            else
            {
                ENABLED = false;
                android.util.Log.d(TAG, "call 'setprop " + SYSTEMPROP + " DEBUG' or 'setprop " + SYSTEMPROP + " VERBOSE' to enable logs");
            }
        }
    }



    /**
     * Enable Logging
     */
    public static void enable()
    {
        initialize();
        ENABLED = true;
    }

    /**
     * Disable Logging
     */
    public static void disable()
    {
        initialize();
        ENABLED = false;
    }

    /**
     * Is logging currently enabled
     * @return true if enabled, false if not
     */
    public static boolean isEnabled()
    {
        initialize();
        return ENABLED;
    }


    /**
     * Set minimum log level.
     * Eg. if level is LogLevelWarning, only warnings and errors will be logged
     *
     * @param level - the minimum level
     */
    public static void setLevel(Level level)
    {
        LEVEL = level;
    }

    /**
     * Set the tag for Android Logcat
     *
     * @param tag Log tag for adb
     */
    public static void setTag(String tag)
    {
        initialize();
        TAG = tag;
    }

    /**
     * Make info log
     * Required Level - LogLevelInfo
     *
     * @param message Log message
     */
    public static void i(String message)
    {
        initialize();

        if (ENABLED && LEVEL == Level.LogLevelInfo)
            android.util.Log.i(TAG, message);
    }


    /**
     * Make warning log
     * Required level - LogLevelInfo or LogLevelWarning
     *
     * @param message Log message
     */
    public static void d(String message)
    {
        initialize();

        if (ENABLED && (LEVEL == Level.LogLevelInfo || LEVEL == Level.LogLevelWarning))
            android.util.Log.d(TAG, message);
    }

    /**
     * Make error log
     * Required level - Any
     *
     * @param message Log message
     */
    public static void e(String message)
    {
        initialize();

        if (ENABLED)
            android.util.Log.e(TAG, message);
    }

    /**
     * Debug log with current time
     * Required level - LogLevelInfo or LogLevelWarning
     *
     * @param message Log message
     */
    public static void p(String message)
    {
        initialize();

        if (ENABLED && (LEVEL == Level.LogLevelInfo || LEVEL == Level.LogLevelWarning))
        {
            android.util.Log.d(TAG, System.currentTimeMillis() + " : " + message);
        }
    }


    /**
     * Checks if the app is running a debug or release build
     * @return true if the build type is debug
     */
    private static boolean isDebugBuild()
    {
        boolean sDebug;

        try
        {
            final Class<?> activityThread = Class.forName("android.app.ActivityThread");
            final Method currentPackage = activityThread.getMethod("currentPackageName");
            final String packageName = (String) currentPackage.invoke(null, (Object[]) null);
            final Class<?> buildConfig = Class.forName(packageName + ".BuildConfig");
            final Field DEBUG = buildConfig.getField("DEBUG");
            DEBUG.setAccessible(true);
            sDebug = DEBUG.getBoolean(null);
        }
        catch (final Throwable t)
        {
            final String message = t.getMessage();
            // Proguard obfuscated build. Most likely a production build.
            sDebug = !(message != null && message.contains("BuildConfig")) && BuildConfig.DEBUG;
        }

        return sDebug;
    }


    /**
     * check for the "log..tag.vortilla" system property
     * @return true if the system property value is "true"
     */
    private static String getSystemProperty(String prop)
    {
        String result = "";
        try
        {
            Process ifc = Runtime.getRuntime().exec("getprop " + prop);
            BufferedReader bis = new BufferedReader(new InputStreamReader(ifc.getInputStream()));
            result = bis.readLine();

            ifc.destroy();
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
