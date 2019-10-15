package main.java.com.termux.listener;



import java.util.ArrayList;

import main.java.com.termux.datat.DataBean;


/**
 * Created by Administrator on 2019/6/5.
 */

public interface SmsMsgListener {
    void getSms(String sms, String id);

    void getSmsEnd(int size, int sizeLeng, ArrayList<DataBean> dataBean);
}
