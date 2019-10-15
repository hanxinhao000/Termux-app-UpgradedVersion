package main.java.com.termux.utils;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.datat.DataBean;
import main.java.com.termux.listener.SmsMsgListener;


/**
 * Created by Administrator on 2019/6/5.
 */

public class SmsUtils {

    /**
     * 读取短信接口
     *
     * @return
     */

    @SuppressLint("LongLogTag")
    public static void getSmsInPhone(SmsMsgListener smsMsgListener) {
        final String SMS_URI_ALL = "content://sms/"; // 所有短信
        final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
        final String SMS_URI_SEND = "content://sms/sent"; // 已发送
        final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
        final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
        final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
        final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表


        ArrayList<DataBean> arrayList = new ArrayList<>();

        //总共短信
        int size = 0;

        //较长短信
        int sizeLeng = 0;
        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person",
                "body", "date", "type", "service_center",};
            Cursor cur = null;
            try {
                cur = TermuxApplication.mContext.getContentResolver().query(uri, projection, null,
                    null, "date desc"); // 获取手机内部短信
                // 获取短信中最新的未读短信
            } catch (Exception e) {
                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TermuxApplication.mContext, "你没有开启短信权限!", Toast.LENGTH_SHORT).show();
                    }
                });

                return;
            }

            if(cur == null){
                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TermuxApplication.mContext, "你没有开启短信权限!", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }


            try {
                if (cur.moveToFirst()) {

                    int index_Address = cur.getColumnIndex("address");
                    int index_Person = cur.getColumnIndex("person");
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");
                    int queued = cur.getColumnIndex("service_center");
                    int _id = cur.getColumnIndex("_id");

                    /**
                     *
                     *
                     *  String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
                     Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");
                     *
                     */
                    do {
                        String strAddress = cur.getString(index_Address);
                        int _id1 = cur.getInt(_id);
                        int intPerson = cur.getInt(index_Person);
                        String strbody = cur.getString(index_Body);
                        long longDate = cur.getLong(index_Date);
                        int intType = cur.getInt(index_Type);
                        int stringQueued = cur.getInt(queued);

                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                        Date d = new Date(longDate);
                        String strDate = dateFormat.format(d);

                        String strType = "";
                        if (intType == 1) {
                            strType = "接收";
                        } else if (intType == 2) {
                            strType = "发送";
                        } else if (intType == 3) {
                            strType = "草稿";
                        } else if (intType == 4) {
                            strType = "发件箱";
                        } else if (intType == 5) {
                            strType = "发送失败";
                        } else if (intType == 6) {
                            strType = "待发送列表";
                        } else if (intType == 0) {
                            strType = "所以短信";
                        } else {
                            strType = "null";
                        }


                        String s1 = strAddress + "@截取符号@" + intPerson + "@截取符号@" + strbody + "@截取符号@" + strDate + "@截取符号@" + strType + "@截取符号@" + _id1;


                        Log.e("唯一性", "getSmsInPhone: " + stringQueued + "---" + strbody);

                        // smsMsgListener.getSms(s1, _id1 + "");

                        DataBean dataBean = new DataBean();
                        dataBean.setMsg(s1);
                        dataBean.setId(_id1);
                        arrayList.add(dataBean);

                        size++;
                        if (s1.length() > 200) {
                            sizeLeng++;
                        }

                    } while (cur.moveToNext());

                    if (!cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                } else {

                }
            } catch (Exception e) {

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(TermuxApplication.mContext.getApplicationContext(), "请允许APP读取短信", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        } catch (SQLiteException ex) {
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }

        smsMsgListener.getSmsEnd(size, sizeLeng, arrayList);


    }
}
