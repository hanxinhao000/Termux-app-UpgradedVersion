package main.java.com.termux.adapter;

import android.view.View;

import com.termux.R;

import java.util.List;

import main.java.com.termux.application.TermuxApplication;

public class ItemSelectAdapter extends ListBaseAdapter<String> {

    public ItemSelectAdapter(List<String> list) {
        super(list);
    }

    @Override
    public ViewHolder getViewHolder() {
        return new ItemSelectViewHolder(View.inflate(TermuxApplication.mContext, R.layout.list_key, null));
    }

    @Override
    public void initView(int position, String s, ViewHolder viewHolder) {

        ItemSelectViewHolder itemSelectViewHolder = (ItemSelectViewHolder) viewHolder;

        itemSelectViewHolder.item_key.setText(s);

    }
}
