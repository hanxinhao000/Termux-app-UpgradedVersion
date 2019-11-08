package main.java.com.termux.pagerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseViewPagerView extends Fragment {

    private View mView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = getLayoutView();

        initView(mView);

        return mView;

    }




    public abstract View getLayoutView();

    public abstract void initView(View mView);

}
