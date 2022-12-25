package com.zxyapp.taobaounion.model;

import com.zxyapp.taobaounion.model.domain.Categories;
import com.zxyapp.taobaounion.model.domain.HomePagerContent;
import com.zxyapp.taobaounion.model.domain.OnSellContent;
import com.zxyapp.taobaounion.model.domain.SearchRecommend;
import com.zxyapp.taobaounion.model.domain.SearchResult;
import com.zxyapp.taobaounion.model.domain.SelectPageCategory;
import com.zxyapp.taobaounion.model.domain.SelectedContent;
import com.zxyapp.taobaounion.model.domain.TicketParams;
import com.zxyapp.taobaounion.model.domain.TicketResult;
import com.zxyapp.taobaounion.presenter.impl.TicketPresenterImpl;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectPageCategory> getSelectPageCategory();

    @GET
    Call<SelectedContent> getSelectedPageContent(@Url String url);

    @GET
    Call<OnSellContent> getOnSellPageContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page,@Query("keyword")String keyword);
}