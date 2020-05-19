package main.java.com.termux.view.adapter;

import android.view.View;

import com.max2idea.android.limbo.utils.UIUtils;
import com.termux.R;

import java.util.List;

import main.java.com.termux.adapter.ListBaseAdapter;
import main.java.com.termux.adapter.ViewHolder;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.view.holder.DownViewHolder;

/**
 * @author ZEL
 * @create By ZEL on 2020/5/7 18:13
 **/
public class DownAdapter extends ListBaseAdapter<String> {
    public DownAdapter(List<String> list) {
        super(list);
    }

    @Override
    public ViewHolder getViewHolder() {

        View inflate = View.inflate(TermuxApplication.mContext, R.layout.list_history_data, null);
        return new DownViewHolder(inflate);
    }

    @Override
    public void initView(int position, String s, ViewHolder viewHolder) {

        DownViewHolder downViewHolder = (DownViewHolder) viewHolder;

        downViewHolder.mTitle.setText(s);

    }
}
