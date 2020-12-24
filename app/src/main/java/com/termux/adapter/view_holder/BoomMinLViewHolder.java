package main.java.com.termux.adapter.view_holder;

import android.view.View;
import android.widget.ImageView;

import com.termux.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import main.java.com.termux.utils.CustomTextView;

/**
 * @author ZEL
 * @create By ZEL on 2020/12/17 14:42
 **/
public class BoomMinLViewHolder extends RecyclerView.ViewHolder {

    public CustomTextView name;
    public CustomTextView value;
    public CardView item_card;
    public ImageView beijin;
    public BoomMinLViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        value = itemView.findViewById(R.id.value);
        item_card = itemView.findViewById(R.id.item_card);
        beijin = itemView.findViewById(R.id.beijin);
    }
}
