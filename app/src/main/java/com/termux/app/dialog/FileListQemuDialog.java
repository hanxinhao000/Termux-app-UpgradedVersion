package main.java.com.termux.app.dialog;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.termux.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import main.java.com.termux.adapter.ListBaseAdapter;
import main.java.com.termux.adapter.ViewHolder;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/20 9:50
 **/
public class FileListQemuDialog extends BaseDialogCentre {

    private File file;
    private ListView list_view;
    private TextView title;
    private TextView tv_empty;
    private TextView def_txt;
    ArrayList<File> files1Array;

    private LinearLayout def_ll;
    public FileListQemuDialog(@NonNull Context context) {
        super(context);
    }

    public FileListQemuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    public void setTitleText(String data){
        title.setText(data);
    }
    public void setFilePath(File filePath){

        File[] files = filePath.listFiles();
        if(files != null && files.length > 0){
            tv_empty.setVisibility(View.GONE);
            files1Array = new ArrayList<>(Arrays.asList(files));
            ListFileAdapter listFileAdapter = new ListFileAdapter(files1Array);
            list_view.setAdapter(listFileAdapter);
        }else{
            tv_empty.setVisibility(View.VISIBLE);
            tv_empty.setText(UUtils.getString(R.string.当前目录下没有qemu安装文件));
            files1Array = new ArrayList<>();
            ListFileAdapter listFileAdapter = new ListFileAdapter(files1Array);
            list_view.setAdapter(listFileAdapter);
        }







    }

    public void setBoomBtnVisible(boolean btnVisible){
        if(!btnVisible){
            def_ll.setVisibility(View.GONE);
        }else{
            def_ll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    void initViewDialog(View mView) {
        file =  new File(Environment.getExternalStorageDirectory(),"/xinhao/qemu");
        list_view = mView.findViewById(R.id.list_view);
        title = mView.findViewById(R.id.title);
        tv_empty = mView.findViewById(R.id.tv_empty);
        def_txt = mView.findViewById(R.id.def_txt);
        def_ll = mView.findViewById(R.id.def_ll);
        File[] files = file.listFiles();
        def_ll.setVisibility(View.GONE);

        if(files != null && files.length > 0){
            tv_empty.setVisibility(View.GONE);
            ArrayList<File> files1 =  new ArrayList<>(Arrays.asList(files));
            ListFileAdapter listFileAdapter = new ListFileAdapter(files1);

            list_view.setAdapter(listFileAdapter);

            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(mOnItemFileClickListener!= null){

                            mOnItemFileClickListener.onItemClick(files1.get(position));

                    }
                }
            });
        }else{

            tv_empty.setVisibility(View.VISIBLE);
            tv_empty.setText(UUtils.getString(R.string.当前目录下没有qemu安装文件));

        }




        def_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mOnDefClickListener!= null){
                    mOnDefClickListener.onDefClickListener("");
                }

            }
        });
    }

    @Override
    int getContentView() {
        return R.layout.dialog_file_mingl_list;
    }


    private class ListFileAdapter extends ListBaseAdapter<File>{

        public ListFileAdapter(List<File> list) {
            super(list);
        }

        @Override
        public ViewHolder getViewHolder() {
            return new ListFileViewHolder(UUtils.getViewLay(R.layout.list_file_list));
        }

        @Override
        public void initView(int position, File file, ViewHolder viewHolder) {

            ListFileViewHolder listFileViewHolder = (ListFileViewHolder) viewHolder;
            listFileViewHolder.msg_title.setText(file.getName());
            listFileViewHolder.msg_message.setText(file.getAbsolutePath());

        }
    }

    private class ListFileViewHolder extends ViewHolder{
        private TextView msg_title;
        private TextView msg_message;

        public ListFileViewHolder(View mView) {
            super(mView);
            msg_title = mView.findViewById(R.id.msg_title);
            msg_message = mView.findViewById(R.id.msg_message);
        }
    }


    private OnItemFileClickListener mOnItemFileClickListener;

    public void setOnItemFileClickListener(OnItemFileClickListener mOnItemFileClickListener){

        this.mOnItemFileClickListener = mOnItemFileClickListener;

    }

    private OnDefClickListener mOnDefClickListener;

    public void setOnOnDefClickListener(OnDefClickListener mOnDefClickListener){

        this.mOnDefClickListener = mOnDefClickListener;

    }

    public interface OnItemFileClickListener{


        void onItemClick(File file);

    }

    public interface OnDefClickListener{


        void onDefClickListener(String file);

    }
}
