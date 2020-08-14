package main.java.com.termux.pagerview;

import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.termux.R;

import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.utils.UUtils;


public class GraphicsView extends BaseViewPagerView {

    private LinearLayout ycm;
    private LinearLayout ycm_manjaro;

    @Override
    public View getLayoutView() {
        return View.inflate(getActivity(), R.layout.view_graphics, null);
    }

    @Override
    public void initView(View mView) {

        ycm = mView.findViewById(R.id.ycm);

        ycm_manjaro = mView.findViewById(R.id.ycm_manjaro);

        ycm_manjaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermuxActivity.mTerminalView.sendTextToTerminal("pkg install wget proot tar -y && wget https://andronixos.sfo2.cdn.digitaloceanspaces.com/Manjaro/manjaro.tar.xz && wget https://andronixos.sfo2.digitaloceanspaces.com/Manjaro/manjaro.sh && chmod 777 manjaro.sh &&  chmod 777 manjaro.tar.xz && ./manjaro.sh \n");
                getActivity().finish();
            }
        });

        ycm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), UUtils.getString(R.string.功能测试中请等待), Toast.LENGTH_SHORT).show();

               // startActivity(new Intent(getContext(), LinuxdeployActivity.class));



            }
        });

    }
}
