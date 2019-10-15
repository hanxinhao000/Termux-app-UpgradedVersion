package main.java.com.termux.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.termux.R;

public class TextDialog extends Dialog {
    private Context mContext;
    private TextView msg;
    private ImageView guanbi;

    public TextDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public TextDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private void initView(Context mContext) {
        this.mContext = mContext;
        View inflate = View.inflate(mContext, R.layout.dialog_text, null);
        msg = inflate.findViewById(R.id.msg);
        guanbi = inflate.findViewById(R.id.guanbi);
        this.setCancelable(false);
        this.setContentView(inflate);
    }

    public TextView getMsg() {
        return msg;
    }

    public ImageView getGuanbi() {
        return guanbi;
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.width = (getWindow().getWindowManager().getDefaultDisplay().getWidth() - (getWindow().getWindowManager().getDefaultDisplay().getWidth() / 10) * 2);
        lp.height = getWindow().getWindowManager().getDefaultDisplay().getHeight() - 500;
        getWindow().setAttributes(lp);
    }
}
