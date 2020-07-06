package main.java.com.termux.fragment.utils;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.bean.CreateSystemBean;
import main.java.com.termux.fragment.BackupFragment;
import main.java.com.termux.fragment.RestoreFragment;
import main.java.com.termux.view.MyDialog;

public class QZHFUtils {

    private File mFile = new File("/data/data/com.termux/");
    private File createFile;

    public void main(MyDialog myDialog, String systemName, BackupFragment restoreFragment) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.getDialog_pro().setText("开始检测备份环境!");
                        myDialog.getDialog_pro_prog().setProgress(15);
                    }
                });

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.getDialog_pro().setText("备份环境监测完成!");
                        myDialog.getDialog_pro_prog().setProgress(50);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.getDialog_pro().setText("开始检测是否有sd卡软链接!");
                        myDialog.getDialog_pro_prog().setProgress(75);
                    }
                });


            /* TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TermuxActivity.mTerminalView.sendTextToTerminal("termux-setup-storage \n");
                    }
                });*/

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!(new File("/data/data/com.termux/files/home/storage").exists())) {


                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TermuxApplication.mContext, "没有找到storage目录,请手动创建", Toast.LENGTH_SHORT).show();

                            myDialog.dismiss();
                            TermuxActivity.mTerminalView.sendTextToTerminal("termux-setup-storage");
                            restoreFragment.getActivity().finish();
                        }
                    });
                    return;
                }

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.getDialog_pro().setText("已检测到软连接!");
                        myDialog.getDialog_pro_prog().setProgress(80);
                    }
                });

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.getDialog_pro().setText("3秒后开始备份!");
                        myDialog.getDialog_pro_prog().setProgress(100);
                    }
                });


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.dismiss();

                        TermuxActivity.mTerminalView.sendTextToTerminal("tar -zcvf ./storage/shared/xinhao/data/" + systemName + " /data/data/com.termux/files && echo \"备份完成，备份文件在->内部存储/xinhao/data/|目录下\" \n");


                        restoreFragment.getActivity().finish();


                    }
                });


            }
        }).start();

    }


    //创建
    private void createSystem(String name) {
        //先扫描有多少文件
        File[] files = mFile.listFiles();

        if (files.length == 1) {
            //默认只有一个系统
            //直接创建
            createFile = new File(mFile, "files1");
            createFile.mkdirs();
            CreateSystemBean createSystemBean = new CreateSystemBean();
            createSystemBean.dir = createFile.getAbsolutePath();
            createSystemBean.systemName = name;

            String s = new Gson().toJson(createSystemBean);


            File fileInfo = new File(createFile, "/xinhao_system.infoJson");
            PrintWriter printWriter = null;
            try {

                fileInfo.createNewFile();
                printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileInfo)));

                printWriter.print(s);
                printWriter.flush();
                printWriter.close();

            } catch (IOException e) {
                Toast.makeText(TermuxApplication.mContext, "系统创建失败!请重试", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }

            }


        } else {
            //有多个系统

            ArrayList<Integer> arrayList = new ArrayList<>();


            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith("files")) {
                    // Log.e("XINHAO_HAN", "readFile: " + files[i].getAbsolutePath());
                    String name1 = files[i].getName();
                    String substring = name1.substring(5, name1.length());

                    if (substring.isEmpty()) {
                        arrayList.add(0);
                    } else {
                        arrayList.add(Integer.parseInt(substring));
                    }

                }
            }

            // Log.e("XINHAO_HAN", "createSystem: " + arrayList);


            int max = getMax(arrayList);
            Log.e("XINHAO_HAN", "最大值: " + max);


            //直接创建
            createFile = new File(mFile, "files" + (max + 1));
            createFile.mkdirs();
            CreateSystemBean createSystemBean = new CreateSystemBean();
            createSystemBean.dir = createFile.getAbsolutePath();
            createSystemBean.systemName = name;

            String s = new Gson().toJson(createSystemBean);


            File fileInfo = new File(createFile, "/xinhao_system.infoJson");
            PrintWriter printWriter = null;
            try {

                fileInfo.createNewFile();
                printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileInfo)));

                printWriter.print(s);
                printWriter.flush();
                printWriter.close();

            } catch (IOException e) {
                Toast.makeText(TermuxApplication.mContext, "系统创建失败!请重试", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }

            }
        }


    }


    //比大小
    private int getMax(ArrayList<Integer> number) {

        int temp = number.get(0);

        for (int i = 0; i < number.size(); i++) {

            if (number.get(i) > temp) {
                temp = number.get(i);
            }

        }


        return temp;
    }
}
