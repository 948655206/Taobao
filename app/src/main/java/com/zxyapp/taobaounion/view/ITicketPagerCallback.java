package com.zxyapp.taobaounion.view;

import com.zxyapp.taobaounion.base.IBaseCallback;
import com.zxyapp.taobaounion.model.domain.TicketResult;

public interface ITicketPagerCallback extends IBaseCallback {
    /**
     * 淘口令加载结果
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);
}
