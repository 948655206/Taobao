package com.zxyapp.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zxyapp.taobaounion.base.BaseApplication;
import com.zxyapp.taobaounion.model.domain.IBaseInfo;
import com.zxyapp.taobaounion.presenter.ITickPresenter;
import com.zxyapp.taobaounion.ui.activity.TicketActivity;

public class TicketUtil {
    public static void toTicketPage(Context context,IBaseInfo baseInfo){
        //特惠内容被点击
        //处理数据
        String title=baseInfo.getTitle();
        //详细的地址
        String url=baseInfo.getUrl();
        if (TextUtils.isEmpty(url)){
            url=baseInfo.getUrl();
        }
        String cover=baseInfo.getCover();
        //拿到ticketPresenter去记载数据
        ITickPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
