package main.java.com.termux.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.termux.R;

import androidx.annotation.NonNull;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/15 16:45
 **/
public abstract class BaseDialogALL extends Dialog {

    public Context mContext;
    private boolean mMid = false;
    private boolean mOutCancel = true;

    private float mDimAmount = 0.5f;

    private int mMargin= 0;
    private int mWidth= 0;
    private int mHeight= 0;


    private Activity activity;

    public BaseDialogALL(@NonNull Context context,Activity activity) {
        super(context);
        initView(context);
        this.activity = activity;
    }

    public BaseDialogALL(@NonNull Context context, int themeResId) {
        super(context, themeResId);
       // initView(context);
    }


    private void initView(Context mContext){

        this.mContext = mContext;

        int contentView = getContentView();

        View viewLay = UUtils.getViewLay(contentView);

        initViewDialog(viewLay);

        setContentView(viewLay);
    }

    public void dialogMid(){

        mMid = true;
    }

    @Override
    public void show() {
        super.show();

        WindowManager.LayoutParams attributes = getWindow().getAttributes();

        attributes.dimAmount = mDimAmount;

        mWidth = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth();

        attributes.width = mWidth;


        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;

        attributes.gravity = Gravity.BOTTOM;

        getWindow().setAttributes(attributes);

        setCancelable(mOutCancel);

    }

    abstract void initViewDialog(View mView);

    abstract int getContentView();
}
