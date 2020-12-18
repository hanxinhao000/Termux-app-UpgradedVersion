package main.java.com.termux.app;

import android.view.View;

import com.max2idea.android.limbo.utils.UIUtils;

import main.java.com.termux.application.TermuxApplication;

public class ViewUtils {

    private static boolean isBooean = false;

    // ViewUtils.xuanzhuanView(mTerminalView,nemu_wo);

    public static void xuanzhuanView(View view1,View view2){

        if(true){
            return;
        }
        if(view1.getVisibility() == View.VISIBLE){
            isBooean = false;
        }else{
            isBooean = true;
        }

        if(isBooean){
            fan(view1, view2);
        }else {

            zhen(view1, view2);
        }


    }

    public static void xuanzhuanView1(View view1,View view2){


        if(view1.getVisibility() == View.VISIBLE){
            isBooean = false;
        }else{
            isBooean = true;
        }

        if(isBooean){
            fan(view1, view2);
        }else {

            zhen(view1, view2);
        }


    }

    //正旋转

    public static void zhen(View view1,View view2){


        
        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (i > -90){


                    i--;


                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int finalI = i;
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view1.setRotationY(finalI);
                        }
                    });
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view1.setVisibility(View.GONE);
                        view2.setVisibility(View.VISIBLE);
                        view2.setRotationY(90);
                    }
                });

                i = 90;

                while (i > 0){


                    i--;


                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int finalI = i;
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view2.setRotationY(finalI);
                        }
                    });
                }


                isBooean = true;

            }
        }).start();




    }

    //反旋转
    public static void fan(View view1,View view2){



        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while (i > -90){


                    i--;


                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int finalI = i;
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view2.setRotationY(finalI);
                        }
                    });
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        view2.setVisibility(View.GONE);
                        view1.setVisibility(View.VISIBLE);
                        view1.setRotationY(90);
                    }
                });

                i = 90;

                while (i > 0){


                    i--;


                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int finalI = i;
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            view1.setRotationY(finalI);
                        }
                    });
                }


                isBooean = false;

            }
        }).start();


    }
}
