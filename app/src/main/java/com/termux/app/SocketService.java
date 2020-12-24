package main.java.com.termux.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.java.com.termux.utils.LogToFile;
import main.java.com.termux.utils.UUtils;

/**
 *
 * 106版本大致预计更新如下(以实际为准):
 * 1.更新自定义功能页面
 * 2.更新qemu内核（可更换）
 * 3.更新自定义命令连续执行
 * 例如: apt-get update  apt-get install vim (能连续执行)
 *
 */

public class SocketService extends Service {

    public static final String TAG = "";
    public static Boolean mainThreadFlag = true;
    public static Boolean ioThreadFlag = true;
    ServerSocket serverSocket = null;
    public static int SERVER_PORT = 6588;




    private CopyOnWriteArrayList<Socket> clients = new CopyOnWriteArrayList<Socket>();
    private ExecutorService mExecutorService = null; // 线程池
    public static sysBroadcastReceiver sysBR;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /* 创建内部类sysBroadcastReceiver 并注册registerReceiver */
        sendMessage("服务已创建...");
        UUtils.showLog("服务已创建...");
        sysRegisterReceiver();

    }

    private void doListen() {
        sendMessage("服务器启动...创建任务");
        UUtils.showLog("服务器启动...创建任务");
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            while (mainThreadFlag) {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                mExecutorService = Executors.newCachedThreadPool(); // 创建一个线程池
                mExecutorService.execute(new Service(socket)); // 启动一个新的线程来处理连接
            }

        } catch (Exception e) {
            LogToFile.d(">>>异常doListen>>>>:",e.getMessage());
            e.printStackTrace();
        }
    }

    /* 创建内部类sysBroadcastReceiver 并注册registerReceiver */
    private void sysRegisterReceiver() {
        sysBR = new sysBroadcastReceiver();
        /* 注册BroadcastReceiver */
        IntentFilter filter1 = new IntentFilter();
        /* 新的应用程序被安装到了设备上的广播 */
        filter1.addAction("android.intent.action.PACKAGE_ADDED");
        filter1.addDataScheme("package");
        filter1.addAction("android.intent.action.PACKAGE_REMOVED");
        filter1.addDataScheme("package");
        registerReceiver(sysBR, filter1);
    }

    /* 内部类：BroadcastReceiver 用于接收系统事件 */
    private class sysBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase("android.intent.action.PACKAGE_ADDED")) {
            } else if (action.equalsIgnoreCase("android.intent.action.PACKAGE_REMOVED")) {
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendMessage("服务已启动onStartCommand...");
        UUtils.showLog("服务已启动onStartCommand...");
        mainThreadFlag = true;
        new Thread() {
            public void run() {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                        Runtime.getRuntime().exec("taskkill /F /pid " + SERVER_PORT + "");
                    } catch (IOException e) {
                        LogToFile.d(">>>>Command异常>>>>","onStartCommand异常"+e.getMessage());
                        e.printStackTrace();
                    }
                }
                doListen();
            }
        }.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭线程
        mainThreadFlag = false;
        ioThreadFlag = false;
        // 关闭服务器
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            LogToFile.d("service>>>>","onDestroy"+e.getMessage());
            e.printStackTrace();
        }
    }

    // 处理与client对话的线程
   public class Service implements Runnable {
        private volatile boolean kk = true;
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";
        private String msg1 = "连接失败";
        private String ip = "";

        public Service(Socket socket) {
            this.socket = socket;
            if (!socket.isClosed() && socket.isConnected()) {
                LogToFile.d("service","服务已启动...");
                sendMessage("服务已启动...");
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    ip = socket.getInetAddress().getHostAddress();
                    ip = socket.getInetAddress().getHostAddress();
                    String m = "";
                    if ("127.0.0.1".equals(ip)) {
                        m = "电脑";
                        sendMessage(m + "端已连接...");
                    } else {
                        m = "手机";
                        sendMessage(m + "端已连接...");
                    }
                    LogToFile.d("service",m + "端已连接...");
//                    sendMessage("客户端：" + socket.getInetAddress().getHostAddress() + "-->" + msg + "连接上...");
                    this.sendmsg(msg, ip);
                } catch (IOException e) {
                    LogToFile.d("service>>>>","Service"+e.getMessage());
                    e.printStackTrace();
                }
            } else {
                LogToFile.d("service","客户端：" + socket.getInetAddress() + "断开连接...");
                sendMessage("客户端：" + socket.getInetAddress() + "断开连接...");
                this.sendmsg(msg1, ip);
            }
        }

        public Service(){
        }

        public void run() {
            while (kk) {
                try {
                    if ((msg = in.readLine()) != null) {
                        // 当客户端发送的信息为：exit时，关闭连接
                        if (msg.equals("exit")) {
                            clients.remove(socket);
                            in.close();
                            socket.close();
                            break;
                            // 接收客户端发过来的信息msg，然后发送给客户端。
                        } else {
                            ip = socket.getInetAddress().getHostAddress();
                            String m = "";
                            if ("127.0.0.1".equals(ip)) {
                                m = "电脑";
//                                sendMessage(m + "发送:" + msg);
                            } else {
                                m = "手机";
                            }
                            LogToFile.d("service",m+"发送:" + msg);
//                             sendMessage(m+"发送:" + msg);
                            this.sendmsg(msg, ip);
                        }
                    }
                } catch (EOFException e) {
                    LogToFile.d("service>>>>",e.getMessage());
                    kk = false;
                    LogToFile.d("service","客户端：" + socket.getInetAddress() + "断开连接...");
                    sendMessage("客户端：" + socket.getInetAddress() + "断开连接...");
                    clients.remove(socket);
                    this.sendmsg(msg1, ip);
                } catch (IOException e) {
                    LogToFile.d("service>>>>","run"+e.getMessage());
                    kk = false;
                    LogToFile.d("service","客户端：" + socket.getInetAddress() + "断开连接...");
                    sendMessage("客户端：" + socket.getInetAddress() + "断开连接...");
                    clients.remove(socket);
                    this.sendmsg(msg1, ip);
                }
            }
        }

        // 向客户端发送信息
        public void sendmsg(String msg, String ip) {

            UUtils.showLog("连接提示(发送消息)：" + msg + "||||ip:" + ip);
            if(msg != null && !(msg.isEmpty()) && !("null".equals(msg))) {

                TermuxActivity.mTerminalView.sendTextToTerminal(msg + "\n");
            }

        }
    }

    public void sendMessage(String msg) {

        UUtils.showLog("连接提示(发送消息)" + msg);
    }



    /**
     * 判断是否断开连接，断开返回true,没有返回false
     *
     * @param socket
     * @return
     */
    public static Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception e) {
            LogToFile.d("service>>>>","isServerClose"+e.getMessage());
            return true;
        }
    }

    //获取手机序列号
    public String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            LogToFile.d("service>>>>","getSerialNumber"+e.getMessage());
            e.printStackTrace();
        }
        return serial;
    }




}
