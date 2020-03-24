package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import main.java.com.termux.adapter.ListBaseAdapter;
import main.java.com.termux.adapter.ViewHolder;
import main.java.com.termux.android_cm.managers.flashlight.Constants;
import main.java.com.termux.app.TermuxService;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.filemanage.filemanager.util.UIUtils;

public class FontActivity extends AppCompatActivity {

    private ListView list_view;

    private File fontFile;

    private ArrayList<FondDatdBean> arrayList;

    private File termuxFontFile = new File("/data/data/com.termux/files/home/.termux/font.ttf");
    private FontAdapter fontAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);
        list_view = findViewById(R.id.list_view);

        File file1 = new File("/data/data/com.termux/files/home/.termux/");

        if(!file1.exists()){
            file1.mkdirs();
        }

        fontFile = new File(Environment.getExternalStorageDirectory(),"/xinhao/font/");
        arrayList = new ArrayList<>();
        if(!(fontFile.exists())){
            boolean mkdirs = fontFile.mkdirs();
            if (!mkdirs){
                Toast.makeText(TermuxApplication.mContext, "你没有SD卡权限!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        if(!(new File(Environment.getExternalStorageDirectory(),"/xinhao/font/termux_def.ttf").exists())){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    writerFile("font_termux.ttf",new File(Environment.getExternalStorageDirectory(),"/xinhao/font/termux_def.ttf"));
                }
            }).start();

        }


        File[] files = fontFile.listFiles();

        if(files == null || files.length == 0){
            Toast.makeText(TermuxApplication.mContext, "没有在|内部存储/xinhao/font/下找到任何字体!", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < files.length; i++) {


            FondDatdBean fondDatdBean = new FondDatdBean();

            fondDatdBean.mFile = files[i];

            arrayList.add(fondDatdBean);

        }
        fontAdapter = new FontAdapter(arrayList);

        list_view.setAdapter(fontAdapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                writerFileFont(arrayList.get(position).mFile);


            }
        });
    }









    class FontAdapter extends ListBaseAdapter<FondDatdBean> {


        public FontAdapter(List<FondDatdBean> list) {
            super(list);
        }

        @Override
        public ViewHolder getViewHolder() {
            return new FontViewHolder(View.inflate(TermuxApplication.mContext,R.layout.item_font,null));
        }

        @Override
        public void initView(int position, FondDatdBean fondDatdBean, ViewHolder viewHolder) {

            FontViewHolder fontViewHolder = (FontViewHolder) viewHolder;

            fontViewHolder.font_name.setText(fondDatdBean.mFile.getName());
            fontViewHolder.font_lujin.setText(fondDatdBean.mFile.getAbsolutePath());

            Typeface fromAsset = Typeface.createFromFile(fondDatdBean.mFile);

            if(fondDatdBean.mFile.getName().equals("termux_def.ttf")){

                fontViewHolder.font_st.setText("Utermux|默认字体");
                fontViewHolder.font_st.setTypeface(fromAsset);
            }else{

                fontViewHolder.font_st.setText("Utermux|中文字体");
                fontViewHolder.font_st.setTypeface(fromAsset);
            }


        }
    }


    class FontViewHolder extends ViewHolder{

        public TextView font_name;
        public TextView font_st;
        public TextView font_lujin;

        public FontViewHolder(View mView) {
            super(mView);
            font_name = (TextView) findViewById(R.id.font_name);
            font_st = (TextView) findViewById(R.id.font_st);
            font_lujin = (TextView) findViewById(R.id.font_lujin);
        }
    }






    class FondDatdBean{


        public File mFile;


    }


    //写出文件
    private void writerFile(String name, File mFile) {

        try {
            InputStream open = getAssets().open(name);

            int len = 0;

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read()) != -1) {
                fileOutputStream.write(len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }


    //写出文件
    private void writerFileFont(File mFileSd) {

        try {

            if(termuxFontFile.exists()){
                termuxFontFile.delete();
            }

            boolean newFile = termuxFontFile.createNewFile();

            if(!newFile){
                Toast.makeText(TermuxApplication.mContext, "创建文件错误!", Toast.LENGTH_SHORT).show();
            }


            FileInputStream fileInputStream = new FileInputStream(mFileSd);

            FileOutputStream fileOutputStream = new FileOutputStream(termuxFontFile);

            int len = 0;

            byte [] b = new byte[1024];


            while ((len = fileInputStream.read(b)) != -1) {
                fileOutputStream.write(b,0,len);
            }

            fileOutputStream.flush();
            fileInputStream.close();
            fileOutputStream.close();



            Toast.makeText(FontActivity.this, "字体设置成功!", Toast.LENGTH_SHORT).show();

        /*    Intent intent = new Intent("com.termux.app.reload_style");
            intent.putExtra("com.termux.app.reload_style","font");

            sendBroadcast(intent);*/

            //finish();
            AlertDialog.Builder ab = new AlertDialog.Builder(FontActivity.this);
            ab.setTitle("字体设置成功!");
            ab.setMessage("如果进入终端没有任何显示，可能是您选择的字体可能已损坏!，请重新选择一个字体文件!");
            ab.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(FontActivity.this, TermuxService.class);
                    intent.setAction(TermuxService.ACTION_STOP_SERVICE);
                    FontActivity.this.startService(intent);
                    ab.create().dismiss();
                    FontActivity.this.finish();


                }
            });
            ab.show();
        } catch (Exception e) {

            Toast.makeText(this, e.getMessage() +":font", Toast.LENGTH_SHORT).show();
        }

    }
}
