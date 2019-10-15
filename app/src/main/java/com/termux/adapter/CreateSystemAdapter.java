package main.java.com.termux.adapter;

import android.graphics.Color;
import android.view.View;

import com.termux.R;

import java.util.List;

import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.bean.ReadSystemBean;
import main.java.com.termux.viewholder.CreateSystemVieHolder;

public class CreateSystemAdapter extends ListBaseAdapter<ReadSystemBean> {

    public CreateSystemAdapter(List<ReadSystemBean> list) {
        super(list);
    }


    @Override
    public ViewHolder getViewHolder() {
        return new CreateSystemVieHolder(View.inflate(TermuxApplication.mContext, R.layout.list_create_system, null));
    }

    @Override
    public void initView(int position, ReadSystemBean readSystemBean, ViewHolder viewHolder) {

        CreateSystemVieHolder createSystemVieHolder = (CreateSystemVieHolder) viewHolder;

        createSystemVieHolder.title.setText(readSystemBean.name);

        createSystemVieHolder.msg.setText(readSystemBean.dir);

        if (readSystemBean.isCkeck) {

            createSystemVieHolder.title.setText(readSystemBean.name + "   <——");
            createSystemVieHolder.title.setTextColor(Color.parseColor("#ad0015"));

        } else {
            createSystemVieHolder.title.setText(readSystemBean.name );
            createSystemVieHolder.title.setTextColor(Color.parseColor("#ffffff"));
        }


    }
}
