package main.java.com.termux.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class MoveScrollView extends ViewGroup {

    private Context mContext;

    private static final String TAG = "XINHAO_HAN_MSG";
    private int width;
    private int height;

    private int left;
    private int top;
    private int right;
    private int down;

    public MoveScrollView(Context context) {
        super(context);
        initView(context);
    }

    public MoveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MoveScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        View childAt = getChildAt(0);

        measureChild(childAt, widthMeasureSpec, heightMeasureSpec);

        width = childAt.getMeasuredWidth();
        height = childAt.getMeasuredHeight();

        Log.e(TAG, "width: " + width);
        Log.e(TAG, "height: " + height);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //初始位置 在 中间？ 还是放中间吧

        int midH = b / 2;
        int midW = r / 2;


        int cMidW = width / 2;
        int cMidH = height / 2;

        left = (midW - cMidW);
        top = (midH - cMidH);
        right = (midW + cMidW);
        down = (midH + cMidH);

        getChildAt(0).layout(left, top, right, down);


        Log.e(TAG, "onLayout: " + "重新测量" );
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int startX = 0;
        int startY = 0;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                startX = (int) event.getX();
                startY = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) event.getX();
                int endY = (int) event.getY();


                left += (endX - startX);
                top += (endY - startY);
                right += (endX - startX);
                down += (endY - startY);


                startX = endX;
                startY = endY;



                Log.e(TAG, "left: " + left );
                Log.e(TAG, "top: " + top );
                Log.e(TAG, "right: " + right );
                Log.e(TAG, "down: " + down );
                break;
            case MotionEvent.ACTION_UP:
                break;

        }





        return true;
    }
}
