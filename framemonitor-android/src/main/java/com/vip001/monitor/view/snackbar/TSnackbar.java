package com.vip001.monitor.view.snackbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class TSnackbar {


    public static abstract class Callback {

        public static final int DISMISS_EVENT_SWIPE = 0;

        public static final int DISMISS_EVENT_ACTION = 1;

        public static final int DISMISS_EVENT_TIMEOUT = 2;

        public static final int DISMISS_EVENT_MANUAL = 3;

        public static final int DISMISS_EVENT_CONSECUTIVE = 4;


        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissEvent {
        }


        public void onDismissed(TSnackbar snackbar, @DismissEvent int event) {

        }


        public void onShown(TSnackbar snackbar) {

        }
    }

    public static final int TYPE_MANAGER_DEFAULT = 1;
    public static final int LENGTH_INDEFINITE = -2;


    public static final int LENGTH_SHORT = -1;


    public static final int LENGTH_LONG = 0;

    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;

    private static final Handler sHandler;
    private static final int MSG_SHOW = 0;
    private static final int MSG_DISMISS = 1;

    static {
        sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        ((TSnackbar) message.obj).showView();
                        return true;
                    case MSG_DISMISS:
                        ((TSnackbar) message.obj).hideView(message.arg1);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private final ViewGroup mParent;
    private final Context mContext;
    private final SnackbarLayout mView;
    private SnackbarBaseViewHolder mViewHolder;
    private View mContentView;
    private int mDuration;
    private Callback mCallback;
    private int mManagerType = TYPE_MANAGER_DEFAULT;

    private TSnackbar(ViewGroup parent) {
        mParent = parent;
        mContext = parent.getContext();
        mView = new SnackbarLayout(mContext);
    }


    /**
     * @param anchor
     * @param contentView
     * @param duration    LENGTH_INDEFINITE LENGTH_SHORT LENGTH_LONG or customedvalue
     * @return
     */

    public static TSnackbar make(View anchor, View contentView,
                                 int duration) {
        return new TSnackbar(findSuitableParent(anchor)).setContentView(contentView).setDuration(duration);
    }

    /**
     * @param anchor
     * @param contentView
     * @param duration    LENGTH_INDEFINITE LENGTH_SHORT LENGTH_LONG or customedvalue
     * @return
     */

    public static TSnackbar make(View anchor, SnackbarBaseViewHolder contentView,
                                 int duration) {
        return new TSnackbar(findSuitableParent(anchor)).setContentView(contentView).setDuration(duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {


                    return (ViewGroup) view;
                } else {

                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {

                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);


        return fallback;
    }

    public TSnackbar setContentView(View contentView) {
        mContentView = contentView;
        return this;
    }

    public TSnackbar setContentView(SnackbarBaseViewHolder holder) {
        mViewHolder = holder;
        mContentView = holder.inflateView(mView).mRootView;
        return this;
    }

    public TSnackbar setData(Object object) {
        mViewHolder.setData(object);
        return this;
    }


    public SnackbarBaseViewHolder getHolder() {
        return mViewHolder;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }


    public TSnackbar setDuration(int duration) {
        mDuration = duration;
        return this;
    }


    public int getDuration() {
        return mDuration;
    }


    public View getView() {
        return mView;
    }


    /**
     * @return 传入默认的type，TYPE_MANAGER_DEFAULT
     */
    public TSnackbar show() {
        mManagerType = TYPE_MANAGER_DEFAULT;
        SnackbarManager.getInstance(mManagerType)
                .show(mDuration, mManagerCallback);
        return this;
    }

    /**
     * @param type 不同type的TSnackbar不会相互影响，相同type的TSnackbar并发时后面的会覆盖前面的
     * @return
     */
    public TSnackbar show(int type) {
        mManagerType = type;
        SnackbarManager.getInstance(type).show(mDuration, mManagerCallback);
        return this;
    }


    public TSnackbar dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL);
        return this;
    }

    private void dispatchDismiss(@Callback.DismissEvent int event) {
        SnackbarManager.getInstance(mManagerType)
                .dismiss(mManagerCallback, event);
    }

    public int getMannagerType() {
        return mManagerType;
    }


    public TSnackbar setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }


    public boolean isShown() {
        return SnackbarManager.getInstance(mManagerType)
                .isCurrent(mManagerCallback);
    }


    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance(mManagerType)
                .isCurrentOrNext(mManagerCallback);
    }

    private final SnackbarManager.Callback mManagerCallback = new SnackbarManager.Callback() {
        @Override
        public void show() {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_SHOW, TSnackbar.this));
        }

        @Override
        public void dismiss(int event) {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_DISMISS, event, 0, TSnackbar.this));
        }
    };

    void showView() {
        if (mView.getParent() == null) {
            if (mContentView != null) {
                if (mContentView.getParent() != null) {
                    ViewGroup group = (ViewGroup) mContentView.getParent();
                    group.removeView(mContentView);
                }
                mView.addView(mContentView);
            }
            mParent.addView(mView);
        }

        mView.setOnAttachStateChangeListener(new SnackbarLayout.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isShownOrQueued()) {


                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onViewHidden(Callback.DISMISS_EVENT_MANUAL);
                        }
                    });
                }
            }
        });
        if (mCallback != null) {
            mCallback.onShown(TSnackbar.this);
        }
        SnackbarManager.getInstance(mManagerType)
                .onShown(mManagerCallback);
    }


    void hideView(int event) {
        onViewHidden(event);
    }

    private void onViewHidden(int event) {

        SnackbarManager.getInstance(mManagerType)
                .onDismissed(mManagerCallback);
        final ViewParent parent = mView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mView);
            mView.removeAllViews();
        }
        if (mCallback != null) {
            mCallback.onDismissed(this, event);
        }
    }

    public static class SnackbarLayout extends FrameLayout {


        interface OnAttachStateChangeListener {
            void onViewAttachedToWindow(View v);

            void onViewDetachedFromWindow(View v);
        }

        private OnAttachStateChangeListener mOnAttachStateChangeListener;

        public SnackbarLayout(Context context) {
            this(context, null);
        }

        public SnackbarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            setClickable(true);

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }


        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewAttachedToWindow(this);
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
            mOnAttachStateChangeListener = listener;
        }
    }
}