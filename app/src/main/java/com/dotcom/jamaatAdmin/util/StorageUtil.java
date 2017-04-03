package com.dotcom.jamaatAdmin.util;

/**
 * Created by anjanik on 19/7/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtil {

    public static String getTempDirectoryPath(Context ctx, String imageContainer) {
        File cache = null;

        // SD Card Mounted
        if (isExternalStorageWriteable()) {
            cache = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/Android/data/"
                    + ctx.getPackageName() + "/cache/" +imageContainer+"/");
        }
        // Use internal storage
        else {
            cache = ctx.getCacheDir();
        }

        // Create the cache directory if it doesn't exist
        cache.mkdirs();
        return cache.getAbsolutePath();
    }

    public static String getTempDirectoryPath(Context ctx) {
        return getTempDirectoryPath(ctx,Constants.TEMP_PATH);
    }

    private static boolean externalStorageAvailable, externalStorageWriteable;

    /**
     * Checks the external storage's state and saves it in member attributes.
     */
    private static void checkStorage() {
        // Get the external storage's state
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // Storage is available and writeable
            externalStorageAvailable = externalStorageWriteable = true;
        } else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            // Storage is only readable
            externalStorageAvailable = true;
            externalStorageWriteable = false;
        } else {
            // Storage is neither readable nor writeable
            externalStorageAvailable = externalStorageWriteable = false;
        }
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is available, false otherwise.
     */
    public static boolean isExternalStorageAvailable() {
        checkStorage();
        return externalStorageAvailable;
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is writeable, false otherwise.
     */
    public static boolean isExternalStorageWriteable() {
        checkStorage();
        return externalStorageWriteable;
    }

    /**
     * Checks the state of the external storage.
     *
     * @return True if the external storage is available and writeable, false
     *         otherwise.
     */
    public static boolean isExternalStorageAvailableAndWriteable() {
        checkStorage();
        if (!externalStorageAvailable) {
            return false;
        } else return externalStorageWriteable;
    }

    public static boolean clearDir(String path) {
        try {
            File file = new File(path);
            if (file != null && file.exists() && file.isDirectory()) {
                //FileUtils.cleanDirectory(file);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public static String writeBitmap(Context ctx, Bitmap bmp, String scaledImageName) {
        FileOutputStream out = null;
        String path = null;
        try {
			/*String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			path = getTempDirectoryPath(ctx,PNBConstants.TEMP_PATH) + File.separator + "Random_"
					+ timeStamp + ".png";*/
            path = getTempDirectoryPath(ctx, Constants.TEMP_PATH)+ File.separator +scaledImageName+".png";
            out = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.PNG, 50, out); // bmp is your
            // Bitmap
            // instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    // //////////////Functions ///////////////////////////////

    public static String WriteBitmapToFile(Bitmap image, Context ctx,String scaledImageName) {
        String path = null;
        FileOutputStream out = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
			/*path = getTempDirectoryPath(ctx,PNBConstants.TEMP_PATH) + File.separator + "Random_"
					+ timeStamp + ".png";*/
            path = getTempDirectoryPath(ctx, Constants.TEMP_PATH)+scaledImageName+".png";

            out = new FileOutputStream(path);

			/*if (out.exists()) {

				out.delete();

			}*/

            ByteBuffer buffer = ByteBuffer.allocate(image.getWidth()

                    * image.getHeight());

            image.copyPixelsToBuffer(buffer);

            byte[] rawBytes = buffer.array();

            Bitmap img = ConvertRawToBitmap(rawBytes, image.getWidth(),

                    image.getHeight());

            ByteArrayOutputStream bitmapBytes = new ByteArrayOutputStream();

            img.compress(Bitmap.CompressFormat.PNG, 100, bitmapBytes);

            FileOutputStream fos = new FileOutputStream(path);

            byte[] imagebytes = bitmapBytes.toByteArray();

            fos.write(imagebytes);

            fos.flush();

            fos.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static Bitmap ConvertRawToBitmap(byte[] RawBytes, int width,
                                            int height) {

        byte[] Bits = new byte[RawBytes.length * 4]; // That's where the RGBA
        // array goes.

        int i;

        for (i = 0; i < RawBytes.length; i++) {

            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = (byte) ~RawBytes[i]; // Invert
            // the
            // source
            // bits

            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.

        }

        Bitmap bi = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        bi.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));

        return bi;

    }
}
