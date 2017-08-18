package com.sx.framelibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.sx.framelibrary.R;


/**
 * @Author sunxin
 * @Date 2017/8/18 9:16
 * @Description
 */

public class ColorTrackTextView extends AppCompatTextView {

    //两支画笔，一支绘制原始颜色，另一支绘制变化的颜色
    private Paint mOrignPaint;//原始
    private Paint mChangePaint;//变化

    //不同朝向
    private Direction mDirection = Direction.LEFT_TO_RIGHT;
    private int mChangeColor;



    public enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    //定义一个进度值
    private float mCurrentProgress = 0.0f;

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context, attrs);
    }

    /**
     * 初始化画笔,并获取属性值
     *
     * @param context
     * @param attrs
     */
    private void initPaint(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);
        int originColor = typedArray.getColor(R.styleable.ColorTrackTextView_originColor,
                getTextColors().getDefaultColor());
        int changeColor = typedArray.getColor(R.styleable.ColorTrackTextView_changeColor,
                getTextColors().getDefaultColor());

        mOrignPaint = getPaintByColor(originColor);
        mChangePaint = getPaintByColor(changeColor);

        //回收
        typedArray.recycle();
    }


    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
        paint.setTextSize(getTextSize());//设置字体大小，就是TextView的字体大小
        return paint;
    }


    //开始绘制一个字体两种颜色
    @Override
    protected void onDraw(Canvas canvas) {

        //根据进度把中间值计算出来
        int middle = (int) (mCurrentProgress * getWidth());

        if (mDirection == Direction.LEFT_TO_RIGHT) {
            drawText(canvas, mChangePaint, 0, middle);
            //绘制变色的文字部分
            drawText(canvas, mOrignPaint, middle, getWidth());
        } else if (mDirection == Direction.RIGHT_TO_LEFT) {
            drawText(canvas, mChangePaint, getWidth()-middle, getWidth());
            //绘制变色的文字部分
            drawText(canvas, mOrignPaint, 0, getWidth()-middle);
        }
    }

    /**
     * 绘制Text
     *
     * @param canvas
     * @param paint
     * @param start
     * @param end
     */
    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        //裁剪Rect,绘制不变色的文字部分
        canvas.save();//保存画布
        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);

        String text = getText().toString();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        //获取字体宽度
        int x = getWidth() / 2 - bounds.width() / 2;
        //基线baseline
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseline = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseline, paint);
        canvas.restore();//回收画布
    }

    /**
     * 设置方向
     * @param direction
     */
    public void setDirection(Direction direction){
        this.mDirection = direction;
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setCurrentProgress(float progress){
        this.mCurrentProgress = progress;
        invalidate();//不断重绘
    }

    public void setChangeColor(int changeColor) {
        this.mChangePaint.setColor(changeColor);
    }

    public void setOriginColor(int changeColor) {
        this.mOrignPaint.setColor(changeColor);
    }

}
