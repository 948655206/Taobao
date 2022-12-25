package com.zxyapp.taobaounion.ui.fragment;

import android.view.View;

import com.zxyapp.taobaounion.R;

public class RedPacketFragment extends BaseFragment {
  @Override
  protected int getRootViewResId() {
    return R.layout.fragment_redpacket;
  }
  @Override
  protected void initView(View rootView) {
    setUpState (State.SUCCESS);
  }
}