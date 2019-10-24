package main.java.com.termux.datat;

import java.io.File;

public class TermuxData {

    private static TermuxData mTermuxData;
    public int isB_R;
    public int config;
    public File mFile;


    public static TermuxData getInstall() {

        if (mTermuxData == null) {


            synchronized (TermuxData.class) {


                if (mTermuxData == null) {
                    mTermuxData = new TermuxData();

                    return mTermuxData;
                } else {
                    return mTermuxData;
                }

            }


        } else {
            return mTermuxData;
        }


    }



    public String fileUrl;

}
