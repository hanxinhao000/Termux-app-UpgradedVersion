package main.java.com.termux.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

/**
 * @author ZEL
 * @create By ZEL on 2020/12/16 14:48
 **/
public class MyDrawerLayout extends DrawerLayout {

    private int mInterceptTouchEventChildId;

    public void setInterceptTouchEventChildId(int id) {
        this.mInterceptTouchEventChildId = id;
    }

    public MyDrawerLayout(Context context) {
        super(context);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mInterceptTouchEventChildId > 0) {
            View scroll = findViewById(mInterceptTouchEventChildId);
            if (scroll != null) {
                Rect rect = new Rect();
                scroll.getHitRect(rect);
                if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);

    }
}
