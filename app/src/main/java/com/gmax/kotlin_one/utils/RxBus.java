package com.gmax.kotlin_one.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
public class RxBus {
    private static RxBus rxBus;
    /**
     * 存储某个标签的 subject集合
     */
    private static ConcurrentMap<Object,List<Subject>> mSubjectMapper = new ConcurrentHashMap<>();

    private RxBus(){}

    public static RxBus getInstance(){
        if (rxBus == null) {
            synchronized (RxBus.class){
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }


    /**
     * 注册事件
     * @param tag 标记
     * @param clazz 类
     * @param <T> 泛型
     * @return 事件
     */

    public  <T> Observable<T> register(@NonNull Object tag,@NonNull Class<T> clazz){

        List<Subject> subjectList = mSubjectMapper.get(tag);

        if (subjectList == null) {
            subjectList = new ArrayList<>();
            mSubjectMapper.put(tag,subjectList);
        }

        Subject<T,T> subject;

        subjectList.add(subject = PublishSubject.create());

        return subject;
    }

    /**
     * 取消注册事件
     * @param tag 标记
     * @param observable  注册事件
     */
    public  void unregister(@NonNull Object tag,@NonNull Observable observable){
        final List<Subject> subjectList = mSubjectMapper.get(tag);

        if (subjectList != null) {
            subjectList.remove(observable);
            if (subjectList.isEmpty()) {
                //集合数据为 0的时候移除map的tag；
                mSubjectMapper.remove(tag);
            }
        }
    }

    /**
     * 发送事件
     * @param tag 标记
     * @param content 发送事件
     */
    public void post(@NonNull Object tag,@NonNull Object content){
        final List<Subject> subjectList = mSubjectMapper.get(tag);

        if (subjectList != null && !subjectList.isEmpty()) {
            for (Subject subject:subjectList) {
                subject.onNext(content);

                //subject.onError(new Exception());
            }
        }
    }
}
