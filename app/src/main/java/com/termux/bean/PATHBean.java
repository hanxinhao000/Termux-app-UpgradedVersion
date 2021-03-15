package main.java.com.termux.bean;

import java.util.List;

/**
 * @author ZEL
 * @create By ZEL on 2021/3/12 16:56
 **/
public class PATHBean {


    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data{

        private String pathKey;
        private String pathValue;

        public String getPathKey() {
            return pathKey;
        }

        public void setPathKey(String pathKey) {
            this.pathKey = pathKey;
        }

        public String getPathValue() {
            return pathValue;
        }

        public void setPathValue(String pathValue) {
            this.pathValue = pathValue;
        }
    }


}
