package main.java.com.termux.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import main.java.com.termux.pagerview.BaseViewPagerView;

public class HomeCollectionViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseViewPagerView> mList;

    public HomeCollectionViewPagerAdapter(FragmentManager fm, ArrayList<BaseViewPagerView> mList) {
        super(fm);
        this.mList = mList;

    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
