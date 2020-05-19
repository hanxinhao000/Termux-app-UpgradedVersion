package main.java.com.termux.view.holder;

import android.view.View;
import android.widget.TextView;

import com.termux.R;

import main.java.com.termux.adapter.ViewHolder;

/**
 * @author ZEL
 * @create By ZEL on 2020/5/7 18:13
 **/
public class DownViewHolder extends ViewHolder {

    public TextView mTitle;
    public DownViewHolder(View mView) {
        super(mView);
        mTitle = mView.findViewById(R.id.title);
    }
}
