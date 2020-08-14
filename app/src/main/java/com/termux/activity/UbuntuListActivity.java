package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.termux.R;

import main.java.com.termux.activity.ubuntulistfragment.DebianLinuxFragment;
import main.java.com.termux.activity.ubuntulistfragment.UbuntuLinuxFragment;
import main.java.com.termux.utils.UUtils;

public class UbuntuListActivity extends AppCompatActivity implements View.OnClickListener {

    private DebianLinuxFragment mDebianLinuxFragment;

    private LinearLayout mDebianLinux;
    private LinearLayout mDebianLinux_1;
    private UbuntuLinuxFragment mUbuntuLinuxFragment;

    public static boolean mIsRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubuntu_list);

        mDebianLinuxFragment = new DebianLinuxFragment();

        mUbuntuLinuxFragment = new UbuntuLinuxFragment();

        mDebianLinux = findViewById(R.id.debian_linux);

        mDebianLinux.setOnClickListener(this);

        mDebianLinux_1 = findViewById(R.id.debian_linux_1);
        mDebianLinux_1.setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_1, mUbuntuLinuxFragment);

        fragmentTransaction.add(R.id.fragment_1, mDebianLinuxFragment);

        fragmentTransaction.show(mDebianLinuxFragment).commit();


    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        if (mIsRun) {
            Toast.makeText(this, UUtils.getString(R.string.有任务正在进行mmkk), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // Toast.makeText(this, "返回键被阻拦了，您可以按home键退出再进来", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }


    }

    @Override
    public void onClick(View v) {

        if (mIsRun)
            return;

        switch (v.getId()) {

            case R.id.debian_linux:

                try {
                    Toast.makeText(this, "debian1", Toast.LENGTH_SHORT).show();
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_1, mDebianLinuxFragment).show(mDebianLinuxFragment).commit();
                } catch (Exception e) {

                    e.printStackTrace();
                }
                break;

            case R.id.debian_linux_1:

                try {
                    Toast.makeText(this, "debian2", Toast.LENGTH_SHORT).show();
                    FragmentManager supportFragmentManager = getSupportFragmentManager();
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_1, mUbuntuLinuxFragment).show(mUbuntuLinuxFragment).commit();
                } catch (Exception e) {

                    e.printStackTrace();
                }
                break;

        }
    }
}
