package com.gkk.slideswitch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义view
 */
public class SlideSwitchView  extends View{

    private static final int STATE_NONE = 0;//四种状态
    private static final int STATE_DOWN = 1;
    private static final int STATE_MOVE = 2;
    private static final int STATE_UP = 3;

    private boolean isOpened = false;// 用来标记滑块是否打开
    private int mState = STATE_NONE;// 用来标记状态

    private Bitmap background;//背景图片
    private Bitmap picture;//图片

    private Paint mPaint = new Paint();//画笔
    private float mCurrentX;//当前点击位置的X坐标（距离自身组件左边缘）
    private int backgroundWidth;//背景的宽度
    private OnSwitchListener mListener;//监听接口

    public SlideSwitchView(Context context) {
        this(context,null);
    }

    public SlideSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置背景资源
     *
     * @param resId 资源ID
     */
    public void setBackground(int resId) {
        background = BitmapFactory.decodeResource(getResources(), resId);
    }
    /**
     * 设置图片资源
     *
     * @param resId 资源ID
     */
    public void setPicture(int resId) {
        picture = BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 设置背景的大小
     * @param widthMeasureSpec 背景的宽度
     * @param heightMeasureSpec 背景的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (background != null) {
            setMeasuredDimension(background.getWidth(), background.getHeight());
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 绘制背景图片和滑动图片
     * @param canvas 画笔
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景的显示
        if (background != null) {
            canvas.drawBitmap(background, 0, 0, mPaint);
        }
        //绘制picture的显示
        if (picture == null) {
            return;
        }

        int pictureWidth = picture.getWidth();//图片的宽度
        backgroundWidth = background.getWidth();//背景图片的宽度

        switch (mState) {
            case STATE_DOWN:
            case STATE_MOVE:
                if (!isOpened) {//关闭状态
                    if (mCurrentX < pictureWidth / 2f) {//点击位置在滑块的左侧
                        canvas.drawBitmap(picture, 0, 0, mPaint);//滑块不动
                    } else {//点击位置在滑块的右侧
                        float left = mCurrentX - pictureWidth / 2f;
                        float maxLeft = backgroundWidth - pictureWidth;
                        if (left > maxLeft) {
                            left = maxLeft;
                        }
                        canvas.drawBitmap(picture, left, 0, mPaint);
                    }
                } else {//滑块处于打开状态
                    float middle = backgroundWidth - pictureWidth / 2f;
                    if (mCurrentX > middle) {//当前点击或移动位置大于滑块的一半
                        canvas.drawBitmap(picture, backgroundWidth - pictureWidth, 0, mPaint);
                    } else {
                        float left = mCurrentX - pictureWidth / 2f;
                        if (left < 0) {
                            left = 0;
                        }
                        canvas.drawBitmap(picture, left, 0, mPaint);
                    }
                }
                break;
            case STATE_UP:
            case STATE_NONE:
                if (!isOpened) {
                    canvas.drawBitmap(picture, 0, 0, mPaint);
                } else {
                    canvas.drawBitmap(picture, backgroundWidth - pictureWidth, 0, mPaint);
                }
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mState = STATE_DOWN;
                mCurrentX = event.getX();//获取当前点击位置的X坐标（距离组件做左边框）
                /*mCurrentRowX = event.getRawX();//获取当前点击位置距离屏幕左边框的距离
                mCurrentRawY = event.getRawY();//获取当前点击位置距离屏幕上边框的距离*/
                invalidate();//刷新界面
                break;
            case MotionEvent.ACTION_MOVE:
                mState = STATE_MOVE;
                mCurrentX = event.getX();
                /*mCurrentRowX = event.getRawX();//获取当前点击位置距离屏幕左边框的距离
                mCurrentRawY = event.getRawY();//获取当前点击位置距离屏幕上边框的距离*/
                invalidate();//刷新界面
                break;
            case MotionEvent.ACTION_UP:
                mState = STATE_UP;
                mCurrentX = event.getX();//获取当前点击位置的的X坐标
                /*mCurrentRowX = event.getRawX();//获取当前点击位置距离屏幕左边框的距离
                mCurrentRawY = event.getRawY();//获取当前点击位置距离屏幕上边框的距离*/
                if (backgroundWidth / 2f > mCurrentX && isOpened) {
                    isOpened = false;
                } else if (backgroundWidth / 2f <= mCurrentX && !isOpened) {
                    isOpened = true;
                }
                if (mListener != null) {
                    mListener.onSwitchChanged(isOpened);
                }
                invalidate();
                break;
        }
        return true;//消费touch事件
    }

    public void setOnSwitchListener(OnSwitchListener listener) {
        this.mListener = listener;
    }
    public interface OnSwitchListener {
        // 开关状态改变--->
        void onSwitchChanged(boolean isOpened);
    }
}
