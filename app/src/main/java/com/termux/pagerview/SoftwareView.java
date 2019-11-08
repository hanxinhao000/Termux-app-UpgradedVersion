package main.java.com.termux.pagerview;

import android.view.View;

import com.termux.R;

public class SoftwareView extends BaseViewPagerView {


    @Override
    public View getLayoutView() {
        return View.inflate(getActivity(),R.layout.view_soft_ware, null);
    }

    @Override
    public void initView(View mView) {




    }
}
