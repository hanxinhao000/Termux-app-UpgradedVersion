package main.java.com.termux.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * XINHAO_HAN贝斯曲线完整版本
 */

public class XHWaveView extends View {
    private Context context;

    private Path path;
    private Paint paint;
    //控制点1XY
    private int k1X;
    private int k1Y;
    //控制点2XY
    private int k2X;
    private int k2Y;

    //水平高度统一值
    private int sizeH = 120;

    //屏幕宽
    private int viewH;
    //屏幕高
    private int viewW;
    //起点
    private int startX = 0;
    private int startY = sizeH;


    //第二个起点
    private int start2X = viewW / 2;
    private int start2Y = sizeH;


    //第一个延长线
    //第一个控制点XY
    private int k11X;
    private int k11Y;
    //第二个控制点
    private int k21X;
    private int k21Y;

    //第二个控制点
    private int k12X;
    private int k12Y;

    private int k22X;
    private int k22Y;

    private Handler handler;

    //屏幕外第一个终点
    private int startW1X;
    private int startW2X;


    private int startH1Y;
    private int startH2Y;

    private int startRX;

    public XHWaveView(Context context) {
        super(context);
        initView(context);
    }

    public XHWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XHWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.parseColor("#AFDEE4"));
        paint.setAntiAlias(true);
        handler = new Handler();
        //newThread();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewH = h;
        viewW = w;
        startNumber();
        newThread();
    }

    //开始计算控制点

    private void startNumber() {
        //第一个控制点(高)
        k1Y = sizeH - 60;
        //第二个控制点(高)
        k2Y = sizeH + 60;

        //第一个控制点X
        k1X = (viewW / 4) - 50;
        //第二个控制点
        k2X = viewW - ((viewW / 4) + 50);

        start2X = viewW / 2;
        start2Y = sizeH;
        startX = 0;


        //初始化预留
        k11X = (viewW + 50) + ((viewW / 4) - 50);
        k21X = (viewW + 50) + (viewW - ((viewW / 4) + 50));


        //初始化第一个预留
        startW1X = viewW + (viewW / 2);
        startW2X = viewW + viewW;


        //屏幕最右边
        startRX = viewW + 50;
        invalidate();


    }


    private int indexH;

    //设置高度
    public void setIndex(int indexH) {
        this.indexH = indexH;


        startY = indexH;
        k1Y = indexH - 60;
        k2Y = indexH + 60;
        start2Y = indexH;
        sizeH = indexH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.parseColor("#55AFDEE4"));
        path.moveTo(startX, startY);
        path.quadTo(k1X, k1Y, start2X, startY);
        path.lineTo(start2X, start2Y);
        path.quadTo(k2X, k2Y, startRX, startY);


        //画后边的延长线

        path.lineTo(startRX, startY);
        path.quadTo(k11X, k1Y, startW1X, startY);
        path.lineTo(startW1X, start2Y);
        path.quadTo(k21X, k2Y, startW2X, startY);


        path.lineTo(startW2X, startY);
        path.lineTo(startW2X, viewH);
        path.lineTo(startX, viewH);

        path.close();
        canvas.drawPath(path, paint);
        path.reset();
        //画第二个


        paint.setColor(Color.parseColor("#AFDEE4"));

        path.moveTo(startX, startY);


        path.quadTo(k1X, k2Y, start2X, startY);
        path.lineTo(start2X, start2Y);
        path.quadTo(k2X, k1Y, startRX, startY);


        //画后边的延长线
        path.lineTo(startRX, startY);
        path.quadTo(k11X, k2Y, startW1X, startY);
        path.lineTo(startW1X, start2Y);
        path.quadTo(k21X, k1Y, startW2X, startY);


        path.lineTo(startW2X, startY);
        path.lineTo(startW2X, viewH);
        path.lineTo(startX, viewH);


        paint.setTextSize(50);
        //canvas.drawText("XINHAO_HAN", viewW / 2 - 120, k1Y, paint);

        path.close();
        canvas.drawPath(path, paint);
        path.reset();


    }

    private void newThread() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //屏幕最右边
                    startRX--;
                    //最初起点
                    startX--;
                    //第二个终点
                    start2X--;
                    //第三个终点
                    startW1X--;
                    //第4个终点
                    startW2X--;

                    //第一个控制点
                    k1X--;
                    //第二个控制点
                    k2X--;
                    //第三个控制点
                    k11X--;
                    //第四个控制点
                    k21X--;


                    if (startW2X < (viewW)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                startNumber();
                            }
                        });

                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                }
            }
        }).start();
    }
}
