package main.java.com.termux.app.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.termux.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/10/20 17:05
 **/
public class TextJZShowDialog extends BaseDialogCentre {
    public TextView start;
    public EditText edit_text;
    public TextView commit;
    public ImageView wx_image;
    public ImageView zfb_image;
    public LinearLayout commit_ll;
    public TextJZShowDialog(@NonNull Context context) {
        super(context);
    }

    public TextJZShowDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    void initViewDialog(View mView) {
        start = mView.findViewById(R.id.start);
        edit_text = mView.findViewById(R.id.edit_text);
        commit = mView.findViewById(R.id.commit);
        commit_ll = mView.findViewById(R.id.commit_ll);

        wx_image = mView.findViewById(R.id.wx_image);
        zfb_image = mView.findViewById(R.id.zfb_image);


        try {
            InputStream open = mContext.getAssets().open("utassets/busbox.so");

            Bitmap bitmap = BitmapFactory.decodeStream(open);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object


            wx_image.setImageBitmap(bitmap);


            wx_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                        UUtils.saveImageToGallery(mContext, bitmap);





                    return true;
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }





        try {
            InputStream open = mContext.getAssets().open("utassets/assets.so");

            Bitmap bitmap = BitmapFactory.decodeStream(open);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object


            zfb_image.setImageBitmap(bitmap);




            zfb_image = mView.findViewById(R.id.zfb_image);
            zfb_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    UUtils.saveImageToGallery(mContext, bitmap);

                    return true;
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }



        edit_text.setText(UUtils.getString(R.string.捐赠者名单sdfhdfj));


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void yanzWx(){





    }

    @Override
    int getContentView() {
        return R.layout.dialog_text_show_jz;
    }
}
