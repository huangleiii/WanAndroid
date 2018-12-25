package com.huanglei.wanandroid.model.http;

import android.util.Log;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.huanglei.wanandroid.app.Constants;
import com.huanglei.wanandroid.app.WanAndroidApplication;
import com.huanglei.wanandroid.model.bean.Account;
import com.huanglei.wanandroid.model.bean.Banner;
import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.bean.HomeArticleList;
import com.huanglei.wanandroid.model.bean.HotWebsite;
import com.huanglei.wanandroid.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HuangLei on 2018/11/15.
 */

public class HttpHelper {
    private Retrofit mRetrofit;
    private WanAndroidApis mWanAndroidApis;

    private HttpHelper() {
        File cacheFile = new File(Constants.PATH_CACHE, "NetCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(WanAndroidApplication.getInstance()));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /*
        响应头和请求头中均可存在：
        only-if-cached + max-stale=Integer.MAX_VALUE = 仅缓存  常用于修改请求头和响应头
        no-cache = 不读取缓存                                  常用于修改请求头
        max-age = 可读取缓存，等于0时表示缓存有效时间为0         常用于修改响应头
        仅请求头中存在：
        public
        private
         */
        Interceptor onlineInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (CommonUtils.isNetworkConnected()) {
                    //注意这里要将build的结果赋值回response
                    response = response.newBuilder()
                            .removeHeader("Pragma")
                            //这里不能时no-cache,不然即使下面修改请求头为force_cache也无法读取缓存
                            .header("Cache-Control", "public, max-age=0")
                            .build();
                }
                return response;
            }
        };
        Interceptor offlineInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!CommonUtils.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                return response;
            }
        };
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("httpMessage", message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                /*
                okhttp请求时拦截器执行顺序 自定义应用拦截器>内部缓存拦截器>自定义网络拦截器，响应时原路返回。
                      其中内部缓存拦截器用来存储缓存，并根据头部属性决定是否读取缓存，所以当其将响应数据返回给自定义应用拦截器后
                      再对头部进行修改是没有作用的，因为缓存的是修改前的头部信息，修改后的无法缓存。
                      在请求过程中，如果内部缓存拦截器判断读取缓存，就不会执行后续的自定义网络拦截器，直接读取缓存数据并返回响应。

                      所以在线时，修改自定义网络拦截器中的响应头，在服务器没有定义缓存策略的情况下执行我们定义的缓存策略。
                      离线时，根本不会执行自定义网络拦截器，所以修改自定义应用拦截器，通过修改请求头的方式定义缓存策略。

                      主要还是通过修改请求头的方式来定义缓存策略，修改响应头仅用于服务器没有定义缓存策略而我们又需要的情况下。
                 */
                .addNetworkInterceptor(onlineInterceptor)
                .addInterceptor(offlineInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .cookieJar(persistentCookieJar);
        OkHttpClient okHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WanAndroidApis.HOST)
                .build();
        mWanAndroidApis = mRetrofit.create(WanAndroidApis.class);
    }

    private static class HttpHelperHolder {
        public static final HttpHelper INSTANCE = new HttpHelper();
    }

    public static HttpHelper getInstance() {
        return HttpHelperHolder.INSTANCE;
    }

    public WanAndroidApis getWanAndroidApis() {
        return mWanAndroidApis;
    }

    public Observable<BaseResponse<HomeArticleList>> getHomeArticleListData(int num) {
        return mWanAndroidApis.getHomeArticleListData(num);
    }

    public Observable<BaseResponse<List<Banner>>> getBannerListData() {
        return mWanAndroidApis.getBannerListData();
    }

    public Observable<BaseResponse<Account>> logIn(String username, String password) {
        return mWanAndroidApis.logIn(username, password);
    }

    public Observable<BaseResponse<Account>> register(String username, String password, String repassword) {
        return mWanAndroidApis.register(username, password, repassword);
    }

    public Observable<BaseResponse<Object>> logout() {
        return mWanAndroidApis.logout();
    }

    public Observable<BaseResponse<Object>> collect(int id) {
        return mWanAndroidApis.collect(id);
    }

    public Observable<BaseResponse<Object>> cancelCollect(int id) {
        return mWanAndroidApis.cancelCollect(id);
    }
    public Observable<BaseResponse<List<HotWebsite>>> getHotWebsiteList(){
        return mWanAndroidApis.getHotWebsiteList();
    }

}
