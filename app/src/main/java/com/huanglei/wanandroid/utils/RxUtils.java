package com.huanglei.wanandroid.utils;

import com.huanglei.wanandroid.model.bean.BaseResponse;
import com.huanglei.wanandroid.model.http.ServerException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by HuangLei on 2018/11/28.
 */

public class RxUtils {
    public static <T> ObservableTransformer<T, T> schedulerTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<BaseResponse<T>, T> responseTransformer() {
        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(final BaseResponse<T> tBaseResponse) throws Exception {
                        if(tBaseResponse.getErrorCode() == BaseResponse.SUCCESS) {
                            return createSuccessObservable(tBaseResponse.getData());
                        } else {
                            return Observable.error(new ServerException(tBaseResponse.getErrorCode(), tBaseResponse.getErrorMsg()));
                        }
                    }
                });
            }
        };
    }
    public static ObservableTransformer<BaseResponse<Object>,Object > noDataResponseTransformer() {
        return new ObservableTransformer<BaseResponse<Object>, Object>() {
            @Override
            public ObservableSource<Object> apply(Observable<BaseResponse<Object>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<Object>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(final BaseResponse<Object> tBaseResponse) throws Exception {
                        if(tBaseResponse.getErrorCode() == BaseResponse.SUCCESS) {
                            //用于返回的BaseResponse中的Data为空的情况。
                            return createSuccessObservable(new Object());
                        } else {
                            return Observable.error(new ServerException(tBaseResponse.getErrorCode(), tBaseResponse.getErrorMsg()));
                        }
                    }
                });
            }
        };
    }

    private static <T> Observable<T> createSuccessObservable(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                emitter.onNext(t);
                emitter.onComplete();
            }
        });
    }
}
