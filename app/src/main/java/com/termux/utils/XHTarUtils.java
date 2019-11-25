package main.java.com.termux.utils;

import android.util.Log;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class XHTarUtils {

    public static int BUFFER_SIZE = 2048;

    public static void unTarGZ(String file, String destDir) throws Exception {
        File tarFile = new File(file);
        unTarGZ(tarFile, destDir);
    }

    public static void unTarGZ(File tarFile, String destDir) throws Exception {
        Log.e("XINHAO_HAN_FILE", "start: " + "执行4" );
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        unTar(new GzipCompressorInputStream(new FileInputStream(tarFile)), destDir);
    }

    private static void unTar(InputStream inputStream, String destDir) throws Exception {
        Log.e("XINHAO_HAN_FILE", "start: " + "执行5" );
        TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream, BUFFER_SIZE);
        TarArchiveEntry entry = null;
        try {
            while ((entry = tarIn.getNextTarEntry()) != null) {

                if (entry.isDirectory()) {//是目录
                    createDirectory(destDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(destDir + File.separator + entry.getName());

                    boolean link = entry.isLink();



                    if(link){
                        Log.e("XINHAO_HAN_FILE", "unTar:软连接文件 :" + tmpFile.getAbsolutePath());
                    }else{
                        Log.e("XINHAO_HAN_FILE", "unTar:普通文件   : " + tmpFile.getAbsolutePath().replace("/data/data/com.termux/files",""));
                    }


                    //createDirectory(tmpFile.getAbsolutePath().replace("/data/data/com.termux/files","")+ File.separator, null);//创建输出目录

                    Log.e("XINHAO_HAN_FILE", "File.separator: " + File.separator );
                    createDirectory( File.separator, null);//创建输出目录
                    OutputStream out = null;
                    try {


                       // File file = new File(tmpFile.getAbsolutePath().replace("/data/data/com.termux", ""));


                        out = new FileOutputStream(tmpFile);
                        int length = 0;
                        byte[] b = new byte[2048];
                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }
                    } finally {
                        IOUtils.closeQuietly(out);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(tarIn);
        }
    }

    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + File.separator + subDir);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
    }


}
