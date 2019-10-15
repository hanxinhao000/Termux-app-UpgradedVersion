package main.java.com.termux.viewholder;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.termux.R;

import main.java.com.termux.adapter.ViewHolder;

public class CreateSystemVieHolder extends ViewHolder {


    public TextView title;

    public TextView msg;



    public CreateSystemVieHolder(View mView) {
        super(mView);

        title = (TextView) findViewById(R.id.title);

        msg = (TextView) findViewById(R.id.msg);


    }
}
