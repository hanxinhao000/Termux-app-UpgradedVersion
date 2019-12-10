package main.java.com.termux.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import main.java.com.termux.datat.FYBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YDFY_Utils {

    public static final String URL = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=";
    public static OkHttpClient okHttpClient = new OkHttpClient();

    public static void eTozh(String s, FYListener fyListener) {

       Request request = new Request.Builder().get().url(URL + s).build();
       
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                fyListener.onFailure(s);
                Log.e("XINHAO_HAN", "onFailure: " + e.toString() );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();

                try {
                    FYBean fyBean = new Gson().fromJson(string, FYBean.class);
                    fyListener.onResponse(fyBean.getTranslateResult().get(0).get(0).getTgt());
                } catch (Exception e) {
                    fyListener.onFailure(s);
                    return;
                }



            }
        });


    }


    public interface FYListener {

        void onResponse(String s);

        void onFailure(String s);


    }

}
