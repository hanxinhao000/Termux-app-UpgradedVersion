package main.java.com.termux.view;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.termux.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import main.java.com.termux.view.adapter.DownAdapter;

/**
 * @author ZEL
 * @create By ZEL on 2020/5/7 18:09
 **/
public class DownDialog extends BaseDialog {

    private ListView mListView;
    private ListView mListView1;

    public DownDialog(@NonNull Context context) {
        super(context);
    }

    public DownDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    int getContentView() {
        return R.layout.list_history;
    }

    @Override
    void initViewDialog(View mView) {

        mListView = mView.findViewById(R.id.list_view);
        mListView1 = mView.findViewById(R.id.list_view_1);


    }

    public void setListArray(ArrayList<String> array) {

        mListView.setAdapter(new DownAdapter(array));
    }
}
