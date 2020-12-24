package main.java.com.termux.app.web;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;


import com.termux.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import main.java.com.termux.app.dialog.LoadingDialog;
import main.java.com.termux.utils.UUtils;

public class RepoInstallerTask extends AsyncTask {

    public static final String INSTALL_DONE = "org.opendroidphp.repository.INSTALLED";
    public static final String INSTALL_ERROR = "org.opendroidphp.repository.INSTALL_ERROR";

    private String repositoryName;
    private String repositoryPath;

    private LinearLayout linearLayout;
    private InputStream inputStream;
    private LoadingDialog loadingDialog;

    public RepoInstallerTask(LinearLayout linearLayout, InputStream inputStream, LoadingDialog loadingDialog) {
        this.linearLayout = linearLayout;
        this.inputStream = inputStream;
        this.loadingDialog = loadingDialog;
    }


    @Override
    public Object doInBackground(Object[] file) {
        repositoryName = (String) file[0];
        repositoryPath = (String) file[1];
        boolean isSuccess = true;

        ZipInputStream zipInputStream = null;
        createDirectory("");
        try {
            if (repositoryName == null || repositoryName.equals("")) {
                zipInputStream = new ZipInputStream(inputStream);
            } else if (new File(repositoryName).exists()) {
                zipInputStream = new ZipInputStream(new FileInputStream(repositoryName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZipEntry zipEntry;
        try {
            FileOutputStream fout;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    createDirectory(zipEntry.getName());
                } else {
                    fout = new FileOutputStream(repositoryPath + "/" + zipEntry.getName());
                    publishProgress(zipEntry.getName());
                    byte[] buffer = new byte[4096 * 10];
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1) {
                        fout.write(buffer, 0, length);
                    }
                    zipInputStream.closeEntry();
                    fout.close();
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
            UUtils.showLog("解压:" + e.toString());
        }
        publishProgress(isSuccess ? INSTALL_DONE : INSTALL_ERROR);
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {

        UUtils.showLog("解压:" + values[0]);
    }

    /**
     * Responsible for creating directory inside application's data directory
     *
     * @param dirName Directory to create during extracting
     */
    protected void createDirectory(String dirName) {
        File file = new File(repositoryPath + dirName);
        if (!file.isDirectory()) file.mkdirs();
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (linearLayout != null)
            linearLayout.setVisibility(View.GONE);

        loadingDialog.dismiss();
        UUtils.showMsg(UUtils.getString(R.string.安装服务器完成fg));

    }


}
