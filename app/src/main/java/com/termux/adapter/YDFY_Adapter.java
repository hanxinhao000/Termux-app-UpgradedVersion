package main.java.com.termux.adapter;

import android.util.Log;
import android.view.View;

import com.termux.R;

import java.util.List;

import main.java.com.termux.application.TermuxApplication;

public class YDFY_Adapter extends ListBaseAdapter<String> {

    public YDFY_Adapter(List<String> list) {
        super(list);
    }

    @Override
    public ViewHolder getViewHolder() {
        return new YDFY_ViewHolder(View.inflate(TermuxApplication.mContext, R.layout.item_sech, null));
    }

    @Override
    public void initView(int position, String s, ViewHolder viewHolder) {

        Log.e("XINHAO_HAN_TEXT", "initView: " + s);
        YDFY_ViewHolder ydfy_viewHolder = (YDFY_ViewHolder) viewHolder;

        try {
            ydfy_viewHolder.title.setText(s.split(" - ")[0]);
            ydfy_viewHolder.message.setText(s.split(" - ")[1]);
        } catch (Exception e) {
            ydfy_viewHolder.title.setText(s);
        }


    }
}
