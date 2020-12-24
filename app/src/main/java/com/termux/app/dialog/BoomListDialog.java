package main.java.com.termux.app.dialog;

import android.content.Context;
import android.view.View;

import com.termux.R;

import androidx.annotation.NonNull;

/**
 * @author ZEL
 * @create By ZEL on 2020/12/23 15:10
 **/
public class BoomListDialog extends BaseDialogALL {
    public BoomListDialog(@NonNull Context context) {
        super(context,R.style.BaseDialog222);
    }

    public BoomListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {

    }

    @Override
    int getContentView() {
        return R.layout.dialog_boom;
    }
}
