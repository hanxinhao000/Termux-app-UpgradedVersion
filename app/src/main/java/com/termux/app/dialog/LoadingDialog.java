package main.java.com.termux.app.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.termux.R;

import androidx.annotation.NonNull;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/19 14:00
 **/
public class LoadingDialog extends BaseDialogCentre {

    public TextView msg_dialog;
    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {

        msg_dialog = mView.findViewById(R.id.msg_dialog);
    }

    @Override
    int getContentView() {
        return R.layout.dialog_loading;
    }
}
