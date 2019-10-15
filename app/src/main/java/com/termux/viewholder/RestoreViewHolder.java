package main.java.com.termux.viewholder;

import android.view.View;
import android.widget.TextView;

import com.termux.R;

import main.java.com.termux.adapter.ViewHolder;

public class RestoreViewHolder extends ViewHolder {


    public TextView msg_title;
    public TextView msg_message;

    public RestoreViewHolder(View mView) {
        super(mView);
        msg_title = (TextView) findViewById(R.id.msg_title);
        msg_message = (TextView) findViewById(R.id.msg_message);
    }
}
