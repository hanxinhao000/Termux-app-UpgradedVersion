package main.java.com.termux.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * @author ZEL
 * @create By ZEL on 2020/12/17 15:02
 **/
public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "courbd.ttf");
            setTypeface(tf);
        }
    }

}
