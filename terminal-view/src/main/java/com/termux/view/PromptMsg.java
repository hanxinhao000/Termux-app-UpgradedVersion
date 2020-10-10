package com.termux.view;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/10 10:09
 **/
public class PromptMsg {

    //默认获取目录
    private String path = "/data/data/com.termux/files/usr/bin";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
