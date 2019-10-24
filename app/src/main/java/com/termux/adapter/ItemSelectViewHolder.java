package main.java.com.termux.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.termux.R;

public class ItemSelectViewHolder extends ViewHolder {

    public TextView item_key;

    public ItemSelectViewHolder(View mView) {
        super(mView);

        item_key = (TextView) findViewById(R.id.item_key);
    }
}
