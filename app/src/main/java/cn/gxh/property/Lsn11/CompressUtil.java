package cn.gxh.property.Lsn11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created  by gxh on 2019/2/11 11:30
 */
public class CompressUtil {

    public static void qualityCompress(String filePath, File newFile, int quality) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap oldBitmap = BitmapFactory.decodeFile(filePath, options);
        qualityCompress(oldBitmap, newFile, quality);
    }

    /**
     * 质量压缩
     *
     * @param bitmap
     * @param newFile
     * @param quality
     */
    public static void qualityCompress(Bitmap bitmap, File newFile, int quality) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        try {
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尺寸压缩
     *
     * @param bitmap
     * @param newFile
     * @param ratio   压缩倍数，值越大，图片额尺寸越小
     */
    public static void compressBySize(Bitmap bitmap, File newFile, int ratio) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio,
                bitmap.getHeight() / ratio,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        RectF rectF = new RectF(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rectF, null);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 采样率压缩
     *
     * @param filePath
     * @param file
     * @param inSampleSize 数值越高，图片像素越低
     */
    public static void compressBySampleSize(String filePath, File file,int inSampleSize) {
        // 数值越高，图片像素越低
        //int inSampleSize = 8;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        //为true的时候不会真正加载图片，而是得到图片的宽高信息。
        //options.inJustDecodeBounds = true;

        options.inSampleSize = inSampleSize;//采样率
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


















































