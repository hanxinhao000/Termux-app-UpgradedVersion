package main.java.com.termux.adapter;

import android.view.View;
import android.widget.TextView;

import com.termux.R;

public class YDFY_ViewHolder extends ViewHolder {

    public TextView title;
    public TextView message;

    public YDFY_ViewHolder(View mView) {
        super(mView);

        title = (TextView) findViewById(R.id.title);
        message = (TextView) findViewById(R.id.message);
    }
}
