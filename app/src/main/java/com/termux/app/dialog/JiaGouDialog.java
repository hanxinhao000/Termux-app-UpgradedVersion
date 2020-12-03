package main.java.com.termux.app.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.termux.R;

import androidx.annotation.NonNull;
import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/12/3 10:50
 **/
public class JiaGouDialog extends BaseDialogCentre {

    private TextView title_install;
    private LinearLayout arm64;
    private LinearLayout arm;
    private LinearLayout i386;
    private LinearLayout amd;

    private ImageView arm64_close;
    private ImageView arm_close;
    private ImageView i386_close;
    private ImageView amd_close;

    public JiaGouDialog(@NonNull Context context) {
        super(context);
    }

    public JiaGouDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {

        title_install = mView.findViewById(R.id.title_install);
        arm64 = mView.findViewById(R.id.arm64);
        arm = mView.findViewById(R.id.arm);
        i386 = mView.findViewById(R.id.i386);
        amd = mView.findViewById(R.id.amd);

        arm64_close = mView.findViewById(R.id.arm64_close);
        arm_close  = mView.findViewById(R.id.arm_close);
        i386_close = mView.findViewById(R.id.i386_close);
        amd_close = mView.findViewById(R.id.amd_close);

        String s = TermuxInstaller.determineTermuxArchName();

        arm64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(arm64_close.getVisibility() == View.VISIBLE || arm_close.getVisibility() == View.VISIBLE){

                    TextShowDialog textShowDialog = new TextShowDialog(mContext);
                    textShowDialog.show();
                    textShowDialog.setCancelable(true);
                    textShowDialog.edit_text.setTextSize(18);
                    textShowDialog.edit_text.setTextColor(Color.parseColor("#d81e06"));
                    textShowDialog.edit_text.setText(UUtils.getString(R.string.当前选择系统可能无法使用));

                    textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textShowDialog.dismiss();
                            if(mJiaGouListener!= null)
                                mJiaGouListener.jiagouString(10);
                        }
                    });


                }else{
                    if(mJiaGouListener!= null)
                        mJiaGouListener.jiagouString(10);
                }







            }
        });
        arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arm64_close.getVisibility() == View.VISIBLE || arm_close.getVisibility() == View.VISIBLE){

                    TextShowDialog textShowDialog = new TextShowDialog(mContext);
                    textShowDialog.show();
                    textShowDialog.setCancelable(true);
                    textShowDialog.edit_text.setTextSize(18);
                    textShowDialog.edit_text.setTextColor(Color.parseColor("#d81e06"));
                    textShowDialog.edit_text.setText(UUtils.getString(R.string.当前选择系统可能无法使用));

                    textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textShowDialog.dismiss();
                            if(mJiaGouListener!= null)
                                mJiaGouListener.jiagouString(11);
                        }
                    });


                }else{
                    if(mJiaGouListener!= null)
                        mJiaGouListener.jiagouString(11);
                }
            }
        });
        i386.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i386_close.getVisibility() == View.VISIBLE || amd_close.getVisibility() == View.VISIBLE){

                    TextShowDialog textShowDialog = new TextShowDialog(mContext);
                    textShowDialog.show();
                    textShowDialog.setCancelable(true);
                    textShowDialog.edit_text.setTextSize(18);
                    textShowDialog.edit_text.setTextColor(Color.parseColor("#d81e06"));
                    textShowDialog.edit_text.setText(UUtils.getString(R.string.当前选择系统可能无法使用));

                    textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textShowDialog.dismiss();
                            if(mJiaGouListener!= null)
                                mJiaGouListener.jiagouString(12);
                        }
                    });


                }else{
                    if(mJiaGouListener!= null)
                        mJiaGouListener.jiagouString(12);
                }
            }
        });
        amd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i386_close.getVisibility() == View.VISIBLE || amd_close.getVisibility() == View.VISIBLE){

                    TextShowDialog textShowDialog = new TextShowDialog(mContext);
                    textShowDialog.show();
                    textShowDialog.setCancelable(true);
                    textShowDialog.edit_text.setText(UUtils.getString(R.string.当前选择系统可能无法使用));
                    textShowDialog.edit_text.setTextSize(18);
                    textShowDialog.edit_text.setTextColor(Color.parseColor("#d81e06"));

                    textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textShowDialog.dismiss();
                            if(mJiaGouListener!= null)
                                mJiaGouListener.jiagouString(13);
                        }
                    });


                }else{
                    if(mJiaGouListener!= null)
                        mJiaGouListener.jiagouString(13);
                }
            }

        });

       switch (s){

           case "aarch64":
           case "arm":
               i386_close.setVisibility(View.VISIBLE);
               amd_close .setVisibility(View.VISIBLE);
               arm64_close.setVisibility(View.GONE);
               arm_close.setVisibility(View.GONE);
               break;
           case "x86_64":
           case "i686":
               arm64_close.setVisibility(View.VISIBLE);
               arm_close.setVisibility(View.VISIBLE);
               i386_close.setVisibility(View.GONE);
               amd_close.setVisibility(View.GONE);
               break;

       }


        title_install.setText(UUtils.getString(R.string.选择要安装的架构dfsd) + TermuxInstaller.determineTermuxArchName() + ")");


    }

    @Override
    int getContentView() {
        return R.layout.dialog_system_jiagou;
    }

    private JiaGouListener mJiaGouListener;
    public void setJiaGouListener(JiaGouListener mJiaGouListener){
        this.mJiaGouListener = mJiaGouListener;
    }

    public interface JiaGouListener{


        void jiagouString(int name);

    }
}
