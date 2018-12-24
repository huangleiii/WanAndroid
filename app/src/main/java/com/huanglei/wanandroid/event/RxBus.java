package com.huanglei.wanandroid.event;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by HuangLei on 2018/11/27.
 */

public class RxBus {
    private Subject<Object> mBus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        return Holder.RX_BUS;
    }

    private static class Holder {
        private static final RxBus RX_BUS = new RxBus();
    }

    public <T> Observable<T> toObservable(Class<T> clazz) {
        return mBus.ofType(clazz);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
    public void post(Object o) {
        mBus.onNext(o);
    }


}
