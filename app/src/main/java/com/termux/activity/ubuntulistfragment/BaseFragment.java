package main.java.com.termux.activity.ubuntulistfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {


    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFragmentView = getFragmentView();

        initFragmentView(mFragmentView);


        return mFragmentView;
    }


    public abstract View getFragmentView();

    public abstract void initFragmentView(View mView);

    public View findViewById(int id) {

        return mFragmentView.findViewById(id);

    }
}
