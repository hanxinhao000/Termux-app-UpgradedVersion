package main.java.com.termux.app.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.termux.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import main.java.com.termux.bean.PATHBean;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.view.MyEditText;

/**
 * @author ZEL
 * @create By ZEL on 2021/3/12 17:11
 **/
public class PATHDialog extends BaseDialogCentre {

    public MyEditText name_edit;
    public MyEditText edit_text;
    private TextView cancel;
    public TextView start;
    public PATHDialog(@NonNull Context context) {
        super(context);
    }

    public PATHDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {

        name_edit = mView.findViewById(R.id.name_edit);
        edit_text = mView.findViewById(R.id.edit_text);
        cancel = mView.findViewById(R.id.cancel);
        start = mView.findViewById(R.id.start);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = name_edit.getText().toString();
                if(key == null || key.isEmpty()){
                    UUtils.showMsg(UUtils.getString(R.string.PATHKEY不能为空fdf));
                    return;

                }
                String value = edit_text.getText().toString();
                if(value == null || value.isEmpty()){
                    UUtils.showMsg(UUtils.getString(R.string.PATH值不能为空dfdf));
                    return;

                }
                String path_other = SaveData.getData("path_other");

                if(path_other == null || path_other.isEmpty() || path_other.equals("def")){
                    //为空
                    PATHBean pathBean = new PATHBean();

                    List<PATHBean.Data> data = new ArrayList<>();

                    PATHBean.Data p = new PATHBean.Data();
                    p.setPathKey(key);
                    p.setPathValue(value);

                    data.add(p);

                    pathBean.setData(data);

                    String s = new Gson().toJson(pathBean);

                    SaveData.saveData("path_other",s);

                }else{

                    PATHBean pathBean = new Gson().fromJson(path_other, PATHBean.class);
                    List<PATHBean.Data> data = pathBean.getData();
                    PATHBean.Data p = new PATHBean.Data();
                    p.setPathKey(key);
                    p.setPathValue(value);
                    data.add(p);

                    String s = new Gson().toJson(pathBean);
                    SaveData.saveData("path_other",s);

                }

                if(mRefListener != null){
                    mRefListener.ref();
                }

                dismiss();


            }
        });

    }

    private RefListener mRefListener;
    public void setRefListener(RefListener mRefListener){

        this.mRefListener = mRefListener;

    }

    @Override
    int getContentView() {
        return R.layout.dialog_path;
    }

    public static interface RefListener{

        void ref();

    }
}
