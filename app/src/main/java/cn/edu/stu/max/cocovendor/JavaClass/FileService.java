package cn.edu.stu.max.cocovendor.JavaClass;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import cn.edu.stu.max.cocovendor.Activity.AdSettingActivity;

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
     * 将一个目录下的所有文件存进一个数组里面
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
}
