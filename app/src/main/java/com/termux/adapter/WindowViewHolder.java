package main.java.com.termux.adapter;

import android.view.View;
import android.widget.TextView;

import com.termux.R;

public class WindowViewHolder extends ViewHolder {

    public TextView title;
    public TextView type;
    public TextView size;

    public WindowViewHolder(View mView) {
        super(mView);
        title = (TextView) findViewById(R.id.title);
        type = (TextView) findViewById(R.id.type);
        size = (TextView) findViewById(R.id.size);
    }
}
