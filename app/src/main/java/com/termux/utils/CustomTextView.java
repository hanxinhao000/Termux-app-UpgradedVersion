package main.java.com.termux.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @author ZEL
 * @create By ZEL on 2020/8/20 10:20
 **/
public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {


    public CustomTextView(Context context) {
        super(context);
        initContext(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContext(context);
    }



    private void initContext(Context context){

        try {
            Typeface typeface = Typeface.createFromFile("/data/data/com.termux/files/home/.termux/font.ttf");  // mContext为上下文
            setTypeface(typeface);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
