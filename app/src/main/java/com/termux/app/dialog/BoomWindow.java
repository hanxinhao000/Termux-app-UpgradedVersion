package main.java.com.termux.app.dialog;

import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.termux.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import main.java.com.termux.adapter.BoomMinLAdapter;
import main.java.com.termux.adapter.MinLAdapter;
import main.java.com.termux.adapter.databean.MinLBean;
import main.java.com.termux.utils.CustomTextView;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/12/23 15:19
 **/
public class BoomWindow  {


    CustomTextView title;
    RecyclerView recyclerView;
    public View getView(BoomMinLAdapter.CloseLiftListener closeLiftListener){


        View mView = UUtils.getViewLay(R.layout.dialog_boom);

        title = mView.findViewById(R.id.title);
        recyclerView = mView.findViewById(R.id.recyclerView);


        String commi22 = SaveData.getData("commi22");
        if (commi22 == null || commi22.isEmpty() || commi22.equals("def")) {

            title.setVisibility(View.VISIBLE);

            title.setText(UUtils.getString(R.string.没有找到命令dfd));


        }else{

            try {

                MinLBean minLBean = new Gson().fromJson(commi22, MinLBean.class);
                if(minLBean.data.list.size() == 0){

                    title.setVisibility(View.VISIBLE);
                    title.setText(UUtils.getString(R.string.没有找到命令dfd));

                }else {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UUtils.getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    BoomMinLAdapter boomMinLAdapter = new BoomMinLAdapter(minLBean.data.list, null);
                    boomMinLAdapter.setCloseLiftListener(closeLiftListener);
                    recyclerView.setAdapter(boomMinLAdapter);
                    title.setVisibility(View.GONE);
                }
            }catch (Exception e){
                title.setVisibility(View.VISIBLE);
                title.setText(UUtils.getString(R.string.命令出错sdsd));
                e.printStackTrace();
            }

        }






        return mView;

    }


}
