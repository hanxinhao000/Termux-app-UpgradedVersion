package main.java.com.termux.app.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.termux.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import androidx.annotation.NonNull;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/11/23 11:43
 **/
public class RootfsDialog extends BaseDialogCentre {

    private LinearLayout ubuntu_18;
    private LinearLayout ubuntu_20;
    private LinearLayout jdk;
    private LinearLayout alpine;
    private LinearLayout centos;
    private LinearLayout debian;
    private LinearLayout kali;
    public RootfsDialog(@NonNull Context context) {
        super(context);
    }

    public RootfsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {

        ubuntu_18 = mView.findViewById(R.id.ubuntu_18);
        ubuntu_20 = mView.findViewById(R.id.ubuntu_20);
        jdk = mView.findViewById(R.id.jdk);
        alpine = mView.findViewById(R.id.alpine);
        centos = mView.findViewById(R.id.centos);
        debian = mView.findViewById(R.id.debian);
        kali = mView.findViewById(R.id.kali);

        ubuntu_18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                installShell("utassets/ubuntu_18.sh","ubuntu_18.sh");

            }
        });

        ubuntu_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                installShell("utassets/ubuntu_20.sh","ubuntu_20.sh");

            }
        });

        jdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                installShell("utassets/installjava","installjava");

            }
        });
        alpine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                installShell("utassets/alpine.sh","alpine.sh");

            }
        });

        centos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                installShell("utassets/centos.sh","centos.sh");

            }
        });

        debian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                installShell("utassets/debian.sh","debian.sh");

            }
        });

        kali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                installShell("utassets/kali.sh","kali.sh");

            }
        });

    }

    private void installShell(String path,String name){

        writerFile(path,new File("/data/data/com.termux/files/home/" + name));
        TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
        TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
        TermuxActivity.mTerminalView.sendTextToTerminal("chmod 777 " + name + "\n");
        TermuxActivity.mTerminalView.sendTextToTerminal("./" + name + "\n");

    }

    @Override
    int getContentView() {
        return R.layout.dialog_rootfs_switch;
    }

    //写出文件
    private void writerFile(String name, File mFile) {

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

        }

    }
}
