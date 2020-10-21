package main.java.com.termux.app.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.termux.R;

import java.lang.reflect.Array;
import java.util.Arrays;

import androidx.annotation.NonNull;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/20 17:05
 **/
public class EndDialog extends BaseDialogCentre {
    private TextView start;
    private TextView edit_text;
    public EndDialog(@NonNull Context context) {
        super(context);
    }

    public EndDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {
        start = mView.findViewById(R.id.start);
        edit_text = mView.findViewById(R.id.edit_text);

        CharSequence text1 = TermuxActivity.mTerminalView.getText555();

        String s = text1.toString();
        String[] split = s.split("\n");

        try {
            String s4 = split[split.length - 4];
            String s3 = split[split.length - 3];
            String s2 = split[split.length - 2];
            String s1 = split[split.length - 1];


            edit_text.setText(UUtils.getString(R.string.镜像运行结束) + "\n" + s4 + "\n" + s3 + "\n" + s2 + "\n" + s1);


        }catch (Exception e){
            e.printStackTrace();
        }



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    int getContentView() {
        return R.layout.dialog_end;
    }
}
