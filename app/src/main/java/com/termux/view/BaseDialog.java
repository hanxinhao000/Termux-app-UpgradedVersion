package main.java.com.termux.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.termux.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author ZEL
 * @create By ZEL on 2020/5/7 18:03
 **/
public abstract class BaseDialog extends Dialog {

    private Context mContext;

    private float mDimAmount = 0.5f;
    private boolean mOutCancel = true;
    private int mMargin = 0;
    private int mWidth = 0;
    private int mHeight = 0;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.BaseDialog222);
        initView(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }


    private void initView(Context mContext){

        this.mContext = mContext;

        int contentView = getContentView();
        View inflate = View.inflate(mContext, contentView, null);

        initViewDialog(inflate);
        setContentView(inflate);
    }

    abstract int getContentView();


    abstract void initViewDialog(View mView);

    @Override
    public void show() {
        super.show();

        WindowManager.LayoutParams attributes = getWindow().getAttributes();

        attributes.gravity = Gravity.BOTTOM;
        attributes.dimAmount = mDimAmount;

        if (mWidth == 0) {
            attributes.width = getScreenWidth(mContext) - 2 * dp2px(mContext, mMargin);
        } else {
            attributes.width = mWidth;
        }

        if (mHeight == 0) {
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            attributes.height = mHeight;
        }

        getWindow().setAttributes(attributes);

        setCancelable(mOutCancel);

    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
