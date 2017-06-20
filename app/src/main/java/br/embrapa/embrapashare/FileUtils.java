package br.embrapa.embrapashare;


import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    public static final String STORAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + File.separator + "EmbrapaShare";

    public static File createImageFile(){
        File storageDir = getStoregeDirectory();
        File image = null;

        if (created(storageDir)) {
            try {
                image = File.createTempFile(
                        getDateString(),  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static File copyInputStreamToFile(InputStream in) {

        File file = new File(STORAGE_DIRECTORY_PATH + File.separator + getDateString() + (int)(Math.random() * 899999999) + ".jpg");

        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static File loadImageFile(String name){
       return new File(STORAGE_DIRECTORY_PATH + File.separator + name);
    }

    public static boolean created(File file){
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static File getStoregeDirectory(){
        return new File(STORAGE_DIRECTORY_PATH);
    }

    public static String getDateString(){
        return new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date());
    }


    public static String[] getExif(File file){
        String[] exif = {"","",""};//TODO testar retornar nulo
        try {
            ExifInterface ei = new ExifInterface(FileUtils.STORAGE_DIRECTORY_PATH + File.separator + file.getName());
            exif[2] = ei.getAttribute(ei.TAG_DATETIME);
            float[] ll= new float[2];
            if(ei.getLatLong(ll)){
                exif[0] = Float.toString(ll[0]);
                exif[1] = Float.toString(ll[1]);
            }
            if(exif[2] == null)
                exif[2] = "";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return exif;
    }
}
