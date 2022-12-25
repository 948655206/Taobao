package com.zxyapp.taobaounion.utils;

import com.zxyapp.taobaounion.model.Api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

  private static final RetrofitManager singleton = new RetrofitManager ();

  private final Retrofit mRetrofit;

  public static RetrofitManager getInstance() {
    return singleton;
  }

  private RetrofitManager() {
    //创建retrofit
    mRetrofit = new Retrofit.Builder ()
            .baseUrl (Constants.BASE_URL)
            .addConverterFactory (GsonConverterFactory.create ())
            .build ();
  }

  public Retrofit getRetrofit(){
    return mRetrofit;
  }



}