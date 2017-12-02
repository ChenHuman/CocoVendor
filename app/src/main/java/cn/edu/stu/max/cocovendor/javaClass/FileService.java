package cn.edu.stu.max.cocovendor.javaClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 0 on 2017/10/4.
 */

public class FileService {

    public Context context;

    /**
     * 将一个目录下的所有文件存进一个数组里面
     * @param filePath  文件路径
     * @return File[]  返回一个文件数组
     */
    public static File[] getFiles(String filePath) {
        File[] currentFiles;
        File root = new File(filePath);
        // 判断文件是否存在
        if (!root.exists()) {
            return null;
        }
        // 如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
        return currentFiles;
    }

    /**
     * 复制单个文件
     * @param fromFile
     * @param toFile
     * @throws Exception
     */
    public static int copyFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * 删除文件
     * @param filePath  文件路径
     * @param isFileAdded
     */
    public static void deleteFile(String filePath, boolean[] isFileAdded) {
        File[] currentFiles = FileService.getFiles(filePath);
        for (int i = 0; i < isFileAdded.length; i++) {
            if (isFileAdded[i]) {
                if (currentFiles[i].exists()) {
                    if (currentFiles[i].isFile()) {
                        currentFiles[i].delete();
                    } else if (currentFiles[i].isDirectory()) {
                        // TODO
                    }
                } else {
                    // TODO
                }
            }
        }
    }

    /**
     * 判断文件是否添加
     * @param filePath  文件路径
     * @param isFileAdded
     */
    public static boolean isFileAdded(String filePath, boolean[] isFileAdded) {
        boolean isFileAddedFlag = false;
        File[] currentFiles = FileService.getFiles(filePath);
        for (int i = 0; i < isFileAdded.length; i++) {
            if (isFileAdded[i]) {
                isFileAddedFlag = true;
            }
        }
        return isFileAddedFlag;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
     public String getSDAvailableSize() {
         File path = new File("/mnt/external_sd/MyCocoCamera/");
         StatFs stat = new StatFs(path.getPath());
         long blockSize = stat.getBlockSize();
         long availableBlocks = stat.getAvailableBlocks();
         String availableSize = Formatter.formatFileSize(context, availableBlocks * blockSize);   // 获得SD卡总容量
         return availableSize;
     }

    /**
     * 获得sd卡总大小
     *
     * @return
     */
     public String getSDTotalSize() {
         File path = new File("/mnt/external_sd/MyCocoCamera/");
         StatFs stat = new StatFs(path.getPath());
         long blockSize = stat.getBlockSize();
         long totalBlocks = stat.getBlockCount();
         String totalSize = Formatter.formatFileSize(context, totalBlocks * blockSize);   // 获得SD卡总容量
         return totalSize;
     }

     // 获取文件位图
    public static Bitmap getUDiskBitmap(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return void
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                } else{
                    temp=new File(oldPath+File.separator+file[i]);
                }
                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
