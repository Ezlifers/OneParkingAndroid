package ezlife.movil.oneparkingapp.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Dario Chamorro on 14/09/2016.
 */
public class ImageUtil {

    public static byte[] getBytesFromBitmap(Bitmap bitmap) throws IOException {

        File folder = new File(Environment.getExternalStorageDirectory()+"/OneParkingApp/");
        if(!folder.exists()){
            folder.mkdirs();
        }
        OutputStream outStream = null;
        File file = new File(Environment.getExternalStorageDirectory() + "/OneParkingApp/"+"temp"+".webp");
        outStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, outStream);
        outStream.flush();
        outStream.close();

        byte[] data = fileToBytes(file);
        file.delete();
        return data;
    }

    private static byte[] fileToBytes(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = stream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
