package main.java.com.termux.app.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.termux.R;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.app.TermuxActivity2;
import main.java.com.termux.download.HttpUtil;
import main.java.com.termux.utils.CustomTextView;
import main.java.com.termux.utils.UUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/19 14:00
 **/
public class LoadingSHDialog extends BaseDialogCentre {

    public TextView msg_dialog;
    public CustomTextView online;
    private Call contentLength;

    private ArrayList<String> arrayList;

    public LoadingSHDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingSHDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {

        msg_dialog = mView.findViewById(R.id.msg_dialog);
        online = mView.findViewById(R.id.online);

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

                if(contentLength != null){
                    try {
                        contentLength.cancel();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    int getContentView() {
        return R.layout.dialog_loading_sh;
    }

    public void offLineRommand(ArrayList<String> arrayList){
        this.arrayList = arrayList;
    }



    public void startURL(String url,ArrayList<String> other, Map<String, Object> music,String onLineID,String offLineID){


        contentLength = HttpUtil.getInstance().getContentLength(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MobclickAgent.onEventObject(UUtils.getContext(), offLineID, music);
                UUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        UUtils.showMsg(UUtils.getString(R.string.服务器未响应));
                      //  dismiss();

                        if(arrayList == null){
                            return;
                        }

                        for (int i = 0; i < arrayList.size(); i++) {

                            TermuxActivity.mTerminalView.sendTextToTerminal(arrayList.get(i) + " \n");

                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MobclickAgent.onEventObject(UUtils.getContext(), onLineID, music);
                String string = response.body().string();
                UUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                       // UUtils.showMsg("进入成功回调");
                        dismiss();

                        try {



                            TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                            TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                            TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");


                            if(other != null){

                                StringBuilder stringBuilder = new StringBuilder();

                                for (int i = 0; i < other.size(); i++) {

                                    stringBuilder.append(" && ").append(other.get(i));

                                }

                                TermuxActivity.mTerminalView.sendTextToTerminal(string + stringBuilder.toString() +  "\n");

                            }else{
                                TermuxActivity.mTerminalView.sendTextToTerminal(string + "\n");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });



            }
        });


    }




    public void startShFileURL(String url,ArrayList<String> other, Map<String, Object> music,String onLineID,String offLineID,File onLine,File offLine,String assets){


        contentLength = HttpUtil.getInstance().getContentLength(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {



                MobclickAgent.onEventObject(UUtils.getContext(), offLineID, music);
                if(offLine != null){
                    writerFile(assets,offLine);
                }

                UUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        UUtils.showMsg(UUtils.getString(R.string.服务器未响应));
                        dismiss();
                        MobclickAgent.onEventObject(UUtils.getContext(), offLineID, music);
                        if(arrayList == null){
                            return;
                        }

                        for (int i = 0; i < arrayList.size(); i++) {

                            TermuxActivity.mTerminalView.sendTextToTerminal(arrayList.get(i) + " \n");

                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MobclickAgent.onEventObject(UUtils.getContext(), onLineID, music);
                String string  = response.body().string();
                UUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                         UUtils.showMsg("进入成功回调");
                        dismiss();
                        try {


                            onLine.createNewFile();
                            PrintWriter printWriter = new PrintWriter(onLine);
                            printWriter.print(string);
                            printWriter.flush();
                            printWriter.close();


                            for (int i = 0; i < arrayList.size(); i++) {

                                TermuxActivity.mTerminalView.sendTextToTerminal(arrayList.get(i) + " \n");

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });



            }
        });


    }


    //写出文件
    public void writerFile(String name, File mFile) {

        try {
            InputStream open = UUtils.getContext().getAssets().open(name);

            int len = 0;

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read()) != -1) {
                fileOutputStream.write(len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

            e.printStackTrace();
            Log.e("XINHAO_HAN", "出错了: " + e.toString() );
        }

    }
}
