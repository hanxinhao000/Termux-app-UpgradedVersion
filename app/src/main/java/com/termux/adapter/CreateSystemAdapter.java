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

        createSystemVieHolder.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] strings = {"删除"};

                AlertDialog.Builder builder = new AlertDialog
                    .Builder(mActivity);
                builder.setTitle("删除完成,需要重进才能刷新");
                // builder.setMessage("这是个滚动列表，往下滑");
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(TermuxActivity.this, "选择了第" + which + "个", Toast.LENGTH_SHORT).show();

                        if(readSystemBean.dir.equals("/data/data/com.termux/files")){
                            Toast.makeText(mActivity, "你不能删除主系统", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MyDialog myDialog = new MyDialog(mActivity);
                        myDialog.show();
                        ZipUtils.delFolder(readSystemBean.dir, new ZipUtils.ZipNameListener() {
                            @Override
                            public void zip(String FileName, int size, int position) {
                                myDialog.getDialog_title().setText(FileName);
                            }

                            @Override
                            public void complete() {

                            }

                            @Override
                            public void progress(long size, long position) {

                            }
                        });


                    }
                });
                builder.show();
                //readSystemBean.dir


                return true;
            }
        });

        if (readSystemBean.isCkeck) {

            createSystemVieHolder.title.setText(readSystemBean.name + "   <——");
            createSystemVieHolder.title.setTextColor(Color.parseColor("#ad0015"));

        } else {
            createSystemVieHolder.title.setText(readSystemBean.name);
            createSystemVieHolder.title.setTextColor(Color.parseColor("#ffffff"));
        }


    }
}
