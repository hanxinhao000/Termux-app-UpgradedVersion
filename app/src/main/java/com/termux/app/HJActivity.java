package main.java.com.termux.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import main.java.com.termux.app.dialog.MinglingDialog;
import main.java.com.termux.app.dialog.PATHDialog;
import main.java.com.termux.bean.PATHBean;
import main.java.com.termux.utils.CustomTextView;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.UUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.termux.R;

import java.util.ArrayList;
import java.util.List;

public class HJActivity extends AppCompatActivity {

    private CardView save;
    private EditText edit_text;
    private LinearLayout zengjia;
    private CustomTextView kong;
    private RecyclerView recyclerView;
    private PATHAdapter pathAdapter;
    private List<PATHBean.Data> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_j);
        save = findViewById(R.id.save);
        edit_text = findViewById(R.id.edit_text);
        zengjia = findViewById(R.id.zengjia);
        kong = findViewById(R.id.kong);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<>();
        getMessage();
        pathClick();
        getOtherMessage();
    }


    //取出其它消息
    private void getOtherMessage(){


        zengjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PATHDialog pathDialog = new PATHDialog(HJActivity.this);

                pathDialog.setRefListener(new PATHDialog.RefListener() {
                    @Override
                    public void ref() {

                        String path_other = SaveData.getData("path_other");
                        if(path_other ==null || path_other.isEmpty() || path_other.equals("def")){
                            //空
                            kong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            return;
                        }else{

                            try{

                                PATHBean pathBean = new Gson().fromJson(path_other, PATHBean.class);

                                if(pathBean.getData() != null && pathBean.getData().size() > 0){
                                    kong.setVisibility(View.INVISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }else{
                                    kong.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.INVISIBLE);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                                kong.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                            }


                        }

                        PATHBean pathBean = new Gson().fromJson(path_other, PATHBean.class);



                        if(pathAdapter == null){
                            data.clear();
                            data.addAll(pathBean.getData());
                            pathAdapter = new PATHAdapter(data,HJActivity.this);
                            recyclerView.setAdapter(pathAdapter);
                        }else{
                            data.clear();
                            data.addAll(pathBean.getData());
                            pathAdapter.notifyDataSetChanged();
                        }


                    }
                });

                pathDialog.show();

            }
        });


        try{

            String path_other = SaveData.getData("path_other");
            if(path_other ==null || path_other.isEmpty() || path_other.equals("def")){
                //空
                kong.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                return;
            }else{
                try{

                    PATHBean pathBean = new Gson().fromJson(path_other, PATHBean.class);

                    if(pathBean.getData() != null && pathBean.getData().size() > 0){
                        kong.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }else{
                        kong.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    kong.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }

            }



            //开始设置Adapter

            PATHBean pathBean = new Gson().fromJson(path_other, PATHBean.class);



            data.addAll(pathBean.getData());

            pathAdapter = new PATHAdapter(data,HJActivity.this);


            recyclerView.setAdapter(pathAdapter);




        }catch (Exception e){
            e.printStackTrace();

        }


    }

    //点击消息
    private void pathClick(){

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = edit_text.getText().toString();
                if(string == null || string.isEmpty()){

                    SaveData.saveData("path_linux","def");

                    UUtils.showMsg(UUtils.getString(R.string.PATH保存成功));
                    return;
                }

                SaveData.saveData("path_linux",string);
                UUtils.showMsg(UUtils.getString(R.string.PATH保存成功));

            }
        });



    }

    //取出消息
    private void getMessage(){


        String path_linux = SaveData.getData("path_linux");
        if(path_linux ==null || path_linux.isEmpty() || path_linux.equals("def")){
            //空

        }else{

            edit_text.setText(path_linux);

        }


    }

    //Adapter
    public  class PATHAdapter extends RecyclerView.Adapter<PATHViewHolder>{

        private List<PATHBean.Data> list;
        private Activity activity;

        public PATHAdapter(List<PATHBean.Data> list, Activity activity){
            this.list = list;
            this.activity = activity;

        }


        @NonNull
        @Override
        public PATHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new PATHViewHolder(UUtils.getViewLayViewGroup(R.layout.item_path,parent));
        }

        @Override
        public void onBindViewHolder(@NonNull PATHViewHolder holder, int position) {

            PATHBean.Data data = list.get(position);

            holder.key.setText(data.getPathKey());
            holder.value.setText(data.getPathValue());


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    MinglingDialog minglingDialog = new MinglingDialog(activity);
                    minglingDialog.show();

                    minglingDialog.xiugai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //修改
                            minglingDialog.dismiss();

                            PATHDialog pathDialog = new PATHDialog(activity);

                            pathDialog.name_edit.setText(data.getPathKey());
                            pathDialog.edit_text.setText(data.getPathValue());

                            pathDialog.start.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    list.get(position).setPathKey(pathDialog.name_edit.getText().toString());
                                    list.get(position).setPathValue(pathDialog.edit_text.getText().toString());

                                    PATHBean pathBean = new PATHBean();

                                    pathBean.setData(list);

                                    String s = new Gson().toJson(pathBean);
                                    SaveData.saveData("path_other",s);

                                    notifyDataSetChanged();
                                    UUtils.showMsg(UUtils.getString(R.string.修改成功));
                                    pathDialog.dismiss();

                                }
                            });

                            pathDialog.show();


                        }
                    });

                    minglingDialog.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //删除


                            if(list.size() == 0){
                                return;
                            }
                            list.remove(position);

                            if(list.size() == 0){
                                kong.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);

                            }
                            PATHBean pathBean = new PATHBean();

                            pathBean.setData(list);

                            String s = new Gson().toJson(pathBean);
                            SaveData.saveData("path_other",s);

                            notifyDataSetChanged();
                            minglingDialog.dismiss();
                            UUtils.showMsg(UUtils.getString(R.string.删除成功));

                        }
                    });



                    return true;
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    //VuewHolder

    public static class PATHViewHolder extends RecyclerView.ViewHolder{

        public CustomTextView key;
        public CustomTextView value;

        public PATHViewHolder(@NonNull View itemView) {
            super(itemView);
            key = itemView.findViewById(R.id.key);
            value = itemView.findViewById(R.id.value);
        }
    }
}
