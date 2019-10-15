package main.java.com.termux.adapter;

import android.view.View;
import android.widget.TextView;

import com.termux.R;

public class DialogViewHolder extends ViewHolder {
    public TextView title;

    public DialogViewHolder(View mView) {
        super(mView);
        title = (TextView) findViewById(R.id.title);
    }
}
