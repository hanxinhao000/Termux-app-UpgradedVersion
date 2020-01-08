package main.java.com.termux.key;

import android.annotation.SuppressLint;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.termux.R;
import com.termux.view.TerminalView;

import main.java.com.termux.app.ExtraKeysView;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.utils.SaveData;

public class KeyData implements View.OnClickListener {


    boolean isLong = false;

    private Button show_key;

    private LinearLayout key_layout;
    private LinearLayout show_key_layout;

    private Button esc;
    private Button f1;
    private Button f2;
    private Button f3;
    private Button f4;
    private Button f5;
    private Button f6;
    private Button f7;
    private Button f8;
    private Button f9;
    private Button f10;
    private Button f11;
    private Button f12;

    private Button key_dian;
    private Button key_1;
    private Button key_2;
    private Button key_3;
    private Button key_4;
    private Button key_5;
    private Button key_6;
    private Button key_7;
    private Button key_8;
    private Button key_9;
    private Button key_0;
    private Button key_jian;
    private Button key_jia;
    private Button key_back;


    //---------------------------------------------------------------------------------------------

    private Button key_table;
    private Button key_Q;
    private Button key_W;
    private Button key_E;
    private Button key_R;
    private Button key_T;
    private Button key_Y;
    private Button key_U;
    private Button key_I;
    private Button key_O;
    private Button key_P;
    private Button key_zuokuohao;
    private Button key_youkuohao;
    private Button key_shuxian;


    private Button key_daxiaoxie;
    private Button key_A;
    private Button key_S;
    private Button key_D;
    private Button key_F;
    private Button key_G;
    private Button key_H;
    private Button key_J;
    private Button key_K;
    private Button key_L;
    private Button key_fenhao;
    private Button key_yinyong;
    private Button key_huiche;


    private Button key_shift;
    private Button key_Z;
    private Button key_X;
    private Button key_C;
    private Button key_V;
    private Button key_B;
    private Button key_N;
    private Button key_M;
    private Button key_key_douhao;
    private Button key_dianhao;
    private Button key_wenhao;
    private Button key_shift_r;
    private Button vibrator_key;


    private Button key_ctrl;
    private Button key_at_l;
    private Button key_kong;
    private Button key_at_r;
    private Button key_ctrl_r;

    private TerminalView mTerminalView;


    private boolean isShift = false;
    private boolean isCaps = false;
    private boolean isCtrl = false;
    private boolean isAlt = false;
    private boolean isVibrator = false;


    private static Vibrator vibrator;


    @SuppressLint("ClickableViewAccessibility")
    public void setKeyView(View view, TerminalView mTerminalView) {

        this.mTerminalView = mTerminalView;
        vibrator = (Vibrator) TermuxApplication.mContext.getSystemService(TermuxApplication.mContext.VIBRATOR_SERVICE);


        esc = view.findViewById(R.id.esc);
        f1 = view.findViewById(R.id.f1);
        f2 = view.findViewById(R.id.f2);
        f3 = view.findViewById(R.id.f3);
        f4 = view.findViewById(R.id.f4);
        f5 = view.findViewById(R.id.f5);
        f6 = view.findViewById(R.id.f6);
        f7 = view.findViewById(R.id.f7);
        f8 = view.findViewById(R.id.f8);
        f9 = view.findViewById(R.id.f9);
        f10 = view.findViewById(R.id.f10);
        f11 = view.findViewById(R.id.f11);
        f12 = view.findViewById(R.id.f12);
        vibrator_key = view.findViewById(R.id.vibrator_key);
        show_key_layout = view.findViewById(R.id.show_key_layout);

        show_key = view.findViewById(R.id.show_key);
        key_layout = view.findViewById(R.id.key_layout);

        key_dian = view.findViewById(R.id.key_dian);
        key_1 = view.findViewById(R.id.key_1);
        key_2 = view.findViewById(R.id.key_2);
        key_3 = view.findViewById(R.id.key_3);
        key_4 = view.findViewById(R.id.key_4);
        key_5 = view.findViewById(R.id.key_5);
        key_6 = view.findViewById(R.id.key_6);
        key_7 = view.findViewById(R.id.key_7);
        key_8 = view.findViewById(R.id.key_8);
        key_9 = view.findViewById(R.id.key_9);
        key_0 = view.findViewById(R.id.key_0);
        key_jian = view.findViewById(R.id.key_jian);
        key_jia = view.findViewById(R.id.key_jia);
        key_back = view.findViewById(R.id.key_back);


        key_table = view.findViewById(R.id.key_table);
        key_Q = view.findViewById(R.id.key_q);
        key_W = view.findViewById(R.id.key_w);
        key_E = view.findViewById(R.id.key_e);
        key_R = view.findViewById(R.id.key_r);
        key_T = view.findViewById(R.id.key_t);
        key_Y = view.findViewById(R.id.key_y);
        key_U = view.findViewById(R.id.key_u);
        key_I = view.findViewById(R.id.key_i);
        key_O = view.findViewById(R.id.key_o);
        key_P = view.findViewById(R.id.key_p);
        key_zuokuohao = view.findViewById(R.id.key_zuokuohao);
        key_youkuohao = view.findViewById(R.id.key_youkuohao);
        key_shuxian = view.findViewById(R.id.key_shuxian);


        key_daxiaoxie = view.findViewById(R.id.key_daxiaoxie);
        key_A = view.findViewById(R.id.key_a);
        key_S = view.findViewById(R.id.key_s);
        key_D = view.findViewById(R.id.key_d);
        key_F = view.findViewById(R.id.key_f);
        key_G = view.findViewById(R.id.key_g);
        key_H = view.findViewById(R.id.key_h);
        key_J = view.findViewById(R.id.key_j);
        key_K = view.findViewById(R.id.key_k);
        key_L = view.findViewById(R.id.key_l);
        key_fenhao = view.findViewById(R.id.key_fenhao);
        key_yinyong = view.findViewById(R.id.key_yinyong);
        key_huiche = view.findViewById(R.id.key_huiche);


        key_shift = view.findViewById(R.id.key_shift);
        key_Z = view.findViewById(R.id.key_z);
        key_X = view.findViewById(R.id.key_x);
        key_C = view.findViewById(R.id.key_c);
        key_V = view.findViewById(R.id.key_v);
        key_B = view.findViewById(R.id.key_b);
        key_N = view.findViewById(R.id.key_n);
        key_M = view.findViewById(R.id.key_m);
        key_key_douhao = view.findViewById(R.id.key_douhao);
        key_dianhao = view.findViewById(R.id.key_dianhao);
        key_wenhao = view.findViewById(R.id.key_wenhao);
        key_shift_r = view.findViewById(R.id.key_shift_r);


        key_ctrl = view.findViewById(R.id.key_ctrl);
        key_at_l = view.findViewById(R.id.key_at_l);
        key_kong = view.findViewById(R.id.key_kong);
        key_at_r = view.findViewById(R.id.key_at_r);
        key_ctrl_r = view.findViewById(R.id.key_ctrl_r);


        esc.setOnClickListener(this);

        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3.setOnClickListener(this);
        f4.setOnClickListener(this);
        f5.setOnClickListener(this);
        f6.setOnClickListener(this);
        f7.setOnClickListener(this);
        f8.setOnClickListener(this);
        f9.setOnClickListener(this);
        f10.setOnClickListener(this);
        f11.setOnClickListener(this);
        f12.setOnClickListener(this);
        show_key.setOnClickListener(this);
        vibrator_key.setOnClickListener(this);


        key_dian.setOnClickListener(this);
        key_1.setOnClickListener(this);
        key_2.setOnClickListener(this);
        key_3.setOnClickListener(this);
        key_4.setOnClickListener(this);
        key_5.setOnClickListener(this);
        key_6.setOnClickListener(this);
        key_7.setOnClickListener(this);
        key_8.setOnClickListener(this);
        key_9.setOnClickListener(this);
        key_0.setOnClickListener(this);
        key_jian.setOnClickListener(this);
        key_jia.setOnClickListener(this);
        key_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        ExtraKeysView.sendKey2(mTerminalView, "BKSP");

                        key_back.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                        key_back.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));


                        if (isVibrator)
                            vibrator.vibrate(50);
                        isLong = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while (isLong) {


                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    while (isLong) {

                                        try {
                                            Thread.sleep(80);

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        TermuxApplication.mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ExtraKeysView.sendKey2(mTerminalView, "BKSP");
                                                if (isVibrator)
                                                    vibrator.vibrate(50);
                                            }
                                        });


                                    }

                                }


                            }
                        }).start();


                        break;

                    case MotionEvent.ACTION_UP:
                        key_back.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        key_back.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        isLong = false;
                        break;

                }

                return true;
            }
        });


        key_table.setOnClickListener(this);
        key_Q.setOnClickListener(this);
        key_W.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        key_W.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                        key_W.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                        if (isAlt) {
                            isAlt = false;
                            mTerminalView.sendTextToTerminalAlt("w", true);
                            key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                            return true;
                        }
                        if (isCtrl) {
                            isCtrl = false;
                            mTerminalView.sendTextToTerminalCtrl("w", true);
                            key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            return true;
                        }

                        if (isShift) {

                            ExtraKeysView.sendKey2(mTerminalView, "UP");

                            key_W.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                            key_W.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));


                            if (isVibrator)
                                vibrator.vibrate(50);
                            isLong = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (isLong) {


                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        while (isLong) {

                                            try {
                                                Thread.sleep(80);

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ExtraKeysView.sendKey2(mTerminalView, "UP");
                                                    if (isVibrator)
                                                        vibrator.vibrate(50);
                                                }
                                            });


                                        }

                                    }


                                }
                            }).start();
                            return true;
                        }


                        if (isVibrator)
                            vibrator.vibrate(50);
                        if (isCaps) {
                            mTerminalView.sendTextToTerminal("W");
                        } else {
                            mTerminalView.sendTextToTerminal("w");
                        }


                        break;

                    case MotionEvent.ACTION_UP:
                        key_W.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        key_W.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        isLong = false;
                        break;

                }

                return true;
            }
        });
        key_E.setOnClickListener(this);
        key_R.setOnClickListener(this);
        key_T.setOnClickListener(this);
        key_Y.setOnClickListener(this);
        key_U.setOnClickListener(this);
        key_I.setOnClickListener(this);
        key_O.setOnClickListener(this);
        key_P.setOnClickListener(this);
        key_zuokuohao.setOnClickListener(this);
        key_youkuohao.setOnClickListener(this);
        key_shuxian.setOnClickListener(this);


        key_daxiaoxie.setOnClickListener(this);

        key_A.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        key_A.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                        key_A.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                        if (isAlt) {
                            isAlt = false;
                            mTerminalView.sendTextToTerminalAlt("a", true);
                            key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                            return true;
                        }
                        if (isCtrl) {
                            isCtrl = false;
                            mTerminalView.sendTextToTerminalCtrl("a", true);
                            key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            return true;
                        }

                        if (isShift) {

                            ExtraKeysView.sendKey2(mTerminalView, "LEFT");

                            key_A.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                            key_A.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));


                            if (isVibrator)
                                vibrator.vibrate(50);
                            isLong = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (isLong) {


                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        while (isLong) {

                                            try {
                                                Thread.sleep(80);

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ExtraKeysView.sendKey2(mTerminalView, "LEFT");
                                                    if (isVibrator)
                                                        vibrator.vibrate(50);
                                                }
                                            });


                                        }

                                    }


                                }
                            }).start();
                            return true;
                        }


                        if (isVibrator)
                            vibrator.vibrate(50);
                        if (isCaps) {
                            mTerminalView.sendTextToTerminal("A");
                        } else {
                            mTerminalView.sendTextToTerminal("a");
                        }


                        break;

                    case MotionEvent.ACTION_UP:
                        key_A.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        key_A.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        isLong = false;
                        break;

                }

                return true;
            }
        });
        key_S.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        key_S.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                        key_S.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                        if (isAlt) {
                            isAlt = false;
                            mTerminalView.sendTextToTerminalAlt("s", true);
                            key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                            return true;
                        }
                        if (isCtrl) {
                            isCtrl = false;
                            mTerminalView.sendTextToTerminalCtrl("s", true);
                            key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            return true;
                        }

                        if (isShift) {

                            ExtraKeysView.sendKey2(mTerminalView, "DOWN");

                            key_S.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                            key_S.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));


                            if (isVibrator)
                                vibrator.vibrate(50);
                            isLong = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (isLong) {


                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        while (isLong) {

                                            try {
                                                Thread.sleep(80);

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ExtraKeysView.sendKey2(mTerminalView, "DOWN");
                                                    if (isVibrator)
                                                        vibrator.vibrate(50);
                                                }
                                            });


                                        }

                                    }


                                }
                            }).start();
                            return true;
                        }


                        if (isVibrator)
                            vibrator.vibrate(50);
                        if (isCaps) {
                            mTerminalView.sendTextToTerminal("S");
                        } else {
                            mTerminalView.sendTextToTerminal("s");
                        }


                        break;

                    case MotionEvent.ACTION_UP:
                        key_S.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        key_S.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        isLong = false;
                        break;

                }

                return true;
            }
        });
        key_D.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        key_D.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                        key_D.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                        if (isAlt) {
                            isAlt = false;
                            mTerminalView.sendTextToTerminalAlt("d", true);
                            key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                            return true;
                        }
                        if (isCtrl) {
                            isCtrl = false;
                            mTerminalView.sendTextToTerminalCtrl("d", true);
                            key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                            return true;
                        }

                        if (isShift) {

                            ExtraKeysView.sendKey2(mTerminalView, "RIGHT");

                            key_D.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                            key_D.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));


                            if (isVibrator)
                                vibrator.vibrate(50);
                            isLong = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (isLong) {


                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        while (isLong) {

                                            try {
                                                Thread.sleep(80);

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ExtraKeysView.sendKey2(mTerminalView, "RIGHT");
                                                    if (isVibrator)
                                                        vibrator.vibrate(50);
                                                }
                                            });


                                        }

                                    }


                                }
                            }).start();
                            return true;
                        }


                        if (isVibrator)
                            vibrator.vibrate(50);
                        if (isCaps) {
                            mTerminalView.sendTextToTerminal("D");
                        } else {
                            mTerminalView.sendTextToTerminal("d");
                        }


                        break;

                    case MotionEvent.ACTION_UP:
                        key_D.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        key_D.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                        isLong = false;
                        break;

                }

                return true;
            }
        });


        key_F.setOnClickListener(this);
        key_G.setOnClickListener(this);
        key_H.setOnClickListener(this);
        key_J.setOnClickListener(this);
        key_K.setOnClickListener(this);
        key_L.setOnClickListener(this);
        key_fenhao.setOnClickListener(this);
        key_yinyong.setOnClickListener(this);
        key_huiche.setOnClickListener(this);

        key_shift.setOnClickListener(this);
        key_Z.setOnClickListener(this);
        key_X.setOnClickListener(this);
        key_C.setOnClickListener(this);
        key_V.setOnClickListener(this);
        key_B.setOnClickListener(this);
        key_N.setOnClickListener(this);
        key_M.setOnClickListener(this);
        key_key_douhao.setOnClickListener(this);
        key_dianhao.setOnClickListener(this);
        key_wenhao.setOnClickListener(this);
        key_shift_r.setOnClickListener(this);


        key_ctrl.setOnClickListener(this);
        key_at_l.setOnClickListener(this);
        key_kong.setOnClickListener(this);
        key_at_r.setOnClickListener(this);
        key_ctrl_r.setOnClickListener(this);


        String text_color_view = SaveData.getData("text_color_view");
        if (!("def".equals(text_color_view))) {
            try {
                setTextKeyColor(Integer.parseInt(text_color_view));
            } catch (Exception e) {

            }

        }

        String vibrator = SaveData.getData("vibrator");

        if ("true".equals(vibrator)) {
            vibrator_key.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

            isVibrator = true;

        } else {

            vibrator_key.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
            isVibrator = false;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.esc:
                ExtraKeysView.sendKey2(mTerminalView, "ESC");
                break;
            case R.id.f1:
                ExtraKeysView.sendKey2(mTerminalView, "F1");
                break;
            case R.id.f2:
                ExtraKeysView.sendKey2(mTerminalView, "F2");
                break;
            case R.id.f3:
                ExtraKeysView.sendKey2(mTerminalView, "F3");
                break;
            case R.id.f4:
                ExtraKeysView.sendKey2(mTerminalView, "F4");
                break;
            case R.id.f5:
                ExtraKeysView.sendKey2(mTerminalView, "F5");
                break;
            case R.id.f6:
                ExtraKeysView.sendKey2(mTerminalView, "F6");
                break;
            case R.id.f7:
                ExtraKeysView.sendKey2(mTerminalView, "F7");
                break;
            case R.id.f8:
                ExtraKeysView.sendKey2(mTerminalView, "F8");
                break;
            case R.id.f9:
                ExtraKeysView.sendKey2(mTerminalView, "F9");
                break;
            case R.id.f10:
                ExtraKeysView.sendKey2(mTerminalView, "F10");
                break;
            case R.id.f11:
                ExtraKeysView.sendKey2(mTerminalView, "F11");
                break;
            case R.id.f12:
                ExtraKeysView.sendKey2(mTerminalView, "F12");
                break;

            //------------------------------------------------

            case R.id.key_dian:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("~");
                } else {
                    mTerminalView.sendTextToTerminal("`");
                }

                break;
            case R.id.key_1:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("!");
                } else {
                    mTerminalView.sendTextToTerminal("1");
                }
                break;
            case R.id.key_2:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("@");
                } else {
                    mTerminalView.sendTextToTerminal("2");
                }
                break;
            case R.id.key_3:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("#");
                } else {
                    mTerminalView.sendTextToTerminal("3");
                }
                break;
            case R.id.key_4:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("$");
                } else {
                    mTerminalView.sendTextToTerminal("4");
                }
                break;
            case R.id.key_5:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("%");
                } else {
                    mTerminalView.sendTextToTerminal("5");
                }
                break;
            case R.id.key_6:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("^");
                } else {
                    mTerminalView.sendTextToTerminal("6");
                }
                break;
            case R.id.key_7:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("&");
                } else {
                    mTerminalView.sendTextToTerminal("7");
                }
                break;
            case R.id.key_8:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("*");
                } else {
                    mTerminalView.sendTextToTerminal("8");
                }
                break;
            case R.id.key_9:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("(");
                } else {
                    mTerminalView.sendTextToTerminal("9");
                }
                break;
            case R.id.key_0:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal(")");
                } else {
                    mTerminalView.sendTextToTerminal("0");
                }
                break;
            case R.id.key_jian:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("_");
                } else {
                    mTerminalView.sendTextToTerminal("-");
                }
                break;
            case R.id.key_jia:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("+");
                } else {
                    mTerminalView.sendTextToTerminal("=");
                }
                break;
            case R.id.key_back:
                if (isVibrator)
                    vibrator.vibrate(50);
                ExtraKeysView.sendKey2(mTerminalView, "BKSP");
                break;


            //------------------------------------------------------

            case R.id.key_table:
                ExtraKeysView.sendKey2(mTerminalView, "TAB");
                break;
            case R.id.key_q:

                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("q", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }


                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("q", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }

                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("Q");
                } else {
                    mTerminalView.sendTextToTerminal("q");
                }
                break;
            case R.id.key_w:

                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("w", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }

                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("w", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }


                if (isShift) {
                    ExtraKeysView.sendKey2(mTerminalView, "UP");
                    return;
                }

                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("W");
                } else {
                    mTerminalView.sendTextToTerminal("w");
                }
                break;
            case R.id.key_e:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("e", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("e", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("E");
                } else {
                    mTerminalView.sendTextToTerminal("e");
                }
                break;
            case R.id.key_r:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("r", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("r", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("R");
                } else {
                    mTerminalView.sendTextToTerminal("r");
                }
                break;
            case R.id.key_t:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("t", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("t", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("T");
                } else {
                    mTerminalView.sendTextToTerminal("t");
                }
                break;
            case R.id.key_y:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("y", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("y", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("Y");
                } else {
                    mTerminalView.sendTextToTerminal("y");
                }
                break;
            case R.id.key_u:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("u", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("u", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("U");
                } else {
                    mTerminalView.sendTextToTerminal("u");
                }
                break;
            case R.id.key_i:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("i", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("i", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("I");
                } else {
                    mTerminalView.sendTextToTerminal("i");
                }
                break;
            case R.id.key_o:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("o", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("o", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("O");
                } else {
                    mTerminalView.sendTextToTerminal("o");
                }
                break;
            case R.id.key_p:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("p", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("p", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("P");
                } else {
                    mTerminalView.sendTextToTerminal("p");
                }
                break;
            case R.id.key_zuokuohao:

                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("{");
                } else {
                    mTerminalView.sendTextToTerminal("[");
                }
                break;
            case R.id.key_youkuohao:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("}");
                } else {
                    mTerminalView.sendTextToTerminal("]");
                }
                break;
            case R.id.key_shuxian:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("|");
                } else {
                    mTerminalView.sendTextToTerminal("\\");
                }
                break;


            //----------------------------------------------------------
            case R.id.key_daxiaoxie:
                if (isVibrator)
                    vibrator.vibrate(50);
                isCaps = !isCaps;
                if (isCaps) {

                    key_daxiaoxie.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                } else {
                    key_daxiaoxie.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                }

                break;
            case R.id.key_a:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("a", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("a", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }

                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    ExtraKeysView.sendKey2(mTerminalView, "LEFT");
                    return;
                }

                if (isCaps) {
                    mTerminalView.sendTextToTerminal("A");
                } else {
                    mTerminalView.sendTextToTerminal("a");
                }
                break;
            case R.id.key_s:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("s", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("s", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }


                if (isShift) {
                    ExtraKeysView.sendKey2(mTerminalView, "DOWN");
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("S");
                } else {
                    mTerminalView.sendTextToTerminal("s");
                }
                break;
            case R.id.key_d:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("d", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("d", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }

                if (isShift) {
                    ExtraKeysView.sendKey2(mTerminalView, "RIGHT");
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("D");
                } else {
                    mTerminalView.sendTextToTerminal("d");
                }
                break;
            case R.id.key_f:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("f", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("f", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("F");
                } else {
                    mTerminalView.sendTextToTerminal("f");
                }
                break;
            case R.id.key_g:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("g", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("g", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("G");
                } else {
                    mTerminalView.sendTextToTerminal("g");
                }
                break;
            case R.id.key_h:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("h", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("h", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("H");
                } else {
                    mTerminalView.sendTextToTerminal("h");
                }
                break;
            case R.id.key_j:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("j", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("j", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("J");
                } else {
                    mTerminalView.sendTextToTerminal("j");
                }
                break;
            case R.id.key_k:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("k", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("k", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("K");
                } else {
                    mTerminalView.sendTextToTerminal("k");
                }
                break;
            case R.id.key_l:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("l", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("l", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("L");
                } else {
                    mTerminalView.sendTextToTerminal("l");
                }
                break;
            case R.id.key_fenhao:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal(":");
                } else {
                    mTerminalView.sendTextToTerminal(";");
                }
                break;
            case R.id.key_yinyong:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("\"");
                } else {
                    mTerminalView.sendTextToTerminal("'");
                }
                break;
            case R.id.key_huiche:
                if (isVibrator)
                    vibrator.vibrate(50);
                ExtraKeysView.sendKey2(mTerminalView, "ENTER");

                break;


            //---------------------------------------------
            case R.id.key_shift:
                if (isVibrator)
                    vibrator.vibrate(50);

                isShift = !isShift;
                if (isShift) {
                    key_shift.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                    key_W.setText("↑");
                    key_A.setText("←");
                    key_D.setText("→");
                    key_S.setText("↓");
                } else {
                    key_shift.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_W.setText("W");
                    key_A.setText("A");
                    key_D.setText("D");
                    key_S.setText("S");

                }

                break;
            case R.id.key_z:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("z", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("z", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("Z");
                } else {
                    mTerminalView.sendTextToTerminal("z");
                }
                break;
            case R.id.key_x:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("x", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("x", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("X");
                } else {
                    mTerminalView.sendTextToTerminal("x");
                }
                break;
            case R.id.key_c:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("c", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("c", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("C");
                } else {
                    mTerminalView.sendTextToTerminal("c");
                }
                break;
            case R.id.key_v:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("v", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("v", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("V");
                } else {
                    mTerminalView.sendTextToTerminal("v");
                }
                break;
            case R.id.key_b:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("b", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("b", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("B");
                } else {
                    mTerminalView.sendTextToTerminal("b");
                }
                break;
            case R.id.key_n:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("n", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("n", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("N");
                } else {
                    mTerminalView.sendTextToTerminal("n");
                }
                break;
            case R.id.key_m:
                if (isAlt) {
                    isAlt = false;
                    mTerminalView.sendTextToTerminalAlt("m", true);
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));

                    return;
                }
                if (isCtrl) {
                    isCtrl = false;
                    mTerminalView.sendTextToTerminalCtrl("m", true);
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    return;
                }
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isCaps) {
                    mTerminalView.sendTextToTerminal("M");
                } else {
                    mTerminalView.sendTextToTerminal("m");
                }
                break;
            case R.id.key_douhao:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("<");
                } else {
                    mTerminalView.sendTextToTerminal(",");
                }
                break;
            case R.id.key_dianhao:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal(">");
                } else {
                    mTerminalView.sendTextToTerminal(".");
                }
                break;
            case R.id.key_wenhao:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isShift) {
                    mTerminalView.sendTextToTerminal("?");
                } else {
                    mTerminalView.sendTextToTerminal("/");
                }
                break;
            case R.id.key_shift_r:
                if (isVibrator)
                    vibrator.vibrate(50);
                key_layout.setVisibility(View.GONE);
                show_key_layout.setVisibility(View.VISIBLE);
                break;


            //---------------------------------------------
            case R.id.key_ctrl:
                if (isVibrator)
                    vibrator.vibrate(50);
                isCtrl = !isCtrl;
                // ExtraKeysView.sendKey2(mTerminalView, "BKSP");
                // mTerminalView.sendTextToTerminal("ctrl");
                // mTerminalView.sendTextToTerminalCtrl("c", true);


                if (isCtrl) {
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                } else {
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                }


                break;
            case R.id.key_at_l:
                if (isVibrator)
                    vibrator.vibrate(50);
                isAlt = !isAlt;
                if (isAlt) {
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                } else {
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                }
                break;
            case R.id.key_kong:
                if (isVibrator)
                    vibrator.vibrate(50);
                mTerminalView.sendTextToTerminal(" ");
                break;
            case R.id.key_at_r:
                if (isVibrator)
                    vibrator.vibrate(50);
                isAlt = !isAlt;
                if (isAlt) {
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                } else {
                    key_at_l.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_at_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                }

                // mTerminalView.sendTextToTerminal("ALT");


                break;
            case R.id.key_ctrl_r:
                if (isVibrator)
                    vibrator.vibrate(50);
                isCtrl = !isCtrl;

                if (isCtrl) {
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));

                } else {
                    key_ctrl.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    key_ctrl_r.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                }
                mTerminalView.sendTextToTerminal("CTRL");
                break;

            case R.id.show_key:
                if (isVibrator)
                    vibrator.vibrate(50);
                if (isVibrator)
                    vibrator.vibrate(50);
                key_layout.setVisibility(View.VISIBLE);
                show_key_layout.setVisibility(View.GONE);
                break;

            case R.id.vibrator_key:
                isVibrator = !isVibrator;
                if (isVibrator) {
                    vibrator_key.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                    // vibrator_key.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back_lan));
                    SaveData.saveData("vibrator", "true");
                } else {
                    // vibrator_key.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    vibrator_key.setBackground(TermuxApplication.mContext.getResources().getDrawable(R.drawable.shape_key_back));
                    SaveData.saveData("vibrator", "def");
                }


                break;


        }
    }


    public void setTextKeyColor(int color) {

        esc.setTextColor(color);
        f1.setTextColor(color);
        f2.setTextColor(color);
        f3.setTextColor(color);
        f4.setTextColor(color);
        f5.setTextColor(color);
        f6.setTextColor(color);
        f7.setTextColor(color);
        f8.setTextColor(color);
        f9.setTextColor(color);
        f10.setTextColor(color);
        f11.setTextColor(color);
        f12.setTextColor(color);

        show_key.setTextColor(color);


        key_dian.setTextColor(color);
        key_1.setTextColor(color);
        key_2.setTextColor(color);
        key_3.setTextColor(color);
        key_4.setTextColor(color);
        key_5.setTextColor(color);
        key_6.setTextColor(color);
        key_7.setTextColor(color);
        key_8.setTextColor(color);
        key_9.setTextColor(color);
        key_0.setTextColor(color);
        key_jian.setTextColor(color);
        key_jia.setTextColor(color);
        key_back.setTextColor(color);


        key_table.setTextColor(color);
        key_Q.setTextColor(color);
        key_W.setTextColor(color);
        key_E.setTextColor(color);
        key_R.setTextColor(color);
        key_T.setTextColor(color);
        key_Y.setTextColor(color);
        key_U.setTextColor(color);
        key_I.setTextColor(color);
        key_O.setTextColor(color);
        key_P.setTextColor(color);
        key_zuokuohao.setTextColor(color);
        key_youkuohao.setTextColor(color);
        key_shuxian.setTextColor(color);
        vibrator_key.setTextColor(color);


        key_daxiaoxie.setTextColor(color);
        key_A.setTextColor(color);
        key_S.setTextColor(color);
        key_D.setTextColor(color);
        key_F.setTextColor(color);
        key_G.setTextColor(color);
        key_H.setTextColor(color);
        key_J.setTextColor(color);
        key_K.setTextColor(color);
        key_L.setTextColor(color);
        key_fenhao.setTextColor(color);
        key_yinyong.setTextColor(color);
        key_huiche.setTextColor(color);

        key_shift.setTextColor(color);
        key_Z.setTextColor(color);
        key_X.setTextColor(color);
        key_C.setTextColor(color);
        key_V.setTextColor(color);
        key_B.setTextColor(color);
        key_N.setTextColor(color);
        key_M.setTextColor(color);
        key_key_douhao.setTextColor(color);
        key_dianhao.setTextColor(color);
        key_wenhao.setTextColor(color);
        key_shift_r.setTextColor(color);


        key_ctrl.setTextColor(color);
        key_at_l.setTextColor(color);
        key_kong.setTextColor(color);
        key_at_r.setTextColor(color);
        key_ctrl_r.setTextColor(color);

    }
}
