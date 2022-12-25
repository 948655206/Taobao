package com.zxyapp.taobaounion.ui.activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.ui.fragment.BaseFragment;
import com.zxyapp.taobaounion.ui.fragment.HomeFragment;
import com.zxyapp.taobaounion.ui.fragment.OnSellFragment;
import com.zxyapp.taobaounion.ui.fragment.SearchFragment;
import com.zxyapp.taobaounion.ui.fragment.SelectedFragment;
import com.zxyapp.taobaounion.utils.LogUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity{

    public static final String TAG="MainActivity";

    @BindView (R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;

    private OnSellFragment mRedPacketFragment;
    private HomeFragment mHomeFragment;
    private SearchFragment mSearchFragment;
    private SelectedFragment mSelectedFragment;
    private FragmentManager mFm;

    @Override
    protected void initView() {
        initListener ();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initEvent() {
        initFragments();
    }

    private void initFragments() {
        mHomeFragment = new HomeFragment ();
        mRedPacketFragment = new OnSellFragment();
        mSearchFragment = new SearchFragment ();
        mSelectedFragment = new SelectedFragment ();
        mFm = getSupportFragmentManager ();

        switchFragment (mHomeFragment);
    }

    /**
     * 跳转到搜索界面
     */
    public void switch2Search(){
        //切换导航栏中的选中项
        mNavigationView.setSelectedItemId(R.id.search);
    }


    private void initListener() {
        if (mNavigationView!=null){
            mNavigationView.setOnNavigationItemSelectedListener (new BottomNavigationView.OnNavigationItemSelectedListener () {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId ()==R.id.home){
                        LogUtils.d (MainActivity.class,"切换到首页");
                        switchFragment(mHomeFragment);
                    }else if (item.getItemId ()==R.id.selected){
                        LogUtils.i (MainActivity.class,"切换到精选");
                        switchFragment(mSelectedFragment);
                    }else if (item.getItemId ()==R.id.red_packet){
                        LogUtils.w (MainActivity.class,"切换到特惠");
                        switchFragment (mRedPacketFragment);
                    }else if (item.getItemId ()==R.id.search){
                        LogUtils.e (MainActivity.class,"切换到搜索");
                        switchFragment (mSearchFragment);
                    }
                    //消费事件
                    return true;
                }
            });
        }

    }
    //上一次显示的fragment
    private BaseFragment lastOneFragment=null;

    private void switchFragment(BaseFragment targetFragment){
        //如果上一个fragment跟当前切换的fragment是同一个则不用切换
        if (lastOneFragment==targetFragment){
            return;
        }
        //修改成add和hide的方式，用Replace会让fragment生命周期destroy，导致程序慢
        //输入事件
        FragmentTransaction fragmentTransaction= mFm.beginTransaction ();
        if (!targetFragment.isAdded()){
            fragmentTransaction.add(R.id.main_page_container,targetFragment);
        }else {
            fragmentTransaction.show(targetFragment);
        }
        if (lastOneFragment!=null){
            fragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment=targetFragment;
//        fragmentTransaction.replace (R.id.main_page_container, targetFragment);
        //提交事件
        fragmentTransaction.commit ();
    }


}