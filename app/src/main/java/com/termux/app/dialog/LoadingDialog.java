package main.java.com.termux.app.dialog;

import android.content.Context;
import android.view.View;

import com.termux.R;

import androidx.annotation.NonNull;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/19 14:00
 **/
public class LoadingDialog extends BaseDialogCentre {
    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {

    }

    @Override
    int getContentView() {
        return R.layout.dialog_loading;
    }
}
