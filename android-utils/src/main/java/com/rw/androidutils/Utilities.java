package com.rw.androidutils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Author        : ravindu
 * Date          : 6/07/16
 * Description   : Frequently used utility functions
 */
@SuppressWarnings("WeakerAccess")
public class Utilities
{

    /**
     * Conver a Bitmap image into a byte array
     *
     * @param image   the input bitmap
     * @param format  compression format (PNG for no compression)
     * @param quality compression quality (0 - 100)
     * @return the resultant compressed byte array
     */
    public static byte[] bitmapToByteArray(Bitmap image, Bitmap.CompressFormat format, int quality)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(format, quality, stream);
        return stream.toByteArray();
    }

    /**
     * Reads a specified file and return the content as a single string
     *
     * @param context  The calling context
     * @param fileName The path to the target file
     * @return the content of the file in a single string
     */
    @Nullable
    public static String readStringFromFile(@NonNull Context context, @NonNull String fileName)
    {
        try
        {
            InputStream inputStream = context.openFileInput(fileName);


            if (inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                return stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e)
        {
            Log.d("File not found: " + e.toString());
        }
        catch (IOException e)
        {
            Log.d("Can not read file: " + e.toString());
        }
        return null;
    }

    /**
     * Write a specified string to a text file. If the file exists, it will be overwritten
     *
     * @param context  the calling context
     * @param data     the string to be saved
     * @param fileName the target file path
     * @throws IOException
     */
    public static void writeStringToFile(@NonNull Context context, String data, @NonNull String fileName) throws IOException
    {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    /**
     * @see Utilities#showYesNoDialog(Context, String, String, String, DialogInterface.OnClickListener, String, DialogInterface.OnClickListener, boolean)
     *
     */
    public static @Nullable AlertDialog showYesNoDialog(@NonNull Context c, String title, String message, String yesText, @Nullable DialogInterface.OnClickListener yesListener, String noText, @Nullable DialogInterface.OnClickListener noListener)
    {
        return showYesNoDialog(c, title, message, yesText, yesListener, noText, noListener, false);
    }

    /**
     * Show a dialog with two buttons and two callbacks for the buttons
     *
     * @param c           the calling context
     * @param title       dialog title
     * @param message     dialog message body
     * @param yesText     text of the positive button
     * @param yesListener positive button callback (can be null)
     * @param noText      negative button text
     * @param noListener  negative button callback (can be null)
     * @param cancellable is dialog dismissible
     */
    public static @Nullable AlertDialog showYesNoDialog(@NonNull Context c, String title, String message, String yesText, @Nullable DialogInterface.OnClickListener yesListener, String noText, @Nullable DialogInterface.OnClickListener noListener, boolean cancellable)
    {
        AlertDialog dialog = null;
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(yesText, yesListener);
            builder.setNegativeButton(noText, noListener);
            builder.setCancelable(cancellable);

            dialog = builder.create();
            dialog.show();
        }
        catch(WindowManager.BadTokenException e)
        {
            e.printStackTrace();
        }

        return dialog;
    }

    /**
     * Show a dialog with a single confirm button
     *
     * @param c          calling context
     * @param title      dialog title
     * @param message    dialog message body
     * @param okText     button text
     * @param okListener button callback (can be null)
     * @param cancellable is dialog dismissible
     */
    public static @Nullable AlertDialog showOkDialog(@NonNull Context c, String title, String message, String okText, @Nullable DialogInterface.OnClickListener okListener, boolean cancellable)
    {
        AlertDialog dialog = null;
        try
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(okText, okListener);
            builder.setCancelable(cancellable);

            dialog = builder.create();
            dialog.show();
        }
        catch(WindowManager.BadTokenException e)
        {
            e.printStackTrace();
        }

        return dialog;
    }

    /**
     * @see Utilities#showOkDialog(Context, String, String, String, DialogInterface.OnClickListener, boolean)
     */
    public static @Nullable AlertDialog showOkDialog(@NonNull Context c, String title, String message, String okText, @Nullable DialogInterface.OnClickListener okListener)
    {
        return showOkDialog(c, title, message, okText, okListener, false);
    }


    public static @Nullable AlertDialog showYesNoCancelDialog(@NonNull Context c, String title, String message,
                                                              String yesText, @Nullable DialogInterface.OnClickListener yesListener,
                                                              String noText, @Nullable DialogInterface.OnClickListener noListener,
                                                              String cancelText, @Nullable DialogInterface.OnClickListener cancelListener)
    {
        return showYesNoCancelDialog(c, title, message, yesText, yesListener, noText, noListener, cancelText, cancelListener, false);
    }

    public static @Nullable AlertDialog showYesNoCancelDialog(@NonNull Context c, String title, String message,
                                                              String yesText, @Nullable DialogInterface.OnClickListener yesListener,
                                                              String noText, @Nullable DialogInterface.OnClickListener noListener,
                                                              String cancelText, @Nullable DialogInterface.OnClickListener cancelListener,
                                                              boolean cancelable)
    {
        AlertDialog dialog = null;
        try
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(yesText, yesListener);
            builder.setNegativeButton(noText, noListener);
            builder.setNeutralButton(cancelText, cancelListener);
            builder.setCancelable(cancelable);

            dialog = builder.create();
            dialog.show();
        }
        catch(WindowManager.BadTokenException e)
        {
            e.printStackTrace();
        }

        return dialog;
    }


    /**
     * Creates and returns a progress dialog
     *
     * @param c           calling context
     * @param message     dialog message body
     * @param canceleable set if canceleable on back button press
     * @return the created progress dialog
     */
    public static ProgressDialog createProgressDialog(@NonNull Context c, String message, boolean canceleable)
    {
        ProgressDialog p = new ProgressDialog(c);
        p.setMessage(message);
        p.setCancelable(canceleable);

        return p;
    }


    /**
     * Resize a bitmap. If keepRatio is true, the created image will have dimensions smaller or equal
     * to the given ones
     *
     * @param image     the input image
     * @param maxWidth  target width
     * @param maxHeight target height
     * @param keepratio if this is true, the original aspect ratio will be maintained
     * @return the resized Bitmap
     */
    public static Bitmap resizeBitmapKeepingRatio(@NonNull Bitmap image, int maxWidth, int maxHeight, boolean keepratio)
    {
        if (maxHeight > 0 && maxWidth > 0)
        {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;

            if (ratioMax > 1)
            {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
                if (finalWidth > maxWidth && keepratio)
                {
                    finalWidth = maxWidth;
                    finalHeight = (int) (finalWidth / ratioBitmap);
                }
            }
            else
            {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
                if (finalHeight > maxHeight && keepratio)
                {
                    finalHeight = maxHeight;
                    finalWidth = (int) (finalHeight * ratioBitmap);
                }
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        }

        return image;
    }


    /**
     * Save a bitmap to a file in a supported format
     *
     * @param bitmap            the bitmap to save
     * @param fileName          target filename
     * @param format            image compression format (PNG for no compression)
     * @param qualityPercentage compression quality. This is ignored if the format is set to PNG.
     */
    public static void saveBitmapToFile(@NonNull Bitmap bitmap, @NonNull String fileName, Bitmap.CompressFormat format, int qualityPercentage)
    {
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(fileName);
            bitmap.compress(format, qualityPercentage, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read a bitmap from file
     *
     * @param fileName the input file path
     * @return the bitmap read from the given file
     */
    public static Bitmap readBitmapFromFile(@NonNull String fileName)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeFile(fileName, options);
    }

    /**
     * Generate key hash for api key generation. The generated key will be printed to logcat.
     *
     * @param context the calling context
     */
    public static void generateKeyHash(@NonNull Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Return false to stop scheduled task
     */
    public interface ScheduledTaskListener
    {
        boolean onScheduledTask();
    }

    /**
     * This will start an executor service with fixed frequency.
     * Ie: runs will occur as follows: t, t+r*1, t+r*2, t+r*3..... t+r*n
     * if one callback gets delayed it will not affect the next callback, hence the delay between
     * two subsequent calls is not guaranteed to be equal.
     *
     * @param poolThreadCount    the number of threads for the executor
     * @param initialDelayMillis initial start delay
     * @param repeatDelayMillis  callback period in ms
     * @param listener           the service callback
     */
    public static void executeScheduledTaskFixedRate(int poolThreadCount, long initialDelayMillis, long repeatDelayMillis, @NonNull final ScheduledTaskListener listener, final boolean isUiThreadRequired)
    {

        final ScheduledExecutorService service = Executors.newScheduledThreadPool(poolThreadCount);
        service.scheduleAtFixedRate(new Runnable()
        {
            private Handler handler = new Handler(Looper.getMainLooper());

            private Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    if (!listener.onScheduledTask())
                    {
                        service.shutdown();
                    }
                }
            };

            @Override
            public void run()
            {
                if (isUiThreadRequired)
                {
                    handler.post(runnable);
                }
                else
                {
                    if (!listener.onScheduledTask())
                    {
                        service.shutdown();
                    }
                }

            }
        }, initialDelayMillis, repeatDelayMillis, TimeUnit.MILLISECONDS);

    }

    /**
     * This will start an executor service with fixed delay
     * Ie: runs will occur as follows: t1, t2, t3 ... tn; where t(n)-t(n-1) is always fixed
     *
     * @param poolThreadCount    the number of threads for the executor
     * @param initialDelayMillis initial start delay
     * @param repeatDelayMillis  callback period in ms
     * @param listener           the service callback
     */
    public static void executeScheduledTaskFixedDelay(int poolThreadCount, long initialDelayMillis, long repeatDelayMillis, @NonNull final ScheduledTaskListener listener, final boolean isUiThreadRequired)
    {

        final ScheduledExecutorService service = Executors.newScheduledThreadPool(poolThreadCount);
        service.scheduleWithFixedDelay(new Runnable()
        {
            private Handler handler = new Handler(Looper.getMainLooper());

            private Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    if (!listener.onScheduledTask())
                    {
                        service.shutdown();
                    }
                }
            };

            @Override
            public void run()
            {
                if (isUiThreadRequired)
                {
                    handler.post(runnable);
                }
                else
                {
                    if (!listener.onScheduledTask())
                    {
                        service.shutdown();
                    }
                }

            }
        }, initialDelayMillis, repeatDelayMillis, TimeUnit.MILLISECONDS);

    }

    /**
     * Returns the given density pixel value in actual pixels
     *
     * @param context   the calling context
     * @param valueInDp input value in dp
     * @return the corresponding pixel value
     */
    public static float dpToPx(Context context, float valueInDp)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    /**
     * Get colors from the current theme
     *
     * @param context        calling context
     * @param attributeColor required color attribute
     * @return color
     */
    @ColorInt
    public static int getThemeColor(@NonNull final Context context, @AttrRes final int attributeColor)
    {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attributeColor});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    /**
     * Sets the system status bar color (the bar on the top of the display with the clock and
     * battery percentage)
     *
     * @param activity calling activity
     * @param color    color to set
     */
    public static void setSystemStatusBarColor(Activity activity, @ColorInt int color)
    {
        Window window = activity.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(color);
        }
    }


    /**
     * Checks and requests the permissions specified in the arguments
     *
     * @param requestingActivity the activity that gets the permission callback
     * @param requestId          the unique id for the request
     * @param permissions        the requested permissions from Manifest.permissions
     * @return true if all permissions are already granted
     */
    public static boolean requestPermissionsIfNotGranted(Activity requestingActivity, int requestId, String... permissions)
    {
        ArrayList<String> toRequestList = new ArrayList<>();

        for (String s : permissions)
        {
            if (ActivityCompat.checkSelfPermission(requestingActivity, s) != PackageManager.PERMISSION_GRANTED)
            {
                toRequestList.add(s);
            }
        }

        if (!toRequestList.isEmpty())
        {
            String[] temp = new String[toRequestList.size()];
            toRequestList.toArray(temp);

            ActivityCompat.requestPermissions(requestingActivity, temp, requestId);
        }

        return toRequestList.isEmpty();
    }


    /**
     * Requests all non-granted permissions that are declared in the manifest
     *
     * @param requestingActivity the activity that gets the permission callback
     * @param requestId          the unique request id
     * @return true if all declared permissions are already granted
     */
    public static boolean requestAllDeclaredPermissionsIfNotGranted(Activity requestingActivity, int requestId)
    {
        String[] requestedPermissions = null;
        try
        {
            PackageInfo info = requestingActivity.getPackageManager().getPackageInfo(requestingActivity.getPackageName(), PackageManager.GET_PERMISSIONS);
            requestedPermissions = info.requestedPermissions;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return requestPermissionsIfNotGranted(requestingActivity, requestId, requestedPermissions);
    }


    /**
     * Loads an image file into a given imageview. App must have the correct permissions
     * before calling this function
     *
     * @param path the filepath of the image
     * @param target the target imageview
     * @return true if image can be read and set to the target view, false otherwise
     */
    public static boolean loadImageToTarget(String path, ImageView target)
    {
        boolean ret = false;
        File imgFile = new File(path);

        if (imgFile.canRead())
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            target.setImageBitmap(myBitmap);

            ret = true;
        }

        return ret;
    }


    /**
     * Set the system status bar text and icon color hint to black.
     * NOTE: This turns off the translucent status bar flag
     *
     * @param act the calling activity
     */
    public static void setLightStatusBar(Activity act)
    {
        View v = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (v != null)
            {
                v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setTextViewActive(TextView t, boolean active)
    {
        t.setAlpha(active ? 1.0f : 0.4f);
        t.setEnabled(active);
    }


    /**
     * Read a text file from the assets directory
     * @param context a valid app context
     * @param assetFile the file to be read
     * @return the text file as a String
     */
    public static String readTextFromAssets(Context context, String assetFile)
    {
        StringBuilder buf = new StringBuilder();
        try
        {
            InputStream stream = context.getAssets().open(assetFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null)
            {
                buf.append(str);
            }
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return buf.toString();
    }


}
