package com.vip001.monitor.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by xxd on 2018/7/9
 */
public class CallbackManager<T> {
    /**
     * 集合
     *
     * @param callback
     */
    private ArrayList<WeakReference<T>> mCallbacks = new ArrayList<WeakReference<T>>();

    /**
     * 包含判断
     *
     * @param callback
     */
    public boolean containsCallback(T callback) {
        boolean result = false;
        Iterator<WeakReference<T>> iter = mCallbacks.iterator();
        while (iter.hasNext()) {
            WeakReference<T> reference = iter.next();
            if (reference.get() == null) {
                iter.remove();
            } else {
                if (reference.get() == callback) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public interface TraversalCallback<T> {
        void execute(T callback);
    }

    /**
     * 添加
     *
     * @param callback
     */
    public void addCallback(T callback) {
        if (!containsCallback(callback)) {
            mCallbacks.add(new WeakReference<T>(callback));
        }
    }

    /**
     * 删除
     *
     * @param callback
     */
    public T removeCallback(T callback) {
        T temp = null;
        int targetIndex = -1;
        for (int i = 0, len = mCallbacks.size(); i < len; i++) {
            temp = mCallbacks.get(i).get();
            if (temp != null && temp == callback) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex != -1) {
            return mCallbacks.remove(targetIndex).get();
        } else {
            return null;
        }
    }

    /**
     * 遍历
     *
     * @param callback
     */
    public void traversal(TraversalCallback<T> callback) {
        T temp = null;
        for (int i = 0, len = mCallbacks.size(); i < len; i++) {
            temp = mCallbacks.get(i).get();
            if (temp != null && callback != null) {
                callback.execute(temp);
            }
        }
    }
}
