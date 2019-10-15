package main.java.com.termux.datat;

/**
 * Created by Administrator on 2019/6/6.
 */

public class DataBean {

    private int id;
    private String msg;
    //为1 表示上传, //为0 表示不上传
    private int update = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "id='" + id + '\'' +
                ", msg='" + msg + '\'' +
                ", update=" + update +
                '}';
    }
}
