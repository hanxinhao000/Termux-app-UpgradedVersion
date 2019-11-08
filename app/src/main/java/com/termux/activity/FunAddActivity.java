package main.java.com.termux.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.termux.R;

import java.util.ArrayList;

import main.java.com.termux.adapter.HomeCollectionViewPagerAdapter;
import main.java.com.termux.pagerview.BaseViewPagerView;
import main.java.com.termux.pagerview.GraphicsView;
import main.java.com.termux.pagerview.HistoryView;
import main.java.com.termux.pagerview.ScriptView;
import main.java.com.termux.pagerview.SoftwareView;
import main.java.com.termux.pagerview.TestFragment;

public class FunAddActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    private LinearLayout mHomeNavigation;

    private LinearLayout mCustomerServiceNavigation;

    private LinearLayout mOrderNavigation;

    private LinearLayout mPersonalCenterNavigation;


    private TextView mTextHome;

    private TextView mTextCustomerService;

    private TextView mTextOrder;

    private TextView mTextPersonalCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_add);

        mViewPager = findViewById(R.id.view_pager);


        mHomeNavigation = findViewById(R.id.home_navigation);

        mCustomerServiceNavigation = findViewById(R.id.customer_service_navigation);

        mOrderNavigation = findViewById(R.id.order_navigation);

        mPersonalCenterNavigation = findViewById(R.id.personal_center_navigation);


        mHomeNavigation.setOnClickListener(this);

        mCustomerServiceNavigation.setOnClickListener(this);

        mOrderNavigation.setOnClickListener(this);

        mPersonalCenterNavigation.setOnClickListener(this);


        mTextHome = findViewById(R.id.text_home);

        mTextCustomerService = findViewById(R.id.text_customer_service);

        mTextOrder = findViewById(R.id.text_order);

        mTextPersonalCenter = findViewById(R.id.text_personal_center);


        ArrayList<BaseViewPagerView> mFragments = new ArrayList<>();

        mFragments.add(new SoftwareView());
        mFragments.add(new GraphicsView());
        mFragments.add(new ScriptView());
        mFragments.add(new HistoryView());

        mViewPager.setAdapter(new HomeCollectionViewPagerAdapter(getSupportFragmentManager(), mFragments));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                setNavigationIndex(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setNavigationIndex(0);
    }

    @Override
    public void onClick(View v) {

        mTextHome.setTextColor(Color.parseColor("#000000"));
        mTextCustomerService.setTextColor(Color.parseColor("#000000"));
        mTextOrder.setTextColor(Color.parseColor("#000000"));
        mTextPersonalCenter.setTextColor(Color.parseColor("#000000"));


        switch (v.getId()) {

            case R.id.home_navigation:
                mTextHome.setTextColor(Color.parseColor("#1BBC9B"));
                mViewPager.setCurrentItem(0);
                break;
            case R.id.customer_service_navigation:
                mTextCustomerService.setTextColor(Color.parseColor("#1BBC9B"));
                mViewPager.setCurrentItem(1);
                break;
            case R.id.order_navigation:
                mTextOrder.setTextColor(Color.parseColor("#1BBC9B"));
                mViewPager.setCurrentItem(2);
                break;
            case R.id.personal_center_navigation:
                mTextPersonalCenter.setTextColor(Color.parseColor("#1BBC9B"));
                mViewPager.setCurrentItem(3);
                break;

        }

    }


    public void setNavigationIndex(int index) {

        mTextHome.setTextColor(Color.parseColor("#000000"));
        mTextCustomerService.setTextColor(Color.parseColor("#000000"));
        mTextOrder.setTextColor(Color.parseColor("#000000"));
        mTextPersonalCenter.setTextColor(Color.parseColor("#000000"));


        switch (index) {

            case 0:
                mTextHome.setTextColor(Color.parseColor("#1BBC9B"));
                break;
            case 1:
                mTextCustomerService.setTextColor(Color.parseColor("#1BBC9B"));
                break;
            case 2:
                mTextOrder.setTextColor(Color.parseColor("#1BBC9B"));
                break;
            case 3:
                mTextPersonalCenter.setTextColor(Color.parseColor("#1BBC9B"));
                break;

        }

    }


}
