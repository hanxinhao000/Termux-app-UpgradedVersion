package main.java.com.termux.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.termux.R;

import java.util.List;

import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.app.ZipUtils;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.bean.ReadSystemBean;
import main.java.com.termux.view.MyDialog;
import main.java.com.termux.viewholder.CreateSystemVieHolder;

public class CreateSystemAdapter extends ListBaseAdapter<ReadSystemBean> {

    private Activity mActivity;

    public CreateSystemAdapter(List<ReadSystemBean> list, Activity activity) {
        super(list);
        mActivity = activity;
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
            createSystemVieHolder.title.setText(readSystemBean.name);
            createSystemVieHolder.title.setTextColor(Color.parseColor("#ffffff"));
        }


    }
}
