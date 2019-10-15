package main.java.com.termux.adapter;

import android.view.View;

import com.termux.R;

import java.io.File;
import java.util.List;

import main.java.com.termux.application.TermuxApplication;

public class WindowAdapter extends ListBaseAdapter<File> {

    public WindowAdapter(List<File> list) {
        super(list);
    }

    @Override
    public ViewHolder getViewHolder() {
        return new WindowViewHolder(View.inflate(TermuxApplication.mContext, R.layout.list_windows_item, null));
    }

    @Override
    public void initView(int position, File file, ViewHolder viewHolder) {

        WindowViewHolder windowViewHolder = (WindowViewHolder) viewHolder;

        windowViewHolder.size.setText("大小:" + (file.length() / 1024 / 1024) + "MB");

        windowViewHolder.title.setText(file.getName());

        try {
            windowViewHolder.type.setText("类型:" + file.getName().substring(file.getName().lastIndexOf(".") + 1));
        }catch (Exception e){
            windowViewHolder.type.setText("类型:未知");
        }

    }
}
