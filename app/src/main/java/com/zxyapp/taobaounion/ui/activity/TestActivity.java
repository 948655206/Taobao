package com.zxyapp.taobaounion.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.zxyapp.taobaounion.R;
import com.zxyapp.taobaounion.ui.custom.TextFlowLayout;
import com.zxyapp.taobaounion.utils.LogUtils;
import com.zxyapp.taobaounion.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends Activity {

    @BindView (R.id.test_navigation_bar)
    public RadioGroup navigationBar;

    @BindView(R.id.test_flow_test)
    public TextFlowLayout flowText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_test);
        ButterKnife.bind (this);
        initListener();

        List<String>  testList=new ArrayList<>();
        testList.add("电脑");
        testList.add("鼠标");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("运动鞋");
        testList.add("肥宅快乐水");

        flowText.setTextList(testList);

        flowText.setOnFlowtTextItemClickListener(new TextFlowLayout.OnFlowtTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                LogUtils.d(TestActivity.this,"click text------>"+text);
            }
        });
    }

    public void showToast(View view){
        ToastUtil.showToast ("测试.....");
    }


    private void initListener() {
        navigationBar.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.home:
                        LogUtils.d (TestActivity.class,"首页------->");
                        break;
                    case R.id.selected:
                        LogUtils.d (TestActivity.class,"精选------->");
                        break;
                    case R.id.search:
                        LogUtils.d (TestActivity.class,"搜索------->");

                        break;
                    case R.id.red_packet:
                        LogUtils.d (TestActivity.class,"红包------->");
                        break;
                }
            }
        });
    }
}