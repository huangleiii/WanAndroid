package com.huanglei.wanandroid.model.http;


import com.huanglei.wanandroid.model.bean.Account;
import com.huanglei.wanandroid.model.bean.Banner;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HomeArticleList;
import com.huanglei.wanandroid.model.bean.HotKey;
import com.huanglei.wanandroid.model.bean.HotWebsite;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by HuangLei on 2018/11/14.
 */

public interface WanAndroidApis {
    String HOST = "http://www.wanandroid.com/";

    @GET("article/list/{num}/json")
    Observable<BaseResponse<HomeArticleList>> getHomeArticleListData(@Path("num") int num);

    @GET("banner/json")
    Observable<BaseResponse<List<Banner>>> getBannerListData();

    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseResponse<Account>> logIn(@Field("username") String username, @Field("password") String password);

    @POST("user/register")
    @FormUrlEncoded
    Observable<BaseResponse<Account>> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    @GET("user/logout/json")
    Observable<BaseResponse<Object>> logout();

    @POST("lg/collect/{id}/json")
    Observable<BaseResponse<Object>> collect(@Path("id") int id);

    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResponse<Object>> cancelCollect(@Path("id") int id);

    @GET("friend/json")
    Observable<BaseResponse<List<HotWebsite>>> getHotWebsiteList();

    @POST("article/query/{num}/json")
    Observable<BaseResponse<HomeArticleList>> getSearchArticleList(@Field("k") String key,@Path("num") int num);

    @GET("hotkey/json")
    Observable<BaseResponse<List<HotKey>>> getSearchHotKeys();

}
